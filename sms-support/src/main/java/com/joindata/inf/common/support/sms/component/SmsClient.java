package com.joindata.inf.common.support.sms.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.support.sms.bootconfig.SmsConfig;
import com.joindata.inf.common.support.sms.pojo.SmsTemplete;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.tools.UuidUtil;
import com.niwodai.inf.notification.client.JmsSender;
import com.niwodai.inf.notification.client.request.SMSNotifyRequest;

/**
 * 发送短信客户端
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 22, 2017 3:07:38 PM
 */
@Component
public class SmsClient
{
    private static final Logger log = Logger.get();

    @Autowired
    private JmsSender jmsSender;

    /**
     * 通过模板发送短信
     * 
     * @param template 短信模板实例
     * @param mobile 目标手机号，可以传多个
     */
    public void send(SmsTemplete template, String... mobile)
    {
        log.info("发送短信给 {} 个手机, 使用模板: {}, 参数: {}", mobile.length, template.getTemplateId(), template.getData());

        SMSNotifyRequest request = new SMSNotifyRequest();
        request.setAppid(SmsConfig.getSystemId());
        request.setSendChannel(SmsConfig.getDefaultChannel());
        request.setTemplateId(template.getTemplateId());
        request.setData(template.getData());
        for(String mobileItem: mobile)
        {
            request.setSerialNumber(UuidUtil.make());
            request.addMobile(mobileItem);
        }

        String result = jmsSender.send(request);

        log.info("发送结果：{}", result);
    }

    /**
     * 发送短信，不使用模板
     * 
     * @param content 短信内容
     * @param mobile 目标手机号，可以填写多个
     */
    public void send(String content, String... mobile)
    {
        log.info("发送短信给 {} 个手机, 内容: {}", mobile.length, content);

        SMSNotifyRequest request = new SMSNotifyRequest();
        request.setAppid(SmsConfig.getSystemId());
        request.setSendChannel(SmsConfig.getDefaultChannel());
        for(String mobileItem: mobile)
        {
            request.setSerialNumber(UuidUtil.make());
            request.addMobile(mobileItem);
            request.addContent(content);
        }

        String result = jmsSender.send(request);

        log.info("发送结果：{}", result);
    }
}
