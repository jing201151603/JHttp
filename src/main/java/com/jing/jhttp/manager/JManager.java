package com.jing.jhttp.manager;

import android.content.Context;
import android.widget.ImageView;

import com.jing.jhttp.listener.OnRequestBitmapListener;
import com.jing.jhttp.listener.OnRequestStringListener;
import com.jing.jhttp.request.Request;
import com.jing.jhttp.request.RequestBitmap;
import com.jing.jhttp.request.RequestImage;
import com.jing.jhttp.request.RequestPool;
import com.jing.jhttp.request.RequestString;

import java.util.Map;

/**
 * Created by bmc on 2016/10/18.
 */

public class JManager {

    private int defaultSize = 50 * 1024 * 1024; // 50M图片大小
    private int cacheSize = defaultSize;

    private JManager() {
    }

    private static final class ManagerHolder {
        private static final JManager jManager = new JManager();
    }

    public static final JManager getInstance() {
        return ManagerHolder.jManager;
    }

    public void init(Context context) {

    }

    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * 设置图片缓存的最大值
     * 当缓存大小大于最大值时，删除最不常用的图片，直到有足够的空间缓存最新的图片
     *
     * @param cacheSize
     * @return
     */
    public JManager setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return ManagerHolder.jManager;
    }

    public void requestBitmapWithImg(Context context, String url, ImageView imageView, int loadImg, int failureImg) {
        requestBitmapWithImg(context, url, imageView, loadImg, failureImg, false);
    }

    public void requestBitmapWithImg(Context context, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache) {
        requestBitmapWithImg(context, url, imageView, loadImg, failureImg, shouldUpdateCache, false);
    }

    /**
     * 适配imageview
     *
     * @param context
     * @param url
     * @param imageView
     * @param loadImg           加载时显示的图片
     * @param failureImg        加载失败显示的图片
     * @param shouldUpdateCache 是否更新缓存图片
     * @param shouldUpdateUi    更新缓存图片后是否更新最新的UI
     */
    public void requestBitmapWithImg(Context context, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        RequestImage requestImage = new RequestImage(context, url, imageView, loadImg, failureImg, null, shouldUpdateCache, shouldUpdateUi);
        RequestPool.getInstance().addRequest(requestImage);
    }

    public void requestBitmap(Context context, String url, OnRequestBitmapListener onRequestBitmapListener) {
        requestBitmap(context, url, false, onRequestBitmapListener);
    }

    public void requestBitmap(Context context, String url, boolean shouldUpdateCache, OnRequestBitmapListener onRequestBitmapListener) {
        requestBitmap(context, url, shouldUpdateCache, false, onRequestBitmapListener);
    }

    /**
     * 请求bitmap
     *
     * @param context
     * @param url
     * @param shouldUpdateCache       是否更新缓存
     * @param shouldUpdateUi          是否更新UI
     * @param onRequestBitmapListener 请求成功的回调，可以在回调方法里面直接更新UI
     */
    public void requestBitmap(Context context, String url, boolean shouldUpdateCache, boolean shouldUpdateUi, OnRequestBitmapListener onRequestBitmapListener) {
        RequestBitmap requestBitmap = new RequestBitmap(context, url, Request.RequestMethod.GET, onRequestBitmapListener, null, shouldUpdateCache, shouldUpdateUi);
        RequestPool.getInstance().addRequest(requestBitmap);
    }

    public void post(String url, Map<String, String> params, OnRequestStringListener onRequestStringListener) {
        RequestString requestString = new RequestString(url, Request.RequestMethod.POST, onRequestStringListener, params);
        RequestPool.getInstance().addRequest(requestString);
    }

    /**
     * post请求
     *
     * @param url
     * @param onRequestStringListener
     */
    public void post(String url, OnRequestStringListener onRequestStringListener) {
        post(url, null, onRequestStringListener);
    }

    /**
     * get请求
     *
     * @param url
     * @param onRequestStringListener
     */
    public void get(String url, OnRequestStringListener onRequestStringListener) {
        RequestString requestString = new RequestString(url, Request.RequestMethod.GET, onRequestStringListener);
        RequestPool.getInstance().addRequest(requestString);
    }

}
