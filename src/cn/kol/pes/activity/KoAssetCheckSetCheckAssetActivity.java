package cn.kol.pes.activity;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoAssetCheckSetAssetAdapter;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;

public class KoAssetCheckSetCheckAssetActivity extends BaseActivity implements OnClickListener {

	private ListView mListView;
	private KoAssetCheckSetAssetAdapter mListAdapter;
	
	private Button mCheckBoxSelectAll;
	private Button mCheckBoxUnselectAll;
	
	private static final String KEY_CHECK_DATE = "key_check_date";
	private static final String KEY_SHIFT = "key_shift";

	public static void startAct(Context context, String checkDateLong, String shift) {
		Intent i = new Intent(context, KoAssetCheckSetCheckAssetActivity.class);
		i.putExtra(KEY_CHECK_DATE, checkDateLong);
		i.putExtra(KEY_SHIFT, shift);
		context.startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_check_set_asset_activity);
		super.onCreate(savedInstanceState);
		
		mCheckBoxSelectAll = (Button) findViewById(R.id.asset_check_set_asset_select_all);
		mCheckBoxUnselectAll = (Button) findViewById(R.id.asset_check_set_asset_unselect_all);
		
		mCheckBoxSelectAll.setText(R.string.ko_title_asset_select_all);
		mCheckBoxUnselectAll.setText(R.string.ko_title_asset_unselect_all);
		
		mCheckBoxSelectAll.setOnClickListener(this);
		mCheckBoxUnselectAll.setOnClickListener(this);
		
		findViewById(R.id.asset_check_set_asset_submit_btn).setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.asset_check_set_asset_list_view);
		mListAdapter = new KoAssetCheckSetAssetAdapter(this);
		
		mListView.setAdapter(mListAdapter);
		
		showLoadingDlg();
		
		Calendar checkDate = KoDataUtil.convertDateStringToCal(getIntent().getStringExtra(KEY_CHECK_DATE));
		LogUtils.e(tag, "checkDate"+StringUtils.formatDateTime(checkDate));
		mKoControl.assetCheckGetSetAssetCheckList(String.valueOf(checkDate.getTimeInMillis()), getIntent().getStringExtra(KEY_SHIFT), CacheUtils.getLoginUserInfo().staffNo);
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_set_asset_select_all:
			mListAdapter.setAssetAllSelected();
			break;
			
		case R.id.asset_check_set_asset_unselect_all:
			mListAdapter.setAssetAllUnselected();
			break;
			
		case R.id.asset_check_set_asset_submit_btn:
			showLoadingDlg();
			Calendar checkDate = KoDataUtil.convertDateStringToCal(getIntent().getStringExtra(KEY_CHECK_DATE));
			
			mKoControl.assetCheckUpdateSetAssetCheck(String.valueOf(checkDate.getTimeInMillis()), 
													 getIntent().getStringExtra(KEY_SHIFT), 
													 mListAdapter.getAssetListData());
			break;
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			@Override
			public void assetCheckGetSetAssetCheckListBack(boolean isSuc, 
														   List<KoAssetCheckSetAssetCheckItem> assetList,
														   String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mListAdapter.setData(assetList);
				}
				else {
					DialogUtils.showToast(KoAssetCheckSetCheckAssetActivity.this, msg);
				}
			}

			@Override
			public void assetCheckUpdateSetAssetCheckListBack(boolean isSuc, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					DialogUtils.showToast(KoAssetCheckSetCheckAssetActivity.this, "提交成功");
					KoAssetCheckSetCheckAssetActivity.this.finish();
				}
				else {
					DialogUtils.showToast(KoAssetCheckSetCheckAssetActivity.this, msg);
				}
			}
			
		};
	}

}
