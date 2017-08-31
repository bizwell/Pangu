package com.joindata.inf.boot.sterotype.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.joindata.inf.boot.sterotype.RestResponse;
import com.joindata.inf.common.basic.exceptions.BizException;
import com.joindata.inf.common.basic.exceptions.GenericException;
import com.joindata.inf.common.util.basic.JsonUtil;

/**
 * 请求拦截器
 * 
 * @author 宋翔
 * @date 2015年12月16日 下午2:35:19
 */
public abstract class RequestInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // 执行自定义拦截处理
        try
        {
            beforeRequest(request);
        }
        catch(BizException | GenericException e)
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; Charset=UTF-8");
            response.getWriter().write(JsonUtil.toJSON(RestResponse.fail(e)));
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        // 执行自定义拦截处理
        afterResponse(request, response, ex);
    }

    /**
     * 请求之前所做的工作
     * 
     * @param request 请求
     * @throws GenericException 遇到任何不允许后续再访问的情况，抛出异常
     */
    protected abstract void beforeRequest(HttpServletRequest request) throws GenericException;

    /**
     * 请求之前所做的工作
     * 
     * @param request 请求
     * @param response 响应
     * @param ex 发生的异常
     * @throws GenericException 遇到任何不允许后续再访问的情况，抛出异常
     */
    protected void afterResponse(HttpServletRequest request, HttpServletResponse response, Exception ex) throws GenericException
    {

    }
}
