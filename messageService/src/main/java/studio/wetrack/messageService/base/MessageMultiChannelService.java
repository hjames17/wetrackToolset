package studio.wetrack.messageService.base;

/**
 * Created by zhanghong on 16/4/12.
 */
public interface MessageMultiChannelService extends MessageService {
    void registerChannel(MessageChannel messageChannel);
}
