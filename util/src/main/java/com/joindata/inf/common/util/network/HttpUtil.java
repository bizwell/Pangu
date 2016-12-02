package com.joindata.inf.common.util.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.joindata.inf.common.util.basic.StringUtil;
import com.xiaoleilu.hutool.io.IoUtil;

/**
 * HTTP 请求工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 下午4:31:10
 */
// TODO 后完善 HTTP 异常处理机制，完善异步、缓存等高级功能
public class HttpUtil
{
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
        HttpClient httpClient = HttpClients.createMinimal();
        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        byte bytes[] = new byte[(int)entity.getContentLength()];

        entity.getContent().read(bytes);

        EntityUtils.consume(response.getEntity());
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
        HttpClient httpClient = HttpClients.createMinimal();
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

        HttpEntity reqEntity = new StringEntity(data, encoding);

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

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

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new FileBody(data)).build());
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPost.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

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
    public static final byte[] post(String url, File data, String fieldName, Set<KeyValuePair> params) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);

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
    public static final byte[] post(String url, String fileName, InputStream in, String fieldName, Set<KeyValuePair> params) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);

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
    public static final byte[] post(String url, Object bean) throws IOException
    {
        HttpPost httpPost = new HttpPost(url);

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

        httpPost.setEntity(entity);

        HttpClient httpClient = HttpClients.createMinimal();

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

        HttpEntity reqEntity = new StringEntity(data, encoding);

        httpPut.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

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

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart(FormBodyPartBuilder.create(fieldName, new FileBody(data)).build());
        HttpEntity reqEntity = multipartEntityBuilder.build();

        httpPut.setEntity(reqEntity);

        HttpClient httpClient = HttpClients.createMinimal();

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

        httpPost.setEntity(entity);

        HttpClient httpClient = HttpClients.createMinimal();

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
        HttpClient httpClient = HttpClients.createMinimal();
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
