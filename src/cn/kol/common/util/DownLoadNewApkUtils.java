/*-----------------------------------------------------------

-- PURPOSE

--    下载新版APK的对话框和逻辑封装

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;
import cn.kol.pes.widget.KoCommonDialog;

public class DownLoadNewApkUtils {
    
    private static final String tag = "DownLoadNewApkUtils";
    private Activity mContext;
    
    public static DownLoadNewApkUtils getInstance(Activity context) {
        return new DownLoadNewApkUtils(context);
    }
    
    public DownLoadNewApkUtils(Activity context) {
        this.mContext = context;
    }
    
    public void showVersionUpdateUI(final KoUpdateApkAdapter cd) {
        
        mIsForceUpdate = cd.isForceUpdate;
        
        KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(mContext, new KoCommonDialog.CommonDlgClick() {
			
			@Override
			public void onOkBack() {
				apkUrl = cd.updateApkUrl;
                if (LogUtils.show) {
                    LogUtils.e(tag, "apkUrl=" + apkUrl);
                }
                updateApp(apkUrl);
			}
			
			@Override
			public void onCancelBack() {
				if (cd.isForceUpdate) {
                    mContext.finish();
                }
			}
		}, cd.updateMsg);
        dlg.setTitle(mContext.getString(R.string.ko_title_there_is_new_apk_version));
        dlg.setBtnText(R.string.ko_title_down_load, R.string.ko_title_cancel);
    }

    ProgressBar mProgress;
    ProgressBar mProgressLarge;
    KoCommonDialog mProgressDialog;
    TextView mProgressText;
    String apkFile;
    String apkUrl;
    int progress = 0;
    boolean isCancelUpdate = false;
    boolean mIsForceUpdate = false;

    public void updateApp(String url) {
        apkUrl = url;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ko_download_apk_progress_item, null);
        mProgress = (ProgressBar) view.findViewById(R.id.update_apk_progressbar_download);
        mProgressLarge = (ProgressBar) view.findViewById(R.id.update_apk_progressbar_large);
        mProgress.setProgress(0);
        mProgressText = (TextView) view.findViewById(R.id.update_apk_textView_download_percent);
        mProgressText.setText("0%");
        
        mProgressDialog = KoCommonDialog.getDlgAndShow(mContext, new KoCommonDialog.CommonDlgClick() {
			
			@Override
			public void onOkBack() {
			}
			
			@Override
			public void onCancelBack() {
				isCancelUpdate = true;
				if (mIsForceUpdate) {
                    mContext.finish();
                }
			}
		}, null);
        mProgressDialog.setTitle(mContext.getString(R.string.ko_title_downloading_new_apk_version));
        mProgressDialog.setOkCancelBtn(false, true);
        mProgressDialog.addViewToMyGroup(view);
        
        downloadStart();
    }

    private void downloadStart() {
        Thread downloadThread = new Thread(mDownApkRunnable);
        downloadThread.start();
    }

    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(mContext.getApplication().getFilesDir().getAbsolutePath());
                if (!file.exists()) {
                    file.mkdir();
                }
                apkFile = mContext.getApplication().getFilesDir().getAbsolutePath() + "/kol_pes.apk";

                FileOutputStream fos = mContext.openFileOutput("kol_pes.apk", Context.MODE_WORLD_READABLE);// new
                                                                                                                      // FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    updateHandler.sendEmptyMessage(1);
                    if (numread <= 0) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!isCancelUpdate);
                fos.close();
                is.close();
                updateHandler.sendEmptyMessage(2);
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    };

    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case 1:
                mProgress.setVisibility(View.VISIBLE);
                mProgressText.setVisibility(View.VISIBLE);
                mProgressLarge.setVisibility(View.GONE);
                if (progress >= 0 && progress <= 100) {
                    mProgress.setProgress(progress);
                    mProgressText.setText(progress + "%");
                }
                break;
            case 2:
                if (isCancelUpdate) {
                    // doCancelUpdate();
                    mProgressDialog.dismiss();
                    if(mIsForceUpdate) {
                        mContext.finish();
                    }
                } else {
                    installApk();
                    mProgressDialog.dismiss();
                }
                break;
            case 3:

                break;
            }
            super.handleMessage(msg);
        }
    };

    private void installApk() {

        File apkfile = new File(apkFile);
        if (!apkfile.exists()) {
            return;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

}
