package com.jing.jhttp.manager;

import android.content.Context;

/**
 * Created by bmc on 2016/10/18.
 */

public class JManager {

    private int defaultSize = 50 * 1024 * 1024; // 50M图片大小
    private int cacheSize = defaultSize;

    private JManager() {
    }

    private static final class ManagerHolder {
        private static final JManager J_MANAGER = new JManager();
    }

    public static final JManager getInstance() {
        return ManagerHolder.J_MANAGER;
    }

    public void init(Context context) {

    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
}
