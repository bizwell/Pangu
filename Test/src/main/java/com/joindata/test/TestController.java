package com.joindata.test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.joindata.inf.common.basic.entities.KeyValuePair;
import com.joindata.inf.common.support.fastdfs.support.component.web.FastDfsFile;
import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.test.mapper.TestMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

    @Autowired
    private RedisClient redisClient;

    @PostMapping("/test/upload")
    @ApiOperation("上传文件到 FastDFS")
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
    @ApiOperation("测试 MyBatis")
    public List<String> list()
    {
        return testMapper.test();
    }

    @PutMapping("/test/redis/{name}/")
    @ApiOperation("添加 Redis 键值对")
    public KeyValuePair putRedisValue(@PathVariable @ApiParam("键") String name, @RequestBody @ApiParam("值") String value)
    {
        try
        {
            redisClient.put(name, value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return KeyValuePair.define("成功", redisClient.getString(name));
    }
}
