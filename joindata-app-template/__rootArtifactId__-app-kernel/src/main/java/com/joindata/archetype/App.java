package com.joindata.archetype;

import com.joindata.inf.boot.Bootstrap;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.support.disconf.EnableDisconf;

@JoindataWebApp(id = "APP.TEST", version = "1.0.0")
@EnableDisconf
public class App
{
    public static void main(String[] args)
    {
        Bootstrap.boot();
    }
}