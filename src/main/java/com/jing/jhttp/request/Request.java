package com.jing.jhttp.request;

import android.graphics.Bitmap;
import android.os.Message;

import com.jing.jhttp.JHandler;
import com.jing.jhttp.listener.OnRequestBitmapListener;
import com.jing.jhttp.listener.OnRequestListener;
import com.jing.jhttp.utils.LogUtils;

import java.util.HashMap;


/**
 * Created by bmc on 2016/10/17.
 */

public class Request implements Runnable {
    protected int defaultTime = 6 * 1000;
    protected int readTime = defaultTime;
    protected int connTimeout = defaultTime;
    protected String defaultEncode = "UTF-8";
    protected String encode = defaultEncode;
    protected String url = "";
    protected HashMap<String, String> params = null;
    protected OnRequestListener onRequestListener;
    protected RequestMethod method;
    public final int result_type_cache = 10;
    public final int result_type_failure = 100;
    public final int result_type_succeed = 1000;
    public final int result_type_update_ui = 10000;
    public final int result_type_update_imageview = 20;
    private int defaultRequestTimes = 3;//默认失败请求次数
    private int requestTimes = defaultRequestTimes;
    protected boolean isFinish = false;

    protected JHandler handler = new JHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case result_type_succeed:
                        onRequestListener.succeed(getResult());
                        break;
                    case result_type_cache:
                        ((OnRequestBitmapListener) onRequestListener).cache(getResult());
                        break;
                    case result_type_update_ui:
                        ((OnRequestBitmapListener) onRequestListener).updateUi(getResult());
                        break;
                    case result_type_failure:
                        onRequestListener.failure((String) getResult());
                        break;
                    case result_type_update_imageview:
                        if (getResult() == null) getImageView().setImageResource(getFailureImg());
                        else getImageView().setImageBitmap((Bitmap) getResult());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public Request(String url, RequestMethod method, OnRequestListener onRequestListener) {
        this.url = url;
        this.method = method;
        this.onRequestListener = onRequestListener;
    }

    public Request(String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params) {
        this.url = url;
        this.params = params;
        this.onRequestListener = onRequestListener;
        this.method = method;
    }

    @Override
    public void run() {

    }

    public enum RequestMethod {
        GET, POST
    }

    public int getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(int requestTimes) {
        this.requestTimes = requestTimes;
    }

    /**
     * 重复请求
     */
    protected void resumeRequest() {
        handler.setResult("result is null,will request count=" + getRequestTimes(), result_type_failure);
        if (getRequestTimes() > 0) {
            LogUtils.w(getClass().getName(), "will reconver this request,times:" + getRequestTimes());
            setRequestTimes(getRequestTimes() - 1);
            RequestPool.getInstance().addRequest(this);
            LogUtils.d(getClass().getName(), "resume this request count:" + getRequestTimes());
        } else handler.setResult("request times all failure", result_type_failure);
        return;
    }

}
