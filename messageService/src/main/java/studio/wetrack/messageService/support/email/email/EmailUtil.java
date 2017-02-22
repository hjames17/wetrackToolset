package studio.wetrack.messageService.support.email.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Date;

public class EmailUtil {
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);


	/**
	 *
	 * @param msg 发送的消息内容
	 * @param fileDataSource 如果有附件 则传入，否则传入空值
	 * @param subject 邮件的主题
	 * @param mailTo 邮件发送给谁
	 * @param sender
	 * @throws Exception 要处理的异常，异常情况可以存库或者其他方式通知管理员
	 */
	public static void sendMail(String msg,String fileDataSource,String subject,String mailTo, MailSendAttachment sender) throws Exception{
    	try {
//			sender.setMailFrom(Const.EMAIL_SEND_FROM);
			sender.setSubject(subject, "utf-8");
			sender.setSendDate(new Date());
			sender.setFileDataSource(fileDataSource);
			sender.setMailContent(msg, "utf-8");
			sender.setMailTo(mailTo.split(","), Message.RecipientType.TO);
			sender.sendMail();
		} catch (MessagingException e) {
			logger.error(" 发送邮件失败 !"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error(" 发送邮件失败 !"+e.getMessage());
			throw e;
		}
	}
	public static void main(String[] args) {
		String r = "a, b";
		String[] l = new String[]{r};
		System.out.println(l.toString());
	}
}
