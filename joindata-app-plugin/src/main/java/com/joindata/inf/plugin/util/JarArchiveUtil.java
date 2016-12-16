package com.joindata.inf.plugin.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;

/**
 * Jar 包工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 13, 2016 4:56:10 PM
 * @deprecated 没啥用了，以后改造一下做成打 ZIP 包的工具
 */
@Deprecated
public class JarArchiveUtil
{
    /**
     * 创建一个 JAR 包
     * 
     * @param inputPathList 输入的文件夹列表
     * @param targetFile 输出目标
     * @param manifest JAR 包描述文件
     * @param libJars 添加的其他 JAR 包，会自动放到生成 JAR 包的 lib 目录中，并且会添加 classpath 在描述文件中
     * @throws IOException 如果发生输入输出错误，抛出该异常
     */
    public static final void createJarFile(List<String> inputPathList, File targetFile, Manifest manifest, File... libJars) throws IOException
    {
        String libDir = "lib";
        addManifestClassPath(manifest, libDir, libJars);

        JarOutputStream target = new JarOutputStream(new FileOutputStream(targetFile), manifest);

        for(String inputPath: inputPathList)
        {
            File inputFile = new File(inputPath);
            addPath(inputFile, inputFile, target);
        }

        // 如果指定了依赖打包，就添加进去
        if(!ArrayUtil.isEmpty(libJars))
        {
            addLibJar(target, libDir, libJars);
        }

        target.close();
    }

    /**
     * 给 Jar 包中添加元素
     */
    private static void addPath(File root, File source, JarOutputStream target) throws IOException
    {
        String path = root.getPath() + "\\";
        BufferedInputStream in = null;
        try
        {
            if(source.isDirectory())
            {
                for(File nestedFile: source.listFiles())
                    addPath(root, nestedFile, target);
                return;
            }

            JarEntry entry = new JarEntry(StringUtil.replaceAll(StringUtil.replaceAll(source.getPath(), path, ""), "\\", "/"));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while(true)
            {
                int count = in.read(buffer);
                if(count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        }
        finally
        {
            if(in != null)
                in.close();
        }
    }

    /**
     * 设置 Manifest 中的 Classpath 信息
     */
    private static void addManifestClassPath(Manifest manifest, String libDir, File... libJars)
    {
        StringBuffer classPathNames = new StringBuffer(". ");
        for(File file: libJars)
        {
            String entryName = libDir + "/" + file.getName();
            classPathNames.append(entryName).append(" ");
        }

        manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, classPathNames.toString().trim());
    }

    /**
     * 添加 Jar
     * 
     * @param jars Jar 文件数组
     */
    private static void addLibJar(JarOutputStream target, String libDir, File... jars) throws IOException
    {
        for(File file: jars)
        {
            BufferedInputStream in = null;
            try
            {
                String entryName = libDir + "/" + file.getName();
                JarEntry entry = new JarEntry(entryName);
                entry.setTime(file.lastModified());
                target.putNextEntry(entry);

                in = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024];
                while(true)
                {
                    int count = in.read(buffer);
                    if(count == -1)
                        break;
                    target.write(buffer, 0, count);
                }
                target.closeEntry();
            }
            finally
            {
                if(in != null)
                    in.close();
            }

        }
    }

    public static void main(String[] args) throws IOException
    {

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "com.joindata.inf.test.plugin.App");

        JarArchiveUtil.createJarFile(CollectionUtil.newList("E:/DEVELOP/WORKSPACE/EclipseWorkspace/Joindata/plugin/target/classes", "E:/DEVELOP/WORKSPACE/EclipseWorkspace/Joindata/plugin/target/lib"), new File("E:/DEVELOP/WORKSPACE/EclipseWorkspace/Joindata/plugin/fuck.jar"), manifest, new File("E:/DEVELOP/ENV/jdk1.8.0_112/jre/lib/ext").listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".jar");
            }
        }));

    }
}
