package com.joindata.inf.common.support.mybatis.support.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * 数据源配置变量类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月1日 上午11:40:23
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "jdbc.properties")
public class DataSourceProperties
{
    /** URL */
    private String url;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 初始化大小 */
    private int initialSize;

    /** 最小空闲数 */
    private int minIdle;

    /** 最大连接数 */
    private int maxActive;

    /** 连接等待超时时间 */
    private long maxWait;

    /** 检测关闭空闲连接的间隔时间毫秒数 */
    private int timeBetweenEvictionRunsMillis;

    /** 最小生存时间毫秒数 */
    private int minEvictableIdleTimeMillis;

    /** 验证语句 */
    private String validationQuery;

    /** 是否在空闲时检测 */
    private boolean testWhileIdle;

    /** 是否在取得时检测 */
    private boolean testOnBorrow;

    /** 是否在归还时检测 */
    private boolean testOnReturn;

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
    public int getTimeBetweenEvictionRunsMillis()
    {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    @DisconfFileItem(name = "jdbc.minEvictableIdleTimeMillis", associateField = "minEvictableIdleTimeMillis")
    public int getMinEvictableIdleTimeMillis()
    {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis)
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

}
