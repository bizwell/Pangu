package com.joindata.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.joindata.inf.common.support.fastdfs.support.component.web.FastDfsFile;
import com.joindata.test.mapper.TestMapper;

import io.swagger.annotations.ApiOperation;

/**
 * 测试
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午3:45:00
 */
@RestController
public class TestController
{
    @Autowired
    private TestMapper testMapper;

    @PostMapping("/test/upload")
    @ApiOperation("上传文件")
    public String uploadFdfs(@RequestParam MultipartFile file) throws InterruptedException, ExecutionException, IllegalStateException, IOException
    {
        FastDfsFile fastDfsFile = new FastDfsFile("/asdf.jpg");
        String id = null;
        try
        {

            file.transferTo(fastDfsFile);
            id = fastDfsFile.getId();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }

        return id;
    }

    @GetMapping("/test/list")
    public List<String> list()
    {
        return testMapper.test();
    }
}
