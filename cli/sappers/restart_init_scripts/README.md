# restart_init_scripts sapper

Restarts one of the given init scripts.

## usage

This will choose one of `myservice` and `myotherservice` and call this init script with `stop` and then `start` with a 10 second sleep inbetween.
```bash
./start myservice myotherservice
```
