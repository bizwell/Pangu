package com.joindata.inf.common.util.basic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;;

/**
 * Bean 对象处理工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月3日 下午5:49:34
 */
public class BeanUtil
{
    /**
     * 将一个对象的属性值按名称复制到另一个对象中去<br />
     * <i>如果被复制的对象为 null，返回 null</i>
     * 
     * @param src 被复制的对象
     * @param dest 需要复制属性的对象
     * @return 需要复制属性的对象
     */
    public static final <T> T copyProperties(Object src, T dest)
    {
        if(src == null)
        {
            return null;
        }

        Map<String, Object> srcValues = BeanUtil.getFieldValues(src);
        Field destFlds[] = ClassUtil.getFields(dest);

        for(Field fld: destFlds)
        {
            if(srcValues.containsKey(fld.getName()))
            {
                try
                {
                    if(StringUtil.isEquals(fld.getName(), "serialVersionUID"))
                    {
                        continue;
                    }

                    if(Modifier.isFinal(fld.getModifiers()))
                    {
                        continue;
                    }

                    fld.set(dest, srcValues.get(fld.getName()));
                }
                catch(IllegalArgumentException | IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return dest;
    }

    /**
     * 创建一个新对象，并将一个对象的属性值按名称复制到新对象<br />
     * <i>如果被复制的对象为 null，返回 null</i>
     * 
     * @param src 被复制的对象
     * @param destClz 需要复制属性的对象 Class
     * @return 需要复制属性的对象
     * @throws RuntimeException 创建新对象时发生错误，抛出该异常
     */
    public static final <T> T copyProperties(Object src, Class<T> destClz)
    {
        if(src == null)
        {
            return null;
        }

        T dest;
        try
        {
            dest = destClz.newInstance();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        Map<String, Object> srcValues = BeanUtil.getFieldValues(src);
        Field destFlds[] = ClassUtil.getFields(dest);

        for(Field fld: destFlds)
        {
            if(srcValues.containsKey(fld.getName()))
            {
                try
                {
                    if(StringUtil.isEquals(fld.getName(), "serialVersionUID"))
                    {
                        continue;
                    }

                    fld.set(dest, srcValues.get(fld.getName()));
                }
                catch(IllegalArgumentException | IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return dest;
    }

    /**
     * 将对象中所有属性的值取出来放到 Map 中
     * 
     * @param obj 要取属性值的对象
     * @return 字段名-字段值的 Map
     */
    public static final Map<String, Object> getFieldValues(Object obj)
    {
        if(obj == null)
        {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        Field flds[] = ClassUtil.getFields(obj);
        for(Field fld: flds)
        {
            try
            {
                map.put(fld.getName(), fld.get(obj));
            }
            catch(IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 序列化对象
     * 
     * @param obj 要序列化的对象
     * @param out 输出流
     */
    public static final void serializeObject(Serializable obj, OutputStream out)
    {
        SerializationUtils.serialize(obj, out);
    }

    /**
     * 反序列化对象
     * 
     * @param in 输入流
     * @param clz 还原对象的 Class
     * @return 还原后的对象
     */
    public static final <T extends Serializable> T deserializeObject(InputStream in, Class<T> clz)
    {
        return SerializationUtils.deserialize(in);
    }

    /**
     * 序列化对象
     * 
     * @param obj 要序列化的对象
     * @return 字节数组
     * @throws IOException
     */
    public static final byte[] serializeObject(Serializable obj)
    {
        return SerializationUtils.serialize(obj);
    }

    /**
     * 反序列化对象
     * 
     * @param bytes 对象序列化后的字节数组
     * @return 还原后的对象
     */
    public static final <T extends Serializable> T deserializeObject(byte[] bytes)
    {
        return SerializationUtils.deserialize(bytes);
    }

    /**
     * 反序列化对象
     * 
     * @param bytes 对象序列化后的字节数组
     * @param clz 还原对象的 Class <i>!!!!!!纳闷，为什么这个可以不传</i>
     * @return 还原后的对象
     */
    public static final <T extends Serializable> T deserializeObject(byte[] bytes, Class<T> clz)
    {
        return SerializationUtils.deserialize(bytes);
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println(copyProperties(new String("abcdefg"), new String("12345")));
        System.out.println(copyProperties(new String("12345"), new String("abcdefg")));
        System.out.println(getFieldValues(new String("abcdefg")));

        {
            serializeObject("woshinibabadeyeye", new FileOutputStream("/temp/123.bin"));
            System.out.println(deserializeObject(new FileInputStream("/temp/123.bin"), String.class));
        }

    }

}