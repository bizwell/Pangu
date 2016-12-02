package com.joindata.inf.common.util.basic.entities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import com.joindata.inf.common.util.basic.StringUtil;

/**
 * 方法反射后的信息
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月23日 下午5:41:02
 */
public class MethodReflectInfo
{
    /** 方法反射对象 */
    private Method method;

    /** 自定义的方法参数反射对象列表 */
    private List<MethodArgReflectInfo> argList;

    public MethodReflectInfo(Method method, List<MethodArgReflectInfo> argList)
    {
        this.method = method;
        this.argList = argList;
    }

    /**
     * 获取方法上的指定注解
     * 
     * @param annoClz 注解 Class
     * @return 指定注解，可能是 null
     */
    public <T extends Annotation> T getAnnotation(Class<T> annoClz)
    {
        return method.getAnnotation(annoClz);
    }

    /**
     * 获取参数信息列表
     * 
     * @return 参数信息列表
     */
    public List<MethodArgReflectInfo> getArgList()
    {
        return argList;
    }

    /**
     * 获取指定名字的参数
     * 
     * @param name 参数名字
     * @return 参数信息
     */
    public MethodArgReflectInfo getArgInfo(String name)
    {
        if(argList == null)
        {
            return null;
        }

        for(MethodArgReflectInfo argInfo: argList)
        {
            if(StringUtil.isEquals(argInfo.getName(), name))
            {
                return argInfo;
            }
        }

        return null;
    }
}
