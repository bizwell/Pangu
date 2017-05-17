package com.joindata.inf.boot.support;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HttpServletBean;

/**
 * 实现 Spring 托管 Servlet
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 17, 2017 1:53:41 PM
 */
@SuppressWarnings("serial")
public class DelegatingServletProxy extends HttpServletBean
{
    private String contextAttribute;

    private WebApplicationContext webApplicationContext;

    private String targetBeanName;

    private boolean targetServletLifecycle = true;

    private volatile Servlet delegate;

    private final Object delegateMonitor = new Object();

    public DelegatingServletProxy()
    {
    }

    public DelegatingServletProxy(Servlet delegate)
    {
        Assert.notNull(delegate, "delegate Servlet object must not be null");
        this.delegate = delegate;
    }

    public DelegatingServletProxy(String targetBeanName)
    {
        this(targetBeanName, null);
    }

    public DelegatingServletProxy(String targetBeanName, WebApplicationContext wac)
    {
        Assert.hasText(targetBeanName, "target Servlet bean name must not be null or empty");
        this.setTargetBeanName(targetBeanName);
        this.webApplicationContext = wac;
        if(wac != null)
        {
            this.setEnvironment(wac.getEnvironment());
        }
    }

    public void setContextAttribute(String contextAttribute)
    {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute()
    {
        return this.contextAttribute;
    }

    public void setTargetBeanName(String targetBeanName)
    {
        this.targetBeanName = targetBeanName;
    }

    protected String getTargetBeanName()
    {
        return this.targetBeanName;
    }

    public void setTargetServletLifecycle(boolean targetServletLifecycle)
    {
        this.targetServletLifecycle = targetServletLifecycle;
    }

    protected boolean isTargetServletLifecycle()
    {
        return this.targetServletLifecycle;
    }

    @Override
    protected void initServletBean() throws ServletException
    {
        synchronized(this.delegateMonitor)
        {
            if(this.delegate == null)
            {
                if(this.targetBeanName == null)
                {
                    this.targetBeanName = this.getServletName();
                }
                WebApplicationContext wac = this.findWebApplicationContext();
                if(wac != null)
                {
                    this.delegate = this.initDelegate(wac);
                }
            }
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException
    {
        Servlet delegateToUse = this.delegate;
        if(delegateToUse == null)
        {
            synchronized(this.delegateMonitor)
            {
                if(this.delegate == null)
                {
                    WebApplicationContext wac = this.findWebApplicationContext();
                    if(wac == null)
                    {
                        throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
                    }
                    this.delegate = this.initDelegate(wac);
                }
                delegateToUse = this.delegate;
            }
        }
        this.invokeDelegate(delegateToUse, req, resp);
    }

    @Override
    public void destroy()
    {
        Servlet delegateToUse = this.delegate;
        if(delegateToUse != null)
        {
            this.destroyDelegate(delegateToUse);
        }
    }

    protected WebApplicationContext findWebApplicationContext()
    {
        if(this.webApplicationContext != null)
        {
            if(this.webApplicationContext instanceof ConfigurableApplicationContext)
            {
                if(!((ConfigurableApplicationContext)this.webApplicationContext).isActive())
                {
                    ((ConfigurableApplicationContext)this.webApplicationContext).refresh();
                }
            }
            return this.webApplicationContext;
        }
        String attrName = this.getContextAttribute();
        if(attrName != null)
        {
            return WebApplicationContextUtils.getWebApplicationContext(super.getServletContext(), attrName);
        }
        else
        {
            return WebApplicationContextUtils.getWebApplicationContext(super.getServletContext());
        }
    }

    protected Servlet initDelegate(WebApplicationContext wac) throws ServletException
    {
        Servlet delegate = wac.getBean(this.getTargetBeanName(), Servlet.class);
        if(this.isTargetServletLifecycle())
        {
            delegate.init(super.getServletConfig());
        }
        return delegate;
    }

    protected void invokeDelegate(Servlet delegate, ServletRequest req, ServletResponse resp) throws ServletException, IOException
    {
        delegate.service(req, resp);
    }

    protected void destroyDelegate(Servlet delegate)
    {
        if(this.isTargetServletLifecycle())
        {
            delegate.destroy();
        }
    }

}