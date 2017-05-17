package com.joindata.inf.common.support.camunda;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 Camunda
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 16, 2017 10:54:57 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "流程引擎支持")
public @interface EnableCamunda
{
    /**
     * BPMN 文件目录, 默认在 bpmn 目录下
     */
    String[] bpmnDir() default {"/bpmn"};

    /** 数据库地址 // TODO */
    String url();

    /** 数据库地址 // TODO */
    String username();

    /** 数据库密码 // TODO */
    String password();
}