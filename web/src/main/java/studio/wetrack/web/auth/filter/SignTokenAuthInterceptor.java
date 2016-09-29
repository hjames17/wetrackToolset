package studio.wetrack.web.auth.filter;


import studio.wetrack.web.auth.domain.SimpleGrantedAuthority;
import studio.wetrack.web.auth.domain.Token;
import studio.wetrack.web.auth.exceptions.TokenAuthorizationException;
import studio.wetrack.web.auth.service.AuthorizationService;
import studio.wetrack.web.auth.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangsong on 15/7/5.
 */
public class SignTokenAuthInterceptor extends HandlerInterceptorAdapter{

//    private static final String HEADER_CUSTOMER_ID = "customerId";
    public static final String HEADER_CUSTOMER_TOKEN = "token";

    @Autowired
    TokenService tokenService;
    @Autowired
    AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws TokenAuthorizationException {

        if (!(handler instanceof HandlerMethod)) {// 可能处理的是静态资源。无需登录检测
            return true;
        }

        boolean shouldToken = false;// 是否需要Token验证

        // 获取是否需要签名及Token验证的标注信息
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        SignTokenAuth signTokenAuth = handlerMethod.getMethodAnnotation(SignTokenAuth.class);// 从方法中获取
        if (signTokenAuth == null) {// 从类定义中获取
            signTokenAuth = handlerMethod.getBeanType().getAnnotation(SignTokenAuth.class);
        }

        if (signTokenAuth != null) {
            shouldToken = signTokenAuth.token();
        }
        if (shouldToken) {
            String tokenString = request.getHeader(HEADER_CUSTOMER_TOKEN);
            if (StringUtils.isBlank(tokenString)) {// 请求头中必须包含token信息
                throw new TokenAuthorizationException("token为空");
            }
            Token token = tokenService.findByTokenString(tokenString);
            if (token == null ) {// 请求头中必须包含token信息
                throw new TokenAuthorizationException("token无效");
            }else{
                if(token.isExpired()){
                    throw new TokenAuthorizationException("登录已经过期");
                }
                request.setAttribute("user", token.getUser());
            }
            if(signTokenAuth != null) {
                String role = signTokenAuth.roleNameRequired();

                if (!role.isEmpty() && !authorizationService.grantAccess(token.getToken(), new SimpleGrantedAuthority(role))) {
                    throw new TokenAuthorizationException("用户类型没有访问权限");
                }
            }
        }
        return true;
    }
}
