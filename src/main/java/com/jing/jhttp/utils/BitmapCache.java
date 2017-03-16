package com.jing.jhttp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.WindowManager;

import com.jing.jhttp.exception.ContextNullException;
import com.jing.jhttp.manager.JManager;
import com.jing.jhttp.manager.SqlManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by bmc on 2016/10/15.
 */

public class BitmapCache {

    private Context context;
    private int displaypixels;


    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();


    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = "";
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/jcache";


    public BitmapCache(Context context) throws ContextNullException {
        if (context != null) {
            this.context = context;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            displaypixels = wm.getDefaultDisplay().getWidth() * wm.getDefaultDisplay().getHeight();
            if (TextUtils.isEmpty(mDataRootPath))
                init(context);
        } else {
            throw new ContextNullException("context connot be null!");
        }
    }

    /**
     * 当context为空时，根据报名设置缓存路径
     *
     * @param context
     */
    private void init(Context context) {
        if (context != null)
            mDataRootPath = context.getCacheDir().getPath();
        else mDataRootPath = "/data/user/0/" + context.getPackageName() + "/cache";
//        mDataRootPath = "/data/data/" + pkg + "/cache";
        LogUtils.w(getClass().getName(), "path=" + mDataRootPath);
    }


    /**
     * 获取储存Image的目录
     *
     * @return
     */
    public String getStorageDirectory() {
        return mDataRootPath + FOLDER_NAME;
    }
//                     return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public void savaBitmap(String fileName, Bitmap bitmap, String url) {
        clearForSpace(bitmap);
        try {
            if (bitmap == null) {
                return;
            }
            String path = getStorageDirectory();
            File folderFile = new File(path);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }
            File file = new File(path + File.separator + fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (SqlManager.getInstance().getjChacheHelper(context).isExist(url)) {//根据url判断是否存在key,存在则删除本地的图片
                String key = SqlManager.getInstance().getjChacheHelper(context).getKey(url);
                deleteFile(key);
            }
            SqlManager.getInstance().getjChacheHelper(context).replace(url, fileName);//更新数据库
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除缓存给新数据预留空间
     *
     * @param bitmap
     */
    public void clearForSpace(Bitmap bitmap) {
        if ((FileSizeUtil.getFolderSize(getStorageDirectory()) + getBitmapsize(bitmap)) < JManager.getInstance().getCacheSize()) {
            LogUtils.w(getClass().getName(), "hava enough space for newBitmap");
            return;
        }
        LogUtils.w(getClass().getName(), "will to clear for newBitmap's space");
        List<Long> keys = sort(SqlManager.getInstance().getjChacheHelper(context).getKeys());
        for (int i = 0; i < keys.size(); i++) {
            double totalSize = FileSizeUtil.getFolderSize(getStorageDirectory()) + getBitmapsize(bitmap);
            if (totalSize >= JManager.getInstance().getCacheSize()) {
                deleteFile(keys.get(0).toString());//删除文件
                SqlManager.getInstance().getjChacheHelper(context).delete(keys.get(0) + "");

                totalSize = FileSizeUtil.getFolderSize(getStorageDirectory()) + getBitmapsize(bitmap);
                if (totalSize < JManager.getInstance().getCacheSize()) return;
                LogUtils.w("req", "temp=" + (temp++) + ",total=" + totalSize / 1024 / 1024 + ",max=" + JManager.getInstance().getCacheSize() / 1024 / 1024);
                clearForSpace(bitmap);
            } else return;
        }

    }

    private int temp = 0;

    public List<Long> sort(List<Long> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 1; j < data.size() - i; j++) {
                Long temp;
                if ((data.get(j - 1)) > (data.get(j))) {   //比较两个整数的大小
                    temp = data.get(j - 1);
                    data.set((j - 1), data.get(j));
                    data.set(j, temp);
                }
            }
        }
        return data;
    }

    public long getBitmapsize(Bitmap bitmap) {
        if (bitmap == null) {
            LogUtils.e(getClass().getName(), "bitmap is null;");
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    /**
     * 从手机或者sd卡获取Bitmap
     *
     * @param url
     * @return
     */
    public Bitmap getBitmap(String url) {
        String fileName = SqlManager.getInstance().getjChacheHelper(context).getKey(url);

        try {
            return getBitmap(new FileInputStream(getStorageDirectory() + File.separator + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
        return null;


    }

    private Bitmap getBitmap(InputStream stream) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        byte[] bytes = getBytes(stream);
        //这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        opts.inSampleSize = 4;
        //end
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

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


    /**
     * 判断文件是否存在
     *
     * @param url
     * @return
     */
    public boolean isFileExists(String url) {
        return SqlManager.getInstance().getjChacheHelper(context).isExist(url);
    }

    /**
     * 获取文件的大小
     *
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        dirFile.delete();
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String fileName) {
        File file = new File(getStorageDirectory() + File.separator + fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

}
