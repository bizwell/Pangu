package com.joindata.inf.common.support.idgen.bootconfig;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.idgen.core.IdRangeFactory;
import com.joindata.inf.common.support.idgen.core.SequenceRepository;
import com.joindata.inf.common.support.idgen.core.SequenceRepositoryZookeeper;
import com.joindata.inf.common.support.idgen.core.attacher.PrefixAttacher;
import com.joindata.inf.common.support.idgen.core.attacher.TimestampAttacher;
import com.joindata.inf.common.support.idgen.properties.IdgenProperties;
import com.joindata.inf.common.util.log.Logger;

/**
 * ID 生成器配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 1:03:18 PM
 */
@Configuration
public class IdgenConfig
{
    private static final Logger log = Logger.get();

    public static final String ZK_CLIENT_BEAN_NAME = "idgen-zkClient";

    @Autowired
    private IdgenProperties properties;

    @Bean(name = ZK_CLIENT_BEAN_NAME, destroyMethod = "close")
    public CuratorFramework zkClient()
    {
        log.info("zkHOSTS: {}", properties.getZkHosts());
        return CuratorFrameworkFactory.newClient(properties.getZkHosts(), new RetryNTimes(10, 5000));
    }

    @Bean(name = "sequenceRepositoryZookeeper")
    public SequenceRepository sequenceRepository()
    {
        return new SequenceRepositoryZookeeper(zkClient());
    }

    @Bean("idRangeFactory")
    public IdRangeFactory idRangeFactory()
    {
        IdRangeFactory idRangeFactory = new IdRangeFactory(sequenceRepository());
        idRangeFactory.setIdgenProperties(properties);
        return idRangeFactory;
    }

    /**
     * 12 位 Padding 的前缀附加器
     */
    @Bean("prefixAttacherOffset12")
    public PrefixAttacher prefixAttacherOffset12()
    {
        return new PrefixAttacher(12);
    }

    /**
     * 17 位 Padding 的前缀附加器
     */
    @Bean("prefixAttacherOffset17")
    public PrefixAttacher prefixAttacherOffset17()
    {
        return new PrefixAttacher(17);
    }

    /**
     * 17 位 Padding 的前缀附加器
     */
    @Bean("prefixAttacherOffset10")
    public PrefixAttacher prefixAttacherOffset10()
    {
        return new PrefixAttacher(10);
    }

    /**
     * 14 位的时间戳前缀附加器<br />
     * <i>由于时间戳永远在最前面，所以用这个默认的即可</i>
     */
    @Bean
    public TimestampAttacher timestampAttacher()
    {
        return new TimestampAttacher(14);
    }
}