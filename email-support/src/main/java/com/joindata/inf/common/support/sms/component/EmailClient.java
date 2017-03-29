/**
 * 
 */
package com.joindata.inf.common.support.sms.component;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.support.sms.bootconfig.EmailConfig;
import com.joindata.inf.common.support.sms.pojo.EmailSendRequest;
import com.joindata.inf.common.support.sms.pojo.EmailTemplate;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.tools.UuidUtil;
import com.niwodai.inf.notification.client.email.notification.request.EmailNotifyRequest;
import com.niwodai.inf.notification.client.email.notification.request.ReturnData;
import com.niwodai.inf.notification.client.email.sender.EmailNotifyJmsSender;

/**
 * 
 * 邮件发送客户端
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-28
 */
@Component
public class EmailClient {

	private static final Logger log = Logger.get();

	@Autowired
	private  EmailNotifyJmsSender emailNotifyJmsSender ;

	public void sendEmail(EmailSendRequest emailSendRequest) {
		log.info("发送邮件给{}, 邮件主题: {}", emailSendRequest.getRecAddress(), emailSendRequest.getSubject());
		EmailNotifyRequest request = new EmailNotifyRequest();
		EmailTemplate template = emailSendRequest.getEmailTemplate();
		request.setUsingTemplate(false);
		if (template != null) {
			request.setTemplateId(template.getTemplateId());
			request.setTemplateContents(template.getData());
			
		}
		
		request.setContentText(emailSendRequest.getContentTxt());
		request.setAppid(Integer.parseInt(EmailConfig.getSystemId()));
		request.setAttachments(emailSendRequest.getAttachments());
		request.setRecAddress(emailSendRequest.getRecAddress());
		request.setCcAddress(emailSendRequest.getCcAddress());
		request.setRequestTime(new Date());
		request.setSubject(emailSendRequest.getSubject());
		request.setSerialNumber(UuidUtil.make());
		ReturnData result = emailNotifyJmsSender.send(request);
		log.info("发送邮件通知结果: {}", result);
	}
}
