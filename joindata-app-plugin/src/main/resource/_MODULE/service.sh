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
    if [ "$prog_user" == "`whoami`" ]; then
    	$current_path/../start.sh
    else
    	su $prog_user -c "$current_path/../start.sh"
    fi
    RETVAL=$?
    return $RETVAL
}

stop() {
    echo -n $"Stopping $prog: "
    if [ "$prog_user" == "`whoami`" ]; then
    	$current_path/../stop.sh
    else
    	su $prog_user -c "$current_path/../stop.sh"
    fi
    RETVAL=$?
    return $RETVAL
}

restart() {
    echo -n $"Restart $prog: "
    if [ "$prog_user" == "`whoami`" ]; then
    	$current_path/../restart.sh
    else
    	su $prog_user -c "$current_path/../restart.sh"
    fi
    RETVAL=$?
    return $RETVAL
}

status() {
    echo -n $"Status $prog: "
    if [ "$prog_user" == "`whoami`" ]; then
    	$current_path/../status.sh
    else
    	su $prog_user -c "$current_path/../status.sh"
    fi
    RETVAL=$?
    return $RETVAL
}

log() {
    echo -n $"Tail log of $prog: "
    if [ "$prog_user" == "`whoami`" ]; then
    	$current_path/../log.sh
    else
    	su $prog_user -c "$current_path/../log.sh"
    fi
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
    log)
            log
            ;;
	dump)
		dump
		;;  
    *)
        echo $"Usage: $0 {start|stop|restart|status|log}"
        RETVAL=2
        ;;
esac
exit $RETVAL

