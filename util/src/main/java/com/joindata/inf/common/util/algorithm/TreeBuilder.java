package com.joindata.inf.common.util.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.dom4j.DocumentException;

import com.joindata.inf.common.util.algorithm.entity.Tree;
import com.joindata.inf.common.util.algorithm.entity.TreeNode;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 递归生成树结构
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Feb 17, 2017 4:30:54 PM
 */
public class TreeBuilder
{
    private static final Logger log = Logger.get();

    /**
     * 根据给定节点元素生成树结构
     * 
     * @param elements 元素
     * @return 树结构的元素集合，一级元素是根节点
     */
    public static Tree buildTree(Collection<TreeNode> elements)
    {
        Tree root = new Tree();
        Set<TreeNode> resultSet = CollectionUtil.newHashSet();
        root.setChildren(resultSet);

        if(CollectionUtil.isNullOrEmpty(elements))
        {
            return root;
        }

        long beginTime = System.nanoTime();
        for(TreeNode node1: elements)
        {
            boolean mark = false;
            for(TreeNode node2: elements)
            {
                if(null != node2 && node1.getParentCode() != null && node1.getParentCode().equals(node2.getCode()))
                {
                    mark = true;
                    if(null == node2.getChildren())
                    {
                        node2.setChildren(new ArrayList<TreeNode>());
                    }
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if(!mark)
            {
                resultSet.add(node1);
            }
        }

        log.debug("构建树结构 - 执行所耗时间: {}ns", System.nanoTime() - beginTime);

        return root;
    }

    public static void main(String[] args) throws DocumentException
    {
        TreeNode tree1 = new TreeNode();
        tree1.setCode("1");
        tree1.setParentCode(null);

        TreeNode tree11 = new TreeNode();
        tree11.setCode("1.1");
        tree11.setParentCode("1");

        TreeNode tree2 = new TreeNode();
        tree2.setCode("2");
        tree2.setParentCode(null);

        TreeNode tree21 = new TreeNode();
        tree21.setCode("2.1");
        tree21.setParentCode("2");

        TreeNode tree211 = new TreeNode();
        tree211.setCode("2.1.1");
        tree211.setParentCode("2.1");

        TreeNode tree2111 = new TreeNode();
        tree2111.setCode("2.1.1.1");
        tree2111.setParentCode("2.1.1");
        TreeNode tree2112 = new TreeNode();
        tree2112.setCode("2.1.1.2");
        tree2112.setParentCode("2.1.1");

        Tree tree = buildTree(CollectionUtil.newHashSet(tree1, tree11, tree2, tree21, tree211, tree2111, tree2112));

        System.out.println(JsonUtil.toJSON(tree));

        // 测试选取后代
        for(TreeNode node: tree.getPosterity("2"))
        {
            System.out.println(JsonUtil.toJSON(node));
        }

        System.out.println(tree.toXML(true));
        System.out.println(tree.toDom().asXML());
    }
}
