package com.joindata.inf.common.support.mybatis.bootconfig;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.mybatis.support.CustomVfs;

/**
 * MyBatis 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午10:32:18
 */
@Configuration
public class MyBatisConfig
{
    @Autowired
    private DataSource dataSource;

    static
    {
        System.setProperty("druid.logType", "log4j2");
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean()
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setVfs(CustomVfs.class);

        return bean;
    }
}