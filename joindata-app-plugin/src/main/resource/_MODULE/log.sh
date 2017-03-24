#!/bin/bash

DANGER='\E[1;31m'  #红
SUCCESS='\E[1;32m' #绿
WARN='\E[1;33m' #黄
INFO='\E[1;36m'  #蓝
HIGHLIGHT='\E[1;35m'      #粉红
RES='\E[0m'

dir=$(cd "$(dirname "`readlink -f $0`")"; pwd)
cd $dir

case "$1" in
	log)
    	logfile="/data/log/__APPID__/DEFAULT/ROLLING.log"
    	logname="运行日志"
    	;;
	out)
    	logfile="/data/tmp/__APPID__/__APPVERSION__/nohup.out"
    	logname="标准输出"
    	;;
	err|error)
    	logfile="/data/tmp/__APPID__/__APPVERSION__/nohup.err"
    	logname="标准错误输出"
    	;;
    "")
    	echo -e "${WARN}将默认查看运行日志${RES}"
    	logfile="/data/log/__APPID__/DEFAULT/ROLLING.log"
    	logname="运行日志"
    	;;
    *)
    	exit 1
        ;;
esac

echo -e "查看${INFO}${logname}${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
echo -e "文件: ${INFO}${logfile}${RES}"
echo "------------------------------------------------"
tail -30f $logfile