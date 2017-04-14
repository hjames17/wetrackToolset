package studio.wetrack.accountService.auth.service.impl;


import studio.wetrack.accountService.auth.domain.Token;
import studio.wetrack.accountService.auth.service.TokenStorageService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanghong on 15/11/21.
 * 仅在内存中保存，每次重启，token都会失效。
 * 适合在开发阶段调试使用
 */
public class TokenInMemoryStorageService implements TokenStorageService {

    private Map<String, Token> tokenKeyMap;
    private Map<String, Map<String, Token>> userIdKeyMap;

    public TokenInMemoryStorageService(){
        this.tokenKeyMap = new HashMap<String, Token>();
        this.userIdKeyMap = new HashMap<String, Map<String, Token>>();
    }



    @Override
    public boolean addToken(Token token){
        tokenKeyMap.put(token.getToken(), token);
        Map<String, Token> userTokenMap = userIdKeyMap.get(token.getUser().getId());
        if(userTokenMap == null){
            userTokenMap = new HashMap<String, Token>();
            userIdKeyMap.put(token.getUser().getId(), userTokenMap);
        }
        userTokenMap.put(token.getToken(), token);
        return true;
    }

    @Override
    public boolean updateToken(Token token) {
        return true;
    }

    @Override
    public Token removeByTokenString(String tokenString){
        Token token = tokenKeyMap.get(tokenString);
        tokenKeyMap.remove(tokenString);
        if(token != null) {
            Map<String, Token> userTokenMap = userIdKeyMap.get(token.getUser().getId());
            if(userTokenMap != null){
                userTokenMap.remove(tokenString);
            }
        }
        return token;
    }

    @Override
    public Token findByTokenString(String tokenString){
        return  tokenKeyMap.get(tokenString);
    }

    @Override
    public Collection<Token> findAllByUserId(String userId){
        Map<String, Token> userTokenMap = userIdKeyMap.get(userId);
        if(userTokenMap != null){
            return userTokenMap.values();
        }

        return null;
    }

}
