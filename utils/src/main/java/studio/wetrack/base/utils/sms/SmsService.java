package studio.wetrack.base.utils.sms;

import studio.wetrack.base.exception.SmsCodeException;

/**
 * Created by chen on 16/10/1.
 * 短信发送接口
 */
public interface SmsService {

    String sendCode(String phone, String code) throws SmsCodeException;

    boolean sendMessage(String phone, String content) throws SmsCodeException;
}
