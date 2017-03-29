package com.joindata.inf.common.support.disconf.util;

import java.io.File;
import java.io.IOException;

import org.dom4j.Document;

import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.support.disconf.cst.DisconfCst;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.XmlUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * Disconf 工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 11:17:05 AM
 */
public class DisconfUtil
{
    private static final Logger log = Logger.get();

    /**
     * 获取 File
     * 
     * @param filename 文件名
     * @return File 对象
     */
    public static final File getFile(String filename)
    {
        return new File(DisconfCst.DOWNLOAD_DIR + "/" + filename);
    }

    /**
     * 读取文件
     * 
     * @param filename 文件名
     * @return 文件内容
     */
    public static final String readFile(String filename)
    {
        try
        {
            return FileUtil.readFile(DisconfCst.DOWNLOAD_DIR + "/" + filename);
        }
        catch(IOException e)
        {
            log.info("读取文件 {} 出错: {}", filename, e.getMessage(), e);
            throw new ResourceException(ResourceErrors.RESOURCE_ERROR, e.getMessage());
        }
    }

    /**
     * 以 XML 读取
     * 
     * @param filename 文件名
     * @return Dom4j Document
     */
    public static final Document readXml(String filename)
    {
        return XmlUtil.toDom(readFile(filename));
    }

    /**
     * 以 JSON 读取
     * 
     * @param filename 文件名
     * @return 读取 JSON 后转换对应的对象
     */
    public static final <T> T readJson(String filename, Class<T> clz)
    {
        return JsonUtil.fromJSON(readFile(filename), clz);
    }
}
