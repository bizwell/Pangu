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

targetconfigdir=/var/config/__APPID__
targetverconfigdir=$targetconfigdir/__APPVERSION__

cps=$CLASSPATH
jars="__LIBJARS__"
opts=`cat $targetconfigdir/*_OPTS`
if [ ! -z `ls $targetverconfigdir/*_OPTS 2>/dev/null`  ]; then
  opts="$opts `cat $targetverconfigdir/*_OPTS`"
fi

appdir=/opt/__APPID__
tmpdir="/data/tmp/__APPID__/__APPVERSION__"
piddir="/var/run/app/__APPID__"
stdoutfile="$tmpdir/nohup.out"
stderrfile="$tmpdir/nohup.err"
pidfile="$piddir/__APPVERSION__.pid"
disconfOptsfile="/var/config/_DISCONF_OPTS"

# 输出提示
echo -e "${SUCCESS}准备启动${RES}  应用ID __APPID__, 版本号 __APPVERSION__"
echo "------------------------------------------------"

mkdir -p $piddir
if [ ! -f $pidfile ]; then
  touch $pidfile
fi

pid=`cat $pidfile`
# 判断 PID 是否存在并且正在运行
if [ ! -z "$pid" ]; then
  if kill -0 $pid > /dev/null 2>&1; then
	  echo "应用正在运行，请先停掉"
	  echo "------------------------------------------------"
	  echo -e "${DANGER}启动失败${RES}"
	  echo ""
	  exit 1;
  fi
fi

if [ ! -f $disconfOptsfile ]; then
  echo "没有找到 Disconf 配置文件，请先创建 $disconfOptsfile"
  echo "------------------------------------------------"
  echo -e "${DANGER}启动失败${RES}"
  echo ""
  exit 1;
fi
disconfOpts=`cat $disconfOptsfile`

# 环境变量
for j in $jars
do
	cps="$cps:$dir/PROGRAM/lib/$j"
done
cps=$cps:$dir/PROGRAM/__PROG__

# 如果是 cygwin 环境需转换路径写法
if [ `uname -o` = "Cygwin" ]; then
	cps=`cygpath -wp $cps`
fi

mkdir -p $tmpdir

# 启动
nohup $JAVA_HOME/bin/java -server $opts $disconfOpts -classpath $cps __MAINCLASS__ >> $stdoutfile 2>&1 &

# 写 PID
echo $! > $pidfile

echo -e "PID: ${HIGHLIGHT}`cat $pidfile`${RES}"
echo -e "用户: ${HIGHLIGHT}`whoami`${RES}"
echo "------------------------------------------------"
echo -e "${SUCCESS}已启动${RES}"
echo ""

echo -e "标准输出追加到: ${HIGHLIGHT}$stdoutfile${RES}"
echo -e "标准错误追加到: ${WARN}[已重定向至标准输出]${RES}"