package com.joindata.inf.common.util.basic;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.util.basic.entities.MethodArgReflectInfo;
import com.joindata.inf.common.util.basic.entities.MethodReflectInfo;
import com.xiaoleilu.hutool.lang.JarClassLoader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * Class 操作相关的工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月3日 下午5:56:41
 */
@Slf4j
public class ClassUtil
{

    /**
     * 获取对象的 Field 列表，并赋予可访问性
     * 
     * @param obj 要获取 Field 的实例对象
     * @return Field 列表
     */
    public static final Field[] getFields(Object obj)
    {
        if(obj == null)
        {
            return null;
        }

        return getFields(obj.getClass());
    }

    /**
     * 获取 Class 的 Field 列表，并赋予可访问性
     * 
     * @param clz 要获取 Field 的 Class 反射对象
     * @return Field 列表
     */
    public static final Field[] getFields(Class<?> clz)
    {
        if(clz == null)
        {
            return null;
        }

        Field flds[] = ArrayUtils.addAll(clz.getDeclaredFields(), (!clz.getSuperclass().equals(Object.class)) ? clz.getSuperclass().getDeclaredFields() : null);

        for(Field fld: flds)
        {
            fld.setAccessible(true);
        }
        return flds;
    }

    /**
     * 获取 Class 的 Method 列表，并赋予可访问性
     * 
     * @param clz 要获取 Method 的 Class 反射对象
     * @return Method 列表
     */
    public static final Method[] getMethods(Class<?> clz)
    {
        if(clz == null)
        {
            return null;
        }

        Method methods[] = ArrayUtils.addAll(clz.getDeclaredMethods(), (!clz.getSuperclass().equals(Object.class)) ? clz.getSuperclass().getDeclaredMethods() : null);

        for(Method method: methods)
        {
            method.setAccessible(true);
        }
        return methods;
    }

    /**
     * 获取 Class 的 Field ，并赋予可访问性
     * 
     * @param clz 要获取 Field 的 Class 反射对象
     * @return Field，如果获取时发生任何异常，返回 null
     */
    public static final Field getField(Class<?> clz, String fldName)
    {
        if(clz == null)
        {
            return null;
        }

        Field fld;
        try
        {
            fld = clz.getDeclaredField(fldName);
            fld.setAccessible(true);
        }
        catch(NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
            return null;
        }

        return fld;
    }

    /**
     * 获取指定类的所有包含指定注解的“属性-注解” Map
     * 
     * @param objClz 要操作的类
     * @param annoClz 要检测的注解 Class
     * @return 属性-注解 TreeMap，会保持属性在类中的排序
     */
    public static <ANNO extends Annotation> Map<Field, ANNO> getFieldAnnotationMap(Class<?> objClz, Class<ANNO> annoClz)
    {
        Field flds[] = getFields(objClz);

        Map<Field, ANNO> map = new TreeMap<Field, ANNO>();
        for(Field fld: flds)
        {
            ANNO anno = fld.getAnnotation(annoClz);
            if(anno != null)
            {
                map.put(fld, anno);
            }
        }
        return map;
    }

    /**
     * 获取指定类的所有包含指定注解的属性集合
     * 
     * @param objClz 要查询的类
     * @param annoClz 要检测的注解 Class
     * @return 属性 Set
     */
    public static <ANNO extends Annotation> Set<Field> getAnnotationFields(Class<?> objClz, Class<ANNO> annoClz)
    {
        Field flds[] = getFields(objClz);

        Set<Field> set = CollectionUtil.newHashSet();
        for(Field fld: flds)
        {
            ANNO anno = fld.getAnnotation(annoClz);
            if(anno != null)
            {
                set.add(fld);
            }
        }
        return set;
    }

