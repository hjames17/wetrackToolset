package studio.wetrack.messageService.messages;


import studio.wetrack.messageService.base.Message;

import java.util.List;

/**
 * Created by zhanghong on 16/3/1.
 */
public class EmailMessage implements Message {
    int id;
    String title;
    String text;
    List<String> receivers;
    String sender;
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

    public void setReceiver(List<String> receivers) {
        this.receivers = receivers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
