package cn.kol.pes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;


public class KoAssetMainActivity extends BaseActivity implements OnClickListener {
	
	private KoAssetGetAssetInfoAdapter mKoAssetGetAssetInfoAdapter;
	private TextView mAssetNumTextView;
	
	private enum ScanStatus {
		ScanAsset,//点检设备的扫入设备
		CancelCheckAsset//取消点检设备的扫描
	}
	
	private ScanStatus mStatus = ScanStatus.ScanAsset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_main_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_asset_check);
		mAssetNumTextView = (TextView) findViewById(R.id.main_asset_asset_text);
		
		findViewById(R.id.main_asset_camera_btn_asset).setOnClickListener(this);
		findViewById(R.id.main_asset_cancel_check_asset_btn).setOnClickListener(this);
		
		findViewById(R.id.main_asset_check_asset_btn).setOnClickListener(this);
		findViewById(R.id.main_asset_manager_check_btn).setOnClickListener(this);
		findViewById(R.id.main_asset_asset_list_btn).setOnClickListener(this);
		findViewById(R.id.main_asset_failure_history_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.main_asset_camera_btn_asset:
			mStatus = ScanStatus.ScanAsset;
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.main_asset_cancel_check_asset_btn:
			mStatus = ScanStatus.CancelCheckAsset;
			KoCaptureActivity.startActForRes(this);
			break;
		
		case R.id.main_asset_check_asset_btn:
			if(mKoAssetGetAssetInfoAdapter != null) {
				KoAssetCheckActivity.startAct(this, mKoAssetGetAssetInfoAdapter.assetId, mKoAssetGetAssetInfoAdapter.description);
			}
			else {
				DialogUtils.showToast(this, R.string.ko_tips_scan_asset_first);
			}
			break;
			
		case R.id.main_asset_manager_check_btn:
			
			break;
			
		case R.id.main_asset_asset_list_btn:
			KoAssetCheckAssetListActivity.startAct(this, KoAssetCheckAssetListActivity.class);
			break;
			
		case R.id.main_asset_failure_history_btn:
			KoAssetListActivity.startAct(this);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			mKoAssetGetAssetInfoAdapter = null;
			
			if(dataS!=null && dataS.length()>1) {
				showLoadingDlg();
				if(mStatus == ScanStatus.ScanAsset) {
					mKoControl.assetCheckGetAssetInfo(dataS);
				}
				else if(mStatus == ScanStatus.CancelCheckAsset) {
					mKoControl.cancelCheckAsset(dataS);
				}
			}
			else {
				mAssetNumTextView.setText(null);
				DialogUtils.showToast(this, R.string.ko_tips_wrong_asset_num);
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected KolPesControlBack initControlBack() {
		
		return new KolPesControlBack() {

			//获取设备相关信息的回调
			@Override
			public void assetCheckGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter) {
				if(isSuc) {
					mKoAssetGetAssetInfoAdapter = adapter;
					mAssetNumTextView.setText(mKoAssetGetAssetInfoAdapter.tagNumber+" "+mKoAssetGetAssetInfoAdapter.description);
				}else {
					mKoAssetGetAssetInfoAdapter = null;
					mAssetNumTextView.setText(null);
					DialogUtils.showToast(KoAssetMainActivity.this, msg);
				}
				
				dismissLoadingDlg();
			}

			@Override
			public void assetCheckCancelCheckAssetBack(boolean isSuc, String msg) {
				DialogUtils.showToast(KoAssetMainActivity.this, msg);
				dismissLoadingDlg();
			}
		};
	}

}
