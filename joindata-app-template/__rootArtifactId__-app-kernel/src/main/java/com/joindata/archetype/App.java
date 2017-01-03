package com.joindata.archetype;

import com.joindata.inf.boot.Bootstrap;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.support.disconf.EnableDisconf;

@JoindataWebApp(value = "APP.TEST")
@EnableDisconf
public class App
{
    public static void main(String[] args)
    {
        Bootstrap.boot();
    }
}