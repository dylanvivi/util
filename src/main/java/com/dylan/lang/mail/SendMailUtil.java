package com.dylan.lang.mail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;

public class SendMailUtil {
	/**
	 * 用apache commons-email 发送邮件
	 * 依赖jar：commons-email.jar，activation.jar，mail.jar
	 */
	public static void sendMutiMessage(String subject, String content, String[] toMailAddr, String... ccAddr) {
		MultiPartEmail email = new MultiPartEmail();
		try {
			setEmail(email, subject, ccAddr, toMailAddr);
			// 要发送的信息
			email.setMsg(content);
			// 发送
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void sendMutiMessage(String fileName, String title, String content, String[] toMailAddr,
			String... ccAddr) {
		HtmlEmail email = new HtmlEmail();
		String[] multiPaths = new String[] { fileName + ".xls" };
		List<EmailAttachment> list = new ArrayList<EmailAttachment>();
		for (int i = 0; i < multiPaths.length; i++) {
			EmailAttachment attachment = new EmailAttachment();
			// 判断当前这个文件路径是否在本地 如果是：setPath 否则 setURL;
			if (multiPaths[i].indexOf("http") == -1) {
				attachment.setPath(multiPaths[i]);
			} else {
				try {
					attachment.setURL(new URL(multiPaths[i]));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription(fileName.substring(fileName.lastIndexOf("/") + 1) + ".xls");
			attachment.setName(fileName.substring(fileName.lastIndexOf("/") + 1) + ".xls");
			list.add(attachment);
		}
		try {
			setEmail(email, title, ccAddr, toMailAddr);
			// 要发送的信息
			email.setHtmlMsg(content);
			// 添加多个附件
			for (int i = 0; i < list.size(); i++) {
				email.attach(list.get(i));
			}
			// 发送
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void sendMutiMessageHtml(String subject, String content, String[] toMailAddr, String... ccAddr) {
		HtmlEmail email = new HtmlEmail();
		try {
			setEmail(email, subject, ccAddr, toMailAddr);
			email.setHtmlMsg(content);
			// 发送
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void sendMutiMessageHtml(String subject, String content, String toMailAddr) {
		HtmlEmail email = new HtmlEmail();
		try {
			setEmail(email, subject, null, toMailAddr);
			// 要发送的信息
			email.setHtmlMsg(content);
			// 发送
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void setEmail(Email email, String subject, String[] ccAddr, String... toMailAddr)
			throws EmailException {
		// 这里是发送服务器的名字：
		email.setHostName(Constants.HOST_NAME);
		// 编码集的设置
		email.setCharset("utf-8");
		// 收件人的邮箱
		for (String addr : toMailAddr) {
			email.addTo(addr);
		}
		// 发送人的邮箱
		email.setFrom(Constants.MAIL_FROM_ADDR);
		// 抄送人的邮箱
		if (ccAddr != null && ccAddr.length > 0) {
			for (String cc : ccAddr) {
				email.addCc(cc);
			}
		}
		// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
		email.setAuthentication(Constants.USER_NAME, Constants.PASSWORD);
		email.setSubject(subject);

	}
}