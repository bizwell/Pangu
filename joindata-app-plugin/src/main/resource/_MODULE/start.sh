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
if [ `uname -o` = "Cygwin" ]; then
	cps=`cygpath -wp $cps`
fi

mkdir -p LOG

nohup java -classpath $cps $opts __MAINCLASS__ >> LOG/nohup.out 2>&1 &

echo '已启动'