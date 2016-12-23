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
     * 如果传入字符串，判定 resourceValue 是否与其相等
     */
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof String)
        {
            return StringUtil.isEquals(this.resourceValue, (String)obj);
        }

        return super.equals(obj);
    }

}
