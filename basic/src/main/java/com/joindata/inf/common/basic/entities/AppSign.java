package com.joindata.inf.common.basic.entities;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppSign
{
    private String sys;

    private String appName;

    private String version;

    public String toSign()
    {
        return sys + "." + appName + "-" + version;
    }

    public String getAppId()
    {
        return sys + "." + appName;
    }

    public static AppSign fromSign(String sign)
    {
        String dot = Pattern.quote(".");
        String slash = Pattern.quote("-");
        String sys = sign.split(dot)[0];
        String appName = sign.split(slash)[0].replaceAll(Pattern.quote(sys + "."), "");
        String version = sign.split(slash)[1];

        return new AppSign(sys, appName, version);
    }

    @Override
    public String toString()
    {
        return toSign();
    }
}
