# Distributed_Systems

## Set up the system
1. Clone the repository to your machine.
2. Set APPDIR variable of the .sh files.

##compile the code
run compile.sh

##run the Bootstrap Server
run runServer.sh <Server_Port> to up the Bootstrap server

##create nodes with UDP sockets
1. configure <Server_Port> in runSocketNode.sh
2. run runSocketNode.sh <Node_Port> <Username>

##create nodes with WebServices
1. configure <Server_Port> in runWebServiceNode.sh
2. run runWebServiceNode.sh <Node_Port> <Username>

<Username> should be unique for each node. If you creat multiple nodes in the same host <Node_Port> should be unique.
