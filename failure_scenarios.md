# Failure injection against `fail` itself

## Basic architecture

api: connecting to to DynamoDB, providing an interface to scheduling runs and querying what runs will run in the next N minutes.

scheduler: connecting to api finds the failure runs to do in the next few minutes, puts their ids on a queue

worker: reads items from the queue, fetches run details from the api, does the failure injection.

Two possible manifestations:

* standalone all fail components run on the same host. The api is reached via `127.0.0.1`.

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

### drop_dynamodb_traffic

#### Expected result

* api: all calls fail, leading to no runs being scheduled or run by the scheduler and worker
* worker, scheduler: no effect

#### Room for improvement

See `fail_dns` above, hit apis falling out of the elb would negate the effect

### network_condition

TBD

### network_delay_redis

Not applicable

### pin_cpu

#### Expected result

There are no CPU-heavy operations, no expected impact, other than performance degradation

#### Expected learnings

* How much does the system slow down?

### drop_ec2api_traffic

#### Expected result

* api, scheduler: no effect
* worker: failure injection runs that use the ec2 api to find targets (for example based on tags) will fail. https://github.com/prezi/fail/issues/16 doesn't affect this, the runs will be detected as failing and appear as `FAILED`.

### fill_mnt_disk

No expected effect

### network_corruption

TBD

### network_partial_drop

TBD

### reboot_machine

#### Expected result

Same as `restart_supervisor_things`

#### Possible learnings

We'll learn if startup on boot fails due to anything messed up in chef

### drop_s3_traffic

Not applicable

### fill_root_disk

#### Expected result

* no component writes directly to disk. logging currently happens via stdout/stderr -> supervisor -> file, which may be a problem

#### Expected learnings

* how does out-of-disk affect services running in supervisor when supervisor wants to write log lines to disk?

### network_delay

TBD

### restart_init_scripts

No expected effect, everything uses supervisor. If supervisor happens to be restarted: see `restart_supervisor_things`