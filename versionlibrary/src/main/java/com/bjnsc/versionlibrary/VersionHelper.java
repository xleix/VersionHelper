package com.bjnsc.versionlibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bjnsc.versionlibrary.model.VersionCls;
import com.bjnsc.versionlibrary.network.RetrofitHelper;
import com.bjnsc.versionlibrary.network.callback.MyBaseObserver;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class VersionHelper {

    public static final int CODE_FAILD_GET_FAILD = 0x1001;
    public static final int CODE_FAILD_NO_NETWORK = 0x1002;
    public static final int CODE_FAILD_DOWNLOAD_FAILD = 0x1003;

    public static final int CODE_FAILD_FORCED_UPDATE = 0x1004;
    public static final int CODE_FAILD_NOT_FORCED_UPDATE = 0x1005;


    // 2019/4/25/025 强制更新，下载失败也按检查失败用code区分

    public interface CheckAppVersionListener {

        void checkAppVersionSuccess();

        void checkAppVersionFaild(int faildCode);

//        void forcedUpdateExit(Context context, boolean isForce);
    }

    private Activity context;
    private CheckAppVersionListener checkAppVersionListener;
    private String appId;
    private volatile static VersionHelper versionHelper;

    public static VersionHelper init(Activity context) {
        if (versionHelper == null) {
            synchronized (VersionHelper.class) {
                if (versionHelper == null) {
                    versionHelper = new VersionHelper(context);
                }
            }
        }
        return versionHelper;
    }

    private VersionHelper(Activity context) {
        this.context = context;

        ApplicationInfo info = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA);
            appId = appInfo.metaData.getString("com.bjnsc.versionhelper");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: 2019/4/29/029 appid 为空的情况
        Log.e("version", "appId = " + appId);

        if(TextUtils.isEmpty(appId)) {
            throw new RuntimeException("appId is null, check you appId please");
        }

//        downLoadCompleteReceiver = new DownLoadCompleteReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);  //添加要收到的广播
//        context.registerReceiver(downLoadCompleteReceiver, intentFilter);
    }

//    public void unRegisterReceiver() {
//        context.unregisterReceiver(downLoadCompleteReceiver);
//    }

    public void updateApp(final CheckAppVersionListener checkAppVersionListener) {
        this.checkAppVersionListener = checkAppVersionListener;
        boolean hasNetwork = NetUtil.isNetworkConnected(context);
        if (hasNetwork) {

            Map<String, Object> paramers = new HashMap<>();
            paramers.put("platform", 0);//0 代表Android  1 代表ios
            paramers.put("project_id", appId);
//            paramers.put("project_id", Ins.appId);
            RetrofitHelper.getInstance().getVersion(paramers, new MyBaseObserver<VersionCls>() {
                @Override
                protected void onBaseNext(VersionCls data) {

                    if ("OK".equals(data.getStatus()) && data.getData() != null) {
                        try {

                            PackageManager manager = context.getPackageManager();
                            PackageInfo info = null;
                            try {
                                info = manager.getPackageInfo(context.getPackageName(), 0);
                            } catch (Exception ee) {
                                return;
                            }
                            if (info == null) return;
                            String versionName = info.versionName;
                            int versionCode = info.versionCode;
//                            int versionCode = Ins.versionCode;
//                            String appUrl = API.VersionHost + data.getData().getFile_link();
                            VersionCls.DataBean verData = data.getData();
                            String appUrl = verData.getFile_link();
                            String newVerName = verData.getVersion_name();
                            int newVerCode = verData.getVersion_code();
                            String forcedUpdate = verData.getForced_update();
                            if (versionCode < newVerCode) {
                                showVersionUpdateDialog(forcedUpdate, newVerName + "_" + newVerCode, data.getData().getComment(), appUrl);
                            } else {
                                if (checkAppVersionListener != null) {
                                    checkAppVersionListener.checkAppVersionSuccess();
                                }
                            }
                        } catch (Exception ee) {
                            if (checkAppVersionListener != null) {
                                checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_GET_FAILD);
                            }
                        }
                    } else {
                        if (checkAppVersionListener != null) {
                            checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_GET_FAILD);
                        }
                    }
                }

                @Override
                protected void onBaseError(Throwable t) {
                    super.onBaseError(t);
                    if (checkAppVersionListener != null) {
                        checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_GET_FAILD);
                    }
                }
            });
        } else {
            if (checkAppVersionListener != null) {
                checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_NO_NETWORK);
            }
        }
    }


    //下载
    private void showVersionUpdateDialog(final String forcedUpdate, final String newVerCode, String comment, final String appUrl) {

        StringBuffer sb = new StringBuffer();
        sb.append("发现新版本");
        sb.append(", 是否更新?\r\n");
        sb.append(comment);

        BounceTopEnter mBasIn = new BounceTopEnter();
//        SlideBottomExit mBasOut = new SlideBottomExit();
        final NormalDialog dialog = new NormalDialog(context);
        dialog.content(sb.toString())//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .title("软件更新")
                .titleTextColor(Color.parseColor("#11776A"))
                .btnText("取消", "确定")
                .showAnim(mBasIn);//
//                .dismissAnim(mBasOut);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        // TODO: 2019/4/19/019 强制更新要加上  取消更新后继续去登陆
                        //1为强制更新 0 为正常更新
                        if ("1".equals(forcedUpdate)) {
                            dialog.dismiss();

                            if (checkAppVersionListener != null) {
//                                checkAppVersionListener.forcedUpdateExit(context, true);
                                checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_FORCED_UPDATE);
                            }
                        } else {
                            dialog.dismiss();
                            if (checkAppVersionListener != null) {
//                                checkAppVersionListener.forcedUpdateExit(context, false);
                                checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_NOT_FORCED_UPDATE);
                            }
                        }
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
//                        if (context instanceof LoginActivity) {
//                            ((LoginActivity) context).setLoginVisibility(View.VISIBLE);
//                        }
                        String fileName = "version" + newVerCode + ".apks";
