# Failure injection against `fail` itself

## Basic architecture

api: connecting to to DynamoDB, providing an interface to scheduling runs and querying what runs will run in the next N minutes.

scheduler: connecting to api finds the failure runs to do in the next few minutes, puts their ids on a queue

worker: reads items from the queue, fetches run details from the api, does the failure injection.

Two possible manifestations:

* standalone: all fail components run on the same host. The api is reached via `127.0.0.1`.

* distributed: no guarantee of where components run. The api is reached via whatever domain name or IP is provided in the configuration.

## Sappers

### fail_dns

#### Expected result

* all components: fail to connect to AWS services, because the endpoints are configured using DNS names
* scheduler, worker
 * standalone: no further effect, loopback interface works
 * distributed: api can't be reached; no failures are scheduled or run. Once DNS resolution is back the failures that should've been scheduled during the DNS outage are added to the database with status `FUTURE`. However, items from the past are not scheduled for running, and will remain in `FUTURE` status forevermore.

#### Expected learnings

* Verify all the expected results
* We don't know if there's any DNS caching going on at the application (or OS) level. If there is then there won't be any failure while both (1) the cache stays populated and (2) the DNS records stay the same as what we have in the cache

#### Room for improvement

* all healthchecks should verify that they can access whatever AWS resources they need
* scheduler, worker healthchecks should verify they can access the api
* do the components crash when they can't access a resource they need?
 * if they crash, are they correctly restarted by supervisor?
 * if they don't crash, do they reconnect once DNS resolution is back up?

#### Learnings

See `fail_dynamodb_traffic` wrt AWS and DNS. The exact same learnings apply here when it comes to resolving DynamoDB from the API.

TODO: redo this test once we have a distributed setup (as opposed to standalone)

### network_delay_memcache

Not applicable

### nullroute

#### Expected result when applied to all nodes but one

* Hit api nodes are removed from the elb, the rest can take the traffic (there's very little traffic). When the failure is over, they stand back in service without issues.
* Hit worker, scheduler nodes cannot connect to any resource they need. When the failure is over, they should resume service.

#### Expected learnings

* do the components crash when they can't access a resource they need?
 * if they crash, are they correctly restarted by supervisor?
 * if they don't crash, do they reconnect once networking is back up?

#### Learnings

* worker, scheduler: throw lots of exceptions during the failure run because it can't connect to SQS. Goes back to normal after the failure is over.

### restart_supervisor_things

#### Expected result, number of nodes irrelevant

* api: some failed requests that are routed to the instance under fire. Can result in:
 * failed CLI commands
 * scheduler fails to query future runs, so runs are not created in the DB
 * scheduler fails to schedule runs, they stay in `FUTURE` status
 * worker fails to start runs, they stay in `SCHEDULED` status
 * worker fails to write back the result of a run to the DB, the run stays in `RUNNING` status and the run log can only be found in the log of the worker, not the DB
* worker
 * if restarted while not doing an injection run: no effect. supervisor restarts, things go back to normal.
 * if restarted in the middle of an injection: ShutdownHookProcessDestroyer is registered in the worker that has a reference to all the children, so it should send a SIGTERM to all child processes. However, the child processes fail to stop their failure injection runs due to https://github.com/prezi/fail/issues/14, so the failure runs are not aborted. Still, we won't get the log of the runs, and the runs stay in status `RUNNING`.
* scheduler: actions taken by the scheduler are very very short between long sleeps, so basically there should be no impact

#### Expected learnings

* verify that ShutdownHookProcessDestroyer works (and is used correctly)

#### Learnings

* api:
 * the apache proxy we use for authentication stays in `503` for quite a while after the service comes back up. this causes the outage to be much longer than ideal, given the failure
 * the cli doesn't handle the HTML response well, even though it should recognize that `503` means it shouldn't expect niceness (`com.fasterxml.jackson.core.JsonParseException: Unexpected character ('<' (code 60)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')`)
* it takes either some manual modifications in the DB or lots of patience to be present when a scheduled failure is just being run (or lots of scheduled failures)

TODO: verify that ShutdownHookProcessDestroyer works (and is used correctly)

#### Room for improvement

* reconfigure the apache proxy to check the api health more often, or even to don't cache its state at all
* gracefully handle responses in the client that the RestLi client fails to parse (not JSON, malformed JSON or incomplete data)
* implement `schedule-once` and/or `reschedule` command to make experimenting easier

### drop_dynamodb_traffic

#### Expected result

* api: all calls fail, leading to no runs being scheduled or run by the scheduler and worker
* worker, scheduler: no effect

#### Learnings

Instances serving DynamoDB endpoints are load-balanced using DNS with a DNS TTL of 60 seconds. This means that to use this sapper, two things must happen while the same resolution is in the hosts DNS cache:

* the sapper must run (resolving the endpoint URLs)
* the application must (try to) establish a connection to DynamoDB

We've also learned that the Java AWS SDK keeps its connection open to DynamoDB open (didn't measure how long).

