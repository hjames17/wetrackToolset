package studio.wetrack.messageService.support.email.email;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

public class MailSendAttachment extends SendMail {

	@Override
	public void setMailContent(String mailContent,String encodeing) throws MessagingException {
		messageBodyPart.setContent(mailContent, "text/html; charset=utf-8");
        multipart.addBodyPart(messageBodyPart);
        if(fileDataSource!=null&&!fileDataSource.equals("")){
        	MimeBodyPart mbp = new MimeBodyPart();
        	FileDataSource fds = new FileDataSource(fileDataSource);
        	mbp.setDataHandler(new DataHandler(fds));
        	mbp.setFileName(fds.getName());
        	multipart.addBodyPart(mbp);
        }
	}
    public MailSendAttachment(String smtpHost, String username, String password){
        super(smtpHost,username,password);
        multipart = new MimeMultipart();
    }

    public static void main(String[] args) {
		MailSendAttachment mailSendAttachment = new MailSendAttachment("smtp.sina.com","msgsendcn","msgsendcn");
    	try {
			mailSendAttachment.setMailFrom("msgsendcn@sina.com");
			mailSendAttachment.setSubject("日志统计入库通知","utf-8");
			mailSendAttachment.setSendDate(new Date());
			mailSendAttachment.setFileDataSource("d:/sidsc2009-12-22 10 40 07.xls");
			String aa= "<html><body><table border='0' cellspacing='1' cellpadding='2'  width='80%'><tr bordercolor='#999999' bgcolor='#009900' align='center'>";
			aa+="<td width=100>SID</td><td width=200>SID名称</td><td width=100>上线词数量</td><td width=100>高露出个数</td><td width=100>测试词个数</td>";
			aa+= "</tr></table></body></html>";
			aa="一档分90个\n二档\n";
			mailSendAttachment.setMailContent(aa,"utf-8");
			mailSendAttachment.setMailTo(new String[]{"ouyangjun@yicha.cn"}, Message.RecipientType.CC);
			mailSendAttachment.setMailTo(new String[]{"ouyangjun@yicha.cn"}, Message.RecipientType.TO);
			mailSendAttachment.sendMail();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

}
