package com.joindata.inf.registry.entity;

import java.util.List;
import java.util.TreeMap;

public class AppVersionSummary extends TreeMap<String, List<AppInstanceSummary>>
{
    private static final long serialVersionUID = -8219607976142750414L;

    public List<AppInstanceSummary> add(String version, List<AppInstanceSummary> instances)
    {
        return this.put(version, instances);
    }
}
