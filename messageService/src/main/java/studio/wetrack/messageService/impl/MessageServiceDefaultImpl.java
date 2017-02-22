package studio.wetrack.messageService.impl;

import studio.wetrack.messageService.base.MessageChannel;
import studio.wetrack.messageService.base.MessageMultiChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghong on 16/3/1.
 */

public class MessageServiceDefaultImpl implements MessageMultiChannelService {
    List<MessageChannel> channelList = new ArrayList<MessageChannel>();

    @Override
    public void registerChannel(MessageChannel messageChannel) {
        if(!channelList.contains(messageChannel)){
            channelList.add(messageChannel);
        }
    }

    @Override
    public void send(int messageId, Map<String, Object> params) {
        for(MessageChannel messageChannel : channelList){
            messageChannel.sendMessage(messageId, params);
        }
    }


}
