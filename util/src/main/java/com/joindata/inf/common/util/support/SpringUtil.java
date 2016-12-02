package com.joindata.inf.common.util.support;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Spring 框架相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月20日 下午4:58:00
 */
public class SpringUtil
{
    /**
     * 把字符串路径集合转换成 Spring 的 Resource 数组
     * 
     * @param classPaths 字符串路径集合
     * @return Spring 认的 Resource 数组
     */
    public static final Resource[] toResource(Collection<String> classPaths)
    {
        if(classPaths == null)
        {
            return null;
        }

        Resource resources[] = new Resource[classPaths.size()];

        Iterator<String> iter = classPaths.iterator();
        int i = 0;
        while(iter.hasNext())
        {
            resources[i] = new ClassPathResource(iter.next());
            i++;
        }

        return resources;
    }
}
