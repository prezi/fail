# Fail

[![Build Status](https://travis-ci.org/prezi/fail.svg?branch=master)](https://travis-ci.org/prezi/fail)

A light-weight implementation of the ideas behind Chaos Monkey. See the original: [Simian Army](https://github.com/Netflix/SimianArmy). This project was originally called `anthropomorphic-battalion` but was renamed for obvious reasons.

## Quick Start

Pre-requisites:

 * All the examples: You must be able to log in to the target servers as root using an active ssh-agent, without a password prompt
 * For EC2 examples: You have AWS credentials configured in ~/.aws/credentials

```sh
# Installing the CLI
## Install the latest release with homebrew 
brew tap prezi/oss
brew install fail
## Or build from source
./gradlew installApp
alias fail=./cli/build/install/fail/bin/fail

# Log in to my-host and sleep for 30 seconds
fail once my-host.my-domain.com noop 30 --scout-type DNS  

# Log in to my-host and use all available CPU for 30 seconds
fail once my-host.my-domain.com pin_cpu 30 --scout-type DNS  

# Log in to my-host and use all available CPU for 30 seconds
fail once my-host.my-domain.com pin_cpu 30 --scout-type DNS  

# Log in to one EC2 host that has the tag service_name=my-service and use all available CPU for 30 seconds
fail once service_name=my-service pin_cpu 30  

# Log in to all EC2 hosts that have the tag service_name=my-service and use all available CPU for 30 seconds
fail once service_name=my-service pin_cpu 30 --all 
```

You may also download a release from https://github.com/prezi/fail/releases. Note that the release contains
ONLY the cli, not any parts of the server side. We try to create releases whenever something changes in the cli, but
feel free to open an issue if something is missing.

### Server side

The so called "online" commands (related to scheduling failures) will only be available once there is a running fail server.
Generally you'll only need a single instance of this running in your organization - no need to run anything on all the target nodes.

After building with `./gradlew installApp` you can run a local server like this:

```sh
api/server/build/install/fail-api/bin/fail-api
```

If you'd like to connect to a server running elsewhere, you can use the `--api` command line flag to the cli app, or the `fail.cli.apiEndpoint` java system property.

## Details

The components, with detailed documentation:

* [cli](cli): As a user of `fail` you'll generally only interact with the CLI. It serves two goals:
 * to run failure injections manually from your own computer
 * to communicate with the server-side, which runs scheduled failure injections
* [api](api): A [rest.li](http://rest.li/) server that stores data about scheduled failure injection runs in DynamoDB
* [scheduler](scheduler): Reads the list of scheduled failures to be run in the next few minutes from the API, and puts them on an SQS queue
* [worker](worker): Grabs items from an SQS queue, fetches the failure run details associated with each item from the API, then runs
  the failure by invoking the CLI.

## PANIC! `fail` is causing an outage!

* `fail once`: Your first reflex reaction is correct: it you kill the process (for example with Ctrl-C), then before going down, we'll
  stop hurting the server (assuming we can communicate with it) by running the `stop` script of the active Sapper on
  each node under attack. This event also gets a separate event in Changelog if Changelog integration is enabled.
* scheduled failures: `fail panic` prevents any further runs from starting. Once https://github.com/prezi/fail/issues/14
  is fixed or worked around, any failures in progress will also be aborted.


------------
![cat](https://i.chzbgr.com/maxW500/3576064768/h35FCCB8D/)
