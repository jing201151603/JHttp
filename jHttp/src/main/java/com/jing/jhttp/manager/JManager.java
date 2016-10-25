package com.jing.jhttp.manager;

import android.content.Context;
import android.widget.ImageView;

import com.jing.jhttp.listener.OnRequestBitmapListener;
import com.jing.jhttp.request.Request;
import com.jing.jhttp.request.RequestBitmap;
import com.jing.jhttp.request.RequestImage;
import com.jing.jhttp.request.RequestPool;

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

    public JManager setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return ManagerHolder.jManager;
    }

    /*public void request(Activity activity, String url, ImageView imageView, int loadImg, int failureImg) {
        request(activity, url, imageView, loadImg, failureImg, false);
    }

    public void request(Activity activity, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache) {
        request(activity, url, imageView, loadImg, failureImg, shouldUpdateCache, false);
    }

    public void request(Activity activity, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        RequestImage requestImage = new RequestImage(activity, url, imageView, loadImg, failureImg, null, shouldUpdateCache, shouldUpdateUi);
        RequestPool.getInstance().addRequest(requestImage);
    }

    public void request(Activity activity, String url, OnRequestListener onRequestListener) {
        request(activity, url, onRequestListener, false);
    }

    public void request(Activity activity, String url, OnRequestListener onRequestListener, boolean shouldUpdateCache) {
        request(activity, url, onRequestListener, false, false);
    }

    public void request(Activity activity, String url, OnRequestListener onRequestListener, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        RequestBitmap requestBitmap = new RequestBitmap(activity, url, Request.RequestMethod.GET, onRequestListener, null, false, false);
        RequestPool.getInstance().addRequest(requestBitmap);
    }*/

    public void request(Context context, String url, ImageView imageView, int loadImg, int failureImg) {
        request(context, url, imageView, loadImg, failureImg, false);
    }

    public void request(Context context, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache) {
        request(context, url, imageView, loadImg, failureImg, shouldUpdateCache, false);
    }

    public void request(Context context, String url, ImageView imageView, int loadImg, int failureImg, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        RequestImage requestImage = new RequestImage(context, url, imageView, loadImg, failureImg, null, shouldUpdateCache, shouldUpdateUi);
        RequestPool.getInstance().addRequest(requestImage);
    }

    public void request(Context context, String url, OnRequestBitmapListener onRequestBitmapListener) {
        request(context, url, onRequestBitmapListener, false);
    }

    public void request(Context context, String url, OnRequestBitmapListener onRequestBitmapListener, boolean shouldUpdateCache) {
        request(context, url, onRequestBitmapListener, false, false);
    }

    public void request(Context context, String url, OnRequestBitmapListener onRequestBitmapListener, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        RequestBitmap requestBitmap = new RequestBitmap(context, url, Request.RequestMethod.GET, onRequestBitmapListener, null, false, false);
        RequestPool.getInstance().addRequest(requestBitmap);
    }

}
