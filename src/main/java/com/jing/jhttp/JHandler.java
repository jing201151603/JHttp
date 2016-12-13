package com.jing.jhttp;

import android.os.Handler;
import android.widget.ImageView;

import com.jing.jhttp.utils.CodeMsgUtils;

/**
 * Created by bmc on 2016/10/18.
 */

public class JHandler<T> extends Handler {

    private ImageView imageView;
    private int loadImg;
    private int failureImg;

    private T result;
    private int code = CodeMsgUtils.Code.noNetwork;//错误码默认是网络不稳定
    private String msg = "";//错误信息

    public T getResult() {
        return result;
    }

    /**
     * 获取对应的错误信息
     *
     * @return
     */
    public String getResultMsg() {
        return msg;
    }

    /**
     * 设置请求的结果
     *
     * @param result
     * @param what
     */
    public void setResult(T result, int what) {
        this.result = result;
        sendEmptyMessage(what);
    }

    /**
     * 设置错误码
     *
     * @param msg
     * @param what
     */
    public void setResultMsg(String msg, int what) {
        this.msg = msg;
        sendEmptyMessage(what);
    }

    /**
     * 设置错误码
     *
     * @param code
     * @param what
     */
    public void setResultCode(int code, int what) {
        this.code = code;
        sendEmptyMessage(what);
    }

    public int getResultCode() {
        return code;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getLoadImg() {
        return loadImg;
    }

    public void setLoadImg(int loadImg) {
        this.loadImg = loadImg;
    }

    public int getFailureImg() {
        return failureImg;
    }

    public void setFailureImg(int failureImg) {
        this.failureImg = failureImg;
    }
}
