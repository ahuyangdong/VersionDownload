package com.dommy.version.util;

import android.content.Context;

import java.io.File;

public class StorageUtil {

    private static final String PATH_APP_DOWNLOAD = "download";

    /**
     * 创建和获取当前App的下载路径
     * @param context
     * @return
     */
    public static String getDownloadPath(Context context) {
        File folder = context.getExternalFilesDir(null);
        File downloadFolder = new File(folder, PATH_APP_DOWNLOAD);
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }
        return downloadFolder.getAbsolutePath();
    }

}
