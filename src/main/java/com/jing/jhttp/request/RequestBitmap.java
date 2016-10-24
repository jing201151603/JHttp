package com.jing.jhttp.request;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.jing.jhttp.listener.OnRequestListener;
import com.jing.jhttp.utils.BitmapCache;
import com.jing.jhttp.utils.LogUtils;
import com.jing.jhttp.utils.TimeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by bmc on 2016/10/17.
 */

public class RequestBitmap extends Request {

    private boolean shouldCache = true;//是否缓存
    private boolean shouldUpdateCache = false;//是否更新缓存
    private boolean shouldUpdateUi = false;//更新缓存之后是否更新UI
    private int displaypixels;
    private BitmapCache bitmapCache = null;
    private Context context;

    /*public RequestBitmap(Activity activity, String url, RequestMethod method, OnRequestListener onRequestListener) {
        this(activity, url, method, onRequestListener, null);
    }


    public RequestBitmap(Activity activity, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params) {
        this(activity, url, method, onRequestListener, params, false);
    }

    public RequestBitmap(Activity activity, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params, boolean shouldUpdateCache) {
        this(activity, url, method, onRequestListener, params, shouldUpdateCache, false);
    }

    public RequestBitmap(Activity activity, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        super(url, method, onRequestListener, params);
        this.shouldUpdateCache = shouldUpdateCache;
        this.shouldUpdateUi = shouldUpdateUi;
        context = activity;
        getPixels(activity);
        bitmapCache = new BitmapCache(activity);
    }*/


    public RequestBitmap(Context context, String url, RequestMethod method, OnRequestListener onRequestListener) {
        this(context, url, method, onRequestListener, null);
    }


    public RequestBitmap(Context context, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params) {
        this(context, url, method, onRequestListener, params, false);
    }

    public RequestBitmap(Context context, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params, boolean shouldUpdateCache) {
        this(context, url, method, onRequestListener, params, shouldUpdateCache, false);
    }

    public RequestBitmap(Context context, String url, RequestMethod method, OnRequestListener onRequestListener, HashMap<String, String> params, boolean shouldUpdateCache, boolean shouldUpdateUi) {
        super(url, method, onRequestListener, params);
        this.shouldUpdateCache = shouldUpdateCache;
        this.shouldUpdateUi = shouldUpdateUi;
        this.context = context;
        getPixels(context);
        bitmapCache = new BitmapCache(context);
    }

    private void getPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displaypixels = wm.getDefaultDisplay().getWidth() * wm.getDefaultDisplay().getHeight();
    }


    @Override
    public void run() {
        Bitmap result = null;

        if (judgeCache()) return;

        switch (this.method) {
            case GET:
                result = get(url);
                break;
        }
        if (result == null) {
            resumeRequest();
            return;
        }


        handler.setResult(result, result_type_succeed);

        if (shouldCache)//是否缓存
            bitmapCache.savaBitmap(TimeUtils.getNow(), result, url);

        if (shouldUpdateUi) //是否更新UI
            handler.setResult(result, result_type_update_ui);

    }

    /**
     * 判断是否有缓存，是否更新缓存
     *
     * @return
     */
    private boolean judgeCache() {
        BitmapCache cache = new BitmapCache(context);
        if (cache.isFileExists(url)) {
            LogUtils.d(getClass().getName(), "hava cache:" + url);
            handler.setResult(cache.getBitmap(url), result_type_cache);
            if (!shouldUpdateCache)
                return true;
        }
        return false;
    }


    /**
     * 从指定URL获取图片
     *
     * @param url
     * @return
     */
    private Bitmap get(String url) {
        Bitmap bitmap = null;
        try {
            URL newUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) newUrl.openConnection();
            connection.setReadTimeout(readTime);
            connection.setConnectTimeout(connTimeout);
            connection.setRequestMethod(RequestMethod.GET.toString());
            connection.setDoInput(true); //允许输入流，即允许下载
            connection.setDoOutput(false); //不允许输出流，即不允许上传，当需要传递参数时开启
            connection.setUseCaches(false); //不使用缓冲
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.connect();//发起请求
            InputStream is = connection.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
            bitmap = getBitmap(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            handler.setResult(e.getMessage(), result_type_failure);
        } catch (IOException e) {
            handler.setResult(e.getMessage(), result_type_failure);
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap getBitmap(InputStream stream) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        byte[] bytes = getBytes(stream);
        //这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, displaypixels);
        //end
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

    }

    /****
     * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
     */
    private int computeSampleSize(BitmapFactory.Options options,
                                  int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     * 数据流转成btyle[]数组
     */
    private byte[] getBytes(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[2048];
        int len = 0;
        try {
            while ((len = is.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
                baos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }


}
