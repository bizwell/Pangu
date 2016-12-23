package com.joindata.inf.common.basic.stereotype;

import javax.servlet.Filter;

/**
 * Filter 包装类，这里面可以指定 Filter 的一些参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 10:06:22 AM
 */
public abstract class WebFilterWrapper
{
    private String name;

    private String mapping;

    private Filter filter;

    protected WebFilterWrapper(String name, String mapping, Filter filter)
    {
        this.name = name;
        this.mapping = mapping;
        this.filter = filter;
    }

    public String getName()
    {
        return name;
    }

    public String getMapping()
    {
        return mapping;
    }

    public Filter getFilter()
    {
        return filter;
    }
}
