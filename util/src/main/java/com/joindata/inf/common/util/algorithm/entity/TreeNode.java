package com.joindata.inf.common.util.algorithm.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.jxpath.JXPathContext;
import org.dom4j.Document;

import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.XmlUtil;

/**
 * 树节点
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Feb 17, 2017 4:31:55 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TreeNode implements Serializable
{
    private static final long serialVersionUID = -7466715440973730439L;

    @XmlTransient
    private transient JXPathContext context;

    @XmlAttribute
    private String code;

    @XmlAttribute
    private String parentCode;

    @XmlElementWrapper(name = "children")
    @XmlElement(name = "node")
    private Collection<TreeNode> children;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Collection<TreeNode> getChildren()
    {
        return children;
    }

    public void setChildren(Collection<TreeNode> children)
    {
        this.children = children;
    }

    public String getParentCode()
    {
        return parentCode;
    }

    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode;
    }

    /**
     * 将树转化为 Dom4j 的 Document 对象
     * 
     * @return DOM 对象
     */
    public Document toDom()
    {
        return XmlUtil.toDom(toXML(false));
    }

    /**
     * 将树转化为 XML
     * 
     * @param pretty 是否格式化
     * @return XML 字符串
     */
    public String toXML(boolean pretty)
    {
        return XmlUtil.jaxbObjectToXML(this, pretty);
    }

    /**
     * 将树转化为 JSON
     * 
     * @param pretty 是否格式化
     * @return JSON 字符串
     */
    public String toJSON(boolean pretty)
    {
        if(pretty)
        {
            return JsonUtil.toPrettyJSON(this, true);
        }

        return JsonUtil.toJSON(this, true);
    }

    /**
     * 获取当前树对象的 JXPathContext
     * 
     * @return JXPathContext
     */
    @Transient
    protected JXPathContext getContext()
    {
        if(context == null)
        {
            context = JXPathContext.newContext(this);
        }

        return context;
    }

    /**
     * 获取指定 code 的所有后代（子、孙...)
     * 
     * @param code 树的 code 属性
     * @return 所有后代
     */
    @SuppressWarnings("unchecked")
    public <T extends TreeNode> List<T> getDescendant(String code)
    {
        return getContext().selectNodes("//children[@code='" + code + "']//children");
    }

    /**
     * 通过 XPath 表达式选取树中的节点
     * 
     * @param xpath XPath 表达式
     * @return 匹配的节点
     */
    @SuppressWarnings("unchecked")
    public List<TreeNode> selectNodes(String xpath)
    {
        return getContext().selectNodes(xpath);
    }

    /**
     * 同 {@code toXML(true)}
     * 
     * @see {@link #toXML(boolean)}
     */
    @Override
    public String toString()
    {
        return this.toXML(true);
    }
}
