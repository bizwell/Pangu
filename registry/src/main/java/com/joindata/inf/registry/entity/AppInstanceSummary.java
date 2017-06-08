package com.joindata.inf.registry.entity;

import java.util.Collection;

import com.joindata.inf.registry.enums.InstanceState;

import lombok.Data;

@Data
public class AppInstanceSummary
{
    private AppInstanceInfo instanceInfo;

    private AppStartInfo startInfo;

    private AppHeartbeatInfo heartbeatInfo;

    private String fatalInfo;

    private Collection<String> hosts;

    private InstanceState state;

    private String abnormalInfo;
}
