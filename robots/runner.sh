#!/bin/bash

robot=$1; shift
sleep=${1:-5}

rm -f nohup.out && touch nohup.out
tail -f nohup.out &
tailer=$!
nohup bash -c "./${robot}/start && sleep ${sleep} && ./${robot}/stop && sleep 1 && kill $tailer" 2>>nohup.out &
wait $! 2>/dev/null
