package com.jing.jhttp.manager;

import android.content.Context;

import com.jing.jhttp.helper.CacheHelper;

/**
 * Created by bmc on 2016/10/18.
 */

public class SqlManager {

    private static final class SqlManagerHolder {
        private static final SqlManager sqlManager = new SqlManager();
    }

    private SqlManager() {
    }

    public final static SqlManager getInstance() {
        return SqlManagerHolder.sqlManager;
    }

    public CacheHelper getjChacheHelper(Context context) {
        CacheHelper jCacheHelper = new CacheHelper(context, "jcache.db", null, 1);
        return jCacheHelper;
    }

}
