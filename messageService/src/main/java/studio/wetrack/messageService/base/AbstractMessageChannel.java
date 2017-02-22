package studio.wetrack.messageService.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhanghong on 16/3/1.
 */
public abstract class AbstractMessageChannel implements MessageChannel {
    private final static Logger logger = LoggerFactory.getLogger(AbstractMessageChannel.class);

    protected Map<Integer, MessageAdapter> adapterMap;
    protected BlockingQueue<MessageRaw> bufList;
    private Thread thread;
//    List<MessageRaw> bufList;

    protected static class MessageRaw{
        public int id;
        public Map<String, Object> params;

        MessageRaw(int id , Map<String, Object> params){
            this.id = id;
            this.params = params;
        }
    }

    public AbstractMessageChannel(){
        adapterMap = new HashMap<Integer, MessageAdapter>();
//        bufList = new ArrayList<MessageRaw>();
        bufList = new LinkedBlockingQueue<MessageRaw>();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                callSend();
            }
        });
        thread.setName(getName() + "-sender");
        thread.start();
    }

    public void registerAdapter(int messageId, MessageAdapter adapter){
        adapterMap.put(messageId, adapter);
    }

    public void sendMessage(int messageId, Map<String, Object> params){
        MessageRaw messageRaw = new MessageRaw(messageId, params);
        bufList.offer(messageRaw);
//        callSend();
    }

    protected void callSend(){
        while(true) {
            try {
                MessageRaw messageRaw = bufList.take();
                if (adapterMap.get(messageRaw.id) != null) {
                    Message message = null;
                    try {
                        message = adapterMap.get(messageRaw.id).build(messageRaw.id, messageRaw.params);
                    }catch (Exception e){
                        logger.error("{} build message failed, id {} , reason : {}", this.getName(), messageRaw.id, e.getMessage());
                    }
                    if(message != null) {
                        try {
                            doSend(message);
                            logger.info("{} sent message {} succeed.", this.getName(), messageRaw.id);
                        }catch (Exception e){
                            logger.error("send message {} failed，messageChannel {} : ",message.getId(),  this.getName(), e.getMessage());
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.error("message channel take message failed ! " + e.getMessage());
//            e.printStackTrace();
            } catch (Exception e){
                //抛弃任何异常
            }
        }
    }

    protected abstract void doSend(Message message);
}
