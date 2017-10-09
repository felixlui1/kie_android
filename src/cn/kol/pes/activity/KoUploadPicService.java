package cn.kol.pes.activity;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.KoUploadImgUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.controller.KolPesNetReqControl;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoParamItem;

public class KoUploadPicService extends Service {

	private final String tag = "KoUploadPicService";
	private static final String KEY_CHECK_ID_STRING = "key_check_id_string";
	private static final String KEY_PATH_LIST_STRING = "key_path_list_string";
	private static final String KEY_IS_END = "key_is_end";
	private static final String KEY_IS_SEQ = "key_is_seq";
	
	private boolean mIsEnd = false;//应该是指是否是结束维修
	private boolean mIsSeq = false;
	
	public static void startSer(Context con, String checkId, String pathListString, boolean isEnd, boolean isSeq) {
		Intent i = new Intent(con, KoUploadPicService.class);
		i.putExtra(KEY_CHECK_ID_STRING, checkId);
		i.putExtra(KEY_PATH_LIST_STRING, pathListString);
		i.putExtra(KEY_IS_END, isEnd);
		i.putExtra(KEY_IS_SEQ, isSeq);
		con.startService(i);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		LogUtils.e(tag, "onCreate");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		LogUtils.e(tag, "onStart");
		super.onStart(intent, startId);

		try {
			String checkId = intent.getStringExtra(KEY_CHECK_ID_STRING);
			String pathListString = intent.getStringExtra(KEY_PATH_LIST_STRING);
			mIsEnd = intent.getBooleanExtra(KEY_IS_END, false);
			mIsSeq = intent.getBooleanExtra(KEY_IS_SEQ, false);

			LogUtils.e(tag, "pathListString=" + pathListString);

			if (pathListString != null && pathListString.length() > 1) {
				List<KoParamItem> picListData = KoDataUtil.picPathDescStringToList(pathListString);
				
				upLoadImgs(checkId, pathListString, picListData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 上传图片
	private void upLoadImgs(final String checkId, final String picPathDescList, final List<KoParamItem> picListData) {

		final AsyncTask<KoParamItem, Void, Boolean> mAsy = new AsyncTask<KoParamItem, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(KoParamItem... params) {
				int sucNum = 0;
				for (KoParamItem p : picListData) {
					if (p != null) {
						try {
							if(KoUploadImgUtil.uploadImg(p.name)) {
								sucNum++;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				return sucNum == picListData.size();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(result) {
					KolPesNetReqControl koControl = new KolPesNetReqControl(new KolPesControlBack() {
						@Override
						public void assetInsertPicPathBack(boolean isSuc, String msg) {
							KoUploadPicService.this.stopSelf();
						}
					});
					
					koControl.assetInserPicPath(checkId, KoDataUtil.picPathDescStringToStringGetFileName(picPathDescList), mIsEnd?"Y":"N", mIsSeq?"Y":"N");
				}
			}
		};
		
		Handler mHand = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				mAsy.execute();
				super.handleMessage(msg);
			}
		};
		
		mHand.sendEmptyMessageDelayed(1, 2000);
	}
	
	
}