//                        download(fileName, fileName, appUrl);
                        dialog.dismiss();

                        String targetName = fileName.substring(0, fileName.length() - 1);
                        File file = new File(CacheUtil.getCachePath() + "/" + targetName);
                        if (file.exists()) {
                            installApk(file);
                        } else {
                            downFile(appUrl, fileName, targetName);
                        }

                    }
                });

    }

    private void downFile(String url, final String fileName, final String targetName) {
        NetUtil.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 404;
                msg.obj = "下载失败，请退出后重试";
                handler.sendMessage(msg);
                if (checkAppVersionListener != null) {
                    checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_DOWNLOAD_FAILD);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    if (is != null) {
                        File file = new File(CacheUtil.getCachePath() + "/" + fileName);
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int len = -1;
                        int progress = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            progress += len;

                            Message msg = new Message();
                            msg.arg1 = progress;
                            msg.arg2 = Integer.parseInt(String.valueOf(total));
                            handler.sendMessage(msg);
                        }
                        fos.flush();

                        if (fos != null) {
                            fos.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                        boolean b = file.renameTo(new File(CacheUtil.getCachePath() + "/" + targetName));
                        if (b) {
                            file = new File(CacheUtil.getCachePath() + "/" + targetName);
                            if (file.exists()) {
                                installApk(file);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 404;
                    msg.obj = "下载失败，请退出后重试";
                    handler.sendMessage(msg);
                    if (checkAppVersionListener != null) {
                        checkAppVersionListener.checkAppVersionFaild(CODE_FAILD_DOWNLOAD_FAILD);
                    }
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                }
            }
        });
    }

//    private DownLoadCompleteReceiver downLoadCompleteReceiver;
//    private DownloadManager downManager;
//    private long downID = -1;
//    private boolean isDownload = false;
//
//    private void download(final String fileName, final String showName, final String downUrl) {
//
//        long id = SharePrefUtil.getInstance().getDownId();
//        if (id != -1) {
//            DownloadManager.Query downloadQuery = new DownloadManager.Query();
//            downloadQuery.setFilterById(id);
//            if (downManager == null) {
//                downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            }
//
//            Cursor cursor = downManager.query(downloadQuery);
//            if (cursor != null && cursor.moveToFirst()) {
//
//                int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
//                int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
//
//                // 下载的文件总大小
//                int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);
//
//                // 截止目前已经下载的文件总大小
//                int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);
//                if (totalSizeBytes == bytesDownloadSoFar) {
////                    SharePrefUtil.getInstance().delDownId();
//                    File file = new File(CacheUtil.getCachePath() + "/" + fileName);
//                    if (file.exists()) {
//                        installApk(file);
//                    } else {
//                        downApk(fileName, showName, downUrl);
//                    }
//                } else {
////                    ToastUtils.showToast("正在下载中...");
//                    queryProgress();
//                }
//            }
//        }
//
//
////        File file = new File(CacheUtil.getCachePath() + "/" + fileName);
////        if (file.exists()) {
////            installApk(file);
////
////        }
//        else {
//            downApk(fileName, showName, downUrl);
//        }
//    }
//
//    private void downApk(String fileName, String showName, String downUrl) {
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
//                downUrl));
//        //设置在什么网络情况下进行下载
//        //request.setAllowedNetworkTypes(DownloadManager.Request.);
//        //设置通知栏标题
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//        request.setTitle(showName);
//        request.setDescription(showName);
//        request.setAllowedOverRoaming(false);
//        //设置文件存放目录
//        request.setDestinationInExternalPublicDir(CacheUtil.CacheFolder, fileName);
//
//        downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        downID = downManager.enqueue(request);
//        SharePrefUtil.getInstance().setDownId(downID);
//        CacheUtil.downloadMap.put(downID + "", fileName);
//
//        queryProgress();
//    }

