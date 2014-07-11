# network_condition sapper

Injects network conditions into IP traffic (both TCP/UDP).

## Usage

The start script has three arguments: the network interface, a list filters
for selecting which packets to hurt, and the network condition to emulate. 

The helper function `match_ip` can be used to create match conditions with
`and` between them, and the output of multiple `match_ip` invocations can be 
concatenated in the parameter list of the start script for `or`-ing the conditions.

```bash
# Add a random delay between 500ms and 1500ms
# to every packet to and from the ports specified on eth0
./start eth0 "$(match_ip 'sport 80')" "$(match_ip 'dport 80')" "$(match_ip 'sport 443')" "$(match_ip 'dport 443')"\
  -- delay 1s 500ms distribution normal
# Stop the sapper
./stop eth0


# This causes the added delay to be 100ms ± 10ms with the next random
# element depending 25% on the last one. This isn't true statistical
# correlation, but an approximation.
# Only packets coming from 127.0.0.1 are affected.
./start eth0 "$(match_ip 'sport 80' 'src 127.0.0.1')" "$(match_ip 'dport 80' 'src 127.0.0.1')" -- delay 100ms 10ms 25%
# Stop the sapper
./stop eth0
```
