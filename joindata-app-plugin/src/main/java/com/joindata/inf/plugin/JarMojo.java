package com.joindata.inf.plugin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

import com.joindata.inf.boot.annotation.JoindataApp;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 创建胖 JAR 包，生成的 JAR 可以使用 java -jar 来执行
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 15, 2016 9:54:25 AM
 */
@Mojo(name = "jar", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Execute(goal = "clean compile dependency:copy-dependencies", phase = LifecyclePhase.PACKAGE)
public class JarMojo extends AbstractMojo
{

    @Parameter(defaultValue = "${project.artifacts}", readonly = true, required = true)
    private Collection<Artifact> artifacts;

    @Parameter(defaultValue = "${project.dependencies}", readonly = true, required = true)
    private Collection<Dependency> dependencies;

    /**
     * 可以包含各种 Native 二进制文件
     */
    @Parameter
    private FileSet[] binlibs;

    /** 输出目录 */
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private File outputDirectory;

    /** 项目 JAR 包名字 */
    @Parameter(defaultValue = "${project.build.finalName}.jar", readonly = true, required = true)
    private String mainJarFilename;

    /** 版本号 */
    @Parameter(defaultValue = "${project.version}", required = true)
    private String implementationVersion;

    /** 最终文件名 */
    @Parameter(defaultValue = "${project.build.finalName}-bootable.jar", required = true)
    private String filename;

    /** 模板版本号 */
    private String onejarVersion = "0.97";

    /** Maven 工程描述对象 */
    @Component
    private MavenProject project;

    @Component
    private MavenProjectHelper projectHelper;

    private String adminName = "嘉银数据.架构组 - 宋翔<songxiang@joindata.com>";

    public void execute() throws MojoExecutionException, MojoFailureException
    {

        // Show some info about the plugin.
        displayPluginInfo();

        JarOutputStream out = null;
        JarInputStream template = null;

        File onejarFile;
        try
        {
            // 创建 JAR 文件先
            onejarFile = new File(outputDirectory, filename);

            // 打开 JAR 文件输出流
            out = new JarOutputStream(new FileOutputStream(onejarFile, false), getManifest());

            // 工程 JAR 包
            getLog().debug("添加工程编译好的类: " + mainJarFilename);
            addToZip(new File(outputDirectory, mainJarFilename), "main/", out);

            // 添加工程依赖 JAR 包
            List<File> dependencyJars = extractDependencyFiles(artifacts);
            getLog().debug("工程依赖 JAR 包数: " + dependencyJars.size());
            for(File jar: dependencyJars)
            {
                addToZip(jar, "lib/", out);
            }

            // 添加系统依赖 JAR 包
            List<File> systemDependencyJars = extractSystemDependencyFiles(dependencies);
            getLog().debug("系统依赖 JAR 包数: " + systemDependencyJars.size());
            for(File jar: systemDependencyJars)
            {
                addToZip(jar, "lib/", out);
            }

            // XXX(暂时无用) 添加 Native 文件
            if(binlibs != null)
            {
                for(FileSet eachFileSet: binlibs)
                {
                    List<File> includedFiles = toFileList(eachFileSet);
                    if(getLog().isDebugEnabled())
                    {
                        getLog().debug("Adding [" + includedFiles.size() + "] native libraries...");
                    }
                    for(File eachIncludedFile: includedFiles)
                    {
                        addToZip(eachIncludedFile, "binlib/", out);
                    }
                }
            }

            // 添加 One-jar 相关资源
            getLog().debug("添加 one-jar 相关资源...");
            template = openOnejarTemplateArchive();
            ZipEntry entry;
            while((entry = template.getNextEntry()) != null)
            {
                // 跳过 One-jar 资源包中的 的 manifest
                if(!"boot-manifest.mf".equals(entry.getName()))
                {
                    addToZip(out, entry, template);
                }
            }

        }
        catch(IOException | DependencyResolutionRequiredException e)
        {
            getLog().error(e);
            throw new MojoExecutionException("打包失败", e);
        }
        finally
        {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(template);

            getLog().info("文件已打包到: " + outputDirectory.getAbsolutePath() + File.separator + filename);
        }
    }

    private void displayPluginInfo()
    {
        getLog().info("打包应用为可执行 JAR 包");
        getLog().info("应用名: " + project.getName());
        getLog().info("应用构件名: " + project.getArtifactId());
        getLog().info("应用版本号: " + implementationVersion);

        getLog().info("本插件改编自 one-jar 开源项目，保留协议: http://one-jar.sourceforge.net/one-jar-license.txt");
        getLog().info("如需帮助请联系: " + adminName);
    }

    /**
     * 获取 One-jar 模板文件名
     */
    private String getOnejarArchiveName()
    {
        return "one-jar-boot-" + onejarVersion + ".jar";
    }

    // /** One-jar 参数文件 */
    // private String getOnejarPropertyFile()
    // {
    // return "one-jar.properties";
    // }

    /**
     * 读取 One-jar 模板文件
     */
    private JarInputStream openOnejarTemplateArchive() throws IOException
    {
        return new JarInputStream(getClass().getClassLoader().getResourceAsStream(getOnejarArchiveName()));
    }

    /**
     * 获取 Manifest 描述文件
     * 
     * @throws DependencyResolutionRequiredException
     * @throws MojoFailureException
     */
    private Manifest getManifest() throws IOException, MojoFailureException, DependencyResolutionRequiredException
    {
        // 复制模板中的描述文件
        ZipInputStream zipIS = openOnejarTemplateArchive();
        Manifest manifest = new Manifest(getFileBytes(zipIS, "boot-manifest.mf"));
        IOUtils.closeQuietly(zipIS);

        // 设置启动类
        manifest.getMainAttributes().putValue("One-Jar-Main-Class", findBootClassName());

        // 设置版本号
        if(implementationVersion != null)
        {
            manifest.getMainAttributes().putValue("ImplementationVersion", implementationVersion);
        }

        return manifest;
    }

    /**
     * 查找启动类类名
     * 
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     * @throws MojoFailureException
     */
    private String findBootClassName() throws MalformedURLException, DependencyResolutionRequiredException, MojoFailureException
    {
        Set<Class<?>> clzSet = CollectionUtil.newHashSet();

        clzSet.addAll(ClassUtil.scanTypeAnnotations(loadProjectClasses(), JoindataApp.class));
        clzSet.addAll(ClassUtil.scanTypeAnnotations(loadProjectClasses(), JoindataWebApp.class));

        if(CollectionUtil.isNullOrEmpty(clzSet))
        {
            throw new MojoFailureException("源代码中找不到 @JoindataApp 或 @JoindataWebApp，没有启动类，无法打包");
        }

        if(clzSet.size() > 1)
        {
            getLog().error("在这些类中找到了启动注解，请检查: " + clzSet);
            throw new MojoFailureException("源代码中有" + clzSet.size() + "处标注了 @JoindataApp 或 @JoindataWebApp，无法分辨哪个是启动类");
        }

        Class<?> bootClz = null;
        for(Class<?> clz: clzSet)
        {
            bootClz = clz;
            getLog().info("启动类为: " + clz.getName());
        }

        return bootClz.getName();
    }

    /**
     * 加载编译后的类，以便读取项目代码中的一些信息
     * 
     * @return 项目所有的类
     */
    private Set<Class<?>> loadProjectClasses() throws DependencyResolutionRequiredException, MalformedURLException
    {
        Set<Class<?>> classSet = CollectionUtil.newHashSet();

        classSet.addAll(ClassUtil.findClasses(new File(project.getBuild().getOutputDirectory())));

        return classSet;
    }

    /**
     * 将元素添加到 JAR 包中
     * 
     * @param sourceFile
     * @param zipfilePath
     * @param out
     * @throws IOException
     */
    private void addToZip(File sourceFile, String zipfilePath, JarOutputStream out) throws IOException
    {
        addToZip(out, new ZipEntry(zipfilePath + sourceFile.getName()), new FileInputStream(sourceFile));
    }

    private final AtomicInteger alternativeEntryCounter = new AtomicInteger(0);

    private void addToZip(JarOutputStream out, ZipEntry entry, InputStream in) throws IOException
    {
        try
        {
            out.putNextEntry(entry);
            IOUtils.copy(in, out);
            out.closeEntry();
        }
        catch(ZipException e)
        {
            if(e.getMessage().startsWith("duplicate entry"))
            {
                // A Jar with the same name was already added. Let's add this one using a modified name:
                final ZipEntry alternativeEntry = new ZipEntry(entry.getName() + "-DUPLICATE-FILENAME-" + alternativeEntryCounter.incrementAndGet() + ".jar");
                addToZip(out, alternativeEntry, in);
            }
            else
            {
                throw e;
            }
        }
    }

    /**
     * 读取文件输入流
     * 
     * @param is
     * @param name
     * @return
     * @throws IOException
     */
    private InputStream getFileBytes(ZipInputStream is, String name) throws IOException
    {
        ZipEntry entry = null;
        while((entry = is.getNextEntry()) != null)
        {
            if(entry.getName().equals(name))
            {
                byte[] data = IOUtils.toByteArray(is);
                return new ByteArrayInputStream(data);
            }
        }
        return null;
    }

    /**
     * 获取依赖的 Artifact 列表
     * 
     * @param artifacts 依赖的 Artifact
     * @return 文件名列表
     */
    private List<File> extractDependencyFiles(Collection<Artifact> artifacts)
    {
        List<File> files = new ArrayList<File>();

        if(artifacts == null)
        {
            return files;
        }

        for(Artifact artifact: artifacts)
        {
            File file = artifact.getFile();

            if(file.isFile())
            {
                files.add(file);
            }
        }
        return files;
    }

    /**
     * 获取系统依赖包
     * 
     * @param systemDependencies 系统依赖列表
     * @return 文件名列表
     */
    private List<File> extractSystemDependencyFiles(Collection<Dependency> systemDependencies)
    {
        final ArrayList<File> files = new ArrayList<File>();

        if(systemDependencies == null)
        {
            return files;
        }

        for(Dependency systemDependency: systemDependencies)
        {
            if(systemDependency != null && "system".equals(systemDependency.getScope()))
            {
                files.add(new File(systemDependency.getSystemPath()));
            }
        }
        return files;
    }

    private static List<File> toFileList(FileSet fileSet) throws IOException
    {
        File directory = new File(fileSet.getDirectory());
        String includes = toString(fileSet.getIncludes());
        String excludes = toString(fileSet.getExcludes());
        return FileUtils.getFiles(directory, includes, excludes);
    }

    private static String toString(List<String> strings)
    {
        StringBuilder sb = new StringBuilder();
        for(String string: strings)
        {
            if(sb.length() > 0)
            {
                sb.append(", ");
            }
            sb.append(string);
        }
        return sb.toString();
    }

}
