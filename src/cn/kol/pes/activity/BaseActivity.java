/*-----------------------------------------------------------

-- PURPOSE

--    是所有界面Activity的父类，提供显示滚动条、消除滚动条、实例化KolPesNetReqControl、实例化标题View等每个界面都会用到的功能

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.KolApplication;
import cn.kol.pes.R;
import cn.kol.pes.controller.KolPesNetReqControl;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.widget.KoPageTitleLayoutView;

public abstract class BaseActivity extends Activity {
	
	protected final String tag =  this.getClass().getSimpleName();
	
	protected ProgressDialog  mDialog;
	protected KolApplication mApp;
	
	protected KoPageTitleLayoutView mTitleView;
	
	private Dialog mLoadingDlg;
	
	protected KolPesNetReqControl mKoControl;
	
	abstract protected KolPesControlBack initControlBack();
	
	public static void startAct(Context context, Class<?> c) {
		Intent i = new Intent(context, c);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		mApp = (KolApplication)getApplication();
			
		mTitleView = (KoPageTitleLayoutView) findViewById(R.id.sd_common_title_view);
		
		mKoControl = new KolPesNetReqControl(initControlBack());
		
		super.onCreate(savedInstanceState);
	}
	
	protected void showLoadingDlg() {//显示加载对话框
		if(mLoadingDlg==null || !mLoadingDlg.isShowing()) {
			mLoadingDlg = DialogUtils.getDialog(this);
			mLoadingDlg.show();
		}
	}
	
	protected void showLoadingDlg(int msgId) {//显示自定义信息的加载对话框
		showLoadingDlg(getString(msgId));
	}
	
	protected void showLoadingDlg(String msg) {//显示自定义信息的加载对话框
		if(mLoadingDlg==null || !mLoadingDlg.isShowing()) {
			mLoadingDlg = DialogUtils.getDialog(this);
			TextView msgView = (TextView) mLoadingDlg.findViewById(R.id.loading_text);
			msgView.setText(msg);
			mLoadingDlg.show();
		}
		else if(mLoadingDlg.isShowing()) {
			TextView msgView = (TextView) mLoadingDlg.findViewById(R.id.loading_text);
			msgView.setText(msg);
		}
	}
	
	protected void dismissLoadingDlg() {//关掉加载对话框
		if(mLoadingDlg!=null && mLoadingDlg.isShowing()) {
			mLoadingDlg.dismiss();
		}
	}

}
