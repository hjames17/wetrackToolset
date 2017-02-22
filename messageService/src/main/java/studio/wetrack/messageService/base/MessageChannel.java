package studio.wetrack.messageService.base;

import java.util.Map;

/**
 * Created by zhanghong on 16/3/5.
 */
public interface MessageChannel {
    void registerAdapter(int messageId, MessageAdapter adapter);
    void sendMessage(int messageId, Map<String, Object> params);
    String getName();

}
