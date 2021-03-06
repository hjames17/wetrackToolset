package studio.wetrack.accountService.auth.service;

import studio.wetrack.accountService.auth.domain.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Created by zhanghong on 15/11/18.
 * 业务无关的通用框架接口
 * 为基于token的用户进行访问授权
 */
@Service
public class AuthorizationService {

    @Autowired
    TokenService tokenService;


//    public AuthorizationService(TokenService tokenService){
//        this.tokenService = tokenService;
//    }
    /**
     * 授权token持有人访问指定的权限
     * @param tokenString token字符串
     * @param requestRole 角色名
     * @return true 当token持有人拥有所需要的权限时
     */
    public boolean grantAccess(String tokenString, GrantedAuthority requestRole){
        Token token = tokenService.findByTokenString(tokenString);
        if(token == null){
            return false;
        }

        return token.getUser().hasRole(requestRole.getAuthority());
    }
}
