#!/bin/bash
# Source function library.
. /etc/init.d/functions

DANGER='\E[1;31m'  #红
SUCCESS='\E[1;32m' #绿
WARN='\E[1;33m' #黄
INFO='\E[1;36m'  #蓝
HIGHLIGHT='\E[1;35m'      #粉红
RES='\E[0m'

prog="__APPID__ __APPVERSION__"
RETVAL=0
cd `dirname $0`
current_path=`pwd`
prog_user=`cat $current_path/../CONFIG/LINUX_USER`

if [ -z $prog_user ]; then
    echo -e "${DANGER}请配置该程序的执行用户${RES}"
    exit 1
fi

start() {
    echo $"Starting $prog: "
    su $prog_user -c "$current_path/../start.sh"
    RETVAL=$?
    return $RETVAL
}

stop() {
    echo -n $"Stopping $prog: "
    su $prog_user -c "$current_path/../stop.sh"
    RETVAL=$?
    return $RETVAL
}

restart() {
    echo -n $"Restart $prog: "
    su $prog_user -c "$current_path/../restart.sh"
    RETVAL=$?
    return $RETVAL
}

status() {
    echo -n $"Status $prog: "
    su $prog_user -c "$current_path/../status.sh"
    RETVAL=$?
    return $RETVAL
}

case "$1" in
    start)
            start
            ;;
    stop)
            stop
            ;;
    restart)
            restart
            ;;
    status)
            status
            ;;
	dump)
		dump
		;;  
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        RETVAL=2
        ;;
esac
exit $RETVAL

