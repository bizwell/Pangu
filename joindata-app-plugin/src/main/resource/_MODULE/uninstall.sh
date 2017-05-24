#!/bin/bash

DANGER='\E[1;31m'  #红
SUCCESS='\E[1;32m' #绿
WARN='\E[1;33m' #黄
INFO='\E[1;36m'  #蓝
HIGHLIGHT='\E[1;35m'      #粉红
RES='\E[0m'

clear
echo ""

dir=$(cd "$(dirname "`readlink -f $0`")"; pwd)
cd $dir

appdir=/opt/__APPID__
appconfigdir=$appdir/__APPVERSION__/CONFIG
targetconfigdir=/var/config/__APPID__
targetverconfigdir=$targetconfigdir/__APPVERSION__
tmpdir="/data/tmp/__APPID__/__APPVERSION__"
logdir="/data/log/__APPID__"

userconfigfile=$targetconfigdir/LINUX_USER
user=`cat $userconfigfile`

echo -e "${SUCCESS}准备卸载${RES}  应用ID __APPID__, 版本号 __APPVERSION__"

# 先确认一把
echo ""
sure=""
while [ -z "$sure" ] || ([ ! $sure == "Y" ] && [ ! $sure = "N" ])
do
    read -p "确定要卸载(Y/N)? " sure
done

if [ "N" == "$sure" ]; then
  echo "------------------------------------------------------------"
  echo -e "${WARN}已终止卸载${RES}"
  exit 1;
fi

echo "------------------------------------------------------------"

echo -en "${INFO}---(STEP 0 )${RES} 检查环境..."
# 判断是否在运行
piddir="/var/run/app/__APPID__"
pidfile="$piddir/pid"
mkdir -p $piddir
if [ ! -f $pidfile ]; then
  touch $pidfile
fi
pid=`cat $pidfile`
if [ ! -z "$pid" ]; then
  if kill -0 $pid > /dev/null 2>&1; then
      echo -e "${DANGER}			程序在运行${RES}"
	  sure=""
      while [ -z "$sure" ] || ([ ! $sure == "Y" ] && [ ! $sure = "N" ])
      do
          echo -en "${WARN}---(STEP - )${RES} 是否强制停止应用并继续卸载(Y/N)? 		"
          read sure
      done
      
      if [ "Y" == "$sure" ]; then
        kill $pid
        echo -en "${WARN}---(STEP - )${RES} 停止应用..."
      else
        echo "------------------------------------------------------------"
        echo -e "${WARN}已终止卸载${RES}"
        exit 1;
      fi
  fi
fi
echo -e "${SUCCESS}				OK${RES}"

# 删除临时文件夹
echo -en "${INFO}---(STEP 1 )${RES} 删除临时文件..."
rm -rf $tmpdir
rm -rf $pidfile
echo -e "${SUCCESS}				OK${RES}"

# 删除应用程序文件夹
echo -en "${INFO}---(STEP 2 )${RES} 删除程序文件..."
rm -rf $appdir/__APPVERSION__
echo -e "${SUCCESS}				OK${RES}"

echo "------------------------------------------------------------"
echo -e "${SUCCESS}已卸载${RES}"
echo ""
echo -e "已移除临时目录: ${HIGHLIGHT}$tmpdir${RES}"
echo -e "已移除程序目录: ${HIGHLIGHT}$appdir/__APPVERSION__${RES}"
echo ""
echo -e "${DANGER}请注意！！！${RES}日志目录、配置文件和程序用户不删除，请自行决定"
echo -e "日志目录: ${HIGHLIGHT}$logdir${RES}"
echo -e "配置目录: ${HIGHLIGHT}$targetconfigdir${RES}"
echo -e "程序用户: ${HIGHLIGHT}$user${RES}"