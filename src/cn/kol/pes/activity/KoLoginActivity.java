/*-----------------------------------------------------------

-- PURPOSE

--    登陆界面，也是该应用启动的第一个界面，只提供了一个按钮点击功能

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;


import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.Constants;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.DownLoadNewApkUtils;
import cn.kol.common.util.KoCommonUtil;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.KolApplication;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoWeekItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;

public class KoLoginActivity extends BaseActivity implements OnClickListener,OnLongClickListener {
	
	private View mLoginBtn;//登录按钮
	private boolean mIsDataEnable = false;
	private TextView mVersion;//显示版本号的控件
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_login_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_app_name);
		
		findViewById(R.id.splash_settings_btn).setOnLongClickListener(this);
		
		mLoginBtn = findViewById(R.id.splash_login_btn);
		mLoginBtn.setOnClickListener(this);
		
		mVersion = (TextView) findViewById(R.id.splash_version_text);
		
		LogUtils.e(tag, KoCommonUtil.getLanguage());
		
		showLoadingDlg(R.string.ko_tips_try_data_enable);
		mKoControl.isDataEnable();//判断服务器数据是否可用
		
		mKoControl.getUpdateApk(String.valueOf(KolApplication.VERSION_CODE));//判断是否需要升级apk
		KoPushMsgService.startSer(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		String versionS = KoDataUtil.isUseTestUrl() ? "test-"+getText(R.string.ko_title_version_code)+KolApplication.VERSION : getText(R.string.ko_title_version_code)+KolApplication.VERSION;
		mVersion.setText(versionS);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_login_btn://扫描登录
			if(mIsDataEnable) {//判断服务器数据库是否可用
				KoCaptureActivity.startActForRes(this);
			}else {
				DialogUtils.showToast(KoLoginActivity.this, R.string.ko_tips_data_enable_false);
			}
			break;
			
		case R.id.splash_settings_btn:
			SettingsActivity.startAct(this, SettingsActivity.class);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.splash_settings_btn:
			SettingsActivity.startAct(this, SettingsActivity.class);
			break;
		default:
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫码结果的回调
			String res = data.getStringExtra("res");
			LogUtils.e(tag, "onActivityResult()"+res);
			mKoControl.login(res);
			showLoadingDlg(R.string.ko_title_is_login);
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void loginBack(boolean isSuc, KoLoginAdapter userData, String msg) {//网络请求登录的回调
				if(isSuc) {
					CacheUtils.setLoginUserInfo(userData);
					KoMainActivity.startAct(KoLoginActivity.this);
					KoLoginActivity.this.finish();
				}else {
					DialogUtils.showToast(KoLoginActivity.this, msg);
				}
				dismissLoadingDlg();
			}

			@Override
			public void dateEnableBack(boolean isSuc, HashMap<String, KoWeekItem> weekMap, String msg) {//服务器数据是否可用的回调
				if(isSuc) {
					mIsDataEnable = true;
					CacheUtils.setWeekMap(weekMap);
				}else {
					mIsDataEnable = false;
					DialogUtils.showToast(KoLoginActivity.this, msg);
				}
				dismissLoadingDlg();
			}
			
			@Override
			public void getUpdateApkBack(boolean isSuc, KoUpdateApkAdapter updateData, String msg) {//apk升级的回调
				
				LogUtils.e(tag, "getUpdateApkBack:isSuc="+isSuc+updateData.isNeedUpdate());
				
				if(isSuc && updateData.isNeedUpdate()) {
					DownLoadNewApkUtils.getInstance(KoLoginActivity.this).showVersionUpdateUI(updateData);
				}
			}
		};
	}

	@Override
	public void onBackPressed() {
		CacheUtils.clearAllCache();
		super.onBackPressed();
	}

}
