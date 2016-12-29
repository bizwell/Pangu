package com.joindata.inf.common.support.sso.entity;

import com.joindata.inf.common.util.basic.StringUtil;

/**
 * 资源模型
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 20, 2016 5:11:58 PM
 */
public class Resource
{
    /** 资源 ID */
    private String resourceId;

    /** 资源名称 */
    private String resourceName;

    /** 资源值 */
    private String resourceValue;

    public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }

    public String getResourceName()
    {
        return resourceName;
    }

    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    public String getResourceValue()
    {
        return resourceValue;
    }

    public void setResourceValue(String resourceValue)
    {
        this.resourceValue = resourceValue;
    }

    /**
     * 创建一个 Resource，通过 path 来初始化<br />
     * <i>将没有 id 和 name</i><br />
     * <i>如果用来判断请求路径是很有用的</i>
     * 
     * @param path 路径
     * @return Resource 对象
     */
    public static final Resource ofPath(String path)
    {
        Resource resource = new Resource();
        resource.setResourceValue(path);
        return resource;
    }

    @Override
    public int hashCode()
    {
        if(this.resourceValue == null)
        {
            return super.hashCode();
        }

        return resourceValue.hashCode();
    }

    /**
     * 如果传入字符串或 Resource，判定 resourceValue 是否与其相等
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }

        if(obj instanceof String)
        {
            return StringUtil.isEquals(this.resourceValue, (String)obj);
        }

        if(obj instanceof Resource)
        {
            return StringUtil.isEquals(this.resourceValue, ((Resource)obj).getResourceValue());
        }

        return super.equals(obj);
    }

    @Override
    public String toString()
    {
        return this.resourceId + "(" + this.resourceName + "): " + this.resourceValue;
    }
}
