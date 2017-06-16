package com.joindata.inf.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.util.StreamUtils;

import com.joindata.inf.boot.annotation.JoindataApp;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.basic.ResourceUtil;
import com.joindata.inf.common.util.basic.StringUtil;

import javassist.CtClass;

//////////////////////////////////////////////////////////////////
//                                                              //
//                          _ooOoo_                             //
//                         o8888888o                            //
//                         88" . "88                            //
//                         (| ^_^ |)                            //
//                         O\  =  /O                            //
//                      ____/`---'\____                         //
//                    .'  \\|     |//  `.                       //
//                   /  \\|||  :  |||//  \                      //
//                  /  _||||| -:- |||||-  \                     //
//                  |   | \\\  -  /// |   |                     //
//                  | \_|  ''\---/''  |   |                     //
//                  \  .-\__  `-`  ___/-. /                     //
//                ___`. .'  /--.--\  `. . ___                   //
//              ."" '<  `.___\_<|>_/___.'  >'"".                //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /               //
//      ========`-.____`-.___\_____/___.-`____.-'========       //
//                           `=---='                            //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
//                                                              //
//                        打包也要按照基本法                                                                                           //
//                                                              //
//////////////////////////////////////////////////////////////////

/**
 * 创建 ZIP 包，这个会把程序入口 JAR 包和依赖都打进一个 ZIP 包
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 15, 2016 9:54:25 AM
 */
