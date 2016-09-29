package studio.wetrack.web.auth.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限拦截器的标注标识。
 * 在没有声明标注时，系统依然默认启用拦截逻辑的处理
 * @author winner
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SignTokenAuth {

	/**
	 * 设置是否需要签名验证，默认值为 true
	 * @return true/false
	 */
	boolean sign() default true;
	
	/**
	 * 设置是否需要用户 Token 验证，默认值为 true
	 * Token验证：是为了保证用户在设备上登录的唯一性判断
	 * @return true/false
	 */
	boolean token() default true;

	String roleNameRequired() default "";

}
