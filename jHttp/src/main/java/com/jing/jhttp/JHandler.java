package com.jing.jhttp;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by bmc on 2016/10/18.
 */

public class JHandler<T> extends Handler {



    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result, int what) {
        this.result = result;
        sendEmptyMessage(what);
    }

}
