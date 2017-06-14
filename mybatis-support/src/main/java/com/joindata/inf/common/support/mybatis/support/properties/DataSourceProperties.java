package com.joindata.inf.common.support.mybatis.support.properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.joindata.inf.common.sterotype.jdbc.sterotype.BaseDataSourceProperties;

/**
 * 数据源配置变量类<br />
 * <i>Disconf 我草你妈！</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 上午11:40:23
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "jdbc.properties")
public class DataSourceProperties extends BaseDataSourceProperties
{
    /** 数据源名称 */
    protected String name;

    /** URL */
    protected String url;

    /** 用户名 */
    protected String username;

    /** 密码 */
    protected String password;

    /** 初始化大小 */
    protected int initialSize;

    /** 最小空闲数 */
    protected int minIdle;

    /** 最大连接数 */
    protected int maxActive;

    /** 连接等待超时时间 */
    protected long maxWait;

    /** 检测关闭空闲连接的间隔时间毫秒数 */
    protected long timeBetweenEvictionRunsMillis;

    /** 最小生存时间毫秒数 */
    protected long minEvictableIdleTimeMillis;

    /** 验证语句 */
    protected String validationQuery;

    /** 是否在空闲时检测 */
    protected boolean testWhileIdle;

    /** 是否在取得时检测 */
    protected boolean testOnBorrow;

    /** 是否在归还时检测 */
    protected boolean testOnReturn;

    @DisconfFileItem(name = "jdbc.url", associateField = "url")
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @DisconfFileItem(name = "jdbc.username", associateField = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @DisconfFileItem(name = "jdbc.password", associateField = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @DisconfFileItem(name = "jdbc.initialSize", associateField = "initialSize")
    public int getInitialSize()
    {
        return initialSize;
    }

    public void setInitialSize(int initialSize)
    {
        this.initialSize = initialSize;
    }

    @DisconfFileItem(name = "jdbc.minIdle", associateField = "minIdle")
    public int getMinIdle()
    {
        return minIdle;
    }

    public void setMinIdle(int minIdle)
    {
        this.minIdle = minIdle;
    }

    @DisconfFileItem(name = "jdbc.maxActive", associateField = "maxActive")
    public int getMaxActive()
    {
        return maxActive;
    }

    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }

    @DisconfFileItem(name = "jdbc.maxWait", associateField = "maxWait")
    public long getMaxWait()
    {
        return maxWait;
    }

    public void setMaxWait(long maxWait)
    {
        this.maxWait = maxWait;
    }

    @DisconfFileItem(name = "jdbc.timeBetweenEvictionRunsMillis", associateField = "timeBetweenEvictionRunsMillis")
    public long getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @DisconfFileItem(name = "jdbc.minEvictableIdleTimeMillis", associateField = "minEvictableIdleTimeMillis")
    public long getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis)
    {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    @DisconfFileItem(name = "jdbc.validationQuery", associateField = "validationQuery")
    public String getValidationQuery()
    {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery)
    {
        this.validationQuery = validationQuery;
    }

    @DisconfFileItem(name = "jdbc.testWhileIdle", associateField = "testWhileIdle")
    public boolean isTestWhileIdle()
    {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle)
    {
        this.testWhileIdle = testWhileIdle;
    }

    @DisconfFileItem(name = "jdbc.testOnBorrow", associateField = "testOnBorrow")
    public boolean isTestOnBorrow()
    {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    @DisconfFileItem(name = "jdbc.testOnReturn", associateField = "testOnReturn")
    public boolean isTestOnReturn()
    {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn)
    {
        this.testOnReturn = testOnReturn;
    }

    @Override
    public DataSource toDataSource()
    {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(this.getUrl());
        ds.setUsername(this.getUsername());
        ds.setPassword(this.getPassword());
        ds.setInitialSize(this.getInitialSize());
        ds.setMinIdle(this.getMinIdle());
        ds.setMaxActive(this.getMaxActive());
        ds.setMaxWait(this.getMaxWait());
        ds.setTimeBetweenEvictionRunsMillis(this.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(this.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(this.getValidationQuery());
        ds.setTestWhileIdle(this.isTestWhileIdle());
        ds.setTestOnBorrow(this.isTestOnBorrow());
        ds.setTestOnReturn(this.isTestOnReturn());

        return ds;
    }
}
