package studio.wetrack.accountService;

import org.springframework.util.StringUtils;
import studio.wetrack.accountService.domain.*;
import studio.wetrack.base.utils.RegExUtil;
import studio.wetrack.web.auth.domain.Token;
import studio.wetrack.web.auth.domain.User;
import studio.wetrack.web.auth.service.TokenService;

/**
 * Created by zhanghong on 16/7/18.
 */
public abstract class AbstrackAccountService implements AccountService {

    TokenService tokenService;

    public TokenService getTokenService() {
        return tokenService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public AbstrackAccountService(TokenService tokenService){
        this.tokenService = tokenService;
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

        String id = findUserAndReturnId(form);
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
        loginOut.setToken(token.getToken());

        return loginOut;
    }

    protected abstract int getLoginLifeTime(LoginForm form) throws AccountException;

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

        if(checkVerification(form)){
            if(!StringUtils.isEmpty(form.getPhone())){
                updatePasswordByPhone(form.getPhone(), form.getNewPass(), form.getType());
            }else{
                updatePasswordByEmail(form.getEmail(), form.getNewPass(), form.getType());
            }
        }

    }

    protected abstract void updatePasswordByEmail(String email, String password, Type type) throws AccountException;

    protected abstract void updatePasswordByPhone(String phone, String password, Type type) throws AccountException;

    protected abstract boolean checkVerification(ResetPass form) throws AccountException;

    @Override
    public String signup(Signup form) throws AccountException {
        return null;
    }
}
