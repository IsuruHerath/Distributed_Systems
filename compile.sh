APPDIR=/home/isuru/Acacia/x10dt/workspace/Distributed_Systems

mkdir -p build/classes
cd $APPDIR/src
find -name "*.java" > $APPDIR/sources.txt
javac @/home/isuru/Acacia/x10dt/workspace/Distributed_Systems/sources.txt -d $APPDIR/build/classes
