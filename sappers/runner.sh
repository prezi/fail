#!/bin/bash

sapper=$1; shift
sleep=${1:-5}

rm -f nohup.out && touch nohup.out
tail -f nohup.out &
tailer=$!
(
    trap "./${sapper}/stop ; sleep 1 ; kill $tailer" EXIT
    set -x
    ./${sapper}/start && sleep ${sleep}
) &>>nohup.out &
supervisor_pid=$!
trap "kill $supervisor_pid" TERM
wait $supervisor_pid 2>/dev/null
