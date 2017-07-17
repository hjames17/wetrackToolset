package studio.wetrack.messageService.messages;


import studio.wetrack.messageService.base.Message;

import java.util.List;

/**
 * Created by zhanghong on 16/3/1.
 */
public class SmsMessage implements Message {
    int id;
    String text;
    List<String> receivers;
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
}
