package studio.wetrack.web.result;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import studio.wetrack.web.filter.AjaxResponseWrapper;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by zhanghong on 16/12/23. 平安夜前一夜
 *
 * 该处理器会处理带有{@link AjaxResponseWrapper}注解的接口，将其返回值封装成一个{@link AjaxResult}对象
 * 使用方式，在配置文件在mappinghandeleradapter中插入该类实例
 *
 *
 <pre class="code">
    &#064;Autowired
    private RequestMappingHandlerAdapter adapter;

    &#064;Override
    public void afterPropertiesSet() throws Exception {
         List&lt;HandlerMethodReturnValueHandler&gt; handlers = adapter.getReturnValueHandlers();
         for(HandlerMethodReturnValueHandler handler : handlers){
             if(handler instanceof RequestResponseBodyMethodProcessor) {
             AjaxResponseReturnValueHandler arvh = new AjaxResponseReturnValueHandler((RequestResponseBodyMethodProcessor)handler);
             List&lt;HandlerMethodReturnValueHandler&gt; newList = new ArrayList&lt;&gt;(handlers);
             newList.add(0, arvh);
             adapter.setReturnValueHandlers(newList);
             break;
             }
         }
     }
     </pre>
 *
 */
public class AjaxResponseReturnValueHandler implements HandlerMethodReturnValueHandler {

    RequestResponseBodyMethodProcessor delegator;

    public AjaxResponseReturnValueHandler(RequestResponseBodyMethodProcessor delegator) {
        this.delegator = delegator;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), AjaxResponseWrapper.class) ||
                returnType.hasMethodAnnotation(AjaxResponseWrapper.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {


        try {
            delegator.handleReturnValue(wrapIt(returnValue), new ReturnValueMethodParameter(wrapIt(returnValue)), mavContainer, webRequest);
        } catch (NoSuchMethodException e) {
            Assert.isTrue(false);//could not happen
        }
    }


    <T> AjaxResult<T> wrapIt(T data){
        AjaxResult<T> ajaxResult = new AjaxResult<T>();

        ajaxResult.setData(data);

        return ajaxResult;
    }


    private class ReturnValueMethodParameter extends MethodParameter{

        private final Object returnValue;

        public ReturnValueMethodParameter(Object returnValue) throws NoSuchMethodException {
            super(AjaxResult.class.getConstructor(), -1);
            this.returnValue = returnValue;
        }

        @Override
        public Class<?> getParameterType() {
            return (this.returnValue != null ? this.returnValue.getClass() : super.getParameterType());
        }

        @Override
        public Type getGenericParameterType() {
            return AjaxResult.class;
        }

    }
}
