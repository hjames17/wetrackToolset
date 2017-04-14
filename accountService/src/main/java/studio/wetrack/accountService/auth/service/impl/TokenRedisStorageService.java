package studio.wetrack.accountService.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.security.core.GrantedAuthority;
import studio.wetrack.accountService.auth.domain.Token;
import studio.wetrack.accountService.auth.domain.User;
import studio.wetrack.accountService.auth.service.TokenStorageService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhanghong on 15/11/21.
 * redis中存储token
 */

public class TokenRedisStorageService implements TokenStorageService{


    /**
     * 所有的用户token都存在一个hash里，用来快速获取
     */
    static final String TOKEN_HASH_KEY = "TOKEN_HASH";
    /**
     * 另外，单独为每个用户都保存一个，方便取特定用户的所有的token，因为redis不支持hash表内查询
     * 相当于每个token有一份冗余数据
     */
    static final String USER_TOKEN_HASH_KEY_PREFIX = "USER_TOKEN_HASH_";

    RedisTemplate<String, Token> redisTemplate;
    BoundHashOperations<String, String, Token> tokenHashOps;

    @Autowired
    public TokenRedisStorageService(RedisTemplate<String, Token> redisTemplate){
        this.redisTemplate = redisTemplate;
        redisTemplate.setValueSerializer(TokenSerializer.INSTANCE);
        redisTemplate.setHashValueSerializer(TokenSerializer.INSTANCE);
        tokenHashOps = redisTemplate.boundHashOps(TOKEN_HASH_KEY);
    }

    @Override
    public boolean addToken(Token token) {
        tokenHashOps.put(token.getToken(), token);
        BoundHashOperations<String, String, Token> userHashOps = redisTemplate.boundHashOps(USER_TOKEN_HASH_KEY_PREFIX + token.getUser().getId());
        userHashOps.put(token.getToken(), token);
        return true;
    }

    @Override
    public boolean updateToken(Token token) {
        //删除老token，放入新的token
        tokenHashOps.delete(token.getToken());
        tokenHashOps.put(token.getToken(), token);
        BoundHashOperations<String, String, Token> userHashOps = redisTemplate.boundHashOps(USER_TOKEN_HASH_KEY_PREFIX + token.getUser().getId());
        userHashOps.delete(token.getToken());
        userHashOps.put(token.getToken(), token);
        return false;
    }

    @Override
    public Token removeByTokenString(String tokenString) {
        Token token = tokenHashOps.get(tokenString);
        tokenHashOps.delete(tokenString);
        BoundHashOperations<String, String, Token> userHashOps = redisTemplate.boundHashOps(USER_TOKEN_HASH_KEY_PREFIX + token.getUser().getId());
        userHashOps.delete(tokenString);
        return token;
    }

    @Override
    public Token findByTokenString(String tokenString) {
        return tokenHashOps.get(tokenString);
    }

    @Override
    public Collection<Token> findAllByUserId(String userId) {
        BoundHashOperations<String, String, Token> userHashOps = redisTemplate.boundHashOps(USER_TOKEN_HASH_KEY_PREFIX + userId);
        return userHashOps.values();
    }


    public enum TokenSerializer implements RedisSerializer<Token>{
        INSTANCE;

        private static String BLANK = " ";

        private static final int MIN_PARTS_LENGTH = 6;

        @Override
        public byte[] serialize(Token token) throws SerializationException {
            if(token == null){
                throw new SerializationException("null object for token");
            }
            StringBuilder builder = new StringBuilder();
            builder.append(token.getToken()).append(BLANK)
                    .append(token.getCreated()).append(BLANK)
                    .append(token.isLoggedout()).append(BLANK)
                    .append(token.getUser().getId()).append(BLANK)
                    .append(token.getUser().getPassword()).append(BLANK)
                    .append(token.getUser().getLoginLifeTime()).append(BLANK);
            if(token.getUser().getAuthorities() != null) {
                for (GrantedAuthority roleObj : token.getUser().getAuthorities()) {
                    builder.append(roleObj.getAuthority()).append(" ");
                }
            }
            String tokenSerialized = builder.toString();
            return tokenSerialized.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public Token deserialize(byte[] bytes) throws SerializationException {
            if(bytes == null){
                return null;
            }
            String serializedString = new String(bytes, StandardCharsets.UTF_8);
            String[] parts = serializedString.split(BLANK);
            if(parts.length < MIN_PARTS_LENGTH){
                throw new SerializationException("deserialize token error, content length is " + parts.length + ", require at least "  +MIN_PARTS_LENGTH);
            }
            User user = null;
            if(parts.length > MIN_PARTS_LENGTH){
                List<String> roles = Arrays.asList(parts).subList(6, parts.length);
                user = new User(parts[3], parts[4], Integer.valueOf(parts[5]), roles.toArray(new String[roles.size()]));
            }else{
                user = new User(parts[3], parts[4], Integer.valueOf(parts[5]));
            }

            Token token = new Token(parts[0], user);
            token.setCreated(Long.valueOf(parts[1]));
            token.setLoggedout(Boolean.valueOf(parts[2]));
            return token;
        }
    }


    public static void main(String[] args){
        String tokenString = "tokenString 16743829012322 false id password -1";
        Token token = TokenSerializer.INSTANCE.deserialize(tokenString.getBytes(StandardCharsets.UTF_8));
        System.out.println("deserialized token " + token.toString());
        System.out.println("seriazlied token " + new String(TokenSerializer.INSTANCE.serialize(token), StandardCharsets.UTF_8));
    }
}
