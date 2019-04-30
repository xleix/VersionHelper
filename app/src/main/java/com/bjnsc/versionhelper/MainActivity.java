package com.bjnsc.versionhelper;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bjnsc.versionlibrary.VersionHelper;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions();
    }

    @AfterPermissionGranted(10086)
    private void requestPermissions() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            Toast.makeText(this, "已获取读写内存卡权限", Toast.LENGTH_SHORT).show();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "需要读写内存卡，去保存apk文件",
                    10086, perms);
        }

    }

    public void getVersionClick(View view) {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要读写内存卡，去保存apk文件", 10086, perms);
            return;
        }

        VersionHelper helper = VersionHelper.init(this);
        int hotfixVersionCode = 1;
//        helper.setNeedHotfix(true);//如果需要热更新需要设置为true 并且设置热更新的目标版本
//        helper.setHotfixVersionCode(hotfixVersionCode);
        helper.updateApp(new VersionHelper.CheckAppVersionListener() {
            @Override
            public void checkAppVersionSuccess(int successCode) {
                switch (successCode) {
                    case  VersionHelper.CODE_SUCCESS_NORMAL:
                        Toast.makeText(MainActivity.this, "当前为最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case  VersionHelper.CODE_SUCCESS_HOTFIX:
                        Toast.makeText(MainActivity.this, "需要进行热更新", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void checkAppVersionFaild(int faildCode) {

                switch (faildCode) {
                    case VersionHelper.CODE_FAILD_DOWNLOAD_FAILD:
                        Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        break;
                    case VersionHelper.CODE_FAILD_FORCED_UPDATE:
                        Toast.makeText(MainActivity.this, "本次更新为强制更新,取消会退出APP", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case VersionHelper.CODE_FAILD_GET_FAILD:
                        Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        break;
                    case VersionHelper.CODE_FAILD_NO_NETWORK:
                        Toast.makeText(MainActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        break;
                    case VersionHelper.CODE_FAILD_NOT_FORCED_UPDATE:
                        Toast.makeText(MainActivity.this, "本次更新为非强制更新", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "去设置页面", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