When the failure injection actually affected the API:

* the AWS SDK in the API timed out on the non-responsive DynamoDB connection after a minute (`org.apache.http.conn.ConnectTimeoutException: Connect to dynamodb.us-east-1.amazonaws.com:443 timed out`), then retried, successfully completing the request. however,
* the CLI timed out after 10 seconds, with an ugly exception shown to the user (`Exception in thread "main" com.linkedin.r2.RemoteInvocationException: com.linkedin.r2.RemoteInvocationException: Failed to get response from server for URI https://fail.prezi.com/Cli?action=RunCli`)
* on the server side this appeared as a `java.nio.channels.ClosedChannelException` (the socket was closed by the CLI)

#### Room for improvement

See `fail_dns` above, hit apis falling out of the elb would negate the effect. Plus:

* Handle API timeouts gracefully in the CLI
* Start multiple API servers and add a DynamoDB healthcheck to the API healthcheck, with a low timeout. API nodes with bad connection will then be removed automatically from the elb, then placed back once they get a good endpoint.

### network_delay_redis

Not applicable

### pin_cpu

#### Expected result

There are no CPU-heavy operations, no expected impact, other than performance degradation

#### Learnings

There is no visible impact to clients.

### drop_ec2api_traffic

#### Expected result

* api, scheduler: no effect
* worker: failure injection runs that use the ec2 api to find targets (for example based on tags) will fail. https://github.com/prezi/fail/issues/16 doesn't affect this, the runs will be detected as failing and appear as `FAILED`.

### fill_mnt_disk

No expected effect

#### Learnings

Filling /mnt on an ebs-backed ec2 instance causes significant IO performance drop, which in turn causes significant service degradation for clients (everything times out all the time).

### network_corruption, network_partial_drop, network_delay

Network connections in the system:

* cli -> api: not critical to the running system, failure modes are obvious (user can't perform online actions). Let's ignore this.
* scheduler, worker -> api: full drop covered by `nullroute`
* api -> dynamodb: full drop covered by `drop_dynamodb_traffic`
* scheduler, worker -> sqs: full drop should be covered by https://github.com/prezi/fail/issues/18; also, `nullroute`
* worker -> target nodes: full drop covered by `nullroute`

#### Expected result

We don't have a good idea of what happens during these partial network outages. It's quite possible that the only effect is performance degradation, but we don't know that.

#### Expected learnings

How do the systems behave during these failures? Currently we don't have a good idea.

### reboot_machine

#### Expected result

Same as `restart_supervisor_things`

#### Possible learnings

We'll learn if startup on boot fails due to anything messed up in chef

#### Learnings

* Everything starts after boot fine
* The run log contains the exception where we lost the SSH connection, but the exit code is 0. This is fine, but the exit code will change to non-zero once https://github.com/prezi/fail/issues/16 is fixed
* The CLI handles the case where it gets a 503 from the ELB nicely, but prints an exception at the end. Full output:

```
17:05:29.061 E API call failed with response code 503: null. Run with -v to get more details.
Exception in thread "main" kotlin.KotlinNullPointerException
	at com.prezi.fail.cli.ActionApiCli.run(ActionApiCli.kt:37)
	at com.prezi.fail.FailPackage$FailPackage$933ed3ae.main(FailPackage.kt:46)
	at com.prezi.fail.FailPackage.main(Unknown Source)
```

#### Room for improvement

* Expect the SSH connection to go down when rebooting the machine, don't log an exception
* Don't print an exception when we get the `503` from ELB.

### drop_s3_traffic

Not applicable

### fill_root_disk

#### Expected result

* no component writes directly to disk. logging currently happens via stdout/stderr -> supervisor -> file, which may be a problem

#### Expected learnings

* how does out-of-disk affect services running in supervisor when supervisor wants to write log lines to disk?

### restart_init_scripts

No expected effect, everything uses supervisor. If supervisor happens to be restarted: see `restart_supervisor_things`
