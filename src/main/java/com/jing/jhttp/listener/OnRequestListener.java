package com.jing.jhttp.listener;

/**
 * Created by bmc on 2016/10/17.
 */

public interface OnRequestListener<Result> {
    void succeed(Result result);

    void failure(String result);

}
