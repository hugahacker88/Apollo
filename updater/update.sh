#!/bin/bash
# This script is used by Updater for Linux and MacOS to update application directory
# first parameter is a current directory, where wallet is executing now (directory, which we should update)
# second parameter is a update directory which contains unpacked jar for update
# third parameter is a boolean flag, which indicates user mode

 
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

# Parameters
# $1 - directory of old installation
# $2 - directory with unpacked new files
# $3 true - for user mode, false for service mode
 
chmod 755 $DIR/update1.sh
nohup $DIR/update1.sh $1 $2 $3 $4 $5 1>/tmp/apollo-update.log 2>/tmp/apollo-update-error.log &

