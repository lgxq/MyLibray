package com.ebeitech.library.util;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liugaoxin on 2016/12/9 0009.
 * 简单的动态申请权限框架，目前只用在fragment中
 */
public class PermissionManager {
    private Fragment mFragment;
    private SparseArray<Runnable> mRequestMaps;

    public PermissionManager(Fragment fragment) {
        mFragment = fragment;
        mRequestMaps = new SparseArray<>();
    }

    /**
     * 调用入口
     * @param permissions 需要验证的权限
     * @param requestCode 申请标识
     * @param runnable 验证通过要执行的代码
     */
    public void requestPermission(String[] permissions, int requestCode, Runnable runnable) {
        List<String> requestPermissions = new ArrayList<>();
        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(mFragment.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }

        int length = requestPermissions.size();
        if(length > 0) {
            mRequestMaps.put(requestCode, runnable);
            mFragment.requestPermissions(requestPermissions.toArray(new String[length]), requestCode);

        } else {
            runnable.run();
        }
    }

    /**
     * 将回调结果传入该方法中
     */
    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(grantResults == null || grantResults.length < 1)
            return;

        //验证是否被赋予了权限
        boolean granted = true;
        boolean shouldShowRationale = false;
        String deniedPermission = "";
        for(int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                shouldShowRationale = mFragment.shouldShowRequestPermissionRationale(permissions[i]);
                deniedPermission = permissions[i];
            }
        }

        //权限处理结果，分为三种情况
        if(granted) {
            Runnable runnable = mRequestMaps.get(requestCode);
            if(runnable != null)
                runnable.run();

        } else if(shouldShowRationale) {
            String keyword = getKeywordByPermission(deniedPermission);
            String message = keyword + "权限被拒绝将无法使用该功能，是否手动开启" + keyword + "权限?";
            showRationale(message);

        } else {
            String keyword = getKeywordByPermission(deniedPermission);
            String message = keyword + "权限被拒绝, 无法使用该功能";
            Toast.makeText(mFragment.getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        //处理完移除该记录，避免内存泄露
        mRequestMaps.remove(requestCode);
    }

    //显示提示框
    private void showRationale(String contextStr) {
        new AlertDialog.Builder(mFragment.getActivity()).setTitle("温馨提示")
                .setMessage(contextStr)
                .setNegativeButton("取消", null)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri packageURI = Uri.parse("package:" + mFragment.getActivity().getPackageName());
                        Intent intent =  new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        mFragment.startActivity(intent);
                    }
                }).show();
    }

    //根据权限类型生成提示语
    private String getKeywordByPermission(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return "摄像头";
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return "文件读写";
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return "定位";
            case Manifest.permission.RECORD_AUDIO:
                return "录音";
        }

        return "";
    }
}