@Mojo(name = "zip", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Execute(goal = "clean compile", phase = LifecyclePhase.PACKAGE)
public class ZipMojo extends AbstractMojo
{

    @Parameter(defaultValue = "${project.artifacts}", readonly = true, required = true)
    private Collection<Artifact> artifacts;

    @Parameter(defaultValue = "${project.dependencies}", readonly = true, required = true)
    private Collection<Dependency> dependencies;

    /** 输出目录 */
    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private File outputDirectory;

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

    private String bootClzName;

    private String appId;

    private String webPort;

    private String adminName = "嘉银数据.架构组 - 宋翔<songxiang@joindata.com>";

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // Show some info about the plugin.
        displayPluginInfo();

        try
        {
            // 获取主类信息
            CtClass mainClass = findBootClass();
            String appVersion = implementationVersion;

            {
                JoindataApp app = (JoindataApp)mainClass.getAnnotation(JoindataApp.class);
                if(app != null)
                {
                    appId = app.id();
                }
            }
            {
                JoindataWebApp app = (JoindataWebApp)mainClass.getAnnotation(JoindataWebApp.class);
                if(app != null)
                {
                    appId = app.id();
                    try
                    {
                        webPort = String.valueOf(app.port());
                    }
                    catch(Exception e)
                    {
                        webPort = "8080";
                    }
                }
            }

            // 编译目录 (target/classes)
            File targetDir = new File(project.getBuild().getOutputDirectory());

            // 程序打包目录 (target/pack)
            File _packRoot = new File(outputDirectory, "pack/");

            // 程序打包目录 (target/pack/x.x.x)
            File packRoot = new File(outputDirectory, "pack/" + appVersion);

            // 程序打包目录 (target/pack/x.x.x/PROGRAM)
            File packProgramRoot = new File(packRoot, "PROGRAM");

            // 程序配置文件目录 (target/pack/x.x.x/CONFIG)
            File packConfigRoot = new File(packRoot, "CONFIG");

            // 程序服务总成 (target/pack/x.x.x/init.d)
            File packServiceRoot = new File(packRoot, "init.d");

            // 程序主目录 (target/pack/x.x.x/PROGRAM/xxx-x.x.x)
            File packProgram = new File(packProgramRoot, appId + "-" + appVersion);

            // 依赖输出目录 (target/pack/x.x.x/PROGRAM/lib)
            File libDir = new File(packProgramRoot, libDirName);

            String moduleFiles[] = {"start.sh", "stop.sh", "restart.sh", "status.sh", "install.sh", "uninstall.sh", "log.sh", "service.sh", "deploy.sh", "deploy-logo", "JMX_OPTS", "JVM_OPTS", "APP_OPTS", "LINUX_USER"};
            Map<String, File> fileMap = CollectionUtil.newMap();
            for(String file: moduleFiles)
            {
                if(file.endsWith("_OPTS") || file.equals("LINUX_USER"))
                {
                    fileMap.put(file, new File(packConfigRoot, file));
                }
                else if(file.equals("install.sh") || file.equals("uninstall.sh"))
                {
                    fileMap.put(file, new File(_packRoot, file));
                }
                else if(file.equals("service.sh"))
                {
                    fileMap.put(file, new File(packServiceRoot, file));
                }
                else if(file.equals("deploy.sh") || file.equals("deploy-logo"))
                {
                    fileMap.put(file, new File(outputDirectory, file));
                }
                else
                {
                    fileMap.put(file, new File(packRoot, file));
                }
            }

            // 依赖 JAR 包名字
            StringBuffer libJars = new StringBuffer();
            // 解压依赖
            List<File> dependencyJars = extractDependencyFiles(artifacts);
            // 最终的 zip 文件 (target/xxx-x.x.x.zip，包括 packRoot 下所有的资源)
            File zipFile = new File(outputDirectory, appId + "-" + appVersion + ".zip");
            // 复制依赖
            getLog().info("工程依赖 JAR 包数: " + dependencyJars.size());
            for(File jar: dependencyJars)
            {
                getLog().debug("复制依赖: " + jar);
                libJars.append(FileUtil.getName(jar.getPath())).append(" ");
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

            // 复制编译后的文件
            for(File f: targetDir.listFiles())
            {
                FileUtil.copyToDir(f, packProgram);
            }

            // 写配置文件和脚本
            for(String file: moduleFiles)
            {
                String content = StreamUtils.copyToString(ResourceUtil.getRootResourceAsStream("_MODULE/" + file), Charset.forName("UTF-8"));
                content = StringUtil.replaceAll(content, "__MAINCLASS__", mainClass.getName());
                content = StringUtil.replaceAll(content, "__LIBJARS__", libJars.toString());
                content = StringUtil.replaceAll(content, "__APPID__", appId);
                content = StringUtil.replaceAll(content, "__APPVERSION__", appVersion);
                content = StringUtil.replaceAll(content, "__PROG__", appId + "-" + appVersion);
                content = StringUtil.replaceAll(content, "__WEBPORT__", webPort);

                FileUtil.writeTo(fileMap.get(file).getPath(), content);

                if(fileMap.get(file).getName().endsWith(".sh"))
                {
                    fileMap.get(file).setExecutable(true, true);
                }
            }

            // 打包文件
            FileUtil.zip(zipFile, _packRoot.listFiles());

            getLog().info("");
            getLog().info("应用 ID: " + appId);
            getLog().info("应用版本: " + appVersion);
            getLog().info("启动类: " + bootClzName);
            getLog().info("");
            getLog().info("发布件已打包到: " + zipFile.getPath());
            getLog().info("执行 " + outputDirectory + " 目录下的 deploy.sh 可进行部署");
            getLog().info("");
            getLog().info("如需帮助请联系: " + adminName);
        }
        catch(IOException e)
        {
            getLog().error("IO 错误, ", e);
        }
        catch(DependencyResolutionRequiredException e)
        {
            getLog().error("无法解析依赖, ", e);
        }
        catch(InterruptedException e)
        {
            getLog().error("线程错误, ", e);
        }
        catch(ClassNotFoundException e)
        {
            getLog().error("找不到类, ", e);
        }

    }

    private void displayPluginInfo()
    {
        getLog().info("");
        getLog().info("打包应用为 ZIP 包");
        getLog().info("工程名: " + project.getName());
        getLog().info("工程构件名: " + project.getArtifactId());
        getLog().info("工程版本号: " + implementationVersion);
        getLog().info("");
    }

    /**
     * 查找启动类
     * 
     * @param dependencyJars
     * 
     * @throws DependencyResolutionRequiredException
     * @throws MalformedURLException
     * @throws MojoFailureException
     * @throws InterruptedException
     */
    private CtClass findBootClass() throws MalformedURLException, DependencyResolutionRequiredException, MojoFailureException, InterruptedException
    {
        Set<CtClass> clzSet = CollectionUtil.newHashSet();

        File classDir = new File(project.getBuild().getOutputDirectory());
        clzSet.addAll(ClassUtil.scanTypeAnnotations(classDir, JoindataApp.class));
        clzSet.addAll(ClassUtil.scanTypeAnnotations(classDir, JoindataWebApp.class));

        if(CollectionUtil.isNullOrEmpty(clzSet))
        {
            throw new MojoFailureException("源代码中找不到 @JoindataApp 或 @JoindataWebApp，没有启动类，无法打包");
        }

        if(clzSet.size() > 1)
        {
            getLog().error("在这些类中找到了启动注解，请检查: " + clzSet);
            throw new MojoFailureException("源代码中有" + clzSet.size() + "处标注了 @JoindataApp 或 @JoindataWebApp，无法分辨哪个是启动类");
        }

        CtClass bootClz = null;
        for(CtClass clz: clzSet)
        {
            bootClz = clz;
            bootClzName = clz.getName();
            getLog().debug("找到启动类: " + clz.getName());
        }

        return bootClz;
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
