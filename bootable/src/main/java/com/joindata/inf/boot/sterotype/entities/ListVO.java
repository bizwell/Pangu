package com.joindata.inf.boot.sterotype.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.joindata.inf.common.basic.entities.PageCalc;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 列表 VO 模板
 * 
 * @author 宋翔
 * @date 2015年11月17日 下午3:20:16
 * @param <E>
 */
public class ListVO<E> implements Serializable, Iterable<E>
{
    private static final long serialVersionUID = -4457395163935952182L;

    /** 真正的 List */
    protected List<E> list;

    /** 分页属性对象 */
    protected PageCalc pageData;

    /** 额外数据 Map */
    protected Map<String, Object> extras;

    public ListVO()
    {
        this.list = new ArrayList<E>();
    }

    public ListVO(List<E> list, PageCalc pageData)
    {
        this.list = list;
        this.pageData = pageData;
    }

    public ListVO(List<E> list)
    {
        this.list = list;
    }

    public List<E> getList()
    {
        return list;
    }

    public void setList(List<E> list)
    {
        this.list = list;
    }

    public PageCalc getPageData()
    {
        return pageData;
    }

    public void setPageData(PageCalc pageData)
    {
        this.pageData = pageData;
    }

    public void addExtra(String key, Object value)
    {
        if(extras == null)
        {
            extras = CollectionUtil.newMap();
        }

        extras.put(key, value);
    }

    public Map<String, Object> getExtras()
    {
        return extras;
    }

    public int size()
    {
        if(list == null)
        {
            return 0;
        }
        return list.size();
    }

    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    public boolean contains(Object o)
    {
        if(list == null)
        {
            return false;
        }
        return list.contains(o);
    }

    public Iterator<E> iterator()
    {
        if(list == null)
        {
            return null;
        }

        return list.iterator();
    }

    public E[] toArray()
    {
        return CollectionUtil.toArray(list);
    }

    public boolean add(E e)
    {
        if(list == null)
        {
            return false;
        }

        return list.add(e);
    }

    public boolean remove(Object o)
    {

        if(list == null)
        {
            return false;
        }
        return list.remove(o);
    }

    public boolean containsAll(Collection<?> c)
    {
        if(list == null)
        {
            return false;
        }
        return list.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c)
    {
        if(list == null)
        {
            return false;
        }

        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c)
    {
        if(list == null)
        {
            return false;
        }
        return list.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c)
    {
        if(list == null)
        {
            return false;
        }
        return list.removeAll(c);
    }

    public boolean retainAll(Collection<?> c)
    {
        if(list == null)
        {
            return false;
        }
        return list.retainAll(c);
    }

    public boolean containsAll(ListVO<?> c)
    {
        if(list == null || c.getList() == null)
        {
            return false;
        }
        return list.containsAll(c.getList());
    }

    public boolean addAll(ListVO<? extends E> c)
    {
        if(list == null || c.getList() == null)
        {
            return false;
        }

        return list.addAll(c.getList());
    }

    public boolean addAll(int index, ListVO<? extends E> c)
    {
        if(list == null || c.getList() == null)
        {
            return false;
        }
        return list.addAll(index, c.getList());
    }

    public boolean removeAll(ListVO<?> c)
    {
        if(list == null || c.getList() == null)
        {
            return false;
        }
        return list.removeAll(c.getList());
    }

    public boolean retainAll(ListVO<?> c)
    {
        if(list == null || c.getList() == null)
        {
            return false;
        }

        return list.retainAll(c.getList());
    }

    public void clear()
    {
        if(list == null)
        {
            return;
        }
        list.clear();
    }

    public E get(int index)
    {
        if(list == null)
        {
            return null;
        }

        return list.get(index);
    }

    public E set(int index, E element)
    {
        if(list == null)
        {
            return null;
        }

        return list.set(index, element);
    }

    public void add(int index, E element)
    {
        if(list == null)
        {
            return;
        }

        list.add(index, element);
    }

    public E remove(int index)
    {
        if(list == null)
        {
            return null;
        }

        return list.remove(index);
    }

    public int indexOf(Object o)
    {
        if(list == null)
        {
            return -1;
        }

        return list.indexOf(o);
    }

    public int lastIndexOf(Object o)
    {
        if(list == null)
        {
            return -1;
        }

        return list.lastIndexOf(o);
    }

    public ListIterator<E> listIterator()
    {
        if(list == null)
        {
            return null;
        }
        return list.listIterator();
    }

    public ListIterator<E> listIterator(int index)
    {
        if(list == null)
        {
            return null;
        }
        return list.listIterator();
    }

    public List<E> subList(int fromIndex, int toIndex)
    {
        if(list == null)
        {
            return null;
        }

        return list.subList(fromIndex, toIndex);
    }
}
