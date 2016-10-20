package com.jing.jhttp;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by bmc on 2016/10/18.
 */

public class JHandler<T> extends Handler {

    private ImageView imageView;
    private int loadImg;
    private int failureImg;

    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result, int what) {
        this.result = result;
        sendEmptyMessage(what);
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
