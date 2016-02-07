#!/bin/bash
SERVER_HOST=127.0.0.1
SERVER_PORT=8889
NODE_PORT=$1
USER_NAME=$2
APPDIR=/home/isuru/Acacia/x10dt/workspace/Distributed_Systems/
cd $APPDIR/bin
java test.Main $SERVER_HOST $SERVER_PORT $NODE_PORT $USER_NAME
