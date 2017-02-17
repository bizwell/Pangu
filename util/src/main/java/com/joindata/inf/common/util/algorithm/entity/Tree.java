package com.joindata.inf.common.util.algorithm.entity;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.jxpath.JXPathContext;
import org.dom4j.Document;

import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.XmlUtil;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tree
{
    @XmlTransient
    private JXPathContext context;

    @XmlElementWrapper(name = "tree")
    private Set<TreeNode> children;

    public Set<TreeNode> getChildren()
    {
        return children;
    }

    public void setChildren(Set<TreeNode> children)
    {
        this.children = children;
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
            return JsonUtil.toPrettyJSON(this);
        }

        return JsonUtil.toJSON(this);
    }

    /**
     * 获取当前树对象的 JXPathContext
     * 
     * @return JXPathContext
     */
    private JXPathContext getContext()
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
    public List<TreeNode> getPosterity(String code)
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
