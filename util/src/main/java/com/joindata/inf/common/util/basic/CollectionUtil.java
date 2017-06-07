package com.joindata.inf.common.util.basic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.joindata.inf.common.basic.entities.KeyValuePair;

/**
 * 集合操作简化工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午5:20:44
 */
public class CollectionUtil
{
    /**
     * 创建新的 HashSet
     * 
     * @param t 初始化对象列表
     * @return 创建后的 HashSet
     */
    @SafeVarargs
    public static final <T> Set<T> newHashSet(T... t)
    {
        Set<T> set = new HashSet<T>();

        if(t == null)
        {
            return set;
        }

        for(T item: t)
        {
            set.add(item);
        }

        return set;
    }

    /**
     * 获取 Map 的 key 集合的迭代器
     * 
     * @param map 要遍历的 Map
     * @return Map 的 Key 的迭代器
     */
    public static final <K, V> Iterator<K> iteratorMapKey(Map<K, V> map)
    {
        if(map == null)
        {
            return null;
        }

        return map.keySet().iterator();
    }

    /**
     * 将元素添加到 List 中
     * 
     * @param list 要添加元素的 List
     * @param item 被添加的元素，可以指定多个
     * @return 添加元素后的 List
     */
    @SuppressWarnings("unchecked")
    public static final <E> List<E> addToList(List<E> list, E... item)
    {
        if(list == null || item == null)
        {
            return null;
        }

        for(E e: item)
        {
            list.add(e);
        }

        return list;
    }

    /**
     * 将 List 元素排序<br />
     * <i>与 Collections.sort(List<E>)不同的是，会执行后返回 List</i>
     * 
     * @param list 要排序的 List
     * @return 排序后的 List
     */
    public static final <E extends Comparable<E>> List<E> sort(List<E> list)
    {
        Collections.sort(list);
        return list;
    }

    /**
     * 创建一个空 Map
     * 
     * @return 一个空的 HashMap
     */
    public static final <K, V> Map<K, V> newMap()
    {
        return new HashMap<K, V>();
    }

    /**
     * 创建一个空 TreeMap
     * 
     * @return 一个空的 TreeMap
     */
    public static final <K, V> Map<K, V> newTreeMap()
    {
        return new TreeMap<>();
    }

    /**
     * 创建一个 Map，并指定两个数组来初始化 Map 内容<br />
     * <i>Key 数组的数量如果小于值数组，Map 将可能只有 Key 数组数量那么多的元素</i><br />
     * <i>Key 数组的数量如果大于值数组，对不上号的值元素将设为 null</i><br />
     * 
     * @param <K>
     * 
     * @param ks Map 的 Key 数组
     * @param vs Map 的值数组
     * 
     * @return 一个 HashMap
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final <K, V> Map<K, V> newMap(K ks[], V vs[])
    {
        Map<K, V> map = new HashMap();
        if(ks != null)
        {
            for(int i = 0; i < ks.length; i++)
            {
                if(vs != null && vs.length > i)
                {
                    map.put(ks[i], vs[i]);
                }
                else
                {
                    map.put(ks[i], null);
                }
            }
        }

        return map;
    }

    /**
     * 创建一个 Map<String, String>，并使用 KeyValuePair 列表来初始化
     * 
     * @return 一个 Map<String, String>
     */
    public static final Map<String, String> newMap(KeyValuePair... items)
    {
        Map<String, String> map = CollectionUtil.newMap();
        if(items == null)
        {
            return map;
        }

        for(KeyValuePair kv: items)
        {
            map.put(kv.getKey(), kv.getValue());
        }

        return map;
    }

    /**
     * 定义一个只有一个元素的 Map
     * 
     * @param key Key
     * @param value 值
     * @return Map
     */
    public static <T> Map<String, T> newMap(String key, T value)
    {
        Map<String, T> map = new HashMap<String, T>(1);
        map.put(key, value);

        return map;
    }

    /**
     * 定义一个只有一个元素的 Map
     * 
     * @param key Key
     * @param value 值
     * @return Map
     */
    public static <K, V> Map<K, V> newMap(K key, V value)
    {
        Map<K, V> map = new HashMap<K, V>(1);
        map.put(key, value);

        return map;
    }

    /**
     * 创建一个新的 List，可填入初始化元素
     * 
     * @param es 初始化元素，可以为多个，也可以没有，接受数组，但类型必须相同
     * @return 一个 ArrayList
     */
    @SafeVarargs
    public static final <E> List<E> newList(E... es)
    {
        List<E> list = new ArrayList<E>();
        if(es != null)
        {
            for(E e: es)
            {
                list.add(e);
            }
        }

        return list;
    }

