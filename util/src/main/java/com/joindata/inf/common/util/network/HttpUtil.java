package com.joindata.inf.common.util.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.joindata.inf.common.basic.entities.KeyValuePair;
import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.xiaoleilu.hutool.io.IoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 请求工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 下午4:31:10
 */
// TODO 后完善 HTTP 异常处理机制，完善异步、缓存等高级功能
@Slf4j
public class HttpUtil
{
    private static ThreadLocal<RequestConfig> CurrentRequsetConfig = new ThreadLocal<>();

    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(6000).setSocketTimeout(7000).build();

    static
    {
        CurrentRequsetConfig.set(DEFAULT_REQUEST_CONFIG);
    }

    /**
     * 设置<strong>当前线程</strong>中 HttpUtil 发起的所有请求的默认超时时间<br />
     * 
     * @param timeout 按秒计算
     */
    public static final void setTimeout(int reqTimeout, int connTimeout, int socketTimeout)
    {
        reqTimeout = reqTimeout / 1000;
        connTimeout = connTimeout / 1000;
        socketTimeout = socketTimeout / 1000;
        CurrentRequsetConfig.set(RequestConfig.copy(DEFAULT_REQUEST_CONFIG).setConnectionRequestTimeout(reqTimeout).setConnectTimeout(connTimeout).setSocketTimeout(socketTimeout).build());
    }

