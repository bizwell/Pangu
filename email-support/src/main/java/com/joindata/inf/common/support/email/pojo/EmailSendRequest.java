package com.joindata.inf.common.support.email.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 
 * 邮件发送请求类
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-28
 */
@Data
public class EmailSendRequest {

	private EmailTemplate emailTemplate;
	/** 接收者地址列表 */
	private List<String> recAddress = new ArrayList<String>();
	private String replyEmail;

	/**
	 * 抄送地址列表
	 */
	private List<String> ccAddress = new ArrayList<String>();
	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件附件
	 */
	private Map<String, byte[]> attachments = new HashMap<String, byte[]>();
	
	/**
	 * 不使用模板时的邮件内容
	 */
	private String contentTxt;
	
	
}
