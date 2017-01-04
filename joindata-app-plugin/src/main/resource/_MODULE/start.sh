#!/bin/bash

dir=`pwd`
cps=$CLASSPATH
jars=`ls PROGRAM/lib`
opts=`cat CONFIG/OPTS`

for j in $jars
do
                cps="$cps:$dir/PROGRAM/lib/$j"
done
cps=$cps:$dir/PROGRAM/__PROG__

# 如果是 cygwin 环境需转换路径写法
if [ -n `which cygpath` ]; then
                cps=`cygpath -wp $cps`
fi

java -classpath $cps $opts __MAINCLASS__