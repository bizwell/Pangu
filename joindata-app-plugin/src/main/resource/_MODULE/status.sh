#!/bin/bash

DANGER='\E[1;31m'  #红
SUCCESS='\E[1;32m' #绿
WARN='\E[1;33m' #黄
INFO='\E[1;36m'  #蓝
HIGHLIGHT='\E[1;35m'      #粉红
RES='\E[0m'

echo ""

dir=$(cd "$(dirname "`readlink -f $0`")"; pwd)
cd $dir

pidfile="/var/run/__APPID__/__APPVERSION__.pid"

echo -e "${SUCCESS}运行状态${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
echo "------------------------------------------------"

# 判断是否在运行
piddir="/var/run/__APPID__"
pidfile="$piddir/__APPVERSION__.pid"

mkdir -p $piddir
if [ ! -f $pidfile ]; then
  echo -e "${DANGER}找不到进程号${RES}"
  echo ""
  exit 1;
fi
pid=`cat $pidfile`
if [ -z "$pid" ]; then
  echo -e "${DANGER}找不到进程号${RES}"
  echo ""
  exit 1;
fi
if ! kill -0 $pid > /dev/null 2>&1; then
  echo -e "${WARN}程序没有在运行${RES}"
  echo ''> $pidfile
  exit 1;
fi

# 输出提示
echo -e "PID: ${HIGHLIGHT}`cat $pidfile`${RES}"

echo "------------------------------------------------"
echo -e "${SUCCESS}程序在运行${RES}"
echo ""