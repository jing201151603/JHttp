package com.jing.jhttp.request;

import com.jing.jhttp.utils.LogUtils;

/**
 * 请求池
 * Created by chenyongjing on 16-1-30.
 */
public class RequestPool extends AbstractPool {
    private final String tag = getClass().getName();

    private static final RequestPool instance = new RequestPool();

    public void addRequest(Request request) {
        try {
            pool.submit(request);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(tag, "add a request error --> " + request.toString() + ",error:" + e.getMessage());

        }
    }

    private RequestPool() {
    }

    public static RequestPool getInstance() {
        return instance;
    }
}
