package com.joindata.inf.common.support.disconf.core.entity;

import lombok.Data;

@Data
public class DisconfRestResponse<T>
{
    private int status;

    private String message;

    private T value;
}
