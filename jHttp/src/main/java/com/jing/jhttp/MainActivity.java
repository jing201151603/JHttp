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

        image = (ImageView) findViewById(R.id.image);
        JManager.getInstance().request(this, urlBitmap, image, R.mipmap.ic_launcher, R.mipmap.ic_launcher);

    }


}
