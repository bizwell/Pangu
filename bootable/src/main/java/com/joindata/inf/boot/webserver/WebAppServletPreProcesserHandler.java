package com.joindata.inf.boot.webserver;

import javax.servlet.ServletContext;

public interface WebAppServletPreProcesserHandler
{
    public void beforeInit(ServletContext context);
}
