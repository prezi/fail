#!/bin/bash

sapper=$1; shift
sleep=${1:-5}

rm -f nohup.out && touch nohup.out
tail -f nohup.out &
tailer=$!
export sapper sleep tailer
(
    trap "./${sapper}/stop ; sleep 1 ; kill $tailer" EXIT
    set -x
    if ./${sapper}/start; then
        sleep ${sleep}
    else
        exitcode=$?
        echo "Start script exited with ${exitcode} (which is non-zero), exiting immediately." >&2
        exit $exitcode
    fi
) &>>nohup.out &
supervisor_pid=$!
trap "kill $supervisor_pid" TERM
wait $supervisor_pid 2>/dev/null
