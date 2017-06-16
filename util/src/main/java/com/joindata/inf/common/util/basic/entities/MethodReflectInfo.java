package com.joindata.inf.common.util.basic.entities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

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

    /** 起始行 */
    private int lineNumber;

    /** 自定义的方法参数反射对象列表 */
    private List<MethodArgReflectInfo> argList;

    private CtMethod ctMethod;

    private Class<?> clz;

    private String name;

    public MethodReflectInfo(Method method)
    {
        this.method = method;
        this.clz = method.getDeclaringClass();
        this.name = method.getName();

        ClassPool pool = ClassPool.getDefault();
        try
        {

            CtClass cc = pool.get(method.getDeclaringClass().getName());
            this.ctMethod = cc.getDeclaredMethod(method.getName());
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        this.lineNumber = ctMethod.getMethodInfo().getLineNumber(0);
        this.argList = parseArgs();
    }

    public CtMethod getCtMethod()
    {
        return this.ctMethod;
    }

    public Class<?> getClz()
    {
        return clz;
    }

    public String getName()
    {
        return name;
    }

    private List<MethodArgReflectInfo> parseArgs()
    {
        List<MethodArgReflectInfo> list = CollectionUtil.newList();

        CtMethod cm = this.ctMethod;

        try
        {
            // 使用javaassist的反射方法获取方法的参数列表
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);

            // 无参数
            if(attr == null)
            {
                return list;
            }

            // 取参数类型
            CtClass[] paramTypes = cm.getParameterTypes();

            // 取注解
            Object[][] annos = cm.getParameterAnnotations();

            // 取参数名
            String[] paramNames = new String[paramTypes.length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for(int i = 0; i < paramNames.length; i++)
            {
                Map<Class<? extends Annotation>, Annotation> annoMap = CollectionUtil.newMap();

                for(int j = 0; j < annos[i].length; j++)
                {
                    Annotation anno = (Annotation)annos[i][j];
                    annoMap.put(anno.annotationType(), anno);
                }

                paramNames[i] = attr.variableName(i + pos);
                MethodArgReflectInfo argInfo = new MethodArgReflectInfo(paramNames[i], paramTypes[i].getName(), annoMap);
                list.add(argInfo);
            }
        }
        catch(NotFoundException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        return list;
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

    public int getLineNumber()
    {
        return lineNumber;
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
