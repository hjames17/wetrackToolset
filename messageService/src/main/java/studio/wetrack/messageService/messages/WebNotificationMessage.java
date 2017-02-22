package studio.wetrack.messageService.messages;


import studio.wetrack.messageService.base.Message;

/**
 * Created by zhanghong on 16/3/1.
 */
public class WebNotificationMessage implements Message {

    public static final int RECEIVER_TYPE_ROLE = 0;
    public static final int RECEIVER_TYPE_ID = 1;


    int id;
    String receiver;
    int type;
    String title;
    String content;
    Object data;
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
