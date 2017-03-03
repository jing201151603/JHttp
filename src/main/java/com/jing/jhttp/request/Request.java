package com.jing.jhttp.request;

import android.graphics.Bitmap;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.jing.jhttp.JHandler;
import com.jing.jhttp.listener.OnRequestBitmapListener;
import com.jing.jhttp.listener.OnRequestListener;
import com.jing.jhttp.utils.CodeMsgUtils;
import com.jing.jhttp.utils.LogUtils;
import com.jing.jhttp.utils.Timer;

import java.util.HashMap;
import java.util.Map;


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
    protected Map<String, String> params = null;
    protected OnRequestListener onRequestListener;
    protected RequestMethod method;
    public static final int result_type_cache = 10;
    public static final int result_type_failure_msg = 100;//错误信息
    public static final int result_type_failure_code = 101;//错误码
    public static final int result_type_succeed = 1000;
    public static final int result_type_update_ui = 10000;
    public static final int result_type_update_imageview = 20;
    private int defaultRequestTimes = 3;//默认失败请求次数
    private int requestTimes = defaultRequestTimes;
    protected boolean isFinish = false;
    protected boolean isShowAnimation = true;//默认是显示动画的
    private Timer timer;

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
                    case result_type_failure_msg:
                        if (onRequestListener != null)
                            onRequestListener.failure(getResultMsg());
                        break;
                    case result_type_failure_code:
                        if (onRequestListener != null)
                            onRequestListener.failure(getResultCode() + "");
                        break;
                    case result_type_update_imageview:
                        if (getResult() == null) getImageView().setImageResource(getFailureImg());
                        else {
                            if (isShowAnimation) {//判断是否显示动画
                                AlphaAnimation mHiddenAction = new AlphaAnimation(0.3f, 1.0f);
                                mHiddenAction.setDuration(300);
                                getImageView().startAnimation(mHiddenAction);
                            }
                            getImageView().setImageBitmap((Bitmap) getResult());
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(getClass().getName(), e.getMessage());
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
//        start_timer();
    }

    public Request(String url, RequestMethod method, OnRequestListener onRequestListener, Map<String, String> params) {
        this.url = url;
        this.params = params;
        this.onRequestListener = onRequestListener;
        this.method = method;
//        start_timer();
    }

    private void start_timer() {
        if (timer == null) {
            timer = new Timer(30 * 1000, 1000, handler);
        } else {
            timer.cancel();
        }
        timer.start();

    }


    @Override
    public void run() {
        LogUtils.w("jingjing", "Request.run");

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
//        handler.setResult("result is null,will request count=" + getRequestTimes(), result_type_failure);
        if (getRequestTimes() > 0) {
            LogUtils.w(getClass().getName(), "will reconver this request,times:" + getRequestTimes());
            setRequestTimes(getRequestTimes() - 1);
            RequestPool.getInstance().addRequest(this);
            LogUtils.d(getClass().getName(), "resume this request count:" + getRequestTimes());
        } else handler.setResultCode(CodeMsgUtils.Code.noNetwork, result_type_failure_code);
        return;
    }

}
