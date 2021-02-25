#!/bin/bash
ABSPATH=$(cd "$(dirname "$0")"; pwd)
SCRIPTPATH=$ABSPATH
echo $SCRIPTPATH
SCRIPT="$SCRIPTPATH/jre-mac/Contents/Home/bin/java -cp "\'"$SCRIPTPATH/lib/*"\'" com.univocity.seedscramble.Main"
echo $SCRIPT
osascript -e "do shell script \"$SCRIPT\" "
