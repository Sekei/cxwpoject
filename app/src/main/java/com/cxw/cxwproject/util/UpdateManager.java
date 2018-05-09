package com.cxw.cxwproject.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.VersionUpdateBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.camera.ImageUtils;
import com.cxw.cxwproject.widget.ToastUtils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;

import u.aly.df;

public class UpdateManager {
    private DownloadManager downloadManager;
    private SharedPreferences sp;

    private Context mContext;
    private int versionCode;

    private VersionUpdateBean mUpdate;

    public UpdateManager(Context context) {
        this.mContext = context;
        // 这里需要优化
        sp = context.getSharedPreferences("update", Context.MODE_PRIVATE);
        downloadManager = (DownloadManager) MyApp.getApp().getSystemService(Context.DOWNLOAD_SERVICE);
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        versionCode = TDevice.getVersionCode();
    }

    public void update(VersionUpdateBean update) {
        // 判断是否是最新版本
        mUpdate = update;
        if (!judgeUpdate(mUpdate.getVersion())) {
            return;
        }
        if (sp.contains("" + versionCode)) {
            if (queryDownloadStatus()) {
            }
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("尘晓屋新版本提醒").setMessage(mUpdate.getContent()).setPositiveButton("立即升级",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 若果强制下载则从当前页面下载
                        if (mUpdate.getIs_update().equals("1")) {
                            downLoadApk(mContext, mUpdate.getUrl());
                        } else {
                            ToastUtils.show("正在后台下载...");
                            download(mUpdate.getUrl());
                            dialog.dismiss();
                        }
                    }
                });
        dialog.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 直接杀死应用 根据判断是否强制升级
                if (mUpdate.getIs_update().equals("1")) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false); // 设置对话框以外的地方都不可以点击
        dialog.create().show();
    }

    public void checkLatestVersion() {
        final LoadingDialog dialog = new LoadingDialog(mContext);
        Api.VersionUpdate(new OnResponse<VersionUpdateBean>() {
            @Override
            public void onResponse(VersionUpdateBean o) {
                dialog.dismiss();
                update(o);
            }

            @Override
            public void onErrorResponse(String error) {
                dialog.dismiss();
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            queryDownloadStatus();
        }
    };

    private boolean queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(sp.getLong("" + versionCode, 0));
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.v("down", "STATUS_PAUSED");
                case DownloadManager.STATUS_PENDING:
                    Log.v("down", "STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    // 正在下载，不做任何事情
                    Log.v("down", "STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    // 完成
                    Log.v("down", "下载完成");
                    String uri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    TDevice.installAPK(mContext, new File(ImageUtils.getRealFilePath(mContext, Uri.parse(uri))));
                    sp.edit().clear().commit();// TODO 这里这样写真的好么

                    break;
                case DownloadManager.STATUS_FAILED:
                    // 清除已下载的内容，重新下载
                    Log.v("down", "STATUS_FAILED");
                    downloadManager.remove(sp.getLong("" + versionCode, 0));
                    sp.edit().clear().commit();
                    return false;
            }
        }
        return true;
    }

    private void download(String url) {
        if (!sp.contains("" + versionCode)) {
            // 开始下载
            Uri resource = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(resource);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            // 设置文件类型
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
            request.setMimeType(mimeString);
            // 在通知栏中显示
            request.setVisibleInDownloadsUi(true);
            // sdcard的目录下的download文件夹
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    "GQF_v" + mUpdate.getVersion() + ".apk");
            request.setTitle(mContext.getText(R.string.app_name));
            long id = downloadManager.enqueue(request);
            // 保存id
            // prefs.edit().putLong(DL_ID, id).commit();
            sp.edit().putLong("" + TDevice.getVersionCode(), id).commit();
        } else {
            // 下载已经开始，检查状态
            queryDownloadStatus();
        }
    }

    private void downLoadApk(final Context mContext, final String downURL) {
        final ProgressDialog pd; // 进度条对话框
        final Handler mHandler = new Handler();
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);// 必须一直下载完，不可取消
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    final File file = downloadFile(downURL, pd);
                    TDevice.installAPK(mContext, file);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(mContext).setTitle("安装").setMessage("已下载最新版本，请安装")
                                    .setCancelable(false)
                                    .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            TDevice.installAPK(mContext, file);
                                        }
                                    }).show();
                        }
                    }, 50);
                    // installApk(mContext,file);
                    // 结束掉进度条对话框
                    // 不能结束对话框
                    // pd.dismiss();
                } catch (Exception e) {
                    pd.dismiss();
                    // Log.e(TAG, "下载新版本失败，原因：" + e.getMessage());
                }
            }
        }.start();
    }

    /**
     * 从服务器下载最新更新文件
     *
     * @param path  下载路径
     * @param
     * @return
     * @throws Exception
     */
    private File downloadFile(String path, ProgressDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            int fileLength = conn.getContentLength();
            pd.setMax(fileLength);
            InputStream is = conn.getInputStream();
            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "GQF_v"
                    + mUpdate.getVersion() + ".apk";
            File file = new File(fileName);
            // 目录不存在创建目录
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            throw new IOException("未发现有SD卡");
        }
    }

    public static boolean judgeUpdate(String newVersion) {
        String oldVersion = TDevice.getVersionName();
        String[] oldV = oldVersion.split("\\.");
        String[] newV = newVersion.split("\\.");
        for (int i = 0; i < newV.length; i++) {
            if (Integer.parseInt(newV[i]) > Integer.parseInt(oldV[i])) {
                return true;
            }
        }
        return false;
    }
}
