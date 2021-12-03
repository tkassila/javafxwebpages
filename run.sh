#!/bin/sh
rundir=$(pwd)
export rundir
#echo rundir=$rundir
# icu is not needed any more:
# $rundir/lib/icu4j-70.1.jar:
java -cp $rundir/javafxwebpages-1.0-SNAPSHOT.jar:$rundir/lib/gson-2.8.9.jar:$CLASSPATH com.metait.javafxwebpages.WebPagesApplication
