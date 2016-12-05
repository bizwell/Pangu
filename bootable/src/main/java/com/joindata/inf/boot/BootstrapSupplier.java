package com.joindata.inf.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BootstrapSupplier extends AnnotationConfigApplicationContext
{
    public static final ApplicationContext boot()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.start();
        context.refresh();
        return context;
    }
}