package studio.wetrack.messageService.channels;

import cn.jpush.api.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;
import studio.wetrack.messageService.messages.EmailMessage;
import studio.wetrack.messageService.support.email.email.EmailUtil;
import studio.wetrack.messageService.support.email.email.MailSendAttachment;

/**
 * Created by zhanghong on 16/3/1.
 */
public class EmailMessageChannel extends AbstractMessageChannel {
    private Logger LOGGER = LoggerFactory.getLogger(EmailMessageChannel.class);

    String smtp;

    String userName;

    String password;

    public EmailMessageChannel(String smtp, String userName, String password){
        this.smtp = smtp;
        this.userName = userName;
        this.password = password;
        sender = new MailSendAttachment(smtp, userName, password);
    }


    MailSendAttachment sender;

    @Override
    protected void doSend(Message message) {
        EmailMessage email = (EmailMessage)message;
        try {
            if(StringUtils.isEmpty(email.getSender())){
                sender.setMailFrom(userName);
            }else{
                sender.setMailFrom(email.getSender());
            }
        } catch (Exception e) {
            LOGGER.error("send email error! msg {}, title {} : {}", email.getId(), email.getTitle(), e.getMessage());
            e.printStackTrace();
            return;
        }

        if(email.getReceivers() != null && email.getReceivers().size() > 0){
            for (String s : email.getReceivers()) {
                try {
                    EmailUtil.sendMail(email.getText(), "", email.getTitle(), s, sender);
                } catch (Exception e) {
                    LOGGER.error("send email error! msg {}, title {}, receiver {} : {}", email.getId(), email.getTitle(), s, e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String getName() {
        return "email";
    }
}
