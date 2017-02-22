package studio.wetrack.messageService.support.email.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

abstract class SendMail{
	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);
    protected BodyPart messageBodyPart = null;
    protected Multipart multipart = null;
    protected MimeMessage mailMessage = null;
    protected Session mailSession = null;
    protected Properties mailProperties = System.getProperties();

    protected InternetAddress mailFromAddress = null;
    protected InternetAddress mailToAddress = null;
    protected MailAuthenticator authenticator = null;

    protected String mailSubject ="";
    protected Date mailSendDate = null;
    // 附件路径
    protected String fileDataSource;

    public SendMail(String smtpHost,String username,String password){

        mailProperties.put("mail.smtp.host",smtpHost);
        mailProperties.put("mail.smtp.auth","true"); //设置smtp认证，很关键的一句
        authenticator = new MailAuthenticator(username,password);
        mailProperties.put("mail.smtp.localhost", "localhost");
        mailSession = Session.getDefaultInstance(mailProperties,authenticator);
        mailMessage = new MimeMessage(mailSession);
        messageBodyPart = new MimeBodyPart();
    }
    //设置邮件主题
    public void setSubject(String mailSubject,String encodeing)throws MessagingException{
        this.mailSubject = mailSubject;
        mailMessage.setSubject(mailSubject,encodeing);
    }
    //所有子类都需要实现的抽象方法，为了支持不同的邮件类型
    protected abstract void setMailContent(String mailContent ,String encodeing)throws MessagingException;

    //设置邮件发送日期
    public void setSendDate(Date sendDate)throws MessagingException{
        this.mailSendDate = sendDate;
        mailMessage.setSentDate(sendDate);
    }

    //设置发件人地址
    public void setMailFrom(String mailFrom)throws MessagingException{
        mailFromAddress = new InternetAddress(mailFrom);
        mailMessage.setFrom(mailFromAddress);
    }
    //设置收件人地址，收件人类型为to,cc,bcc(大小写不限)
    public void setMailTo(String[] mailTo,javax.mail.Message.RecipientType mailType)throws Exception{
        for(int i=0;i<mailTo.length;i++){
            mailToAddress = new InternetAddress(mailTo[i]);
            mailMessage.addRecipient(mailType,mailToAddress);
        }
    }
    //开始投递邮件
    public void sendMail()throws MessagingException,SendFailedException{
        if(mailToAddress == null){
        	logger.info("请你必须你填写收件人地址！");
//            System.exit(1);
        }
        else{
            mailMessage.setContent(multipart);
            logger.info("正在发送邮件，请稍候.......");
            Transport.send(mailMessage);
            logger.info("恭喜你，邮件已经成功发送!");
        }
    }
	public String getFileDataSource() {
		return fileDataSource;
	}
	public void setFileDataSource(String fileDataSource) {
		this.fileDataSource = fileDataSource;
	}

}
