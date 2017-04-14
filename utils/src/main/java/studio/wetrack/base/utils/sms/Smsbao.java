package studio.wetrack.base.utils.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.base.exception.SmsCodeException;
import studio.wetrack.base.utils.HttpUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by chen on 16/10/1.
 * 短信宝接口
 */
public class Smsbao implements SmsService{

    private final static Logger LOGGER = LoggerFactory.getLogger(Smsbao.class);


    private String titlePrefix;
    private String username;// ="vincent_young";
    private String password;// ="766B72DE844E96417F2B9271D9590B60";


    public Smsbao(String titlePrefix, String username, String password) {
        this.titlePrefix = titlePrefix;
        this.username = username;
        this.password = password;

    }

    public String sendCode(String phone, String code) throws SmsCodeException {
        if(code == null) {
            code = String.valueOf(Math.floor((Math.random() * 1000000)) + 1000000).substring(1, 7);
        }
        String content = "您的短信验证码是：" + code;
        sendMessage(phone, content);
        return code;
    }

    public boolean sendMessage(String phone, String content) throws SmsCodeException{

        String url =
                "http://api.smsbao.com/sms?u=" + username + "&p=" + password + "&m="
                        + phone + "&c=";
        content = titlePrefix + content;
        LOGGER.info("send message phone:{},content:{}", phone, content);
        try {
            url += URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("send message to phone:{} failed, exception :{}", phone, e.getMessage());
            throw new SmsCodeException(e.getMessage());
        }
        String result = null;
        try {
            result = HttpUtil.get(url, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("send message to phone:{} failed, exception :{}", phone, e.getMessage());
            throw new SmsCodeException(e.getMessage());
        }
        if (!result.equals("0")) {
            LOGGER.error("send message to phone:{} failed, result :{}", phone, result);
            throw new RuntimeException("发送短信失败，错误码：" + result);
        }
        return true;
    }
}
