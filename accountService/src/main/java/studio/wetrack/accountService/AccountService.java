package studio.wetrack.accountService;

import studio.wetrack.accountService.domain.*;
import studio.wetrack.base.exception.SmsCodeException;

/**
 * Created by zhanghong on 16/7/18.
 */
public interface AccountService{

    LoginOut login(LoginForm form) throws AccountException;
    LoginOut login(SmartLoginForm form) throws AccountException;
    //使用手机验证码登录
    LoginOut quickLogin(LoginForm form) throws AccountException;

    void logout(LoginOut form) throws AccountException;

    void changePass(ChangePass form) throws AccountException;

    void resetPass(ResetPass form) throws AccountException;

    String signup(Signup form) throws AccountException;

    void requestSmsCodeForResetPass(String phone) throws SmsCodeException;

    boolean checkSmsCodeForResetPass(String phone, String code);

    void requestSmsCodeForLogin(String phone) throws SmsCodeException;

    boolean checkSmsCodeForLogin(String phone, String code);
}
