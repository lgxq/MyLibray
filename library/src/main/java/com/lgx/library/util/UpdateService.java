package com.lgx.library.util;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.ebeitech.library.R;

import java.io.File;

/**
 * Created by Administrator on 2016/7/25 0025.
 * 在线更新,启动该service即下载apk
 */
public class UpdateService extends Service {
    /** 安卓系统下载类 **/
    private DownloadManager manager;
    /** 接收下载完的广播 **/
    private DownloadCompleteReceiver receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //调用下载，有些手机需要验证文件读写权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "应用读写权限被禁止，您可以在权限管理中手动打开", Toast.LENGTH_LONG).show();
        }else{
            initDownManager();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    /**
     * 初始化下载器
     */
    private void initDownManager() {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse("url"));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir("/lgx/apk", getResources().getString(R.string.app_name) + ".apk"); //该方法可以放在外部目录上

        // 将下载请求放入队列
        manager.enqueue(down);

        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    /**
     * 接受下载完成后的广播
     */
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //自动安装apk
                installAPK(manager.getUriForDownloadedFile(downId));
                //停止服务并关闭广播
                UpdateService.this.stopSelf();
            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri apk) {
            if(Build.VERSION.SDK_INT < 23) {
                // 通过Intent安装APK文件
                Intent intents = new Intent();

                intents.setAction("android.intent.action.VIEW");
                intents.addCategory("android.intent.category.DEFAULT");
                intents.setType("application/vnd.android.package-archive");
                intents.setData(apk);
                intents.setDataAndType(apk,"application/vnd.android.package-archive");
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intents);
            } else {
                openFile(getFileByUri(apk), getApplicationContext());
            }
        }

        //android 6.0用普通方法安装会失败，这里用打开一般文件的方法安装
        private void openFile(File var0, Context var1) {
            Intent var2 = new Intent();
            var2.addFlags(268435456);
            var2.setAction("android.intent.action.VIEW");
            String var3 = getMIMEType(var0);
            var2.setDataAndType(Uri.fromFile(var0), var3);
            try {
                var1.startActivity(var2);
            } catch (Exception var5) {
                var5.printStackTrace();
                Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
            }

        }

        private String getMIMEType(File var0) {
            String var1;
            String var2 = var0.getName();
            String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
            var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
            return var1;
        }

        private File getFileByUri(Uri apk) {
            String path;
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(apk, proj, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
                cursor.close();
            } else {
                path = apk.getPath();
            }
            return new File(path);
        }
    }
}