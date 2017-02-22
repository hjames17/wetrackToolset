package studio.wetrack.base.utils.sms;

/**
 * Created by chen on 16/10/1.
 * 短信发送接口
 */
public interface SmsService {


    String sendCode(String phone);

    boolean sendMessage(String phone, String content);
}
