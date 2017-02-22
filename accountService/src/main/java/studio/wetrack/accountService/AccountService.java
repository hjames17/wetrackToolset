package studio.wetrack.accountService;

import studio.wetrack.accountService.domain.*;

/**
 * Created by zhanghong on 16/7/18.
 */
public interface AccountService{

    LoginOut login(LoginForm form) throws AccountException;
    LoginOut login(SmartLoginForm form) throws AccountException;

    void logout(LoginOut form) throws AccountException;

    void changePass(ChangePass form) throws AccountException;

    void resetPass(ResetPass form) throws AccountException;

    String signup(Signup form) throws AccountException;

}
