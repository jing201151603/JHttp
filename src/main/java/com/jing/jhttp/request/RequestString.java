package com.jing.jhttp.request;

import android.text.TextUtils;

import com.jing.jhttp.listener.OnRequestListener;
import com.jing.jhttp.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bmc on 2016/10/17.
 */

public class RequestString extends Request {

    public RequestString(String url, RequestMethod method, OnRequestListener onRequestListener) {
        super(url, method, onRequestListener);
    }

    public RequestString(String url, RequestMethod method, OnRequestListener onRequestListener, Map<String, String> params) {
        super(url, method, onRequestListener, params);
    }

    @Override
    public void run() {

        try {
            String result = "";
            switch (method) {
                case GET:
                    result = get(url);
                    break;
                case POST:
                    result = post(url, params);
                    break;
            }
            if (TextUtils.isEmpty(result)) {
                resumeRequest();
                return;
            }

            setFinish(true);//设置标识，标识请求完成

            handler.setResult(result, result_type_succeed);

        } catch (Exception e) {
            e.printStackTrace();
            handler.setResultMsg(e.getMessage(), result_type_failure);
        }

    }


    /**
     * get请求字符串
     *
     * @param url
     * @return
     */
    private String get(String url) {
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection connection = null;

        try {
            URL newUrl = new URL(url);
            connection = (HttpURLConnection) newUrl.openConnection();
            connection.setReadTimeout(readTime);
            connection.setConnectTimeout(connTimeout);
            connection.setRequestMethod(RequestMethod.GET.toString());
            connection.setDoInput(true); //允许输入流，即允许下载
            connection.setDoOutput(false); //不允许输出流，即不允许上传，当需要传递参数时开启
            connection.setUseCaches(false); //不使用缓冲
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();//发起请求
            inputStream = connection.getInputStream();
            result = inputStream2String(inputStream);
        } catch (MalformedURLException e) {
            LogUtils.e(getClass().getName(), e.getMessage());
        } catch (IOException e) {
            LogUtils.e(getClass().getName(), e.getMessage());
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                LogUtils.e(getClass().getName(), e.getMessage());
            }
            if (connection != null) connection.disconnect();
        }
        return result;
    }

    /**
     * post请求带参数的
     *
     * @param url
     * @param params
     * @return
     */
    private String post(String url, Map<String, String> params) {
        InputStream inputStream = null;
        String result = "";
        HttpURLConnection connection = null;
        try {
            URL newUrl = new URL(url);
            connection = (HttpURLConnection) newUrl.openConnection();
            connection.setReadTimeout(readTime);
            connection.setConnectTimeout(connTimeout);
            connection.setRequestMethod(RequestMethod.POST.toString());
            connection.setDoInput(true); //允许输入流，即允许下载
            connection.setDoOutput(true); //允许输出流，即允许上传，当需要传递参数时开启
            connection.setUseCaches(false); //不使用缓冲
            connection.setRequestProperty("Connection", "Keep-Alive");
            if (params != null)
                postParam(connection.getOutputStream(), params);
            connection.connect();//发起请求
            inputStream = connection.getInputStream();
            result = inputStream2String(inputStream);
        } catch (MalformedURLException e) {
            LogUtils.e(getClass().getName(), e.getMessage());
        } catch (IOException e) {
            LogUtils.e(getClass().getName(), e.getMessage());
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                LogUtils.e(getClass().getName(), e.getMessage());
            }
            if (connection != null) connection.disconnect();
        }

        return result;
    }

    /**
     * post请求不带参数的
     *
     * @param url
     * @return
     */
    private String postString(String url) {
        return post(url, null);
    }

    private void postParam(OutputStream outputStream, Map<String, String> params) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                if (!TextUtils.isEmpty(stringBuilder)) stringBuilder.append("&");
                String key = iterator.next();
                stringBuilder.append(URLEncoder.encode(key, encode));
                stringBuilder.append("=");
                stringBuilder.append(params.get(key));
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encode));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String inputStream2String(InputStream inputStream) {
        StringBuffer out = null;
        try {
            out = new StringBuffer();
            byte[] b = new byte[4096];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                out.append(new String(b, 0, n));
            }
            LogUtils.d(getClass().getName(), new Integer(out.length()).toString());
        } catch (IOException e) {
            LogUtils.e(getClass().getName(), e.getMessage());
            return "";
        }
        return out.toString();
    }

}