    /**
     * 发起一个简单的 GET 请求
     * 
     * @param url 要访问的 HTTP 地址
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] get(String url) throws IOException
    {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(CurrentRequsetConfig.get());
        HttpClient httpClient = HttpClients.createMinimal();
        HttpResponse response = httpClient.execute(httpGet);

        log.debug("GET 请求 URL: {}", url);

        HttpEntity entity = response.getEntity();

        byte bytes[] = new byte[(int)entity.getContentLength()];

        entity.getContent().read(bytes);

        EntityUtils.consume(response.getEntity());
        return bytes;
    }

    /**
     * 发起一个简单的 GET 请求，返回的JSON转换成对象
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @param retClz 返回的 JSON 转换成什么对象
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final <T> T getJson(String url, Class<T> retClz, Map<String, Object> params) throws IOException
    {
        String json = StringUtil.toString(get(url, params));
        return JsonUtil.fromJSON(json, retClz);
    }

    /**
     * 发起一个简单的 GET 请求，返回的JSON转换成对象
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @param retClz 返回的 JSON 转换成什么对象
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final <T> T getJson(String url, Class<T> retClz, KeyValuePair... keyValuePair) throws IOException
    {
        Map<String, Object> params = CollectionUtil.newMap();

        for(KeyValuePair kv: keyValuePair)
        {
            params.put(kv.getKey(), kv.getValue());
        }

        String json = StringUtil.toString(get(url, params));

        return JsonUtil.fromJSON(json, retClz);
    }

    /**
     * 发起一个简单的 GET 请求，返回值转成 String
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String getString(String url, KeyValuePair... keyValuePair) throws IOException
    {
        Map<String, Object> params = CollectionUtil.newMap();

        for(KeyValuePair kv: keyValuePair)
        {
            params.put(kv.getKey(), kv.getValue());
        }

        return StringUtil.toString(get(url, params));
    }

    /**
     * 发起一个简单的 GET 请求，返回值转成 String
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String getString(String url, Map<String, Object> params) throws IOException
    {
        return StringUtil.toString(get(url, params));
    }

    /**
     * 发起一个简单的 GET 请求，返回值转成 String
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @param encoding 返回转 String 的编码
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final String getString(String url, String encoding, KeyValuePair... keyValuePair) throws IOException
    {
        Map<String, Object> params = CollectionUtil.newMap();

        for(KeyValuePair kv: keyValuePair)
        {
            params.put(kv.getKey(), kv.getValue());
        }

        return StringUtil.toString(get(url, params), encoding);
    }

    /**
     * 发起一个简单的 GET 请求
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] get(String url, KeyValuePair... keyValuePair) throws IOException
    {
        Map<String, Object> params = CollectionUtil.newMap();

        for(KeyValuePair kv: keyValuePair)
        {
            params.put(kv.getKey(), kv.getValue());
        }

        return get(url, params);
    }

    /**
     * 发起一个简单的 GET 请求
     * 
     * @param url 要访问的 HTTP 地址
     * @param params 请求的参数
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] get(String url, Map<String, Object> params) throws IOException
    {
        String urlFinal = url + "?" + com.xiaoleilu.hutool.http.HttpUtil.toParams(params);

        log.debug("GET 请求 URL: {}", urlFinal);

        HttpGet httpGet = new HttpGet(urlFinal);
        httpGet.setConfig(CurrentRequsetConfig.get());
        HttpClient httpClient = HttpClients.createMinimal();
        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        byte bytes[] = IoUtil.read(entity.getContent()).toByteArray();

        EntityUtils.consume(entity);

        return bytes;
    }

    /**
     * 发起一个简单的 GET 请求，将返回数据写入输出流中
     * 
     * @param url 要访问的 HTTP 地址
     * @param out 要写入数据的输出流
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final void get(String url, OutputStream out) throws IOException
    {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(CurrentRequsetConfig.get());
        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("GET 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        IoUtil.copy(entity.getContent(), out);

        EntityUtils.consume(response.getEntity());
    }

    /**
     * 用 POST 方式发送一条字符串数据给指定 URL
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 要发送的数据
     * @param encoding 发送数据的编码格式
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, String data, String encoding) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        HttpEntity reqEntity = new StringEntity(data, encoding);

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发个文件给指定 URL<br />
     * <i>这里是用模拟 HTML FORM，然后 FORM 中有一个 File 标签，File 标签的 value 就是你传进来的文件，File 标签的 name 就是你传进来的 fieldName</i>
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 要发送的文件
     * @param fieldName 文件字段的字段名
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, File data, String fieldName) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new FileBody(data)).build());
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发个文件和多个值给指定 URL<br />
     * <i>这里是用模拟 HTML FORM，然后 FORM 中有一个 File 标签，File 标签的 value 就是你传进来的文件，File 标签的 name 就是你传进来的 fieldName，params 中的键值对就类似于简单的输入表单</i>
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 要发送的文件
     * @param fieldName 文件字段的字段名
     * @param params 键值对字段
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, File data, String fieldName, KeyValuePair... params) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new FileBody(data)).build());
        if(params != null)
        {
            for(KeyValuePair param: params)
            {
                multipartEntityBuilder.addTextBody(param.getKey(), param.getValue());
            }
        }
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发个文件和多个值给指定 URL<br />
     * <i>这里是用模拟 HTML FORM，然后 FORM 中有一个 File 标签，File 标签的 value 就是你传进来的文件，File 标签的 name 就是你传进来的 fieldName，fileName 是文件名，params 中的键值对就类似于简单的输入表单</i>
     * 
     * @param url 要访问的 HTTP 地址
     * @param fileName 文件名
     * @param in 要发送的文件的输入流
     * @param fieldName 文件字段的字段名
     * @param params 键值对字段
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, String fileName, InputStream in, String fieldName, KeyValuePair... params) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new InputStreamBody(in, fileName)).build());
        if(params != null)
        {
            for(KeyValuePair param: params)
            {
                multipartEntityBuilder.addTextBody(param.getKey(), param.getValue());
            }
        }
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发送JSON字符串值给指定 URL<br />
     * 编码方式是 UTF-8
     * 
     * @param url 要访问的 HTTP 地址
     * @param obj 将被转成 JSON 的对象（要发送的数据）
     * @param retClz 返回对象要转成什么类型
     * @return 返回的数据，会通过 JSON 转换器转成指定 Class
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final <T> T postJson(String url, Object obj, Class<T> retClz) throws IOException
    {
        String ret = postJson(url, obj);
        return JsonUtil.fromJSON(ret, retClz);
    }

    /**
     * 用 POST 方式发送JSON字符串值给指定 URL<br />
     * 编码方式是 UTF-8
     * 
     * @param url 要访问的 HTTP 地址
     * @param obj 将被转成 JSON 的对象（要发送的数据）
     * @return 返回的数据，会转成字符串
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final String postJson(String url, Object obj) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        HttpEntity reqEntity = new StringEntity(JsonUtil.toJSON(obj), "UTF-8");

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return StringUtil.toString(byteOut.toByteArray());
    }

    /**
     * 用 POST 方式发送字符串给指定 URL<br />
     * 编码方式是 UTF-8
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 请求数据
     * @return 返回的数据，会转成字符串
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final String postString(String url, String data) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        HttpEntity reqEntity = new StringEntity(data, "UTF-8");

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return StringUtil.toString(byteOut.toByteArray());
    }

    /**
     * 用 POST 方式发送多个key-value值给指定 URL<br />
     * 
     * @param url 要访问的 HTTP 地址
     * @param keyValuePair 键值对，可指定多个
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, KeyValuePair... keyValuePair) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        Set<NameValuePair> params = CollectionUtil.newHashSet();
        if(keyValuePair != null)
        {
            for(KeyValuePair param: keyValuePair)
            {
                params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }

        HttpEntity reqEntity = new UrlEncodedFormEntity(params);

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发送多个键值对给指定 URL<br />
     * <i>这个方法会将 bean 中到的 field 解析为 key-value</i>
     * 
     * @param url 要访问的 HTTP 地址
     * @param bean 对象
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, Serializable bean) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        Set<NameValuePair> params = CollectionUtil.newHashSet();
        if(bean != null)
        {
            Map<String, Object> map = BeanUtil.getFieldValues(bean);
            Iterator<String> iter = CollectionUtil.iteratorMapKey(map);
            while(iter.hasNext())
            {
                String key = iter.next();
                if(map.get(key) == null)
                {
                    continue;
                }
                params.add(new BasicNameValuePair(key, map.get(key).toString()));
            }
        }

        HttpEntity reqEntity = new UrlEncodedFormEntity(params, Charset.forName("UTF-8"));

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 POST 方式发送自定义数据<br />
     * <i>这里的发送内容需要自定义，请参阅 apache-httpcomponents-client</i>
     * 
     * @see HttpEntity Http 实体相关文档
     * @param url 要访问的 HTTP 地址
     * @param entity 自定义 HTTP 实体数据
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] post(String url, HttpEntity entity) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        httpPost.setEntity(entity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("POST 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 PUT 方式发送一条字符串数据给指定 URL
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 要发送的数据
     * @param encoding 发送数据的编码格式
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] put(String url, String data, String encoding) throws IOException
    {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(CurrentRequsetConfig.get());

        HttpEntity reqEntity = new StringEntity(data, encoding);

        httpPut.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("PUT 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPut);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 PUT 方式发个文件给指定 URL<br />
     * <i>这里是用模拟 HTML FORM，然后 FORM 中有一个 File 标签，File 标签的 value 就是你传进来的文件，File 标签的 name 就是你传进来的 fieldName</i>
     * 
     * @param url 要访问的 HTTP 地址
     * @param data 要发送的文件
     * @param fieldName 文件字段的字段名
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] put(String url, File data, String fieldName) throws IOException
    {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(CurrentRequsetConfig.get());

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new FileBody(data)).build());
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPut.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("PUT 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPut);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 用 PUT 方式发送自定义数据<br />
     * <i>这里的发送内容需要自定义，请参阅 apache-httpcomponents-client</i>
     * 
     * @see HttpEntity Http 实体相关文档
     * @param url 要访问的 HTTP 地址
     * @param entity 自定义 HTTP 实体数据
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] put(String url, HttpEntity entity) throws IOException
    {
        HttpPut httpPost = new HttpPut(url);
        httpPost.setConfig(CurrentRequsetConfig.get());

        httpPost.setEntity(entity);

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("PUT 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }

        return byteOut.toByteArray();
    }

    /**
     * 发起一个简单的 DELETE 请求
     * 
     * @param url 要访问的 HTTP 地址
     * @return 返回的数据
     * @throws IOException 发生任何 IO 错误，抛出该异常
     * @throws ResourceException 如果返回码不在 2xx 范围内，抛出该异常
     */
    public static final byte[] delete(String url) throws IOException
    {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(CurrentRequsetConfig.get());

        HttpClient httpClient = HttpClients.createMinimal();

        log.debug("DELETE 请求 URL: {}", url);

        HttpResponse response = httpClient.execute(httpDelete);

        HttpEntity resEntity = response.getEntity();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        IoUtil.copy(resEntity.getContent(), byteOut);

        EntityUtils.consume(response.getEntity());

        if(response.getStatusLine().getStatusCode() < 200 && response.getStatusLine().getStatusCode() >= 300)
        {
            throw new ResourceException(ResourceErrors.UNRESOLVABLE, StringUtil.toString(byteOut.toByteArray()));
        }
        return byteOut.toByteArray();
    }

    public static void main(String[] args) throws ClientProtocolException, IOException, UnsupportedCharsetException, ResourceException
    {

        System.out.println(new String(get("http://localhost:8088/get"), "UTF-8"));
        System.out.println(new String(post("http://localhost:8088/post", "测试一下 Post，I'm fine, thank you!", "UTF-8"), "UTF-8"));
        System.out.println(new String(post("http://localhost:8088/postfile", new File("/temp/test/size.txt"), "file"), "UTF-8"));
        System.out.println(new String(post("http://localhost:8088/post", new StringEntity("自定义数据", "UTF-8")), "UTF-8"));
        System.out.println(new String(post("http://192.168.1.138:8888", KeyValuePair.define("A", "1"), KeyValuePair.define("B", "2"))));
        System.out.println(new String(put("http://localhost:8088/put", "测试一下 Put，I'm fine, thank you!", "UTF-8"), "UTF-8"));
        System.out.println(new String(put("http://localhost:8088/putfile", new File("/temp/test/size.txt"), "file"), "UTF-8"));
        System.out.println(new String(put("http://localhost:8088/put", new StringEntity("自定义数据", "UTF-8")), "UTF-8"));
        System.out.println(new String(delete("http://localhost:8088/delete"), "UTF-8"));
    }

}
