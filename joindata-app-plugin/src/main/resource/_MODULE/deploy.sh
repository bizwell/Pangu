#!/bin/bash

dir=$(cd "$(dirname "`readlink -f $0`")"; pwd)
cd $dir

DANGER='\E[1;31m'  #红
SUCCESS='\E[1;32m' #绿
WARN='\E[1;33m' #黄
INFO='\E[1;36m'  #蓝
HIGHLIGHT='\E[1;35m'      #粉红
RES='\E[0m'

app=__APPID__-__APPVERSION__
duser="root"

usage="$INFO用法:$RES $0 $HIGHLIGHT-d$RES [部署应用的用户,不指定就是 root] $HIGHLIGHT-u$RES [执行应用的用户] $HIGHLIGHT-h$RES [部署到哪台服务器] [$HIGHLIGHT-r$RES(部署完成后是否启动/重启)]"

if [ $# == 0 ] || [ ${1:0:1} != '-' ]; then
	echo -e "$usage"
	exit 1;
fi

echo -e "$INFO准备部署$RES"

cat deploy-logo

echo -e "$WARN提示: $RES本程序以静默方式部署应用，不保证每个环节可以顺利完成，能否部署成功取决于本地和远程服务器操作系统环境，请关注程序输出以确保部署过程无误"

restartenable=''

echo "------------------------------------------"
while getopts "d:a:u:h:r" arg #选项后面的冒号表示该选项需要参数
do
	case $arg in
		d)
			duser=$OPTARG
			echo -e "部署用户: $INFO$OPTARG$RES"
			;;
		a)
			app=$OPTARG
			echo -e "应用: $INFO$OPTARG$RES"
			;;
		u)
			appuser=$OPTARG
			echo -e "应用程序用户: $INFO$OPTARG$RES"
			;;
		h)
			host=$OPTARG
			echo -e "部署至服务器: $INFO$OPTARG$RES"
			;;
		r)
			restartenable='-r'
			echo -e "$INFO部署完成后将启动/重启应用$RES"
			;;
		?)
			echo -e "$DANGER包含不能识别的参数$RES"
			echo -e "$usage"
			exit 1
			;;
	esac
done
echo "------------------------------------------"


remoteok=$(ssh -l $duser -o BatchMode=yes -o ConnectTimeout=5 $host echo Y 2>&1)
if [ "$remoteok" != 'Y' ]; then
	echo -e "$DANGER无法通过免密方式登录到 $duser@$host，部署终止$RES"
	echo -e "$INFO提示: $RES可以执行$HIGHLIGHT ssh-copy-id $duser@$host $RES来配置免密登录"
	exit 1;
fi

disconfexists=$(ssh $duser@$host "if [ -f /var/config/_DISCONF_OPTS ]; then echo Y; fi;" 2>&1)
if [ "$disconfexists" != 'Y' ]; then
	echo -e "$DANGER远程服务器不存在 /var/config/_DISCONF_OPTS 文件，请先创建并编辑$RES"
	exit 1;
fi

echo -e "安装程序将保存在$HIGHLIGHT $duser@$host:~/$app $RES目录下"
echo ""

echo -e "$INFO正在上传程序...$RES"
rsync --progress $app.zip $duser@$host:~/$app/
echo ""

echo -e "$INFO远程安装...$RES"
ssh $duser@$host -t "cd $app; rm -rf */* *.sh; unzip -qo $app.zip; chmod 750 *.sh; ./install.sh -q $restartenable -u $appuser"
echo -e "$SUCCESS部署完成$RES"
