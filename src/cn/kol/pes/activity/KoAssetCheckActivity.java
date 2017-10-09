package cn.kol.pes.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.widget.KoAssetCheckItemView;

public class KoAssetCheckActivity extends BaseActivity implements OnClickListener {

	private ViewGroup mParentView;
	
	private static final String KEY_ASSET_ID = "key_asset_id";
	private static final String KEY_ASSET_NAME = "key_asset_name";
	
	private TextView mAssetNameView;
	private Button mSubmitCheckBtn;
	
	private TextView mLastCheckTimeView;
	
	public static void startAct(Context context, String assetId, String assetName) {
		Intent i = new Intent(context, KoAssetCheckActivity.class);
		i.putExtra(KEY_ASSET_ID, assetId);
		i.putExtra(KEY_ASSET_NAME, assetName);
		context.startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_check_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_asset_check_item_title);
		
		mLastCheckTimeView = (TextView)findViewById(R.id.asset_check_last_check_time_view);
		
		mParentView = (ViewGroup) findViewById(R.id.asset_check_add_check_parent_layout);
		mSubmitCheckBtn = (Button) findViewById(R.id.asset_check_add_check_btn);
		mSubmitCheckBtn.setOnClickListener(this);
		
		mAssetNameView = (TextView) findViewById(R.id.asset_check_asset_name_text);
		
		showLoadingDlg();
		mKoControl.getCheckItemList(getIntent().getStringExtra(KEY_ASSET_ID));
		mAssetNameView.setText(getIntent().getStringExtra(KEY_ASSET_NAME));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_add_check_btn:
			if(isFilledCheckListOk()) {
				showLoadingDlg();
				KoLoginAdapter staff = CacheUtils.getLoginUserInfo();
				mKoControl.assetCheckSubmitCheck(getIntent().getStringExtra(KEY_ASSET_ID), staff.staffNo, 
												 getFilledCheckList());
			}
			break;
		}
	}
	
	private boolean isFilledCheckListOk() {
		int filledCount = 0;
		for(int i=0; i<mParentView.getChildCount(); i++) {
			KoAssetCheckItemView checkItemV = (KoAssetCheckItemView) mParentView.getChildAt(i);
			if(!checkItemV.isInputedValueOk()) {
				return false;
			}
			if(checkItemV.isInputedValue()) {
				filledCount++;
			}
		}
		
		if(filledCount>0) {
			return true;
		}
		else {
			DialogUtils.showToast(this, "您没有填写任何点检问题！");
			return false;
		}
	}
	
	private List<KoAssetCheckCheckItem> getFilledCheckList() {
		List<KoAssetCheckCheckItem> checkList = new ArrayList<KoAssetCheckCheckItem>();
		for(int i=0; i<mParentView.getChildCount(); i++) {
			KoAssetCheckItemView checkItemV = (KoAssetCheckItemView) mParentView.getChildAt(i);
			if(checkItemV.isInputedValue()) {
				checkList.add(checkItemV.getCheckValueItem());
			}
		}
		return checkList;
	}

	@Override
	protected KolPesControlBack initControlBack() {
		
		return new KolPesControlBack() {

			@Override
			public void getCheckItemListDataBack(boolean isSuc, List<KoAssetCheckCheckItem> checkList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					for (KoAssetCheckCheckItem item : checkList) {
						KoAssetCheckItemView checkItemV = new KoAssetCheckItemView(KoAssetCheckActivity.this, item);
						mParentView.addView(checkItemV);
					}
					mLastCheckTimeView.setText(msg);
					mSubmitCheckBtn.setVisibility(View.VISIBLE);
				}
				else {
					mSubmitCheckBtn.setVisibility(View.GONE);
					DialogUtils.showToast(KoAssetCheckActivity.this, msg);
				}
			}

			@Override
			public void assetCheckSubmitCheckBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					DialogUtils.showToast(KoAssetCheckActivity.this, R.string.ko_tips_submit_success);
					KoAssetCheckActivity.this.finish();
				}
				else {
					DialogUtils.showToast(KoAssetCheckActivity.this, msg);
				}
			}
		};
	}

}
