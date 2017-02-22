package studio.wetrack.messageService.messages;


import studio.wetrack.messageService.base.Message;

/**
 * Created by zhanghong on 16/3/1.
 */
public class EmailMessage implements Message {
    int id;
    String title;
    String text;
    String receiver;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
