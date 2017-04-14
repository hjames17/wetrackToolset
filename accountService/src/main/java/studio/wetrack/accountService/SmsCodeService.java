package studio.wetrack.accountService;

import studio.wetrack.base.exception.SmsCodeException;

/**
 * Created by zhanghong on 17/2/27.
 */
public interface SmsCodeService {

    enum CodeType{
        RESET_PASS,
        SIGNUP,
        LOGIN,

        ;
    }

    /**
     *
     * @param phone 手机号码
     * @param type 验证码类型
     */
    void requestSmsCode(String phone, CodeType type) throws SmsCodeException;

    boolean checkSmsCode(String phone, String code, CodeType type, boolean deleteAfterMatch);

    boolean removeSmsCode(String phone, String code, CodeType type);

    long getCodeValidTimeForType(CodeType type);
    long setCodeValidTimeForType(CodeType type, long time);
}
