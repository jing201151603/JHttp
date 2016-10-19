package com.jing.jhttp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.jhttp.listener.OnRequestListener;
import com.jing.jhttp.manager.JManager;
import com.jing.jhttp.request.Request;
import com.jing.jhttp.request.RequestBitmap;
import com.jing.jhttp.request.RequestPool;
import com.jing.jhttp.request.RequestString;
import com.jing.jhttp.utils.BitmapCache;
import com.jing.jhttp.utils.FileSizeUtil;
import com.jing.jhttp.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String tag = "main";
    String urlStr = "http://210.14.133.184:1119/res/json/privateFunds.json";
    String urlBitmap = "http://2t.5068.com/uploads/allimg/151029/51-1510291S338-50.jpg";
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BitmapCache cache = new BitmapCache(getApplicationContext());
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        image = (ImageView) findViewById(R.id.image);
        RequestBitmap requestBitmap=new RequestBitmap(this, urlBitmap, Request.RequestMethod.GET, onRequestListenerBitmap);
        requestBitmap.setRequestTimes(5);
        RequestPool.getInstance().addRequest(requestBitmap);

//        SqlManager.getInstance().getjChacheHelper(getApplicationContext()).clear();
    }

    public List<Long> sort(List<Long> data) {
        List<Long> newData = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 1; j < data.size() - i; j++) {
                Long temp;
                if ((data.get(j - 1)).compareTo(data.get(j)) > 0) {   //比较两个整数的大小
                    temp = data.get(j - 1);
                    data.set((j - 1), data.get(j));
                    data.set(j, temp);
                }
            }
        }
        return newData;
    }

    private void testBitmap() {
        RequestPool.getInstance().addRequest(new RequestBitmap(this, urlBitmap, Request.RequestMethod.GET, onRequestListenerBitmap));
    }

    private void addRequest() {
        RequestPool.getInstance().addRequest(new RequestString(urlStr, Request.RequestMethod.GET, onRequestListenerString));
        RequestPool.getInstance().addRequest(new RequestString(urlStr, Request.RequestMethod.GET, onRequestListenerString));
        RequestPool.getInstance().addRequest(new RequestString(urlStr, Request.RequestMethod.GET, onRequestListenerString));
        RequestPool.getInstance().addRequest(new RequestString(urlStr, Request.RequestMethod.GET, onRequestListenerString));
        RequestPool.getInstance().addRequest(new RequestString(urlStr, Request.RequestMethod.GET, onRequestListenerString));
    }

    private void testRequest() {
        new Thread() {
            @Override
            public void run() {
                super.run();

//                Request request = new Request(urlStr, Request.RequestMethod.GET, onRequestListenerString);
//                request.start();

            }
        }.start();
    }

    private OnRequestListener onRequestListenerString = new OnRequestListener<String>() {
        @Override
        public void succeed(String result) {
            LogUtils.d(getClass().getName(), "result=" + result);

        }

        @Override
        public void failure(String result) {

        }

        @Override
        public void cache(String s) {

        }

        @Override
        public void updateUi(String s) {

        }
    };
    private OnRequestListener onRequestListenerBitmap = new OnRequestListener<Bitmap>() {
        @Override
        public void succeed(Bitmap result) {
            BitmapCache cache = new BitmapCache(getApplicationContext());
            LogUtils.d("reques", "succeed,size=" + FileSizeUtil.getFolderSize(cache.getStorageDirectory()) + "," + FileSizeUtil.getFolderSize(cache.getStorageDirectory()) / 1024 / 1024 + "," + JManager.getInstance().getCacheSize() / 1024 / 1024);
            image.setImageBitmap(result);


        }

        @Override
        public void failure(String result) {
            LogUtils.d("req", "failure=" + result);
        }

        @Override
        public void cache(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
            LogUtils.d("reques", "cache");

        }

        @Override
        public void updateUi(Bitmap bitmap) {

        }
    };

}
