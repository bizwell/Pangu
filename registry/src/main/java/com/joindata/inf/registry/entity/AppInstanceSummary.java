package com.joindata.inf.registry.entity;

import lombok.Data;

@Data
public class AppInstanceSummary
{
    private AppInstanceInfo instanceInfo;

    private AppStartInfo startInfo;

    private String fatalInfo;
}
