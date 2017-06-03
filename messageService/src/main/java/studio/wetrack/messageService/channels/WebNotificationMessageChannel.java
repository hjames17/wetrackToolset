package studio.wetrack.messageService.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;

/**
 * Created by zhanghong on 16/3/1.
 */
public class WebNotificationMessageChannel extends AbstractMessageChannel {
    private static Logger logger = LoggerFactory.getLogger(WebNotificationMessageChannel.class);


    WebNotificationMessageChannel(){

    }

    @Override
    protected void doSend(Message message){
//        WebNotificationMessage webMessage = (WebNotificationMessage)message;
//        WebSocketManager.pushMessage(webMessage.getReceiver(), webMessage.getType(), Jackson.mobile().writeValueAsString(webMessage));
    }

    @Override
    public String getName() {
        return "web notification";
    }
}
