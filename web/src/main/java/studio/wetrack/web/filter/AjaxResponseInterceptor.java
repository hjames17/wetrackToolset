package studio.wetrack.web.filter;

import com.fasterxml.jackson.core.JsonEncoding;
import org.springframework.core.Conventions;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;
import studio.wetrack.base.utils.jackson.Jackson;
import studio.wetrack.web.result.AjaxResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by zhanghong on 15/12/24 平安夜
 * 处理带有{@link AjaxResponseWrapper}的请求，将其返回值包装成一个{@link AjaxResult}返回
 * 已废弃，请使用AjaxResponseReturnValueHandler
 */
@Deprecated
public class AjaxResponseInterceptor extends HandlerInterceptorAdapter {


    private HandlerMethod getHandler(Object handler) throws Exception{
        if(!(handler instanceof HandlerMethod)){
            throw new Exception();
        }

        //TODO : 目前只能配合 <mvc:annotation-driven/> 使用, 否则拿不到handlerMethod
        return (HandlerMethod)handler;
    }

    String getSimpleNameWithFirstLetterInLowerCase(String typeName) throws Exception{
        int start = typeName.lastIndexOf(".");
        String simpleName = typeName.substring(start + 1);
        return simpleName.toLowerCase();
    }

    JsonEncoding getJsonEncoding(MediaType contentType) {
        if (contentType != null && contentType.getCharSet() != null) {
            Charset charset = contentType.getCharSet();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

    boolean isAjaxWrapperHandler(HandlerMethod handlerMethod){
        AjaxResponseWrapper ajaxResponseWrapper = handlerMethod.getMethodAnnotation(AjaxResponseWrapper.class);// 从方法中获取
        if (ajaxResponseWrapper == null) {// 从类定义中获取
            ajaxResponseWrapper = handlerMethod.getBeanType().getAnnotation(AjaxResponseWrapper.class);
        }

        return (ajaxResponseWrapper != null);
    }


    void flushResult(AjaxResult<Object> result, HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception{
        if(modelAndView != null) {
            //取消视图
            modelAndView.clear();
        }
        String json = Jackson.mobile().writeValueAsString(result);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().format(request.getLocale(), "%s", json);
    }


    /**
     * 正常处理的结果在这里包装
     * @param request 请求
     * @param response 响应
     * @param handler 处理函数
     * @param modelAndView 数据
     * @throws Exception 异常
     */
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

        //如果是options请求,不处理
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return;
        }

        if(handler instanceof WebSocketHttpRequestHandler){
            return;
        }

        //首先判断方法或者其所属类是否带有@AjaxResponseWrapper注解,没有就返回
        HandlerMethod handlerMethod = getHandler(handler);
        if(!isAjaxWrapperHandler(handlerMethod)){
            return;
        }

        AjaxResult<Object> ajaxResult = new AjaxResult<Object>();

        //获取方法返回值类型，如果是void返回success
        ajaxResult.setSuccess(true);
        if(handlerMethod.isVoid()){
        }else {
            Type t = handlerMethod.getReturnType().getGenericParameterType();

            if (t.getTypeName().equals(String.class.getTypeName())) {
                //返回的是字符串，getviewname方法取出方法的返回字符串
                String message = modelAndView.getViewName();
                ajaxResult.setData(message);
            } else {
                //非字符串返回类型的值，放在modelMap中，key为类型的直接名的小写
                String dataKey = Conventions.getVariableNameForReturnType(handlerMethod.getMethod(), handlerMethod.getMethod().getReturnType(), null);
//                String dataKey = getSimpleNameWithFirstLetterInLowerCase(t.getTypeName());
                Object data = modelAndView.getModelMap().get(dataKey);

                ajaxResult.setData(data);
            }
        }

        flushResult(ajaxResult, request, response, modelAndView);
    }

}
