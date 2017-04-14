package studio.wetrack.base.exception;

import studio.wetrack.base.exception.BusinessException;

/**
 * Created by zhanghong on 17/2/27.
 */
public class SmsCodeException extends BusinessException {

    public SmsCodeException(String message) {
        super("smscode_", message);
    }
}
