package com.cloudolp.peony.server.wxpay.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: http请求工具
 *
 * @author weber
 * @date 8/26/2017 1:28 AM
 */
public class HttpClientUtil {
    private final static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 设置http请求超时时间
     *
     * @param connTimeoutMS    链接超时时间（ms）
     * @param connReqTimeoutMS 从connect Manager获取Connection 超时时间（ms）
     * @param rwTimeoutMS      读取超时时间（ms）
     * @return RequestConfig
     */
    private static RequestConfig getRequestConfig(int connTimeoutMS, int connReqTimeoutMS, int rwTimeoutMS) {
        if (connTimeoutMS <= 0) {
            connTimeoutMS = 6000;
        }
        if (connReqTimeoutMS <= 0) {
            connReqTimeoutMS = 6000;
        }
        if (rwTimeoutMS <= 0) {
            rwTimeoutMS = 6000;
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connTimeoutMS).setConnectionRequestTimeout(connReqTimeoutMS).setSocketTimeout(rwTimeoutMS).build();
        return requestConfig;
    }

    /**
     * 发送http GET请求 设置默认链接超时、获取conn超时、读取超时
     *
     * @param url
     * @param params 参数map
     *               是否启用ssl
     * @return
     */
    public static String doGet(String url, Map<String, ?> params) {
        return doGetWithTimeout(url, params, 0, 0, 0);
    }

    /**
     * 发送http GET请求 默认没有链接超时和读取超时处理
     *
     * @param url
     * @param params           参数map
     *                         是否启用ssl
     * @param connTimeoutMS    链接超时时间（ms）
     * @param connReqTimeoutMS 从connect Manager获取Connection 超时时间（ms）
     *                         读取超时时间（ms）
     * @return
     */
    public static String doGetWithTimeout(String url, Map<String, ?> params, int connTimeoutMS, int connReqTimeoutMS, int rwTimeoutMSS) {
        log.info("调用发送get请求：url：{}，params：{}", url, params);

        HttpClient httpClient = null;
        HttpGet httpGet = null;
        if (StringUtils.isNotBlank(url) && params != null && !params.isEmpty()) {
            httpClient = HttpClients.createDefault();
            try {
                List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
                // 建立一个NameValuePair数组，用于存储欲传送的参数
                for (Map.Entry<String, ?> e : params.entrySet()) {
                    paramsList.add(new BasicNameValuePair(e.getKey(), e.getValue() == null ? "" : e.getValue().toString()));
                }
                String paramsStr = URLEncodedUtils.format(paramsList, "UTF-8");
                if (url.lastIndexOf("?") == -1) {
                    url += "?";
                }
                httpGet = new HttpGet(url + paramsStr);
                // 设置链接超时、获取conn超时、读取超时
                httpGet.setConfig(getRequestConfig(connTimeoutMS, connReqTimeoutMS, rwTimeoutMSS));
                // 发送请求
                HttpResponse response = httpClient.execute(httpGet);
                // 将响应数据流转为字符串，注意指定编码，否则中文乱码
                String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("调用发送get请求成功：result:" + entity);
                return entity;
            } catch (Exception e) {
                log.error("调用发送get请求失败:\n" + e);
            }


        } else {
            log.error("调用发送get请求失败: 参数错误！");
        }
        return null;
    }

    /**
     * 发送http POST请求 设置默认链接超时、获取conn超时、读取超时
     *
     * @param url
     * @param params  参数map
     * @param jsonObj 请求体
     *                是否启用ssl
     * @return
     */
    public static String doPost(String url, Map<String, ?> params, JSONObject jsonObj) {
        return doPostWithTimeout(url, params, jsonObj, 0, 0, 0);
    }

    /**
     * 发送http POST请求 默认没有链接超时和读取超时处理
     *
     * @param url
     * @param params           参数map
     * @param jsonObj          请求体
     *                         是否启用ssl
     * @param connTimeoutMS    链接超时时间（ms）
     * @param connReqTimeoutMS 从connect Manager获取Connection 超时时间（ms）
     *                         读取超时时间（ms）
     * @return
     */
    public static String doPostWithTimeout(String url, Map<String, ?> params, JSONObject jsonObj, int connTimeoutMS, int connReqTimeoutMS, int rwTimeoutMSS) {
        log.info("调用发送get请求：url：{}，params：{}", url, params);

        HttpClient httpClient = null;
        HttpPost httpPost = null;
        if (StringUtils.isNotBlank(url) && params != null && !params.isEmpty()) {
            httpClient = HttpClients.createDefault();
            try {
                List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
                // 建立一个NameValuePair数组，用于存储欲传送的参数
                for (Map.Entry<String, ?> e : params.entrySet()) {
                    paramsList.add(new BasicNameValuePair(e.getKey(), e.getValue() == null ? "" : e.getValue().toString()));
                }
                String paramsStr = URLEncodedUtils.format(paramsList, "UTF-8");
                if (url.lastIndexOf("?") == -1) {
                    url += "?";
                }
                httpPost = new HttpPost(url + paramsStr);
                // 设置链接超时、获取conn超时、读取超时
                httpPost.setConfig(getRequestConfig(connTimeoutMS, connReqTimeoutMS, rwTimeoutMSS));
                // 构建消息实体
                StringEntity requestEntity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
                requestEntity.setContentEncoding("UTF-8");
                requestEntity.setContentType("application/json");
                httpPost.setEntity(requestEntity);
                // 发送请求
                HttpResponse response = httpClient.execute(httpPost);
                // 将响应数据流转为字符串，注意指定编码，否则中文乱码
                String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.info("调用发送get请求成功：result:" + entity);
                return entity;
            } catch (Exception e) {
                log.error("调用发送get请求失败:\n" + e);
            }


        } else {
            log.error("调用发送get请求失败: 参数错误！");
        }
        return null;
    }


}
