package studio.wetrack.messageService.support.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

/**
 * Created by zhanghong on 17/7/4.
 * 支持一个用户可以有多个连接，一个用户也可以属于多个分组
 */
public interface WebSocketSessionStore {

    void addGroup(String groupName);

    void put(List<String> groupName, String id, WebSocketSession session);
    void put(String groupName, String id, WebSocketSession session);

    void remove(String id);

    void remove(String groupName, String id);

    List<WebSocketSession> getByGroup(String groupName);

    List<WebSocketSession> getById(String id);


}
