package com.joindata.inf.common.util.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.joindata.inf.common.basic.entities.Date;
import com.joindata.inf.common.basic.entities.DateTime;
import com.joindata.inf.common.basic.entities.Time;
import com.joindata.inf.common.basic.entities.TimeMillis;
import com.joindata.inf.common.util.basic.support.CustomJsonDateDeserizer;
import com.joindata.inf.common.util.basic.support.CustomJsonDateSerializer;
import com.joindata.inf.common.util.basic.support.CustomJsonDateTimeDeserizer;
import com.joindata.inf.common.util.basic.support.CustomJsonTimeDeserizer;
import com.joindata.inf.common.util.basic.support.CustomJsonTimeMillisDeserizer;

/**
 * JSON 处理相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月4日 下午7:22:13
 */
public class JsonUtil
{
    static
    {
        CustomJsonDateDeserizer dateDeserializer = new CustomJsonDateDeserizer();
        CustomJsonTimeDeserizer timeDeserializer = new CustomJsonTimeDeserizer();
        CustomJsonDateTimeDeserizer dateTimeDeserializer = new CustomJsonDateTimeDeserizer();
        CustomJsonTimeMillisDeserizer timeMillisDeserializer = new CustomJsonTimeMillisDeserizer();

        ParserConfig.getGlobalInstance().putDeserializer(Date.class, dateDeserializer);
        ParserConfig.getGlobalInstance().putDeserializer(Time.class, timeDeserializer);
        ParserConfig.getGlobalInstance().putDeserializer(DateTime.class, dateTimeDeserializer);
        ParserConfig.getGlobalInstance().putDeserializer(TimeMillis.class, timeMillisDeserializer);

        CustomJsonDateSerializer serializer = new CustomJsonDateSerializer();
        SerializeConfig.getGlobalInstance().put(Date.class, serializer);
        SerializeConfig.getGlobalInstance().put(Time.class, serializer);
        SerializeConfig.getGlobalInstance().put(DateTime.class, serializer);
        SerializeConfig.getGlobalInstance().put(TimeMillis.class, serializer);
    }

    /**
     * 把对象转换成 JSON 字符串<br />
     * <i>需要转换的对象属性必须要有配套的 getter 方法</i><br />
     * <i>如果值为 null，将不输出</i>
     * 
     * @param obj 要转换的对象
     * @return JSON 字符串
     */
    public static final String toJSON(Object obj)
    {
        return JSON.toJSONString(obj);
    }

    /**
     * 把对象转换成 JSON 字符串<br />
     * <i>需要转换的对象属性必须要有配套的 getter 方法</i>
     * 
     * @param obj 要转换的对象
     * @param writeNull 如果值为 null，是否输出 null
     * @return JSON 字符串
     */
    public static final String toJSON(Object obj, boolean writeNull)
    {
        if(writeNull)
        {
            return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        }

        return JSON.toJSONString(obj);
    }

    /**
     * 把对象转换成 JSON 字符串，并格式化<br />
     * <i>需要转换的对象属性必须要有配套的 getter 方法</i>
     * 
     * @param obj 要转换的对象
     * @param writeNull 如果值为 null，是否输出 null
     * @return JSON 字符串
     */
    public static final String toPrettyJSON(Object obj, boolean writeNull)
    {
        if(writeNull)
        {
            return JSON.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue);
        }

        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
    }

    /**
     * 把 JSON 字符串转换成 Java 对象<br />
     * <i>将 JSON 中的字段映射到对象的字段中</i>
     * 
     * @param json JSON 字符串
     * @param clz 要转成对象的 Class 反射对象
     * @return 转成后的对象
     */
    public static final <T> T fromJSON(String json, Class<T> clz)
    {
        return JSON.parseObject(json, clz, Feature.OrderedField);
    }

    /**
     * 把 JSON 字符串转成可手动解析的 JSON 对象<br />
     * <i>剩下的就交给你们年轻人了</i>
     * 
     * @param json JSON 字符串
     * @return JSON 对象
     */
    public static final JSONObject toJSONObject(String json)
    {
        return JSON.parseObject(json);
    }

    /**
     * 把 JSON 字符串美观化成人类可读格式
     * 
     * @param json JSON 字符串
     * @return 格式化后的 JSON 字符串
     */
    public static final String getPrettyJSON(String json)
    {
        return JSON.toJSONString(JSON.parse(json), true);
    }

    /**
     * 把 JSON 字符串包裹成 JSONP
     * 
     * @param func JSONP 回调函数名
     * @param json JSON 字符串
     * @return JSONP
     */
    public static final String mkJsonp(String func, String json)
    {
        return func + "(" + json + ")";
    }

    public static void main(String[] args)
    {
        @SuppressWarnings("unused")
        class TestJSON
        {
            String name;

            String gender = "female";

            byte age = 1;

            Date birthDate = new Date();

            DateTime birthDateTime = new DateTime();

            Time birthTime = new Time();

            TimeMillis birthTimeMillis = new TimeMillis();

            boolean inShanghai;

            Map<String, Object> skill = new HashMap<String, Object>();

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public String getGender()
            {
                return gender;
            }

            public void setGender(String gender)
            {
                this.gender = gender;
            }

            public byte getAge()
            {
                return age;
            }

            public void setAge(byte age)
            {
                this.age = age;
            }

            public Date getBirthDate()
            {
                return birthDate;
            }

            public void setBirthDate(Date birthDate)
            {
                this.birthDate = birthDate;
            }

            public DateTime getBirthDateTime()
            {
                return birthDateTime;
            }

            public void setBirthDateTime(DateTime birthDateTime)
            {
                this.birthDateTime = birthDateTime;
            }

            public Time getBirthTime()
            {
                return birthTime;
            }

            public void setBirthTime(Time birthTime)
            {
                this.birthTime = birthTime;
            }

            public boolean isInShanghai()
            {
                return inShanghai;
            }

            public void setInShanghai(boolean inShanghai)
            {
                this.inShanghai = inShanghai;
            }

            public TimeMillis getBirthTimeMillis()
            {
                return birthTimeMillis;
            }

            public void setBirthTimeMillis(TimeMillis birthTimeMillis)
            {
                this.birthTimeMillis = birthTimeMillis;
            }

            public Map<String, Object> getSkill()
            {
                List<String> basicSkill = new ArrayList<String>();
                basicSkill.add("sitdown");
                basicSkill.add("standup");

                List<String> advanceSkill = new ArrayList<String>();
                advanceSkill.add("say: daddy!");
                advanceSkill.add("shake hand");

                Map<String, Object> learnProgress = new HashMap<String, Object>();
                learnProgress.put("calculate", "15%");
                learnProgress.put("gohome", "0%");
                learnProgress.put("silence", 100);

                skill.put("basic", basicSkill);
                skill.put("advance", advanceSkill);
                skill.put("progress", learnProgress);

                return skill;
            }

            public void setSkill(Map<String, Object> skill)
            {
                this.skill = skill;
            }

        }

        String json = toJSON(new TestJSON());
        System.out.println(json);
        {
            TestJSON test = fromJSON(json, TestJSON.class);
            System.out.println(test.getName() + " - " + test.getGender() + " - " + test.getAge() + " - " + test.getBirthDate() + " - " + test.getBirthDateTime() + " - " + test.getSkill());
        }
        System.out.println(toJSONObject("{\"age\":4,\"gender\":\"male\",\"name\":\"duoduo\"}"));
        System.out.println(getPrettyJSON("{\"age\":4,\"gender\":\"male\",\"name\":\"duoduo\"}"));
    }
}