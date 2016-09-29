package studio.wetrack.accountService;

import studio.wetrack.web.exception.BusinessException;

/**
 * Created by zhanghong on 16/7/18.
 *
 * discarded AjaxException, change to BusinessException
 */
public class AccountException extends BusinessException {

    public AccountException(String message) {
        super("Account_", message);
    }
}
