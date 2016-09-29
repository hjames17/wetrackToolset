package studio.wetrack.web.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *	@author zhanghong
 *  把一个web请求包装成一个AjaxResult对象返回
 *  相当于使用 ResponseBody + AjaxResult
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AjaxResponseWrapper {
}
