package studio.wetrack.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangsong on 15/7/5.
 */
public class BusinessExceptionHandler extends AbstractExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    public BusinessExceptionHandler() {
    }

    public ModelAndView getModelAndView(Exception ex, HttpServletRequest request, HandlerMethod handler) {
        if(ex instanceof BusinessException) {
            BusinessException e = (BusinessException)ex;
            String message = e.getMessage();
            logger.info("业务处理结果：msg=[{}]", message);
            return this.createJsonView(e.getCode(), message, ex, request);
        } else {
            return null;
        }
    }

    protected ModelAndView createJsonView(String code, String message, Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.addStaticAttribute("code", code);
        jsonView.addStaticAttribute("success", Boolean.valueOf(false));
        jsonView.addStaticAttribute("message", message);
        mav.setView(jsonView);
        return mav;
    }
}