package com.joindata.inf.common.support.camunda.core;

import javax.ws.rs.Path;

import org.camunda.bpm.engine.rest.impl.DefaultProcessEngineRestServiceImpl;

@Path(CustomProcessEngineRestServiceImpl.PATH)
public class CustomProcessEngineRestServiceImpl extends DefaultProcessEngineRestServiceImpl
{
    public static final String PATH = "/api/";
}