    /**
     * 判断是否为空或者 null
     * 
     * @param collection 集合对象
     * @return true，如果为空或者 null
     */
    public static boolean isNullOrEmpty(Collection<?> collection)
    {
        if(collection == null || collection.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为空或者 null
     * 
     * @param map Map 对象
     * @return true，如果为空或者 null
     */
    public static boolean isNullOrEmpty(Map<?, ?> map)
    {
        if(map == null || map.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * 把传进来的 Collection 转成数组
     * 
     * @param collection 各种形式的 Collection
     * @return 数组，如果 Collection 为空或为 null，都将返回 null
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T[] toArray(Collection<T> collection)
    {
        if(collection == null)
        {
            return null;
        }

        while(collection.contains(null))
        {
            collection.remove(null);
        }

        if(collection.size() == 0)
        {
            return null;
        }

        T array[] = (T[])Array.newInstance(collection.iterator().next().getClass(), collection.size());

        int i = 0;
        for(T t: collection)
        {
            array[i] = t;
            i++;
        }

        return array;
    }

    /**
     * 反转列表
     * 
     * @param list 列表
     */
    public static void reverse(List<?> list)
    {
        Collections.reverse(list);
    }

    /**
     * 创建一个 EnumSet
     * 
     * @param Enum 内容
     * @return EnumSet，如果 es 为 null 或空，返回 null
     */
    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> newEnumSet(E... es)
    {
        if(ArrayUtil.isEmpty(es))
        {
            return null;
        }

        if(es.length == 1)
        {
            return EnumSet.of(es[0]);
        }

        List<E> elist = new ArrayList<>();
        for(int i = 1; i < es.length; i++)
        {
            elist.add(es[i]);
        }

        return EnumSet.of(es[0], CollectionUtil.toArray(elist));
    }

    /**
     * 判断两者是否有交集
     * 
     * @param collection1 集合1
     * @param collection2 集合2
     * @return true，如果有交集。如果两者任何一个为 null，返回 false
     */
    public static boolean hasIntersection(Collection<?> collection1, Collection<?> collection2)
    {
        if(collection1 == null || collection2 == null)
        {
            return false;
        }

        if(collection1.size() < collection2.size())
        {
            for(Object obj: collection1)
            {
                if(collection2.contains(obj))
                {
                    return true;
                }
            }
        }
        else
        {
            for(Object obj: collection2)
            {
                if(collection1.contains(obj))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 将集合的每个元素强转成指定的元素的集合
     * 
     * @param collection 原 Collection
     * @return 强转后的 Collection
     */
    @SuppressWarnings("unchecked")
    public static <Source, Target, SourceCollection extends Collection<Source>, TargetCollection extends Collection<Target>> TargetCollection cast(SourceCollection collection)
    {
        if(collection == null)
        {
            return null;
        }
        TargetCollection ret = (TargetCollection)ClassUtil.newInstance(collection.getClass());
        collection.forEach(item ->
        {
            ret.add((Target)item);
        });

        return ret;
    }

    /**
     * 集合中是否包含指定元素
     * 
     * @param collection 集合
     * @param version 要判断的元素
     * @return true, 如果包含
     */
    public static boolean contains(Collection<String> collection, String item)
    {
        if(collection == null)
        {
            return false;
        }

        return collection.contains(item);
    }

    public static void main(String[] args)
    {
        System.out.println(newHashSet("A", "B", "C"));

        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("a", 1);
            map.put("b", "二");
            map.put("c", "叁");
            Iterator<String> iter = iteratorMapKey(map);
            while(iter.hasNext())
            {
                System.out.println(iter.next());
            }
        }
        {
            List<String> list = new ArrayList<>();
            list.add("2");
            list.add("3");
            list.add("1");
            System.out.println(sort(list));
        }

        System.out.println(newMap());
        System.out.println(newMap(new String[]{"a", "b", "c"}, new String[]{"1", "2", "3"}));
        System.out.println(newList());
        System.out.println(newList("1", "2", "3", "4"));

        System.out.println(isNullOrEmpty(newList()));

        System.out.println(JsonUtil.toJSON(toArray(newList("a", "b", "c"))));

        {

            Object a = "A";
            Object b = "B";
            Object c = "1";

            Set<String> set = cast(CollectionUtil.newHashSet(a, b, c));
            System.out.println(set);
        }

    }

}
