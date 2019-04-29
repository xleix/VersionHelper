package com.bjnsc.versionlibrary;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by gtaoeng on 2017/11/3.
 */

public class CacheUtil {

    public static Map<String, String> downloadMap = new HashMap<>();
    public static String CacheFolder = "VersionHelp";
    public static String CachePath = null;

    public static String getCachePath() {
        if (CachePath != null)
            return CachePath;
        CachePath = Environment.getExternalStorageDirectory() + "/"
                + CacheFolder;
        File f = new File(CachePath);
        if (!f.exists()) {
            f.mkdir();
        }
        return CachePath;
    }

    public static String getMapCache() {

        String mapCache = getCachePath();
        String temp = mapCache + "/MapCache";
        File f = new File(temp);
        if (!f.exists()) {
            f.mkdirs();
        }
        return temp;
    }
    public static String getFileCache() {

        String mapCache = getCachePath();
        String temp = mapCache + "/FileCache";
        File f = new File(temp);
        if (!f.exists()) {
            f.mkdirs();
        }
        return temp;
    }

    public static String getArcgisMapFile(String fileName) {
        String filepath = getCachePath() + "/" + fileName;
        String temp = "file://" + filepath;
        File f = new File(filepath);
        if (!f.exists()) {
            return null;
        }
        return temp;
    }

    public static String getCurDate() {

        String time = DateFormat.format("yyyy-MM-dd kk:mm:ss",
                Calendar.getInstance(Locale.CHINA))
                + "";
        return time;
    }

    public static String getCurDateName() {

        String time = DateFormat.format("yyyyMMddkkmmss",
                Calendar.getInstance(Locale.CHINA))
                + "";
        return time;
    }

    public static boolean moveDirectory(String srcDirName, String destDirName) {

        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists()) {
            boolean bc = destDir.mkdirs();
            if (!bc) destDir.mkdirs();
        }

        File[] sourceFiles = srcDir.listFiles();
        for (File sourceFile : sourceFiles) {
            if (sourceFile.isFile())
                moveFile(sourceFile.getAbsolutePath(),
                        destDir.getAbsolutePath());
            else if (sourceFile.isDirectory())
                moveDirectory(
                        sourceFile.getAbsolutePath(),
                        destDir.getAbsolutePath() + File.separator
                                + sourceFile.getName());
        }
        return srcDir.delete();
    }


    private static boolean moveFile(String srcFileName, String destDirName) {

        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();

        return srcFile.renameTo(new File(destDirName + File.separator
                + srcFile.getName()));
    }

    public static void deleteFile(String folder) {
        File file = new File(folder);
        if (file.isFile()) {
            file.delete();
            return;
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                File subFile = childFiles[i];
                if (subFile.isDirectory()) {
                    deleteFile(subFile.getPath());
                } else {
                    childFiles[i].delete();
                }
            }
            file.delete();
        }
    }


    public static double calcLength(double lon1, double lat1, double lon2,
                                    double lat2) {
        return Math.sqrt((lon1 - lon2) * (lon1 - lon2) + (lat1 - lat2)
                * (lat1 - lat2));
    }


    public static boolean WriteFile(String fileName, byte[] datas) {
        String todbfile = fileName;
        try {
            RandomAccessFile rf = new RandomAccessFile(todbfile, "rw");
            long len = rf.length();
            rf.seek(len);
            rf.write(datas);
            rf.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int ReadFileLength(String fileName) {
        RandomAccessFile rf;
        try {
            rf = new RandomAccessFile(fileName, "rw");
            int len = (int) rf.length();
            rf.close();
            return len;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static byte[] ReadFile(String fileName, int offset) {
        String todbfile = fileName;
        try {
            RandomAccessFile rf = new RandomAccessFile(todbfile, "rw");
            int len = (int) rf.length();
            rf.seek(offset);
            int fileSize = 1024 * 20;
            byte[] bys = null;
            if (offset + fileSize <= len) {
                bys = new byte[fileSize];
                rf.read(bys, 0, fileSize);
            } else {
                int sz = len - offset;
                bys = new byte[sz];
                rf.read(bys, 0, sz);
            }

            rf.close();
            return bys;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] ReadFile(String fileName) {
        String todbfile = fileName;
        try {
            RandomAccessFile rf = new RandomAccessFile(todbfile, "rw");
            int len = (int) rf.length();
            int offset = 0;
            rf.seek(0);
            int fileSize = 1024;
            byte[] bys = new byte[len];
            while (true) {
                if (offset + fileSize <= len) {
                    offset += rf.read(bys, offset, fileSize);
                } else {
                    int sz = len - offset;
                    rf.read(bys, offset, sz);
                    break;
                }
            }

            rf.close();
            return bys;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param file
     * @return byte单位
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * @param file 文件夹
     * @return 返回文件夹中的文件数量
     */
    public static long getFolderCount(File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderCount(fileList[i]);

                } else {
                    size = size + 1;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getUriContent(Context context, Uri uri) {

        try {
            InputStream inputStream = context.getContentResolver()
                    .openInputStream(uri);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String ret = "";
            String lineStr = null;
            while ((lineStr = bufferedReader.readLine()) != null) {
                ret += lineStr;

            }
            System.out.println("lineStr：" + ret);
            bufferedReader.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
