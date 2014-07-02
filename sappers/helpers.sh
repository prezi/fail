#!/bin/bash

iptables_save_file() {
    echo "/tmp/iptables.$1.dump"
}

save_iptables() {
    save_file=$(iptables_save_file "$1")
    iptables-save > ${save_file}
    if [ $? -ne 0 ]; then
        echo "Failed to save iptables rules to $save_file; bailing out."
        exit 1
    fi
}

restore_iptables() {
    save_file=$(iptables_save_file "$1")
    iptables-restore < ${save_file}
    if [ $? -ne 0 ]; then
        echo "Failed to restore iptables rules saved when starting from $save_file, flushing iptables."
        iptables -F
    fi
    rm -f ${save_file}
}