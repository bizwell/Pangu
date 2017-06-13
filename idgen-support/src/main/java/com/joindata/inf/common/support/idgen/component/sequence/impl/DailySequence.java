package com.joindata.inf.common.support.idgen.component.sequence.impl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

import com.joindata.inf.common.support.idgen.core.SequenceRepository;
import com.joindata.inf.common.support.idgen.core.attacher.PrefixAttacher;
import com.joindata.inf.common.support.idgen.core.util.IdKeyBuilder;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.log.Logger;

import lombok.Setter;

/**
 * 按天产生的序列号， 每天序列号都从0开始递增
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年6月6日
 */
@Setter
public class DailySequence extends AbstractBaseSequence implements InitializingBean
{
    private static final Logger logger = Logger.get();

    @Resource(name = "prefixAttacherOffset10")
    private PrefixAttacher attacher;

    @Resource(name = "sequenceRepositoryZookeeper")
    private SequenceRepository sequenceRepository;

    private int timePrefix;

    private String originName;

    public DailySequence()
    {
        String tomorrow = DateUtil.formatDate(DateUtil.plusDays(DateUtil.getCurrentDate(), 1));
        new Timer().schedule(new DailyResetSequenceTask(), DateUtil.parseDate(tomorrow), DateUtil.DAY_MILLIS - 1);
    }

    public static void main(String[] args)
    {
        String tomorrow = DateUtil.formatDate(DateUtil.plusDays(DateUtil.getCurrentDate(), 1));
        System.out.println(DateUtil.parseDate(tomorrow));
    }

    @Override
    public long next()
    {
        return attacher.attach(super.increase(), timePrefix);
    }

    private class DailyResetSequenceTask extends TimerTask
    {

        @Override
        public void run()
        {
            resetSequence();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        originName = this.name;
        resetSequence();
    }

    private void resetSequence()
    {
        logger.info("开始重置sequence");
        Date currentDate = new Date();
        String currentDateStr = DateUtil.formatDate(currentDate, DateUtil.UGLY_DATE_FORMAT2);
        setName(getSequenceNameWhitTimeSuffix(currentDate));
        timePrefix = Integer.parseInt(currentDateStr);

        // 清空历史的节点数据
        String historySequenceName = getSequenceNameWhitTimeSuffix(DateUtil.plusDays(currentDate, -1));
        idRangeFactory.clearRange(historySequenceName);
        try
        {
            String dataId = IdKeyBuilder.getSequenceKey(historySequenceName);
            if(sequenceRepository.exist(dataId))
            {
                sequenceRepository.delete(dataId);
            }
        }
        catch(Exception e)
        {
            logger.warn("删除历史节点数据失败", e);
        }

        logger.info("重置sequence完成");
    }

    private String getSequenceNameWhitTimeSuffix(Date date)
    {
        return originName + "-" + DateUtil.formatDate(date, DateUtil.UGLY_DATE_FORMAT2);
    }
}
