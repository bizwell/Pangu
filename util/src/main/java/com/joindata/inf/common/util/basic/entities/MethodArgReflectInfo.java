package com.joindata.inf.common.util.basic.entities;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * 方法参数反射信息<br />
 * <i>是一个充血模型。这个比 java 自带的反射 Method 里增加了一些快捷访问方式和它实现不了的东西，比如参数名</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月23日 下午5:51:30
 */
public class MethodArgReflectInfo
{
    /** 参数名 */
    private String name;

    /** 参数类型 */
    private String type;

    /** 参数上的注解 Map */
    private Map<Class<? extends Annotation>, Annotation> annos;

    public MethodArgReflectInfo(String name, String type, Map<Class<? extends Annotation>, Annotation> annos)
    {
        super();
        this.name = name;
        this.type = type;
        this.annos = annos;
    }

    /**
     * 获取指定的注解
     * 
     * @param annoClz 注解 Class
     * @return 注解实例，如果没有就是 null
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnotation(Class<T> annoClz)
    {
        return (T)annos.get(annoClz);
    }

    /**
     * 判断注解列表中是否包含被注解为指定注解的项目
     * 
     * @param annoClz 注解上面的注解 Class
     * @return 注解实例列表，如果没有就是 null
     */

    public boolean hasAnnotationsAnnotatedBy(Class<? extends Annotation> annoClz)
    {
        Collection<Annotation> values = annos.values();
        for(Annotation anno: values)
        {
            if(anno.annotationType().getAnnotation(annoClz) != null)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取参数名
     * 
     * @return 参数名
     */
    public String getName()
    {
        return name;
    }

    /**
     * 获取参数类型
     * 
     * @return 参数类型
     */
    public String getType()
    {
        return type;
    }
}
