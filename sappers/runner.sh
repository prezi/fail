#!/bin/bash

saper=$1; shift
sleep=${1:-5}

rm -f nohup.out && touch nohup.out
tail -f nohup.out &
tailer=$!
nohup bash -cx "./${sapper}/start && sleep ${sleep} && ./${sapper}/stop && sleep 1 && kill $tailer" 2>>nohup.out &
wait $! 2>/dev/null
