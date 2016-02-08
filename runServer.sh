#!/bin/bash
PORT=$1
APPDIR=/home/isuru/Acacia/x10dt/workspace/Distributed_Systems
cd $APPDIR/src/boostrapserver
./a.out $PORT
