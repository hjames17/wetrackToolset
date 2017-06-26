package studio.wetrack.accountService.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import studio.wetrack.accountService.auth.domain.Token;
import studio.wetrack.accountService.auth.domain.User;
import studio.wetrack.base.utils.common.UUIDGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhanghong on 15/11/18.
 * 业务无关的通用框架
 * token管理器
 * 支持单用户多个token管理
 * TODO token自动清理
 */
public class TokenServiceImpl implements TokenService{


    TokenStorageService tokenStorageService;

    @Autowired
    public TokenServiceImpl(TokenStorageService tokenStorageService){
        this.tokenStorageService = tokenStorageService;
    }

    /**
     * true，只允许一个同时登录的人， false， 允许多个登录
     * 默认为true
     * 废弃，转入{@link User}
     */
    @Deprecated
    boolean onlyOnePermitted = true;

    public Token login(User user){
        return doLogin(user);
    }

    public Token login(String id, String password){
        return doLogin(new User(id, password, User.NEVER_EXPIRED, User.ROLE_FULL));
    }
    public Token login(String id, String password, int loginLifeTime){
        return doLogin(new User(id, password, loginLifeTime, User.ROLE_FULL));
    }

    public boolean updateToken(Token token){
        return tokenStorageService.updateToken(token);
    }

    private Token doLogin(User user){

        //found exist token list for this user
        Collection<Token> tokens =  tokenStorageService.findAllByUserId(user.getId());
        List<Token> copiedTokens = new ArrayList<Token>();
        copiedTokens.addAll(tokens);

        //create new token
        String tokenString = UUIDGenerator.generate().toUpperCase();
        Token token = new Token(tokenString, user);
        addToken(token);

        //remove existing tokens by condition
        //TODO 一到达上限就踢所有人，这合适吗？
        if(tokens != null && user.maxLoginLimitReached(tokens.size())){
            for (Token exist : copiedTokens) {
                if (exist.isExpired() || exist.isLoggedout()) {
                    tokenStorageService.removeByTokenString(exist.getToken());
                }
            }
        }
        return token;
    }


    public void logout(String token){
        removeByTokenString(token);
    }

    private boolean addToken(Token token){
        return tokenStorageService.addToken(token);
    }

    private Token removeByTokenString(String tokenString){
        return tokenStorageService.removeByTokenString(tokenString);
    }

    public Token findByTokenString(String tokenString){
        return  tokenStorageService.findByTokenString(tokenString);
    }

    public Collection<Token> findAllByUserId(String userId){
        return tokenStorageService.findAllByUserId(userId);
    }

    public boolean isOnlyOnePermitted() {
        return onlyOnePermitted;
    }

    public void setOnlyOnePermitted(boolean onlyOnePermitted) {
        this.onlyOnePermitted = onlyOnePermitted;
    }
}
