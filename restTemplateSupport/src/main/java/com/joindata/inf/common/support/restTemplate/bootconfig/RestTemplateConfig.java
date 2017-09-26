package com.joindata.inf.common.support.restTemplate.bootconfig;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.joindata.inf.common.support.restTemplate.properties.RestTemplateProperties;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 *
 */
@Configuration
@Slf4j
public class RestTemplateConfig {


    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(RestTemplateProperties restTemplateProperties) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(restTemplateProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getDefaultMaxPerRoute());
        Set<RouterConfig> routerConfigs = RouterConfig.parse(restTemplateProperties.getRouters());
        if (null != routerConfigs) {
            for (RouterConfig routerConfig : routerConfigs) {
                connectionManager.setMaxPerRoute(routerConfig.getHttpRoute(), routerConfig.getMax());
            }
        }
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(restTemplateProperties.getRetryCount(), true));
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(restTemplateProperties.getConnectTimeout());
        requestFactory.setReadTimeout(restTemplateProperties.getReadTimeout());
        requestFactory.setHttpClient(httpClientBuilder.build());

        return requestFactory;
    }

    /**
     * http 同步请求
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory) {
        List<HttpMessageConverter<?>> messageConverters = Arrays.asList(
                new StringHttpMessageConverter(),
                new FormHttpMessageConverter(),
                new MarshallingHttpMessageConverter(),
                new FastJsonHttpMessageConverter()// default Charset: UTF-8
        );
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setInterceptors(Arrays.asList(RestTemplateConfig::intercept));

        return restTemplate;
    }

    private static ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long startTime = System.currentTimeMillis();
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        request.getHeaders().set("taskId", s);
        ClientHttpResponse response = new RestTemplateResponseWrapper(execution.execute(request, body));

        log.info("调用第三方接口 url: {} \n  method: {} \n " + "headers: {}  \n body:{} \n", request.getURI(), request.getMethod(),
                request.getHeaders(), new String(body, "UTF-8"));
        String responseBody = StreamUtils.copyToString(response.getBody(), Charset.forName("UTF-8"));
        log.info("第三方接口返回 statusCode:{} \n body:{} \n  header:{} \n 耗时：{}ms", response.getStatusCode(), responseBody, response.getHeaders(), System.currentTimeMillis() - startTime);

        return response;
    }

    /**
     * http 异步请求
     */
    @Bean
    public AsyncRestTemplate asyncRestTemplate(RestTemplate restTemplate, RestTemplateProperties restTemplateProperties) throws IOException {
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connectionManager.setMaxTotal(restTemplateProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getDefaultMaxPerRoute());

        Set<RouterConfig> routerConfigs = RouterConfig.parse(restTemplateProperties.getRouters());
        if (null != routerConfigs) {
            for (RouterConfig routerConfig : routerConfigs) {
                connectionManager.setMaxPerRoute(routerConfig.getHttpRoute(), routerConfig.getMax());
            }
        }

        HttpAsyncClientBuilder clientBuilder = HttpAsyncClientBuilder.create()
                .setConnectionManager(connectionManager);

        HttpComponentsAsyncClientHttpRequestFactory requestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(restTemplateProperties.getConnectTimeout());
        requestFactory.setReadTimeout(restTemplateProperties.getReadTimeout());
        requestFactory.setHttpAsyncClient(clientBuilder.build());

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(requestFactory, restTemplate);
        asyncRestTemplate.setInterceptors(Arrays.asList(RestTemplateConfig::asyncIntercept));

        return asyncRestTemplate;
    }

    private static ListenableFuture<ClientHttpResponse> asyncIntercept(HttpRequest request, byte[] body, AsyncClientHttpRequestExecution
            execution) throws IOException {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        request.getHeaders().set("taskId", s);
        ListenableFuture<ClientHttpResponse> future = execution.executeAsync(request, body);

        log.info("调用第三方接口 url: {} \n  method: {} \n " + "headers: {}  \n body:{} \n", request.getURI(), request.getMethod(),
                request.getHeaders(), new String(body, "UTF-8"));

        return future;
    }


    /**
     * 将Response的Stream存到buffer中，以便后面::getBody再次读取。
     */
    private static class RestTemplateResponseWrapper implements ClientHttpResponse {

        private ClientHttpResponse clientHttpResponse;

        private byte[] buffer;


        public RestTemplateResponseWrapper(ClientHttpResponse clientHttpResponse) throws IOException {
            this.clientHttpResponse = clientHttpResponse;
            this.buffer = StreamUtils.copyToByteArray(clientHttpResponse.getBody());
        }


        @Override
        public HttpStatus getStatusCode() throws IOException {
            return clientHttpResponse.getStatusCode();
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return clientHttpResponse.getRawStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return clientHttpResponse.getStatusText();
        }

        @Override
        public void close() {
            clientHttpResponse.close();
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(buffer);
        }

        @Override
        public HttpHeaders getHeaders() {
            return clientHttpResponse.getHeaders();
        }
    }

    @EqualsAndHashCode
    public static class RouterConfig {

        private RouterConfig() {

        }

        private RouterConfig(String schema, String host, Integer port, Integer max) {
            this.max = max;
            this.httpRoute = new HttpRoute(new HttpHost(host, port, schema));
        }

        private HttpRoute httpRoute;

        public HttpRoute getHttpRoute() {
            return httpRoute;
        }

        public Integer getMax() {
            return max;
        }

        private Integer max;

        public static Set<RouterConfig> parse(String config) {
            if (StringUtils.isEmpty(config)) {
                return null;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(config, ";");
            Set<RouterConfig> routerConfigs = new HashSet<>();
            while (stringTokenizer.hasMoreTokens()) {
                String temp = stringTokenizer.nextToken();
                String[] data = temp.split(":");
                if (data.length < 4) {
                    log.error("解析restTemplate配置文件失败 {}", config);
                    return null;
                }
                String schema = data[0];
                String host = data[1];
                Integer port = null;
                Integer max = null;
                try {
                    port = Integer.valueOf(data[2]);
                    max = Integer.valueOf(data[3]);
                } catch (Exception ex) {
                    log.error("解析restTemplate配置文件失败 {}", config);
                    return null;
                }
                if (!"http".equals(schema) || !"https".equals(schema)) {
                    log.error("解析restTemplate配置文件失败 {}", config);
                    return null;
                }
                routerConfigs.add(new RouterConfig(schema, host, port, max));
            }
            return routerConfigs;
        }
    }
}

