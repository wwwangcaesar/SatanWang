package com.lovcreate.core.util;

import java.io.File;

/**
 * Created by Bright on 2017/8/23 0023
 * 文件工具类
 */

public class FileUtil {

    public static boolean deleteFile(String filePath) {
        if (null == filePath) {
            return false;
        }
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

}
