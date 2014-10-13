# Fail

[![Build Status](https://travis-ci.org/prezi/fail.svg?branch=master)](https://travis-ci.org/prezi/fail)

A light-weight implementation of the ideas behind Chaos Monkey. See the original: [Simian Army](https://github.com/Netflix/SimianArmy). This project was originally called `anthropomorphic-battalion` but was renamed for obvious reasons.

## Getting started

Build:

```sh
./gradlew installApp
```

Run:

```sh
cd cli/build/install/fail
./bin/fail charge $TAG $SAPPER $SECONDS [$ARG1 $ARG2 ...]
```

These will choose a single EC2 node in us-east that has the tag $TAG (with any value), and run $SAPPER for $SECONDS
seconds on it, optionally passing $ARG1 $ARG2 ... to the sapper. Details below.

### Server side

The so called "online" commands will only be available once there is a running fail server. After building with `./gradlew installApp` you can run a local server like this:

```sh
cd api/server/build/install/fail-api
./bin/fail-api
```

If you'd like to connect to a server running elsewhere, you can use the `--api` command line flag to the cli app described below, or the `fail.cli.apiEndpoint` java system property.

## Configuring

At startup the file `~/.fail.properties` is loaded as a properties file. The precedence for configuration values
(which are all system properties) looks like this (higher overrides lower):

 - command line options
 - `-D` via `JAVA_OPTS`
 - `~/.fail.properties`
 - Default values hard-coded in `fail`

### Full list of supported configuration options for the client

#### `fail.dryRun` (`-n`, `--dry-run`)
Skips running sappers. Set to `true` or `false` if setting it via system properties. Works for online commands, too.

#### `fail.sarge.targz` (`-p`, `--sappers`)
Path to the tarball containing sappers; lets you provide your own. The default points to the sappers shipped with fail.

#### `fail.sarge.scoutType` (`-s`, `--scout-type`)
Defines how the first argument to `fail` is used when choosing target servers. See below for supported values.

#### `fail.sarge.mercyType` (`-a`, `--all`)
Defines how the list of servers provided by the Scout is filtered. See below for supported values.

If given via a command line argument (`-a`, `--all`) then NO_MERCY is used, which means there will be no filtering on the list provided by the Scout.

#### `fail.sarge.ssh.auth_type`
Authentication method used when connecting to target servers.
 * `NONE` uses whatever is provided by the environment
 * `SSH_AGENT` uses the ssh agent if one is available. This is the default.

#### `fail.sarge.ssh.disable_strict_host_key_checking`
What the title says. Set to `true` or `false` if setting it via system properties. Defaults to `true`.

#### `fail.awsScout.availabilityZone` (`-z`, `--availability-zone`)
When using the `TAG` scout type, choose servers only from this availability zone. If not specified, use all AZs.

#### `fail.useChangelog` (`-c`, `--use-changelog`)
Send data to a [Changelog](https://github.com/prezi/changelog) server about sapper runs. See
[the documentation of `changelog-client-java`](https://github.com/prezi/changelog-client-java#configuration) for how to configure
the Changelog client.

#### `fail.cli.apiEndpoint` (`--api`)
Set the base url of the server side, for online commands. You can check if the remote works by issuing `fail --api https://yourendpoint api-check`. The default is `http://localhost:8080`.

#### `fail.cli.debug` (`-v`, `--debug`)
Sets up debug logging on both the client and the server (for this request).

#### `fail.cli.trace` (`-vv`, `--trace`)
Sets up trace logging on both the client and the server (for this request).

## How does this thing work?

### Scout: finding targets

Scouts are pieces of code that figure out which hosts to hurt based on a string. The string passed to the Scout is the
first command-line argument. You can choose which implementation to use by setting the `fail.sarge.scoutType`
system property; default: `TAG`. At the time of writing there are two implementations (in `src/kotlin/com/prezi/fail/sarge/scout`),
with the values to use here defined by the enum `ScoutType`:

 * `TAG` (class `TagScout`): finds instances on EC2 that have a tag. The input is the tag itself. For AWS authentication it uses
   profile files as described [here](http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html).
   By default it looks for the file at `~/.aws/credentials`; this location can be overridden with the ENV variable
   `AWS_CREDENTIAL_PROFILES_FILE`. Caveat: currently works with the US-EAST region, will be configurable some time in the
   future.
 * `DNS` (class `DnsScout`): splits the argument along `:`, and considers each item a target. They can be DNS names
   or IP addresses.

### Mercy: choosing which targets to attack

You can think of this component as a filter on the list of targets provided by the Scout. Choose an implementation by
setting the system property `fail.sarge.mercyType` to a value from the enum `MercyType` (the default is `HURT_JUST_ONE`):

 * `HURT_JUST_ONE`: choose a random target from the list provided by the Scout.
 * `NO_MERCY`: attack all the targets found by the Scout.


### Sapper: bring the pain to the servers

A Sapper is a pair of scripts that start and stop a specific kind of malfunction on a target server. See the directories
under `cli/sappers` for a list. `noop` deserves a special mention; you can use it to check that everything is in order before
starting to actually hurt nodes. Note that all targets will be attacked _in parallel_. If you introduce
critical failures on all your nodes, the service _will_ go down.

More details can be found in the [sappers directory](https://github.com/prezi/fail/tree/master/sappers)'s [README](https://github.com/prezi/fail/blob/master/sappers/README.md).

### PANIC! `fail` is causing an outage!

Your first reflex reaction is correct: it you kill the process (for example with Ctrl-C), then before going down, we'll
stop hurting the server (assuming we can communicate with it) by running the `stop` script of the active Sapper on
each node under attack. This event also gets a separate event in Changelog if Changelog integration is enabled.


## Changelog integration

Anything done with this tool is inherently dangerous, and thus important to track. When running against production
servers it's a very very good idea to automatically collect what exactly is happening. To this end
[Changelog](https://github.com/prezi/changelog) integration can be enabled by setting `fail.useChangelog=true`, and
configuring the client as described in [here](https://github.com/prezi/changelog-client-java#configuration).

------------
![cat](https://i.chzbgr.com/maxW500/3576064768/h35FCCB8D/)