//
//    private void queryProgress() {
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (!isDownload) {
//
//                    DownloadManager.Query downloadQuery = new DownloadManager.Query();
//                    downloadQuery.setFilterById(downID);
//                    Cursor cursor = downManager.query(downloadQuery);
//                    if (cursor != null && cursor.moveToFirst()) {
//
//                        int totalSizeBytesIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
//                        int bytesDownloadSoFarIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
//
//                        // 下载的文件总大小
//                        int totalSizeBytes = cursor.getInt(totalSizeBytesIndex);
//
//                        // 截止目前已经下载的文件总大小
//                        int bytesDownloadSoFar = cursor.getInt(bytesDownloadSoFarIndex);
//
//                        if (totalSizeBytes == bytesDownloadSoFar) {
////                            SharePrefUtil.getInstance().delDownId();
//
//                            Message ms = new Message();
//                            ms.what = 1;
//                            handler.sendMessage(ms);
//                        }
//
//                        cursor.close();
//
//                        Message msg = new Message();
//                        msg.arg1 = bytesDownloadSoFar;
//                        msg.arg2 = totalSizeBytes;
//                        handler.sendMessage(msg);
//                    }
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();
//
//    }

    Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int progress = msg.arg1;
                    int maxsize = msg.arg2;
                    showDownloadDialog(progress, maxsize);
                }
            });

            int what = msg.what;
            if (what == 404) {
                if (downloadDlg != null && downloadDlg.isShowing()) {
                    downloadDlg.dismiss();
                }
                String info = (String) msg.obj;
                NormalDialog dialog = new NormalDialog(context);
                dialog.style(NormalDialog.STYLE_ONE)//
                        .title("提示")
                        .titleTextColor(Color.parseColor("#11776A"))
                        .titleLineColor(Color.parseColor("#11776A"))
                        .btnNum(1)
                        .content(info)
                        .contentGravity(Gravity.CENTER)
                        .btnText("确定");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        context.finish();
                    }
                });
            }
        }
    };

    private Dialog downloadDlg;
    private NumberProgressBar down_numberProgressBar;
    private TextView down_tv_value;

    private void showDownloadDialog(int progress, int max) {

        if (downloadDlg == null) {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_app_download, null);
            downloadDlg = new android.app.AlertDialog.Builder(context).create();
            downloadDlg.setCancelable(false);
            downloadDlg.show();
            downloadDlg.getWindow().setContentView(view);
            downloadDlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            down_numberProgressBar = (NumberProgressBar) view.findViewById(R.id.number_progress_bar);
            down_tv_value = (TextView) view.findViewById(R.id.tv_value);
        }
        if (down_numberProgressBar != null) {
            down_numberProgressBar.setProgress(progress / (1024 * 1024));
            down_numberProgressBar.setMax(max / (1024 * 1024));
        }
        if (down_tv_value != null) {
            String vvla = String.format("%.2f MB/%.2f MB", progress * 1.0 / (1024 * 1024), max * 1.0 / (1024 * 1024));
            down_tv_value.setText(vvla);
        }

        if (progress == max) {
            downloadDlg.dismiss();
        }


    }

//    private class DownLoadCompleteReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
//                final long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (CacheUtil.downloadMap.get(id + "").endsWith(".apk")) {
//                            isDownload = true;
//                            try {
//                                File file = new File(CacheUtil.getCachePath() + "/" + CacheUtil.downloadMap.get(id + ""));
//                                installApk(file);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
//                Toast.makeText(context, CacheUtil.downloadMap.get(id + "") + "下载完成！",
//                        Toast.LENGTH_SHORT).show();
//            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
//                //点击
//            }
//        }
//    }

    private void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri data = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);

            intent.setDataAndType(data, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }

}
