#!/bin/bash
#
# Launches migration script, performs some sanity cheks
# Note that if databse already contains items they will be updated
#
function check_command() {
    local command=$1
    command -v ${command} >/dev/null 2>&1 || { echo >&2 "This script requires ${command} to be installed"; exit 1; }
}

check_command node

node ./migration/launcher.js "$@"