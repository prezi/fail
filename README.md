# Anthropomorphic Battalion

A light-weight implementation of the ideas behind Chaos Monkey. See the original: [Simian Army](https://github.com/Netflix/SimianArmy). 

## Getting started

Build:

```sh
./gradlew build
```

Run:

```sh
cd build/distributions/
tar -xzf anthropomorphic-battalion.tgz
cd anthropomorphic-battalion
./bin/anthropomorphic-battalion $TAG $SAPPER $SECONDS
```

Alternatively:

```sh
./gradlew run -PappArgs=$TAG,$SAPPER,$SECONDS
```

These will choose a single EC2 node in us-east that has the tag $TAG (with any value), and ron $SAPPER for $SECONDS
seconds on it. Details below.

## How does this thing work?

### Scout: finding targets

Scouts are pieces of code that figure out which hosts to hurt based on a string. The string passed to the Scout is the
first command-line argument. You can choose which implementation to use by setting the `anthro.sarge.scoutType`
system property; default: `TAG`. At the time of writing there are two implementations (in `src/kotlin/com/prezi/anthro/sarge/scout`),
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
setting the system property `anthro.sarge.mercyType` to a value from the enum `MercyType` (the default is `HURT_JUST_ONE`):
  
 * `HURT_JUST_ONE`: choose a random target from the list provided by the Scout.
 * `NO_MERCY`: attack all the targets found by the Scout.
                        
                        
### Sapper: bring the pain to the servers
                      
A Sapper is a pair of scripts that start and stop a specific kind of malfunction on a target server. See the directories
under `sappers` for a list. `noop` deserves a special mention; you can use it to check that everything is in order before
starting to actually hurt nodes. Note that targets all targets will be attacked _in parallel_. If you introduce
critical failures on all your nodes, the service _will_ go down.


## Changelog integration

Anything done with this tool is inherently dangerous, and thus important to track. When running against production
servers it's a very very good idea to automatically collect what exactly is happening. To this end 
[Changelog](https://github.com/prezi/changelog) integration can be enabled by setting a few system properties:

 * `anthro.useChangelog`: send events to Changelog? Defaults to `false`.
 * `changelog.endpoint`: the full URL where events will be posted, including `/api/events`.
 * `changelog.auth.providerType`: the authentication provider to use for authentication against the Changelog API.
   Possible values are defined in the enum `ChangelogAuthProviderType` (the default is `NOOP`):
   * `NOOP`: no authentication
   * `HTTP_BASIC_AUTH`: HTTP Basic authentication. The username and password to use are configured with the
     `changelog.auth.httpBasic.username` and `changelog.auth.httpBasic.password` system properties.

## Kotlin vs. IntelliJ

At the time of writing this project uses the latest Kotlin version, which is not supported by the latest stable Kotlin IntelliJ plugin. Use the
nightly, available by adding http://teamcity.jetbrains.com/guestAuth/repository/download/bt345/.lastSuccessful/updatePlugins.xml
in Settings -> Plugins -> Browse Repositories.

------------
![cat](https://i.chzbgr.com/maxW500/3576064768/h35FCCB8D/)
