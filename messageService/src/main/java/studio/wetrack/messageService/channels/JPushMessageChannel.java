package studio.wetrack.messageService.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.base.utils.jackson.Jackson;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;
import studio.wetrack.messageService.messages.JPushMessage;
import studio.wetrack.messageService.support.jpush.JPusher;


/**
 * Created by zhanghong on 16/3/1.
 */
public class JPushMessageChannel extends AbstractMessageChannel {
    private Logger logger = LoggerFactory.getLogger(JPushMessageChannel.class);

    JPusher jPusher;
    public JPushMessageChannel(JPusher jPusher){
        this.jPusher = jPusher;
    }

    @Override
    protected void doSend(Message message) {
        JPushMessage jpMessage = new JPushMessage();
        logger.info("jpush发送消息，消息内容为:{}", Jackson.base().writeValueAsString(jpMessage));

        boolean success = jPusher.pushEvent(jpMessage);
    }

    @Override
    public String getName() {
        return "jpush";
    }
}
