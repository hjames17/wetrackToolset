package studio.wetrack.accountService;

import org.springframework.util.StringUtils;
import studio.wetrack.accountService.domain.*;
import studio.wetrack.base.exception.SmsCodeException;
import studio.wetrack.base.utils.RegExUtil;
import studio.wetrack.accountService.auth.domain.Token;
import studio.wetrack.accountService.auth.domain.User;
import studio.wetrack.accountService.auth.service.TokenService;

/**
 * Created by zhanghong on 16/7/18.
 */
public abstract class AbstrackAccountService implements AccountService {

    TokenService tokenService;

    SmsCodeService smsCodeService;

    public AbstrackAccountService(TokenService tokenService, SmsCodeService smsCodeService){
        this.tokenService = tokenService;
        this.smsCodeService = smsCodeService;
    }

    @Override
    public LoginOut login(SmartLoginForm form) throws AccountException {
        if(StringUtils.isEmpty(form.getAccount())){
            throw new AccountException("无效的用户");
        }

        LoginForm lf = new LoginForm();
        if(RegExUtil.isMobilePhone(form.getAccount())){
            lf.setPhone(form.getAccount());
        }else if(RegExUtil.isValidEmail(form.getAccount())){
            lf.setEmail(form.getAccount());
        }else{
            //默认认为输入的使用用户名
            lf.setUserName(form.getAccount());
        }
        lf.setPassword(form.getPassword());
        return login(form);
    }

    @Override
    public LoginOut login(LoginForm form) throws AccountException {
        if(StringUtils.isEmpty(form.getEmail()) && StringUtils.isEmpty(form.getPhone()) && StringUtils.isEmpty(form.getWeixinId())){
            throw new AccountException("无效的用户");
        }
        if(StringUtils.isEmpty(form.getPassword())){
            throw new AccountException("没有输入密码");
        }

        String id = findUserAndCheckPassAndReturnId(form);
        if(id == null){
            throw new AccountException("用户名或者密码错误");
        }

        User user;
        if(form.getType() == null){
            user = new User(id, form.getPassword(), getLoginLifeTime(form));
        }else{
            user = new User(form.getType().getName() + "_" + id, form.getPassword(), getLoginLifeTime(form), form.getType().getRolesStringArray());
        }
        Token token = tokenService.login(user);
        LoginOut loginOut = new LoginOut();
        loginOut.setId(id);
        loginOut.setType(form.getType());
        loginOut.setToken(token.getToken());

        return loginOut;
    }


    @Override
    public LoginOut quickLogin(LoginForm form) throws AccountException{
        if(StringUtils.isEmpty(form.getPhone())){
            throw new AccountException("请输入手机");
        }
        if(!RegExUtil.isMobilePhone(form.getPhone())){
            throw new AccountException("无效的手机号码");
        }
        if(StringUtils.isEmpty(form.getSmsCode())){
            throw new AccountException("请输入手机验证码");
        }

        String id = findUserAndReturnId(form);
        if(id == null){
            throw new AccountException("不存在该用户");
        }

        if(!checkSmsCodeForLogin(form.getPhone(), form.getSmsCode())){
            throw new AccountException("验证码无效");
        }else{
            smsCodeService.removeSmsCode(form.getPhone(), form.getSmsCode(), SmsCodeService.CodeType.LOGIN);
        }

        User user;
        if(form.getType() == null){
            user = new User(id, form.getSmsCode(), getLoginLifeTime(form));
        }else{
            user = new User(form.getType().getName() + "_" + id, form.getSmsCode(), getLoginLifeTime(form), form.getType().getRolesStringArray());
        }
        Token token = tokenService.login(user);
        LoginOut loginOut = new LoginOut();
        loginOut.setId(id);
        loginOut.setType(form.getType());
        loginOut.setToken(token.getToken());

        return loginOut;
    }

    protected abstract int getLoginLifeTime(LoginForm form) throws AccountException;

    protected abstract String findUserAndCheckPassAndReturnId(LoginForm form) throws AccountException;
    protected abstract String findUserAndReturnId(LoginForm form) throws AccountException;


    @Override
    public void logout(LoginOut form) throws AccountException {
        if(StringUtils.isEmpty(form.getId())){
            throw new AccountException("用户id无效");
        }
        if(StringUtils.isEmpty(form.getToken())){
            throw new AccountException("您没有token，是否已经登录？");
        }
        tokenService.logout(form.getToken());
    }

    @Override
    public void changePass(ChangePass form) throws AccountException {
        if(StringUtils.isEmpty(form.getId())){
            throw new AccountException("用户id为空");
        }
        if(StringUtils.isEmpty(form.getOldPass())){
            throw new AccountException("请输入原来的密码");
        }
        if(StringUtils.isEmpty(form.getNewPass())){
            throw new AccountException("请输入新的密码");
        }

        if(checkPassword(form.getId(), form.getOldPass(), form.getType())){
            updatePassword(form.getId(), form.getNewPass(), form.getType());
        }else{
            throw new AccountException("原密码错误");
        }

    }

    protected abstract void updatePassword(String id, String password, Type type) throws AccountException;

    protected abstract boolean checkPassword(String id, String password, Type type) throws AccountException;


    @Override
    public void resetPass(ResetPass form) throws AccountException {
        if(StringUtils.isEmpty(form.getVerification())){
            throw new AccountException("请提供验证码");
        }
        if(StringUtils.isEmpty(form.getEmail()) && StringUtils.isEmpty(form.getPhone())){
            throw new AccountException("无效的用户");
        }

        if(checkSmsCodeForResetPass(form.getPhone(), form.getVerification())){
            if(!StringUtils.isEmpty(form.getPhone())){
                updatePasswordByPhone(form.getPhone(), form.getNewPass(), form.getType());
            }else{
                updatePasswordByEmail(form.getEmail(), form.getNewPass(), form.getType());
            }
        }else{
            throw new AccountException("无效的验证码");
        }

    }

    protected abstract void updatePasswordByEmail(String email, String password, Type type) throws AccountException;

    protected abstract void updatePasswordByPhone(String phone, String password, Type type) throws AccountException;


    @Override
    public String signup(Signup form) throws AccountException {
        return null;
    }


    @Override
    public void requestSmsCodeForResetPass(String phone) throws SmsCodeException{
        smsCodeService.requestSmsCode(phone, SmsCodeService.CodeType.RESET_PASS);
    }
    @Override
    public boolean checkSmsCodeForResetPass(String phone, String code){
        return smsCodeService.checkSmsCode(phone, code, SmsCodeService.CodeType.RESET_PASS, true);
    }
    @Override
    public void requestSmsCodeForLogin(String phone) throws SmsCodeException{
        smsCodeService.requestSmsCode(phone, SmsCodeService.CodeType.LOGIN);
    }
    @Override
    public boolean checkSmsCodeForLogin(String phone, String code){
        return smsCodeService.checkSmsCode(phone, code, SmsCodeService.CodeType.LOGIN, false);
    }


    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public SmsCodeService getSmsCodeService() {
        return smsCodeService;
    }

    public void setSmsCodeService(SmsCodeService smsCodeService) {
        this.smsCodeService = smsCodeService;
    }
}
