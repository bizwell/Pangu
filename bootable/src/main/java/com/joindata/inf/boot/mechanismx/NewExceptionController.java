package com.joindata.inf.boot.mechanismx;


import com.joindata.inf.common.basic.errors.BaseErrorCode;
import com.joindata.inf.common.basic.exceptions.BaseRunTimeException;
import com.joindata.inf.common.util.basic.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
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
 * @author <a href="mailto:likanghua@joindata.com">li kang hua</a>
 * @since 2016年12月14日
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
public class NewExceptionController extends ResponseEntityExceptionHandler {
    private static final String LOCALEPARAM = "locale";

    private static final Locale DEFAULTLOCALE = Locale.SIMPLIFIED_CHINESE;

    @Autowired
    private MessageSource messageSource;


    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;


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
    public Message hanldeIllegalArgumentException(IllegalArgumentException ex) throws Exception {
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
        Message message = handleMessage("S" + status.value());
        return ResponseEntity.ok(message);
    }


    private Message handleMessage(String errorCode) {
        return handleMessage(errorCode, null);
    }

    private Message handleMessage(String errorCode, Object... params) {
        /**
         * 获取国际化参数
         */
        Locale locale = getLocale(httpServletRequest.getParameter(LOCALEPARAM));
        String method = httpServletRequest.getMethod();
        String message = null;
        try {
            message = messageSource.getMessage(errorCode, params, locale);
        } finally {
            if (StringUtil.isNullOrEmpty(message)) {
                return new Message(errorCode);
            }
        }
        String[] data = message.split(";");
        String url = data[0];
        String errorMessage = null;
        if (data.length > 1) {
            errorMessage = data[1];
        }
        boolean canRedirect = "GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method);
        boolean isAjaxRequest = "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
        if (!isAjaxRequest && canRedirect && StringUtils.isNotBlank(url)) {
            try {
                httpServletResponse.sendRedirect(url);
            } catch (Exception ex) {
                log.error("error:{}", ex.getMessage());
            }
            return null;
        }
        return new Message(errorCode, errorMessage);
    }

    private Locale getLocale(String param) {
        if (StringUtil.isNullOrEmpty(param)) {
            return DEFAULTLOCALE;
        }
        return org.apache.commons.lang3.LocaleUtils.toLocale(param);
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

    public static void main(String[] args) {
        System.out.print("".split(";")[0] + "+++++");
    }
}

