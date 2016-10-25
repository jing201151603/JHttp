package com.jing.jhttp.listener;

/**
 * Created by bmc on 2016/10/25.
 */

public interface OnRequestStringListener extends OnRequestListener<String> {
    @Override
    void succeed(String o);

    @Override
    void failure(String result);
}
