package com.joindata.inf.common.support.sms.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信模板参数
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 22, 2017 1:49:36 PM
 */
public class SmsTemplete
{
    private int templateId;

    private Map<String, String> data;

    private SmsTemplete(int templateId)
    {
        this.templateId = templateId;
        this.data = new HashMap<>();
    }

    public static final SmsTemplete of(int templateId)
    {
        return new SmsTemplete(templateId);
    }

    /**
     * 添加参数
     * 
     * @param name 参数名
     * @param value 参数值
     * @return 返回参数对象，方便链式调用
     */
    public SmsTemplete withParam(String name, String value)
    {
        this.data.put(name, value);
        return this;
    }

    /** 获取模板 ID */
    public int getTemplateId()
    {
        return templateId;
    }

    /** 获取参数 Map */
    public Map<String, String> getData()
    {
        return data;
    }

}
