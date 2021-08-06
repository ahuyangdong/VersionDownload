package com.dommy.version.util;

import android.content.Context;
import android.content.pm.PackageManager;

public class VersionUtil {

    /**
     * 获取当前App的versionCode值
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
