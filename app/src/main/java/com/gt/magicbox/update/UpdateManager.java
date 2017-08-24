package com.gt.magicbox.update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gt.magicbox.R;
import com.gt.magicbox.update.bean.AppUpdateBean;
import com.gt.magicbox.utils.commonutil.AppUtils;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * Created by jack-lin on 2017/8/23 0023.
 */

public class UpdateManager {
    private final static String TAG="update";
    private Context context;
    private String appId="";
    String VERSION_URL="http://deeptel.com.cn/app/79B4DE7C/getInfoByAppId.do";
    String APK_FILE_NAME="MagicBox.apk";
    private static final int NEED_UPDATE=0;
    private static final int DOWNLOADING=1;
    private static final int DOWNLOAD_FINISH=2;
    private static final int FAILURE_DOWNLOAD=3;
    private static final int CANCEL_DOWNLOAD=4;

    private Dialog mDownloadDialog;
    private TextView dialogContentTextView;
    private ProgressBar mProgress;
    private boolean cancelUpdate ;
    private long breakPoints=-1L;
    private ProgressDownloader downloader;
    private File file;
    private long mTotalBytes;
    private long mContentLength;
    private AppUpdateBean appUpdateBean;
    private Dialog dialog;
    private boolean isNeedUpdate=false;


    private OnTaskFinishListener onTaskFinishListener;
    private Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NEED_UPDATE:
                    showDownloadDialog();
                    break;
                case DOWNLOADING:
                    int progress = msg.arg1;
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    if (mDownloadDialog!=null)mDownloadDialog.dismiss();
                    installAPK();
                    break;
                case FAILURE_DOWNLOAD:
                    if (mDownloadDialog!=null&&mDownloadDialog.isShowing()){
                        dialogContentTextView.setText("下载安装包出现问题");
                        mProgress.setVisibility(View.GONE);
                        break;
                    }
                case CANCEL_DOWNLOAD:
                    if (mDownloadDialog!=null){
                        mDownloadDialog.dismiss();
                    }
                    if (downloader!=null){
                        downloader.pause();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };    public UpdateManager (Context context,String appId){
        this.appId=appId;
        this.context=context;
    }
    public void setOnTaskFinishListener(OnTaskFinishListener onTaskFinishListener) {
        this.onTaskFinishListener = onTaskFinishListener;
    }
    public void requestUpdate(){
        RequestVersionTask versionTask=new RequestVersionTask();
        versionTask.execute();

    }
    OkHttpClient client = new OkHttpClient();
    public String post()   {

        FormBody body = new FormBody.Builder()
                .add("appId",appId)
                .build();

        Request request = new Request.Builder()
                .url(VERSION_URL)
                .post(body)
                .build();
        Log.d("requestUpdate","request=="+request.toString());
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e(TAG,"IOException:"+response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private boolean compareVersion(int versionCode) {
        int currentVersionCode = AppUtils.getAppVersionCode();
        Log.i(TAG, "checkUpdate versionCode=" + versionCode + "  currentVersionCode=" + currentVersionCode);
        if (versionCode > 0 && versionCode != currentVersionCode) {
            if (versionCode > currentVersionCode) {
                mHandler.sendEmptyMessage(NEED_UPDATE);
                return true;
            }
        }
        return false;
    }
    private class RequestVersionTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            if (onTaskFinishListener!=null){
                onTaskFinishListener.onTaskResult(isNeedUpdate);
            }
            super.onPostExecute(o);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String response = post();
            Log.d(TAG, "response==" + response);
            String str = ConvertUtils.unicode2String(response);
            AppUpdateBean updateBean = new Gson().fromJson(str, AppUpdateBean.class);
            if (updateBean == null)return null ;
                appUpdateBean = updateBean;
            isNeedUpdate = compareVersion(Integer.parseInt(updateBean.appVersionCode));
            return null;
        }
    }
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        Log.d(TAG,"appUpdateBean="+appUpdateBean.remarks);

        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("正在更新");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        dialogContentTextView = (TextView) v.findViewById(R.id.update_content);
        if (appUpdateBean==null)return;
        String strVersion= TextUtils.isEmpty(appUpdateBean.appVersionName)?"最新版本":appUpdateBean.appVersionName+"版";
        dialogContentTextView.setText(strVersion+"更新内容 :  "+appUpdateBean.remarks);
        v.findViewById(R.id.start).setOnClickListener(onClickListener);
        v.findViewById(R.id.pause).setOnClickListener(onClickListener);
        v.findViewById(R.id.cancel).setOnClickListener(onClickListener);

        builder.setView(v);
        // 取消更新
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(true);
        mDownloadDialog.show();
        // 现在文件

    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         switch (v.getId()){
             case R.id.start:
                 Log.d(TAG,"onClickListener start");

                 downloadAPK(appUpdateBean.apkUrl,APK_FILE_NAME);
                 break;
             case R.id.pause:
                 downloader.pause();
                 breakPoints = mTotalBytes;
                 break;
             case R.id.cancel:
                 mHandler.sendEmptyMessage(CANCEL_DOWNLOAD);
                 // 设置取消状态
                 break;
         }
        }
    };
    private void downloadAPK(String url, final String fileName){
        if (breakPoints==-1L) {
            breakPoints = 0L;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
            downloader = new ProgressDownloader(url, file, listener);
            downloader.download(0L);
        }else if (breakPoints>0){
            downloader.download(breakPoints);

        }
    }
    private void installAPK(){
        if (mDownloadDialog!=null)mDownloadDialog.dismiss();
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(),APK_FILE_NAME)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    private  ProgressResponseBody.ProgressListener listener=new ProgressResponseBody.ProgressListener() {
        @Override
        public void onPreExecute(long contentLength) {
         // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
            if (mContentLength == 0L) {
                mContentLength = contentLength;
                mProgress.setMax((int) (mContentLength / 1024));
            }
        }

        @Override
        public void update(long totalBytes, boolean done) {
            mTotalBytes = totalBytes + breakPoints;
            mProgress.setProgress((int) (totalBytes + breakPoints) / 1024);
            if (done) {
                installAPK();
            }
        }
    };
}
