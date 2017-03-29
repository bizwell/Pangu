package com.joindata.inf.common.basic.entities;

import java.io.Serializable;

/**
 * 分页计算结果
 * 
 * @author SongXiang
 * @date 2013-3-20 上午10:06:59
 */
public class PageCalc implements Serializable
{
    private static final long serialVersionUID = -2907448189497614108L;

    /** 记录总数 */
    private int totalCount;

    /** 每页数量 */
    private int pageSize;

    /** 页码总数 */
    private int pageCount;

    protected PageCalc()
    {
    }

    public PageCalc(int totalCount, int pageSize)
    {
        this.totalCount = totalCount;
        this.pageSize = pageSize;

        // 用出发计算出页码数量，如果有零头，则加一页
        this.pageCount = totalCount / pageSize + (totalCount % pageSize > 0 ? 1 : 0);
    }

    /**
     * 根据页码计算记录起始位置
     * 
     * @param page 页码
     * @return 记录起始位置
     */
    public int getStart(int page)
    {
        return (page * this.getPageSize()) - this.getPageSize();
    }

    /**
     * 根据页码计算记录结束位置
     * 
     * @param page 页码
     * @return 记录结束位置
     */
    public int getEnd(int page)
    {
        return page * this.getPageSize();
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public int getPageSize()
    {
        return pageSize;
    }
}
