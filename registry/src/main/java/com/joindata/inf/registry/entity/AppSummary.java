package com.joindata.inf.registry.entity;

import java.util.HashMap;

public class AppSummary extends HashMap<String, AppVersionSummary>
{
    private static final long serialVersionUID = 2694362307200454266L;

    public AppVersionSummary add(String appId, AppVersionSummary instances)
    {
        return this.put(appId, instances);
    }
}
