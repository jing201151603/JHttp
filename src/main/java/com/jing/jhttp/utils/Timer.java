package com.jing.jhttp.utils;

import android.os.CountDownTimer;

import com.jing.jhttp.JHandler;
import com.jing.jhttp.request.Request;

/**
 * author: 陈永镜 .
 * date: 2017/3/3 .
 * email: jing20071201@qq.com
 * <p>
 * introduce:
 */
public class Timer extends CountDownTimer {

    private JHandler jHandler;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public Timer(long millisInFuture, long countDownInterval, JHandler jHandler) {
        super(millisInFuture, countDownInterval);
        this.jHandler = jHandler;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        jHandler.setResultCode(CodeMsgUtils.Code.noNetwork, Request.result_type_failure_code);
    }

}
