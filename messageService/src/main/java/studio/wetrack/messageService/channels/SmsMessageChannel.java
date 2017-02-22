package studio.wetrack.messageService.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.base.utils.sms.SmsService;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;
import studio.wetrack.messageService.messages.SmsMessage;

/**
 * Created by zhanghong on 16/3/1.
 */
public class SmsMessageChannel extends AbstractMessageChannel {
    private Logger LOGGER = LoggerFactory.getLogger(SmsMessageChannel.class);

    SmsService smsService;

    SmsMessageChannel(SmsService smsService){
        this.smsService = smsService;
    }

    @Override
    protected void doSend(Message message) {
        SmsMessage sms = (SmsMessage)message;
        boolean success = smsService.sendMessage(sms.getReceiver(), sms.getText());
    }

    @Override
    public String getName() {
        return "sms";
    }
}
