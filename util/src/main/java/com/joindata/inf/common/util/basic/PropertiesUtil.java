package com.joindata.inf.common.util.basic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 属性数据实用工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月24日 下午2:44:15
 */
public class PropertiesUtil
{
    /**
     * 读取指定 properties 文件中的指定属性值
     * 
     * @param filePath properties 文件路径
     * @param property 属性名
     * @throws RuntimeException 发生 IO 错误，抛出该异常，但不要求强制处理
     */
    public static String getProperty(String filePath, String property)
    {
        return loadProperties(filePath).getProperty(property);
    }

    /**
     * 从指定文件读取 Properties 并装入 Properties 文件
     * 
     * @param filePath 文件路径
     * @return Properties 对象
     * @throws RuntimeException 发生 IO 错误，抛出该异常，但不要求强制处理
     */
    public static Properties loadProperties(String filePath)
    {
        try
        {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            properties.load(bf);
            inputStream.close();
            return properties;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定流读取 Properties 并装入 Properties 文件
     * 
     * @param input 输入流
     * @return Properties 对象
     * @throws RuntimeException 发生 IO 错误，抛出该异常，但不要求强制处理
     */
    public static Properties loadProperties(InputStream input)
    {
        try
        {
            Properties properties = new Properties();
            BufferedReader bf = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            properties.load(bf);
            input.close();
            return properties;
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
