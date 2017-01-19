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

appdir=/opt/app/product/__APPID__
appconfigdir=$appdir/__APPVERSION__/CONFIG
targetconfigdir=/var/config/__APPID__/__APPVERSION__

echo -e "${SUCCESS}准备安装${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
echo "------------------------------------------------------------"

echo -en "${INFO}---(STEP 0 )${RES} 检查环境..."
# 判断是否在运行
piddir="/var/run/__APPID__"
pidfile="$piddir/__APPVERSION__.pid"
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
          echo -en "${WARN}---(STEP - )${RES} 是否强制停止应用并继续安装(Y/N)? 		"
          read sure
      done
      
      if [ "Y" == "$sure" ]; then
        kill $pid
        echo -en "${WARN}---(STEP - )${RES} 停止应用..."
      else
        echo "------------------------------------------------------------"
        echo -e "${WARN}已终止安装${RES}"
        exit 1;
      fi
  fi
fi
echo -e "${SUCCESS}				OK${RES}"

# 复制应用程序文件夹
echo -en "${INFO}---(STEP 1 )${RES} 复制程序文件..."
mkdir -p $appdir
cp -Rfp __APPVERSION__ $appdir
chmod 100 $appdir/__APPVERSION__/*.sh
echo -e "${SUCCESS}				OK${RES}"

# 创建配置文件软链接
echo -en "${INFO}---(STEP 2 )${RES} 创建配置文件软链接..."
mkdir -p $targetconfigdir
rm -f $targetconfigdir/*
ln -s $appconfigdir/* $targetconfigdir/
echo -e "${SUCCESS}			OK${RES}"
echo "------------------------------------------------------------"

echo -e "${SUCCESS}已安装${RES}"
echo ""
echo -e "程序目录: ${HIGHLIGHT}$appdir/__APPVERSION__${RES}"
echo -e "配置目录: ${HIGHLIGHT}$targetconfigdir${RES}"