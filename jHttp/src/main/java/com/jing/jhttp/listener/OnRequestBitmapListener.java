package com.jing.jhttp.listener;

/**
 * Created by bmc on 2016/10/25.
 */

public interface OnRequestBitmapListener extends OnRequestListener {
    @Override
    void succeed(Object o);

    @Override
    void failure(String result);

    void cache(Object result);

    void updateUi(Object result);

}
