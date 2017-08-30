package com.joindata.inf.boot.mechanism.newrest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.joindata.inf.boot.annotation.NewRestStyle;
import com.joindata.inf.common.basic.support.BootInfoHolder;

@Component
public class WrapperReturnValueMethod implements InitializingBean
{
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public void afterPropertiesSet() throws Exception
    {
        // 是否启用新 REST 风格
        if(BootInfoHolder.getBootAnno(NewRestStyle.class) == null)
        {
            return;
        }

        handlerMethodReturnValueHandlers();
    }

    private void handlerMethodReturnValueHandlers()
    {
        List<HandlerMethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<>(requestMappingHandlerAdapter.getReturnValueHandlers());

        for(int index = 0; index < methodReturnValueHandlers.size(); index++)
        {
            final HandlerMethodReturnValueHandler delegate = methodReturnValueHandlers.get(index);

            if(delegate instanceof RequestResponseBodyMethodProcessor)
            {
                methodReturnValueHandlers.set(index, new HandlerMethodReturnValueHandler()
                {
                    public boolean supportsReturnType(MethodParameter returnType)
                    {
                        return delegate.supportsReturnType(returnType);
                    }

                    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception
                    {
                        if(!returnType.getDeclaringClass().getCanonicalName().startsWith(BootInfoHolder.getAppPackage()))
                        {
                            return;
                        }
                        if(returnType.getMethod().isAnnotationPresent(Raw.class))
                        {
                            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
                            return;
                        }

                        if(!returnType.getDeclaringClass().getCanonicalName().startsWith(BootInfoHolder.getAppPackage()))
                        {
                            delegate.handleReturnValue(methodReturnValueHandlers, returnType, mavContainer, webRequest);
                            return;
                        }

                        // 加code:200 表示请求成功
                        delegate.handleReturnValue(ReturnValueWrapper.wrapper(returnValue), returnType, mavContainer, webRequest);
                    }
                });
            }
            else if(delegate instanceof HttpEntityMethodProcessor)
            {
                methodReturnValueHandlers.set(index, new HandlerMethodReturnValueHandler()
                {

                    public boolean supportsReturnType(MethodParameter returnType)
                    {
                        return delegate.supportsReturnType(returnType);
                    }

                    @SuppressWarnings("rawtypes")
                    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception
                    {
                        if(returnType.getMethod().isAnnotationPresent(Raw.class))
                        {
                            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
                            return;
                        }
                        if(!returnType.getDeclaringClass().getCanonicalName().startsWith(BootInfoHolder.getAppPackage()))
                        {
                            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
                            return;
                        }
                        // HttpEntityMethodProcessor.handleReturnValue方法会将入参returnValue强转ResponseEntity，并调用ResponseEntity.getBody()
                        delegate.handleReturnValue(new ResponseEntity<>(ReturnValueWrapper.wrapper(((ResponseEntity)returnValue).getStatusCodeValue(), ((ResponseEntity)returnValue).getBody()), ((ResponseEntity)returnValue).getStatusCode()), returnType, mavContainer, webRequest);
                    }
                });
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(methodReturnValueHandlers);
    }

}
