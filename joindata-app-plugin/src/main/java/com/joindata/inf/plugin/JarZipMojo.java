package com.joindata.inf.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
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

import com.joindata.inf.boot.annotation.JoindataApp;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.plugin.util.JarArchiveUtil;

/**
 * 创建 ZIP 包，这个会把程序入口 JAR 包和依赖都打进一个 ZIP 包，解压后可通过 java -jar 来运行
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 15, 2016 9:54:25 AM
 * @deprecated JAR 包运行会出各种问题，不建议用这个打包
 */
@Mojo(name = "jarzip", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Execute(goal = "clean compile", phase = LifecyclePhase.PACKAGE)
public class JarZipMojo extends AbstractMojo
{

    @Parameter(defaultValue = "${project.artifacts}", readonly = true, required = true)
    private Collection<Artifact> artifacts;

    @Parameter(defaultValue = "${project.dependencies}", readonly = true, required = true)
    private Collection<Dependency> dependencies;

    /** 输出目录 */
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private File outputDirectory;

    /** 项目 JAR 包名字 */
    @Parameter(defaultValue = "${project.build.finalName}.jar", readonly = true, required = true)
    private String mainJarFilename;

    /** 依赖输出文件夹名字 */
    private String libDirName = "lib";

    /** 版本号 */
    @Parameter(defaultValue = "${project.version}", required = true)
    private String implementationVersion;

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

        // 依赖输出目录
        File libDir = new File(outputDirectory, libDirName);

        // 工程主要 jar 文件
        File mainJarFile = new File(outputDirectory, mainJarFilename);

        // 最终的 zip 文件
        File zipFile = new File(outputDirectory, project.getBuild().getFinalName() + ".zip");

        try
        {
            // 复制依赖
            List<File> dependencyJars = extractDependencyFiles(artifacts);
            getLog().info("工程依赖 JAR 包数: " + dependencyJars.size());
            for(File jar: dependencyJars)
            {
                getLog().debug("复制依赖: " + jar);
                FileUtil.copyToDir(jar.getPath(), libDir.getPath());
            }

            // 添加系统依赖 JAR 包
            List<File> systemDependencyJars = extractSystemDependencyFiles(dependencies);
            getLog().info("系统依赖 JAR 包数: " + systemDependencyJars.size());
            for(File jar: systemDependencyJars)
            {
                getLog().debug("复制依赖: " + jar);
                FileUtil.copyToDir(jar.getPath(), libDir.getPath());
            }

            // 创建主要 JAR 文件
            {
                Manifest manifest = new Manifest();
                manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
                manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, findBootClassName());
                manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, implementationVersion);
                manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_TITLE, project.getName());
                dependencyJars.addAll(systemDependencyJars);
                manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, JarArchiveUtil.getManifestClassPath(libDirName, dependencyJars));
                
                JarArchiveUtil.createJarFile(new File(project.getBuild().getOutputDirectory()), mainJarFile, manifest);
            }

            // 打包文件
            FileUtil.zip(zipFile, libDir, mainJarFile);
        }
        catch(IOException e)
        {
            getLog().error("IO 错误", e);
        }
        catch(DependencyResolutionRequiredException e)
        {
            getLog().error("无法解析依赖", e);
        }

        getLog().info("文件已打包到: " + zipFile.getPath());
    }

    private void displayPluginInfo()
    {
        getLog().info("打包应用为可执行 JAR 包");
        getLog().info("应用名: " + project.getName());
        getLog().info("应用构件名: " + project.getArtifactId());
        getLog().info("应用版本号: " + implementationVersion);
        getLog().info("如需帮助请联系: " + adminName);
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
}
