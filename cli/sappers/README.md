# sappers

All sappers must conform to the following minimalistic "API":

1. they are contained in a directory named after the sapper
2. there must be a `start` file
   1. it must be executable
   2. it must have a shebang line
   3. it should exit with `0` if starting the sapper was successful
   4. it should exit with a non-zero value if starting the sapper was unsuccessful
3. there must be a `stop` file
   1. it must be executable
   2. it must have a shebang line
   3. it should stop the sapper in a quick and reliable way

## helpers.sh

Helper functions that implement functionality common between sappers. They can be included with the line

```sh
. $(dirname $0)/../helpers.sh
```

### `save_iptables`, `restore_iptables`

Save and restore iptables rules. Useful in sappers that create iptables rules to do their thing.
`save_iptables <name>` should be used in the `start` script: it dumps the current iptables rules into a temporary file. 
The argument is used to build the name of the dump file. `restore_iptables <name>` can then be called with the same 
argument in the `stop` script to restore iptables to the state before the sapper ran.

If `save_iptables` fails to dump the data, it will immediately `exit 1`.

If `restore_iptables` fails to load the data, it will flush all iptables rules to be on the safe side. It also cleans
up the dump file when it's done.

### `drop_traffic_to`

Takes DNS names and ip addresses as arguments, and drops all outgoing packets to those hosts.
