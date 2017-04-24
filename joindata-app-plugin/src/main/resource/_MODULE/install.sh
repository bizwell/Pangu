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

addedconfigfile=""
depredconfigfile=""

userconfigfile=$targetconfigdir/LINUX_USER
userconfigfile2=$appconfigdir/LINUX_USER

quiet='N'
restartapp='N'
appuser=''

tmpdir="/data/tmp/__APPID__/__APPVERSION__"
logdir="/data/log/__APPID__"
mkdir -p $tmpdir
mkdir -p $logdir

echo -e "${SUCCESS}准备安装${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
# 可用选项
while getopts "qru:" arg
do
  case $arg in
    q)
      quiet='Y'
      export quiet=$quiet
      echo -e "$INFO静默选项: $RES将覆盖安装"
      ;;
    r)
      echo -e "$INFO重启指令: $RES将重启应用"
      restartapp='Y'
      ;;
    u)
      appuser=$OPTARG
      echo -e "$INFO指定用户: $RES$OPTARG"
      ;;
  esac
done
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
	  sure=$quiet
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
# 先清理旧的目录，毛都不剩
rm -r $appdir/__APPVERSION__
mkdir -p $appdir
cp -Rfp __APPVERSION__ $appdir
chmod 500 $appdir/__APPVERSION__/*.sh
chmod 500 $appdir/__APPVERSION__/init.d/*.sh
echo -e "${SUCCESS}				OK${RES}"

# 处理配置文件钩子
echo -en "${INFO}---(STEP 2 )${RES} 处理配置文件钩子..."
mkdir -p $targetconfigdir
mkdir -p $targetverconfigdir
appcfgs=`ls $appconfigdir`
# 如果有新的配置文件，复制到配置钩子文件夹，生成警告
for appcfg in $appcfgs
do
	if [ ! -f "$targetconfigdir/$appcfg" ]; then
		cp $appconfigdir/$appcfg $targetconfigdir/
		addedconfigfile="$addedconfigfile $targetconfigdir/$targetcfg"
	fi
done
targetcfgs=`ls $targetconfigdir/`
# 如果没有该钩子，生成警告
for targetcfg in $targetcfgs
do
	if [ ! -f "$appconfigdir/$targetcfg" ]; then
		if [ ! -d "$targetconfigdir/$targetcfg" ]; then
			depredconfigfile="$depredconfigfile $targetconfigdir/$targetcfg"
		fi
	fi
done
echo -e "${SUCCESS}			OK${RES}"

# 设置用户
inputuser=$appuser
echo "------------------------------------------------------------"
while [ -z "$inputuser" ]
do
	echo -en "${WARN}运行该程序的用户: ${RES}"
	read inputuser
done

id $inputuser >& /dev/null
if [ $? -ne 0 ]
then
   useradd -M $inputuser
fi
echo -en "${INFO}---(STEP 3 )${RES} 配置权限..."
chown -R $inputuser $appdir/__APPVERSION__/
chown -R $inputuser $tmpdir
chown -R $inputuser $logdir
chmod -R 771 $piddir
chown -R $inputuser $targetconfigdir/ $appconfigdir/
chown $inputuser $pidfile
echo $inputuser > $userconfigfile
echo $inputuser > $userconfigfile2
echo -e "${SUCCESS}				OK${RES}"
echo "------------------------------------------------------------"

echo -e "${SUCCESS}已安装${RES}"
echo ""
echo -e "程序目录: ${HIGHLIGHT}$appdir/__APPVERSION__${RES}"
echo -e "配置目录: ${HIGHLIGHT}$targetconfigdir${RES}"
echo -e "日志目录: ${HIGHLIGHT}$logdir${RES}"
echo -e "临时目录: ${HIGHLIGHT}$tmpdir${RES}"
echo -e "版本专有配置目录: ${HIGHLIGHT}$targetverconfigdir${RES}"

if [ ! -z "$depredconfigfile" ]; then
	echo ""
	echo -e "${WARN}注意: 以下配置文件可能已过期，请务必处理，以免误读${RES}"
	for cfgfile in $depredconfigfile
	do
		echo -e "- ${HIGHLIGHT}$cfgfile${RES}"
	done
fi

if [ ! -z "$addedconfigfile" ]; then
	echo ""
	echo -e "${WARN}注意: 以下配置文件是新增的，请确认是否需要编辑${RES}"
	for cfgfile in $addedconfigfile
	do
		echo -e "- ${HIGHLIGHT}$cfgfile${RES}"
	done
fi

if [ ! -z `ls $targetverconfigdir/*_OPTS 2>/dev/null`  ]; then
	echo ""
	echo -e "${WARN}注意: 该版本包含专有配置文件，请确认这些文件是否影响此次运行${RES}"
	for cfgfile in `ls $targetverconfigdir/*_OPTS`
	do
		echo -e "- ${HIGHLIGHT}$cfgfile${RES}"
	done
fi

echo ""
echo -e "${INFO}现在可以做这些事: ${RES}"
echo -e "修改程序用户: ${HIGHLIGHT}$targetconfigdir/LINUX_USER${RES}"
echo -e "修改添加启动参数: ${HIGHLIGHT}$targetconfigdir/*_OPTS${RES}"
echo -e "修改添加该版本专有的启动参数: ${HIGHLIGHT}$targetverconfigdir/*_OPTS${RES}"
echo -e "通过服务脚本启动程序: ${HIGHLIGHT}$appdir/__APPVERSION__/init.d/service.sh${RES}"

if [ $restartapp == 'Y' ]; then
  $appdir/__APPVERSION__/init.d/service.sh restart
fi