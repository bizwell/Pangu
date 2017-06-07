package com.joindata.inf.registry.entity;

import java.util.Map;
import java.util.TreeMap;

public class AppVersionSummary extends TreeMap<String, Map<String, AppInstanceSummary>>
{
    private static final long serialVersionUID = -8219607976142750414L;

    public Map<String, AppInstanceSummary> add(String version, Map<String, AppInstanceSummary> instances)
    {
        return this.put(version, instances);
    }
}
