package com.joindata.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joindata.inf.common.support.fastdfs.support.component.web.FastDfsMultipartFile;
import com.joindata.test.mapper.TestMapper;

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
    public String uploadFdfs(@RequestParam FastDfsMultipartFile file) throws InterruptedException, ExecutionException, IllegalStateException, IOException
    {
        return file.save();
    }

    @GetMapping("/test/list")
    public List<String> list()
    {
        return testMapper.test();
    }
}
