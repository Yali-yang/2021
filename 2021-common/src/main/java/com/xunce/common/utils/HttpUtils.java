package com.xunce.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpUtil
 * @Description TODO
 * @Author MeiZhongHao
 * @Date 2021/10/11 15:30
 * @Version 1.0
 **/
@Slf4j
public class HttpUtils {

    private static final String utf8 = "UTF-8";

    private static final int SUCCESS = 200;

    private static final int CREATESUCCESS = 201;


    /**
     * 发送get无参请求
     *
     * @param url 请求url
     * @return
     */
    public static JSONObject doGet(String url) {
        return doGet(url, null);
    }

    public static JSONObject doGet(String url, Map<String, String> param) {
        JSONObject result = null;
        String response = doGet(url, param, null, utf8);
        if (!StringUtils.isEmpty(response)) {
            result = JSON.parseObject(response);
        }
        return result;
    }

    public static JSONObject doGet(String url, Map<String, String> param, Map<String, String> header) {
        JSONObject result = null;
        String response = doGet(url, param, header, utf8);
        if (!StringUtils.isEmpty(response)) {
            result = JSON.parseObject(response);
        }
        return result;
    }


    /**
     * 发送get有参请求
     *
     * @param url   请求url
     * @param param 请求参数
     * @return
     */
    public static String doGet(String url, Map<String, String> param, Map<String, String> header, String charset) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }

            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == SUCCESS || response.getStatusLine().getStatusCode() == CREATESUCCESS) {
                //响应实体JSON处理
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            } else {
                log.info("http get请求失败->code：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 发送get有参请求
     *
     * @param url   请求url
     * @param param 请求参数
     * @return
     */
    public static String doGetForDownload(String url, Map<String, String> param, Map<String, String> header, String charset) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }

            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == SUCCESS || response.getStatusLine().getStatusCode() == CREATESUCCESS) {
                //响应实体JSON处理
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            } else {
                log.info("http get请求失败->code：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送post无参请求
     *
     * @param url
     * @return
     */
    public static JSONObject doPost(String url) {
        return doPost(url, null);
    }

    /**
     * 发送post有参请求
     *
     * @param url
     * @param param
     * @return
     */
    public static JSONObject doPost(String url, Map<String, Object> param) {
        JSONObject result = null;
        String response = doPost(url, param, null, utf8);
        if (!StringUtils.isEmpty(response)) {
            result = JSON.parseObject(response);
        }
        return result;
    }

    /**
     * 发送post有参请求
     *
     * @param url
     * @param param
     * @return
     */
    public static JSONObject doPost(String url, Map<String, Object> param, Map<String, String> header) {
        JSONObject result = null;
        String response = doPost(url, param, header, utf8);
        if (!StringUtils.isEmpty(response)) {
            result = JSON.parseObject(response);
        }
        return result;
    }

    /**
     * 发送post有参请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPost(String url, Map<String, Object> param, Map<String, String> header, String charset) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            // 创建参数列表
            if (param != null) {
                String entity = JSON.toJSONString(param);
                httpPost.setEntity(new StringEntity(entity, "utf-8"));
            }
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key) + "");
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == SUCCESS || response.getStatusLine().getStatusCode() == CREATESUCCESS) {
                //响应实体JSON处理
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, charset);
            } else {
                log.info("http post请求失败->code：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * FileMultiPart POST请求
     *
     * @param url
     * @param header
     * @param reqParam
     * @return
     */
    public static String postFileMultiPart(String url, Map<String, String> header, Map<String, String> reqParam) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpPost httpPost = new HttpPost(url);

            //setConnectTimeout：设置连接超时时间，单位毫秒。setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
            RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
            httpPost.setConfig(defaultRequestConfig);

            System.out.println("executing request " + httpPost.getURI());

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            for (Map.Entry<String, String> param : reqParam.entrySet()) {
                multipartEntityBuilder.addTextBody(param.getKey(), param.getValue(), ContentType.create("text/plain", Consts.UTF_8));
            }
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            multipartEntityBuilder.setContentType(ContentType.create("multipart/form-data", Consts.UTF_8));
            HttpEntity reqEntity = multipartEntityBuilder.build();
            httpPost.setEntity(reqEntity);

            // 执行post请求.
            CloseableHttpResponse response = httpclient.execute(httpPost);
            System.out.println("got response");

            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发送get有参请求
     *
     * @param url   请求url
     * @param param 请求参数
     * @return
     */
    public static String doDelete(String url, Map<String, String> header, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }

            URI uri = builder.build();
            // 创建http GET请求
            HttpDelete httpdelete = new HttpDelete(uri);
            if (header != null) {
                for (String key : header.keySet()) {
                    httpdelete.setHeader(key, header.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpdelete);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == SUCCESS || response.getStatusLine().getStatusCode() == CREATESUCCESS) {
                //响应实体JSON处理
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf8");
            } else {
                log.info("http get请求失败->code：{}", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * PUT请求
     *
     * @param url
     * @param header
     * @param reqParam
     * @return
     */
    public static String doPut(String url, Map<String, String> header, Map<String, String> reqParam) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpPut httpPut = new HttpPut(url);

            //setConnectTimeout：设置连接超时时间，单位毫秒。setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
            RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(15000).build();
            httpPut.setConfig(defaultRequestConfig);

            if (header != null) {
                for (String key : header.keySet()) {
                    httpPut.setHeader(key, header.get(key));
                }
            }
            // 执行post请求.
            CloseableHttpResponse response = httpclient.execute(httpPut);
            System.out.println("got response");

            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, Charset.forName("UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * FileMultiPart POST请求
     *
     * @param url
     * @param header
     * @param reqParam
     * @return
     */
    /**
     * 以post方式调用第三方接口,以form-data 形式  发送 MultipartFile 文件数据
     *
     * @param url           post请求url
     * @param fileParamName 文件参数名称
     * @param file 文件
     * @param paramMap      表单里其他参数
     * @return
     */
    public static String doPostFormData(String url, String fileParamName, File file, Map<String, String> paramMap, Map<String, String> header) {
        log.info("url ::: {}", url);
        log.info("paramMap ::: {}", JSON.toJSONString(paramMap));
        log.info("header ::: {}", JSON.toJSONString(header));
        // 创建Http实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);

        // 请求参数配置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
                .setConnectionRequestTimeout(10000).build();
        httpPost.setConfig(requestConfig);

        FileInputStream fileInputStream = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            fileInputStream = new FileInputStream(file);
            // 文件流
            builder.addBinaryBody(fileParamName, fileInputStream, ContentType.MULTIPART_FORM_DATA, file.getName());
            //表单中其他参数
            if (paramMap != null) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.setHeader(key, header.get(key));
                }
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回
                String res = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                log.info("上传文件返回结果 ::: {}", res);
                return res;
                /*if (!StringUtils.isEmpty(res)) {
                    return JSON.parseObject(res);
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用HttpPost失败！" + e.toString());
        } finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("关闭HttpPost连接失败！");
                }
            }
        }
        return null;
    }
}
