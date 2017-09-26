package com.joindata.inf.boot.mechanism;


import com.joindata.inf.common.basic.errors.BaseErrorCode;
import com.joindata.inf.common.basic.exceptions.BaseRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 异常处理类
 * 继承spring默认的异常类  只需要重写异常handler就可以
 *
 * @author <a href="mailto:zhangkai@joindata.com">Zhang Kai</a>
 * @since 2016年12月14日
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
public class NewExceptionController extends ResponseEntityExceptionHandler {


    @Autowired
    private MessageSource messageSource;


    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;


    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setCacheSeconds(60);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * 程序抛出的异常
     */
    @ExceptionHandler(value = BaseRunTimeException.class)
    public Message handleBaseRuntimeException(BaseRunTimeException ex) throws IOException {
        String errorMesage = ex.toString() + (ex.getCode() != null ? "  errorCode: " + ex.getCode().toString() : "");
        log.warn(errorMesage, ex);
        return handleMessage(ex.getCode().toString(), ex.getArgs());
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    public Message hanldeIllegalArgumentException(IllegalArgumentException ex)  throws  Exception{
        log.warn(ex.toString(), ex);
         return handleMessage(BaseErrorCode.S400.toString());

    }

    @ExceptionHandler(value = Exception.class)
    public Message handleOtherException(Exception ex) throws Exception {
        log.error(ex.toString(), ex);
        return handleMessage(BaseErrorCode.S408.toString());
    }


    @Override
    public ResponseEntity handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn(ex.toString(), ex);
        return ResponseEntity.ok(new Message("S" + status.value()));
    }


    private Message handleMessage(String errorCode) throws IOException {
        return handleMessage(errorCode, null);
    }

    private Message handleMessage(String errorCode, Object... params) throws IOException {
        /**
         * 获取国际化参数
         */
        String local = httpServletRequest.getParameter("locale");
        String method = httpServletRequest.getMethod();
        String message = messageSource.getMessage(errorCode, params, new Locale(local));
        String[] data = message.split(";");
        String url = data[1];
        String errorMessage = data[0];
        boolean isGetOrPost = "GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method);
        boolean isAjaxRequest = "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
        if (!isAjaxRequest && isGetOrPost) {
            httpServletResponse.sendRedirect(url);
            return null;
        }
        return new Message(errorCode, errorMessage);
    }

    public static class Message {

        private String message;


        public Message() {

        }

        public Message(String code) {
            this.code = code;
        }

        public Message(String code, String message) {
            this.code = code;
            this.message = message;
        }


        private String code;


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
