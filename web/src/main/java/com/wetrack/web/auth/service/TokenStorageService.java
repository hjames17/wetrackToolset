package com.wetrack.web.auth.service;


import com.wetrack.web.auth.domain.Token;

import java.util.Collection;

/**
 * Created by zhanghong on 15/11/21.
 */
public interface TokenStorageService {

    boolean addToken(Token token);

    Token removeByTokenString(String tokenString);

    public Token findByTokenString(String tokenString);

    public Collection<Token> findAllByUserId(String userId);
}