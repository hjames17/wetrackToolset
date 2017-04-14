package studio.wetrack.accountService.auth.service;


import studio.wetrack.accountService.auth.domain.Token;

import java.util.Collection;

/**
 * Created by zhanghong on 15/11/21.
 */
public interface TokenStorageService {

    boolean addToken(Token token);

    boolean updateToken(Token token);

    Token removeByTokenString(String tokenString);

    public Token findByTokenString(String tokenString);

    public Collection<Token> findAllByUserId(String userId);
}
