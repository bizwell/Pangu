package com.joindata.inf.registry.entity;

import java.util.Collection;

import lombok.Data;

@Data
public class InstanceSign
{
    String pid;

    Collection<String> hosts;

    String startTime;
}