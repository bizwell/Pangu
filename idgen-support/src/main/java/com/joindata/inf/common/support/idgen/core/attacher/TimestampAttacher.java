package com.joindata.inf.common.support.idgen.core.attacher;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.joindata.inf.common.basic.cst.PanguEra;
import com.joindata.inf.common.support.idgen.core.IDGeneratorException;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.basic.StringUtil;

import lombok.Getter;

// TODO 记得补文档，补得清楚点
public class TimestampAttacher
{
    private TimestampKeeper timestampKeeper;

    public TimestampAttacher(int offset)
    {
        this.timestampKeeper = new TimestampKeeper(offset);

        String tomorrow = DateUtil.formatDateTime(DateUtil.plusDays(DateUtil.getCurrentDate(), 1));
        new Timer().schedule(timestampKeeper, DateUtil.parseDate(tomorrow), DateUtil.DAY_MILLIS - 1);
    }

    /**
     * 为指定值添加前缀
     * 
     * @param seq 序列值
     * @return 加了时间前缀的序列值
     */
    public long attach(long seq)
    {
        long val = seq + this.timestampKeeper.getTimestampValue();

        if(val < 0)
        {
            throw new IDGeneratorException("ID 不够用了");
        }

        return val;
    }

    @Getter
    private class TimestampKeeper extends TimerTask
    {
        private long dayDuration;

        public long timestampValue;

        public TimestampKeeper(int offset)
        {
            this.dayDuration = Long.parseLong(1 + StringUtil.makeRepeat('0', offset));
            this.timestampValue = this.dayDuration + Long.parseLong(DateUtil.getDaysBetween(PanguEra.BASE_DATE, new Date()) + StringUtil.makeRepeat('0', offset));
        }

        @Override
        public void run()
        {
            this.timestampValue = this.timestampValue + this.dayDuration;
        }
    }
}
