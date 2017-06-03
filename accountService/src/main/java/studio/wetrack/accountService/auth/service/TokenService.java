package studio.wetrack.accountService.auth.service;

import studio.wetrack.accountService.auth.domain.Token;
import studio.wetrack.accountService.auth.domain.User;

import java.util.Collection;

/**
 * Created by zhanghong on 15/11/18.
 * 业务无关的通用框架
 * token管理器
 * 支持单用户多个token管理
 * TODO token自动清理
 */
public interface TokenService {

    public Token login(User user);

    public Token login(String id, String password);
    public Token login(String id, String password, int loginLifeTime);

    public boolean updateToken(Token token);


    public void logout(String token);

    public Token findByTokenString(String tokenString);

    public Collection<Token> findAllByUserId(String userId);

    public boolean isOnlyOnePermitted();

    public void setOnlyOnePermitted(boolean onlyOnePermitted);
}
