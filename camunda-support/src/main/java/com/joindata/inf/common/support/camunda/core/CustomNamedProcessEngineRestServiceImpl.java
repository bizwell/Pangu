package com.joindata.inf.common.support.camunda.core;

import javax.ws.rs.Path;

import org.camunda.bpm.engine.rest.impl.NamedProcessEngineRestServiceImpl;

@Path(CustomNamedProcessEngineRestServiceImpl.PATH)
public class CustomNamedProcessEngineRestServiceImpl extends NamedProcessEngineRestServiceImpl
{
    public static final String PATH = "/api/engine/engine/";
}
