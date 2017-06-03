package studio.wetrack.web.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import studio.wetrack.accountService.AccountService;
import studio.wetrack.accountService.domain.*;
import studio.wetrack.base.exception.BusinessException;
import studio.wetrack.base.exception.SmsCodeException;
import studio.wetrack.base.utils.sms.SmsService;
import studio.wetrack.web.auth.filter.SignTokenAuth;

/**
 * Created by zhanghong on 17/2/24.
 */
//@Controller
public class AccountController {

    private static final String ACC_API_PATH = "/account";
    private static final String ACC_LOGIN_PATH = ACC_API_PATH + "/login";
    private static final String ACC_QUICK_LOGIN_PATH = ACC_API_PATH + "/quickLogin";
    private static final String ACC_LOGIN_SMSCODE_PATH = ACC_API_PATH + "/login/smsCode";
    private static final String ACC_SMART_LOGIN_PATH = ACC_API_PATH + "/smartLogin";
    private static final String ACC_LOGOUT_PATH = ACC_API_PATH + "/logout";
    private static final String ACC_CHECK_TOKEN_PATH = ACC_API_PATH + "/checkToken";
    private static final String ACC_CHANGE_PASS_PATH = ACC_API_PATH + "/changePass";
    private static final String ACC_RESET_PASS_PATH = ACC_API_PATH + "/resetPass";
    private static final String ACC_RESET_PASS_SMSCODE_PATH = ACC_API_PATH + "/resetPass/smsCode";

//    static String loginPath = ACC_LOGIN_PATH;
//    static String smartLoginPath = ACC_SMART_LOGIN_PATH;
//    static String logoutPath = ACC_LOGOUT_PATH;
//    static String checkTokenPath = ACC_CHECK_TOKEN_PATH;
//    static String changePassPath = ACC_CHANGE_PASS_PATH;
//    static String resetPassPath = ACC_RESET_PASS_PATH;


    AccountService accountService;

    SmsService smsService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = AccountController.ACC_LOGIN_PATH)
    public LoginOut login(@RequestBody LoginForm loginForm) throws BusinessException{
        return accountService.login(loginForm);
    }

    @RequestMapping(value = AccountController.ACC_QUICK_LOGIN_PATH)
    public LoginOut quickLogin(@RequestBody LoginForm loginForm) throws BusinessException{
        return accountService.quickLogin(loginForm);
    }

    @RequestMapping(value = AccountController.ACC_LOGIN_SMSCODE_PATH)
    public void smsCodeForLogin(@RequestParam(value = "phone") String phone) throws SmsCodeException {
        accountService.requestSmsCodeForLogin(phone);
    }
    @RequestMapping(value = AccountController.ACC_RESET_PASS_SMSCODE_PATH)
    public void smsCodeForResetPass(@RequestParam(value = "phone") String phone) throws SmsCodeException {
        accountService.requestSmsCodeForResetPass(phone);
    }

    @RequestMapping(value = AccountController.ACC_SMART_LOGIN_PATH)
    public LoginOut smartLogin(@RequestBody SmartLoginForm loginForm) throws BusinessException{
        return accountService.login(loginForm);
    }

    @SignTokenAuth
    @RequestMapping(value = AccountController.ACC_LOGOUT_PATH)
    public void logout(@RequestBody LoginOut loginOut) throws BusinessException{
        accountService.logout(loginOut);
    }

    @SignTokenAuth
    @RequestMapping(value = AccountController.ACC_CHECK_TOKEN_PATH)
    public void checkToken() throws BusinessException{
        //emtpy is ok
    }

    @RequestMapping(value = AccountController.ACC_CHANGE_PASS_PATH)
    public void changePass(@RequestBody ChangePass changeForm) throws BusinessException{
        accountService.changePass(changeForm);
    }

    @RequestMapping(value = AccountController.ACC_RESET_PASS_PATH)
    public void resetPass(@RequestBody ResetPass resetForm) throws BusinessException{
        accountService.resetPass(resetForm);
    }

}
