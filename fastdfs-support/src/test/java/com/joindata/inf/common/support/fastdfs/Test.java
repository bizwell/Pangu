package com.joindata.inf.common.support.fastdfs;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.support.disconf.bootconfig.DisconfConfig;

@SpringBootApplication
@Import(DisconfConfig.class)
public class Test
{
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = SpringApplication.run(Test.class, args);

        // System.err.println(context.getBean(TestFastDFS.class).uploadFile(new File("D:\\Documents\\Downloads\\Git命令模式（推荐）.doc")));
        System.err.println(context.getBean(TestFastDFS.class).downloadFile("group1/M00/00/00/wKiJZFhAY1yAG2w8ABFgAK_YJSs210.doc", new File("D:\\Documents\\Downloads\\Git命令模式（推荐）_download.doc")));

        Thread.currentThread().join();
    }
}
