package com.jing.jhttp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.jing.jhttp.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bmc on 2016/10/18.
 */

public class CacheHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private String id = "id";
    private String url = "url";//图片的URL
    private String time = "time";//插入数据库时的时间，唯一的
    private String key = "key";//缓存的key
    private String path = "path";//图片缓存的路径
    public static String tableName = "cache";//表名


    public CacheHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        getDataBase();
    }

    private SQLiteDatabase getDataBase() {
        if (database == null) {
            synchronized (CacheHelper.class) {
                database = getWritableDatabase();
            }
        }
        return database;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table cache (id integer primary key autoincrement,url text not null,time text not null,key text not null,path text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String url,String key) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.url, url);
        contentValues.put(this.time, key);
        contentValues.put(this.key, key);
        contentValues.put(this.path, key);
        database.insert(tableName, url, contentValues);//参数2：若contentValues为空，则插入一行空数据
    }

    public void replace(String url, String key) {
        if (TextUtils.isEmpty(getKey(url))) {//为null时不存在，则做插入动作
            insert(url,key);
        } else {//不为null时，则更新
            update(url, key);
        }
    }

    public boolean isExist(String url) {
        if (!TextUtils.isEmpty(getKey(url))) return true;
        return false;
    }

    public void update(String url, String key) {
        database.execSQL("update cache set key=? ,time=? where url=?;", new String[]{key, key, url});
    }

    public String getKey(String url) {
        Cursor cursor = database.rawQuery("select key from cache where url=?;", new String[]{url});
        String key = "";
        while (cursor.moveToNext()) {
            key = cursor.getString(cursor.getColumnIndex(this.key));
            return key;
        }
        cursor.close();
        return key;
    }

    public List<Long> getKeys() {
        List<Long> keys = new ArrayList<>();
        Cursor cursor = database.rawQuery("select key from cache;", null);
        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex(this.key));
            keys.add(Long.parseLong(key));
        }
        cursor.close();
        LogUtils.d("requ",keys.size()+"");
        return keys;
    }

    public void delete(String key) {
        database.execSQL("delete from cache where key=?;", new String[]{key});
    }

    public void clear(){
        database.execSQL("delete from cache;");
    }


}
