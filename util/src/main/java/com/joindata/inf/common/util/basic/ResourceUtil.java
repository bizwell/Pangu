package com.joindata.inf.common.util.basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

/**
 * 运行时资源相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月21日 下午6:16:35
 */
public class ResourceUtil
{
    /**
     * 指定资源是否在 jar 包中
     * 
     * @param resource 资源
     * @return true，如果是在 jar 包中。如果发生任何 IO 异常，均返回 false
     */
    public static boolean isResourceInJar(String resource)
    {
        try
        {
            return ResourceUtils.isJarURL(new ClassPathResource(resource).getURL());
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取资源在应用中的路径<br />
     * <i>适用于开发环境中直接运行的程序</i><br />
     * <i>在 Jar 包中的时候，会是一个 JAR 协议的 URL，对应的资源将不可直接读取</i>
     * 
     * @param resource 资源
     * @return 资源的绝对路径。如果路径不存在，返回 null
     */
    public static String getResourcePath(String resource)
    {
        try
        {
            return ResourceUtils.getFile("classpath:" + resource).getAbsolutePath();
        }
        catch(FileNotFoundException e)
        {
            return null;
        }
    }

    /**
     * 获取自己所在的 jar 文件路径
     * 
     * @param clz 要获取 jar 文件路径的 jar 所在包含的任何 class
     * @return 该 clz 所在的 jar 文件
     * @throws ClassNotFoundException 找不到给定的 Class，抛出该异常
     */
    public static String getJarSelf(String className) throws ClassNotFoundException
    {
        Class<?> clz = Class.forName(className);
        return getJarSelf(clz);
    }

    /**
     * 获取自己所在的 jar 文件路径
     * 
     * @param clz 要获取 jar 文件路径的 jar 所在包含的任何 class
     * @return 该 clz 所在的 jar 文件
     */
    public static String getJarSelf(Class<?> clz)
    {
        try
        {
            return URLDecoder.decode(clz.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            // 麻痹根本不可能发生好吧
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从指定类所属的 jar 中解压指定资源
     * 
     * @param jarPath Jar 文件
     * @param resource Jar 中的资源
     * @param targetPath 解压后的目录
     * @return 解压后的目录
     * @throws IOException 解压过程中发生任何 IO 错误，抛出该异常
     */
    public static String extractResourceFromSelfJar(Class<?> clz, String resource, String targetPath) throws IOException
    {
        FileUtil.mkdirs(targetPath);

        InputStream in = new FileInputStream(getJarSelf(clz));

        JarArchiveInputStream zip = new JarArchiveInputStream(in);

        while(true)
        {
            ZipEntry entry = zip.getNextJarEntry();
            if(entry == null)
            {
                break;
            }
            String name = entry.getName();
            if(name.startsWith(resource))
            {
                if(entry.isDirectory() || entry.getExtra() == null)
                {
                    continue;
                }

                InputStream resIn = ClassUtil.getRootResourceAsStream(name);
                FileUtil.delete(targetPath + "/" + StringUtil.replaceFirst(name, resource, ""));
                FileUtil.writeTo(targetPath + "/" + StringUtil.replaceFirst(name, resource, ""), resIn);
                resIn.close();
            }
        }

        zip.close();
        in.close();

        return targetPath;
    }
}
