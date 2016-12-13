package com.joindata.inf.plugin.pkg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import com.joindata.inf.boot.annotation.JoindataApp;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.plugin.pkg.util.JarArchiveUtil;

/**
 * 测试 HelloWorld
 *
 * @author songxiang
 * @deprecated Don't use!
 */
@Mojo(name = "pkg")
public class PackageClass extends AbstractMojo
{
    @Component
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        {
            Set<Class<?>> types = ClassUtil.scanTypeAnnotations("com.joindata", JoindataApp.class);
            for(Class<?> clz: types)
            {
                getLog().info(clz.getName());
            }
        }

        {
            Set<Class<?>> types = ClassUtil.scanTypeAnnotations("com.joindata", JoindataWebApp.class);
            for(Class<?> clz: types)
            {
                getLog().info(clz.getName());
            }
        }

        try
        {
            List<String> classPathElements = project.getCompileClasspathElements();
            if(CollectionUtil.isNullOrEmpty(classPathElements))
            {
                throw new MojoFailureException("没有内容可以打包，请检查是否已编译");
            }

            System.out.println("classes: " + classPathElements);

            System.out.println("basedir: " + project.getBasedir());
            System.out.println("file: " + project.getFile());

            System.out.println("testFile: " + new File(project.getBasedir(), project.getArtifactId() + "-" + project.getVersion() + ".jar"));

            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "ABC.aaa");

            JarArchiveUtil.createJarFile(new File(classPathElements.get(0)), new File(project.getBasedir(), project.getArtifactId() + "-" + project.getVersion() + ".jar"), manifest);
            
        }
        catch(DependencyResolutionRequiredException | IOException e)
        {
            getLog().error(e);
            throw new MojoFailureException(e.getMessage());
        }

        getLog().info("---------------------");
    }
}
