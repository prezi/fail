# network_latency robot

Adds network latency to IP traffic (both TCP and UDP).

## Usage

The start script has three arguments: the network interface, a list of
ports, and the latency to add. You can prefix a port with `s` to match
only source ports, or `d` for destination ports, or no prefix to match
both. The network latency's format is `DELAY [JITTER [CORRELATION]]`.


```bash
# This is the default, it adds a random delay between 500ms and 1500ms
# to every packet to and from the ports specified on eth0
./start eth0 "80 443 3306 11211" "1s 500ms"
# Stop the robot
./stop eth0


# This causes the added delay to be 100ms ± 10ms with the next random
# element depending 25% on the last one. This isn't true statistical
# correlation, but an approximation.
./start eth0 "80 443 3306 11211" "100ms 10ms 25%"
# Stop the robot
./stop eth0
```
