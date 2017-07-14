package studio.wetrack.messageService.channels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import studio.wetrack.base.utils.jackson.Jackson;
import studio.wetrack.messageService.base.AbstractMessageChannel;
import studio.wetrack.messageService.base.Message;
import studio.wetrack.messageService.messages.WebNotificationMessage;
import studio.wetrack.messageService.support.websocket.WebSocketSessionStore;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhanghong on 16/3/1.
 */
public class WebNotificationMessageChannel extends AbstractMessageChannel {

    static Logger log = LoggerFactory.getLogger(WebNotificationMessageChannel.class);

    private static Logger logger = LoggerFactory.getLogger(WebNotificationMessageChannel.class);

    WebSocketSessionStore webSocketSessionStore;

    public WebNotificationMessageChannel(WebSocketSessionStore webSocketSessionStore){
        this.webSocketSessionStore = webSocketSessionStore;
    }

    @Override
    protected void doSend(Message message){
        WebNotificationMessage webMessage = (WebNotificationMessage)message;
        List<WebSocketSession> sessionList;
        if(webMessage.getType() == WebNotificationMessage.RECEIVER_TYPE_ID){
            sessionList = webSocketSessionStore.getById(webMessage.getReceiver());
        }else{
            sessionList = webSocketSessionStore.getByGroup(webMessage.getReceiver());
        }
        if(sessionList != null){
            for (WebSocketSession session : sessionList) {
                sendMessage(session, webMessage);
            }
        }

    }

    private boolean sendMessage(WebSocketSession session, WebNotificationMessage message){
        TextMessage textMessag = new TextMessage(Jackson.mobile().writeValueAsString(message));

        if (session.isOpen()) {
            try {
                session.sendMessage(textMessag);
                log.info("session id {} 发送成功", session.getId());
                return true;
            } catch (IOException e) {
                log.error("session id {} 发送失败: {}", session.getId(), e.getMessage());
            }
        } else {
            log.error("session id {} 发送失败, 连接已经关闭", session.getId());
        }

        return false;
    }

    @Override
    public String getName() {
        return "web notification";
    }
}