    /**
     * 获取指定类的所有包含指定注解的“方法-注解” Map
     * 
     * @param objClz 要操作的类
     * @param annoClz 要检测的注解 Class
     * @return 方法-注解 HashMap
     */
    public static <ANNO extends Annotation> Map<Method, ANNO> getMethodAnnotationMap(Class<?> objClz, Class<ANNO> annoClz)
    {
        Method[] methods = getMethods(objClz);

        Map<Method, ANNO> map = new TreeMap<Method, ANNO>();
        for(Method method: methods)
        {
            ANNO anno = method.getAnnotation(annoClz);
            if(anno != null)
            {
                map.put(method, anno);
            }
        }
        return map;
    }

    /**
     * 获取调用者的 Class<br />
     * <i>这个东西没搞清楚之前，还是不要随便调用，基本只用于框架开发</i>
     * 
     * @return 调用者的 Class
     */
    public static Class<?> getCaller()
    {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        boolean used = false;
        for(StackTraceElement ste: stack)
        {
            if(used == false && !StringUtil.isEquals(ste.getClassName(), "java.lang.Thread") && !StringUtil.isEquals(ste.getClassName(), ClassUtil.class.getName()))
            {
                used = true;
                continue;
            }

            if(used)
            {
                try
                {
                    return Class.forName(ste.getClassName());
                }
                catch(ClassNotFoundException e)
                {
                    System.err.print("找不到类: " + ste.getClassName());
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 获取调用者的 Class<br />
     * <i>这个东西没搞清楚之前，还是不要随便调用，基本只用于框架开发</i>
     * 
     * @param anchor 指定该Class，最终会获取调用该 Class 类的类
     * @return 调用者的 Class
     */
    public static Class<?> getCaller(Class<?> upon)
    {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        for(StackTraceElement ste: stack)
        {
            if(!StringUtil.isEquals(ste.getClassName(), "java.lang.Thread") && !StringUtil.isEquals(ste.getClassName(), ClassUtil.class.getName()) && !StringUtil.isEquals(ste.getClassName(), upon.getName()))
            {
                try
                {
                    return Class.forName(ste.getClassName());
                }
                catch(ClassNotFoundException e)
                {
                    System.err.print("找不到类: " + ste.getClassName());
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 获取指定包下面和子孙包下面所有类的 Class
     * 
     * @param packageName 包名
     * @return 类 Class 的集合
     */
    public static final Set<Class<?>> scanPackage(String packageName)
    {
        return com.xiaoleilu.hutool.util.ClassUtil.scanPackage(packageName);
    }

    /**
     * 获取指定包的 class 文件路径集合
     * 
     * @param packageName 包名
     * @return 类 Class 的集合
     */
    public static final Set<String> getClassPaths(String packageName)
    {
        return com.xiaoleilu.hutool.util.ClassUtil.getClassPaths(packageName);
    }

    /**
     * 获取当前 Java 运行时加载的本地资源文件夹路径
     * 
     * @return 路径集合
     */
    public static final Set<String> getClassPathResources()
    {
        return com.xiaoleilu.hutool.util.ClassUtil.getClassPathResources();
    }

    /**
     * 获取当前线程的 ClassLoader
     * 
     * @return 类加载器对象
     */
    public static final ClassLoader getContextClassLoader()
    {
        return com.xiaoleilu.hutool.util.ClassUtil.getContextClassLoader();
    }

    /**
     * 获取当前 Java 运行时环境的 ClassLoader
     * 
     * @return 类加载器对象
     */
    public static final ClassLoader getClassLoader()
    {
        return com.xiaoleilu.hutool.util.ClassUtil.getClassLoader();
    }

    /**
     * 获取 Java 运行环境的 ClassPath 变量
     * 
     * @return ClassPath 的集合
     */
    public static final String[] getJavaClassPaths()
    {
        return com.xiaoleilu.hutool.util.ClassUtil.getJavaClassPaths();
    }

    /**
     * 加载 Class
     * 
     * @param className 类名
     * @param isInitialized 是否初始化
     * @return 类 Class 对象
     */
    public static final Class<?> loadClass(String className, boolean isInitialized)
    {
        return com.xiaoleilu.hutool.util.ClassUtil.loadClass(className, isInitialized);
    }

    /**
     * 创建实例
     * 
     * @param clz Class 对象
     * @return 实例化后的对象
     */
    public static final <T> T newInstance(Class<T> clz)
    {
        return com.xiaoleilu.hutool.util.ClassUtil.newInstance(clz);
    }

    /**
     * 创建实例
     * 
     * @param type type 对象
     * @return 实例化后的对象
     */
    public static final <T> T newInstance(Type type)
    {
        if(type == null)
        {
            return null;
        }

        String className = StringUtil.substringAfterFirst(type.toString(), " ");

        return newInstance(className);
    }

    /**
     * 创建实例
     * 
     * @param className 类名
     * @return 实例化后的对象
     */
    public static final <T> T newInstance(String className)
    {
        return com.xiaoleilu.hutool.util.ClassUtil.newInstance(className);
    }

    /**
     * 扫描指定文件夹下面所有在类或接口上面有指定注解的类或接口<br />
     * 直接解析字节码，适用于在非运行时查找类的场景，使用的 <strong>javassist</strong> 库
     * 
     * @param dir 要扫描的文件夹
     * @param annoClz 匹配的注解
     * @param excludeAnnoClz 排除的注解
     * @return 有该注解的类或接口的 CtClass(Javassist 反射类)
     */
    @SafeVarargs
    public static Set<CtClass> scanTypeAnnotations(File dir, Class<? extends Annotation> annoClz, Class<? extends Annotation>... excludeAnnoClz)
    {
        log.debug("准备文件夹中类的注解： {}", annoClz.getCanonicalName());
        log.debug("在哪个文件夹中扫描: {}", dir);

        ClassPool pool = new ClassPool();
        Set<CtClass> set = CollectionUtil.newHashSet();
        try
        {
            pool.appendClassPath(dir.getPath());

            set = ClassUtil.findClassNames(dir).stream().map(clzName ->
            {
                try
                {
                    return pool.getCtClass(clzName);
                }
                catch(NotFoundException e)
                {
                    log.error("出错，找不到类: {}", e.getMessage(), e);
                }
                return null;
            }).filter(ctClz ->
            {
                boolean match = ctClz != null && ctClz.hasAnnotation(annoClz);
                for(Class<? extends Annotation> exclude: excludeAnnoClz)
                {
                    log.debug("由于包含 {} 这个注解，便排除在扫描结果之外", exclude.getCanonicalName());
                    match = false;
                }
                if(match)
                {
                    log.debug("扫描到 {} 是包含 {} 注解的，收了", ctClz.getName(), annoClz.getCanonicalName());
                }
                return match;
            }).collect(Collectors.toSet());
        }
        catch(NotFoundException e)
        {
            log.error("出错，找不到类: {}", e.getMessage(), e);
        }
        catch(MalformedURLException e)
        {
            log.error("出错: {}", e.getMessage(), e);
        }
        return set;

    }

    /**
     * 扫描指定包下面所有在类或接口上面有指定注解的类或接口
     * 
     * @param scanPackage 要扫描的包
     * @param annoClz 匹配的注解
     * @param excludeAnnoClz 排除的注解
     * @return 有该注解的类或接口
     */
    @SafeVarargs
    public static Set<Class<?>> scanTypeAnnotations(String scanPackage, Class<? extends Annotation> annoClz, Class<? extends Annotation>... excludeAnnoClz)
    {
        log.debug("准备包中的注解： {}", annoClz.getCanonicalName());
        log.debug("在哪个包中扫描: {}", scanPackage);

        Set<Class<?>> classSet = scanPackage(scanPackage);

        Set<Class<?>> set = CollectionUtil.newHashSet();
        for(Class<?> clz: classSet)
        {
            if(clz.isAnnotationPresent(annoClz))
            {
                // 找要排除的注解
                boolean excluded = false;
                if(excludeAnnoClz != null)
                {
                    for(Class<? extends Annotation> excludeItem: excludeAnnoClz)
                    {
                        if(clz.isAnnotationPresent(excludeItem))
                        {
                            log.debug("由于包含 {} 这个注解，便排除在扫描结果之外", excludeItem.getCanonicalName());
                            excluded = true;
                            break;
                        }
                    }
                }

                // 如果有排除的注解就不返回
                if(excluded)
                {
                    continue;
                }

                log.debug("扫描到 {} 是包含 {} 注解的，收了", clz.getCanonicalName(), annoClz.getCanonicalName());
                // 剩下的都是符合要求的精英
                set.add(clz);
            }
        }

        return set;
    }

    /**
     * 扫描指定 Class 集合中所有在类或接口上面有指定注解的类或接口
     * 
     * @param classes 要扫描的类集合
     * @param annoClz 匹配的注解
     * @param excludeAnnoClz 排除的注解
     * @return 有该注解的类或接口
     */
    @SafeVarargs
    public static Set<Class<?>> scanTypeAnnotations(Set<Class<?>> classSet, Class<? extends Annotation> annoClz, Class<? extends Annotation>... excludeAnnoClz)
    {
        log.debug("准备扫描类中的注解： {}", annoClz.getCanonicalName());
        log.debug("在哪些类中扫描: {}", classSet);
        Set<Class<?>> set = CollectionUtil.newHashSet();
        for(Class<?> clz: classSet)
        {
            if(clz == null)
            {
                continue;
            }

            if(clz.isAnnotationPresent(annoClz))
            {
                // 找要排除的注解
                boolean excluded = false;
                if(excludeAnnoClz != null)
                {
                    for(Class<? extends Annotation> excludeItem: excludeAnnoClz)
                    {
                        if(clz.isAnnotationPresent(excludeItem))
                        {
                            log.debug("由于包含 {} 这个注解，便排除在扫描结果之外", excludeItem.getCanonicalName());
                            excluded = true;
                            break;
                        }
                    }
                }

                // 如果有排除的注解就不返回
                if(excluded)
                {
                    continue;
                }

                log.debug("扫描到 {} 是包含 {} 注解的，收了", clz.getCanonicalName(), annoClz.getCanonicalName());
                // 剩下的都是符合要求的精英
                set.add(clz);
            }
        }

        return set;
    }

    /**
     * 在一个类或接口里面根据注解筛选出符合要求的方法
     * 
     * @param clz 要扫描的类的 Class
     * @param annoClz 匹配的注解
     * @param excludeAnnoClz 排除的注解
     * @return 匹配的方法
     */
    @SuppressWarnings("unchecked")
    public static Set<Method> scanMethodAnnotations(Class<?> clz, Class<? extends Annotation> annoClz, Class<? extends Annotation>... excludeAnnoClz)
    {
        Method[] methods = clz.getDeclaredMethods();

        Set<Method> set = CollectionUtil.newHashSet();
        for(Method method: methods)
        {
            if(method.getAnnotation(annoClz) != null)
            {
                // 找要排除的注解
                boolean excluded = false;
                if(excludeAnnoClz != null)
                {
                    for(Class<? extends Annotation> excludeItem: excludeAnnoClz)
                    {
                        if(clz.isAnnotationPresent(excludeItem))
                        {
                            excluded = true;
                            break;
                        }
                    }
                }

                // 如果有排除的注解就不返回
                if(excluded)
                {
                    continue;
                }

                // 剩下的都是符合要求的精英
                set.add(method);
            }
        }

        return set;
    }

    /**
     * 获取方法反射信息
     * 
     * @return 方法反射信息
     */
    public static final MethodReflectInfo getMethodInfo(Method method)
    {
        return new MethodReflectInfo(method, getMethodArgInfo(method));
    }

    /**
     * 获取方法参数反射信息
     * 
     * @param method 要获取的方法
     * @return 方法列表
     */
    public static List<MethodArgReflectInfo> getMethodArgInfo(Method method)
    {
        List<MethodArgReflectInfo> list = CollectionUtil.newList();

        try
        {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(method.getDeclaringClass().getName());
            CtMethod cm = cc.getDeclaredMethod(method.getName());

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
     * 加载指定目录下（包括子目录）所有的 Jar 包到运行环境中
     * 
     * @param dir 目录
     */
    public static void loadJarInDir(File dir)
    {
        if(dir == null || !dir.exists())
        {
            return;
        }

        List<File> files = FileUtil.getFileTree(dir, true, ".jar");
        log.debug("找到了的 JAR 包: {}", files);

        loadJars(files);
    }

    /**
     * 加载指定 Jar 包
     * 
     * @param jars JAR 数组
     */
    public static void loadJars(File... jars)
    {
        if(jars == null || ArrayUtil.isEmpty(jars))
        {
            return;
        }

        for(File f: jars)
        {
            JarClassLoader.loadJar(f);
            log.debug("加载 JAR 包: {}", f);
        }
    }

    /**
     * 加载指定 Jar 包
     * 
     * @param jars JAR 列表
     */
    public static void loadJars(List<File> jars)
    {
        if(jars == null || CollectionUtil.isNullOrEmpty(jars))
        {
            return;
        }

        for(File f: jars)
        {
            JarClassLoader.loadJar(f);
            log.debug("加载 JAR 包: {}", f);
        }
    }

    /**
     * 从目录中取到所有的类的名称<br />
     * 找到的只会解析类名，不会加载也不会初始化
     * 
     * @param dir 目录，将扫描该目录下所有文件
     * @return 查找到的类的集合，不会返回 null
     * @throws MalformedURLException
     */
    public static Set<String> findClassNames(File dir) throws MalformedURLException
    {
        if(dir == null || !dir.exists())
        {
            return CollectionUtil.newHashSet();
        }

        Set<String> set = CollectionUtil.newHashSet();

        URLClassLoader loader = new URLClassLoader(new URL[]{dir.toURI().toURL()}, ClassUtil.getClassLoader());

        String dirPath = dir.getAbsolutePath();
        List<String> list = FileUtil.getFileTree(dirPath, true, "class");

        for(String item: list)
        {
            // 删掉绝对路径前缀
            String className = StringUtil.replaceFirst(item, dirPath + File.separator, "");
            // 删掉 .class 扩展名
            className = StringUtil.removeLast(className, ".class".length());
            // 生成类名，替换文件路径为 .
            className = StringUtil.replaceAll(className, File.separator, ".");

            log.debug("找到的类: {}", className);

            set.add(className);
        }
        try
        {
            loader.close();
        }
        catch(IOException e)
        {
            log.error("关闭类加载器时发生 IO 异常: {}", e.getMessage(), e);
        }

        log.debug("在 {} 中查找到 {} 个类", dirPath, list.size());

        return set;
    }

    /**
     * 从目录中取到所有的类<br />
     * 找到的类会被加载，但不会初始化
     * 
     * @param dir 目录，将扫描该目录下所有文件
     * @return 查找到的类的集合，不会返回 null
     * @throws MalformedURLException
     */
    public static Set<Class<?>> findClasses(File dir) throws MalformedURLException
    {
        if(dir == null || !dir.exists())
        {
            return CollectionUtil.newHashSet();
        }

        Set<Class<?>> set = CollectionUtil.newHashSet();

        URLClassLoader loader = new URLClassLoader(new URL[]{dir.toURI().toURL()}, ClassUtil.getClassLoader());

        String dirPath = dir.getAbsolutePath();
        List<String> list = FileUtil.getFileTree(dirPath, true, "class");

        for(String item: list)
        {
            // 删掉绝对路径前缀
            String className = StringUtil.replaceFirst(item, dirPath + File.separator, "");
            // 删掉 .class 扩展名
            className = StringUtil.removeLast(className, ".class".length());
            // 生成类名，替换文件路径为 .
            className = StringUtil.replaceAll(className, File.separator, ".");
            Class<?> clz = ClassUtil.parseClass(loader, className);

            if(clz == null)
            {
                log.warn("无法加载的类: {}", className);
                continue;
            }

            log.debug("找到的类: {}", className);

            set.add(clz);
        }
        try
        {
            loader.close();
        }
        catch(IOException e)
        {
            log.error("关闭类加载器时发生 IO 异常: {}", e.getMessage(), e);
        }

        log.debug("在 {} 中查找到 {} 个可用类", dirPath, list.size());

        return set;
    }

    /**
     * 将 Class 名解析为 Class
     * 
     * @param clzName Class 全名
     * @return Class 对象，如果找不到，返回 null
     */
    public static Class<?> parseClass(String clzName)
    {
        log.debug("准备解析 Class: {}", clzName);
        if(clzName == null)
        {
            log.warn("解析 Class 错误，提供的类名 null");
            return null;
        }

        switch(clzName)
        {
            case "boolean":
                return boolean.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "double":
                return double.class;
            case "void":
                return void.class;
            case "float":
                return float.class;
            case "byte":
                return byte.class;
            default:
                try
                {
                    return ClassUtils.getClass(clzName);
                }
                catch(ClassNotFoundException e)
                {
                    log.warn("Class {} 找不到", clzName);
                    return null;
                }
                catch(NoClassDefFoundError e)
                {
                    log.warn("Class {} 没有定义: ", clzName);
                    return null;
                }
        }
    }

    /**
     * 将 Class 名解析为 Class
     * 
     * @param loader 指定必须从这个 ClassLoader 中找类
     * @param clzName Class 全名
     * @return Class 对象，如果找不到，返回 null
     */
    public static Class<?> parseClass(ClassLoader loader, String clzName)
    {
        log.debug("准备解析 Class: {}", clzName);
        if(clzName == null)
        {
            log.warn("解析 Class 错误，提供的类名 null");
            return null;
        }

        switch(clzName)
        {
            case "boolean":
                return boolean.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "double":
                return double.class;
            case "void":
                return void.class;
            case "float":
                return float.class;
            case "byte":
                return byte.class;
            default:
                try
                {
                    return ClassUtils.getClass(loader, clzName);
                }
                catch(ClassNotFoundException e)
                {
                    log.warn("Class {} 找不到", clzName);
                    return null;
                }
                catch(NoClassDefFoundError e)
                {
                    log.warn("Class {} 没有定义: ", clzName);
                    return null;
                }
        }
    }

    /**
     * 获取一个泛型类的第一个参数的实际类型
     * 
     * @param clz 包含泛型的 Class
     * @return 泛型参数实际 Type
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getNestedGenericType(Class<?> clz)
    {
        log.debug("准备解析 Class {} 的第一个泛型参数实际类型");
        Class<T> t = (Class<T>)((ParameterizedType)(clz.getGenericInterfaces()[0])).getActualTypeArguments()[0];

        log.debug("解析出了个 {}", t);
        return t;
    }

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, MalformedURLException
    {
        {
            String str = new String("abcdefg");
            Field flds[] = getFields(str);
            for(Field fld: flds)
            {
                System.out.println(fld.getName() + " = " + fld.get(str));
            }
        }

        {
            Field flds[] = getFields(String.class);
            for(Field fld: flds)
            {
                System.out.println(fld.getName());
            }
        }

        // TODO 添加单元测试 getFieldAnnotationMap();

        System.out.println(scanPackage("com.joindata"));

        System.out.println(getClassPaths("com.joindata"));

        {
            for(String str: getJavaClassPaths())
            {
                System.out.println(str);
            }
        }

        System.out.println(getClassPathResources());

        System.out.println(getContextClassLoader());
        System.out.println(getClassLoader());

        System.out.println(loadClass("com.joindata.inf.common.util.basic.WordUtil", true));
        System.out.println(newInstance(WordUtil.class));
        Object obj = newInstance("com.joindata.inf.common.util.basic.WordUtil");
        System.out.println(obj);

        System.out.println(findClasses(new File("E:/DEVELOP/WORKSPACE/GitWorkspace/Passport/passport-service-app/target/classes")));

        System.out.println(scanTypeAnnotations(new File("E:/DEVELOP/WORKSPACE/GitWorkspace/Passport/passport-service-app/target/classes"), Configuration.class));
    }

}