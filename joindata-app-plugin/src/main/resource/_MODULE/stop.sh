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

piddir="/var/run/__APPID__"
pidfile="/var/run/__APPID__/__APPVERSION__.pid"


echo -e "${SUCCESS}准备停止${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
echo "------------------------------------------------"

mkdir -p $piddir
if [ ! -f $pidfile ]; then
  touch $pidfile
fi

pid=`cat $pidfile`
# 判断 PID 是否存在
if [ -z "$pid" ]; then
  echo "" > $pidfile
  echo "找不到进程号, 请确认是否启动"
  echo "------------------------------------------------"
  echo -e "${DANGER}停止失败${RES}"
  echo ""
  exit 1;
fi
# 判断进程是否不存在
if ! kill -0 $pid > /dev/null 2>&1; then
  echo "进程不存在, 请确认是否启动"
  echo "------------------------------------------------"
  echo -e "${WARN}无需停止${RES}"
  echo ""
  exit 0;
fi

# 停止
kill $pid
# 输出提示
echo -e "PID: ${HIGHLIGHT}`cat $pidfile`${RES}"

echo "------------------------------------------------"

i=1
while :
do
  if kill -0 $pid > /dev/null 2>&1; then
    case $i in
      1) echo -en "${INFO}正在停止.  ${RES}\r";;
      2) echo -en "${INFO}正在停止.. ${RES}\r";;
      3) echo -en "${INFO}正在停止...${RES}\r";;
      *) echo -en "${INFO}正在停止...${RES}\r";;
    esac

    if [ $i -eq 3 ]; then
      i=1;
    else
      i=`expr $i + 1`
    fi

    sleep 0.6;
  else
    break;
  fi

done

echo "" > $pidfile

echo -e "${SUCCESS}已停止     ${RES}"
echo ""