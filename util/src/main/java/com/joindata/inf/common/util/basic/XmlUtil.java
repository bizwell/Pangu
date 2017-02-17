package com.joindata.inf.common.util.basic;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.joindata.inf.common.util.algorithm.entity.Tree;
import com.joindata.inf.common.util.log.Logger;

/**
 * XML 相关的工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Feb 17, 2017 6:32:55 PM
 */
public class XmlUtil
{
    private static final Logger log = Logger.get();

    /**
     * 将 JAXB 对象生成 XML
     * 
     * @param root 对象
     * @param pretty 是否格式化
     * @return XML 字符串
     */
    public static final String jaxbObjectToXML(Object root, boolean pretty)
    {
        String xmlString = "";
        try
        {
            JAXBContext context = JAXBContext.newInstance(Tree.class);
            Marshaller m = context.createMarshaller();

            if(pretty)
            {
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            }

            StringWriter sw = new StringWriter();
            m.marshal(root, sw);
            xmlString = sw.toString();

        }
        catch(JAXBException e)
        {
            log.error("解析 JAXB 对象出错, {}", e.getMessage(), e);
        }

        return xmlString;
    }

    /**
     * 将 XML 字符串转换成 dom4j 的 Document 对象
     * 
     * @param xml xml 字符串
     * @return Document 对象
     */
    public static final Document toDom(String xml)
    {
        try
        {
            return DocumentHelper.parseText(xml);
        }
        catch(DocumentException e)
        {
            log.error("解析 XML 出错, {}", e.getMessage(), e);
            return null;
        }
    }
}
