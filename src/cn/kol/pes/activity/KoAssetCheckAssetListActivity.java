package cn.kol.pes.activity;

import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoAssetCheckAssetListAdapter;
import cn.kol.pes.model.item.KoAssetCheckAssetItem;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoAssetCheckAssetListActivity extends BaseActivity implements OnClickListener, IKoPickTimeDlgBack {

	private ListView mAssetListView;
	private KoAssetCheckAssetListAdapter mListAdapter;
	
	private TextView mStartView;
	private TextView mEndView;
	
	private Button mStartBtn;
	private Button mEndBtn;
	
	private Calendar mNowCal = Calendar.getInstance();
	private Calendar mStartCal = Calendar.getInstance();
	private Calendar mEndCal = Calendar.getInstance();
	
	private boolean mStartClicked = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_check_asset_list_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_select_asset_need_check_title);
		
		mStartView = (TextView) findViewById(R.id.asset_check_asset_item_start_date_text);
		mEndView = (TextView) findViewById(R.id.asset_check_asset_item_end_date_text);
		
		mStartBtn = (Button) findViewById(R.id.asset_check_asset_item_start_date_btn);
		mEndBtn = (Button) findViewById(R.id.asset_check_asset_item_end_date_btn);
		
		mStartBtn.setOnClickListener(this);
		mEndBtn.setOnClickListener(this);
		
		mAssetListView = (ListView) findViewById(R.id.asset_check_asset_list_list_view);
		mListAdapter = new KoAssetCheckAssetListAdapter(this);
		
		mAssetListView.setAdapter(mListAdapter);
		
		mStartCal.setTimeInMillis(mNowCal.getTimeInMillis()-KoDataUtil.revertDaysToMills(5));
		mEndCal.setTimeInMillis(mNowCal.getTimeInMillis()+KoDataUtil.revertDaysToMills(5));
		
		mStartView.setText(StringUtils.formatDateWithoutTime(mStartCal));
		mEndView.setText(StringUtils.formatDateWithoutTime(mEndCal));

	}
	
	@Override
	protected void onResume() {
		showLoadingDlg();
		mKoControl.assetCheckGetAssetItemList(String.valueOf(mStartCal.getTimeInMillis()), 
											  String.valueOf(mEndCal.getTimeInMillis()));
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_asset_item_start_date_btn:
			mStartClicked = true;
			KoPickTimeDlg dlgStart = new KoPickTimeDlg(this, this, mStartCal);
			dlgStart.show();
			break;
			
		case R.id.asset_check_asset_item_end_date_btn:
			mStartClicked = false;
			KoPickTimeDlg dlgEnd = new KoPickTimeDlg(this, this, mEndCal);
			dlgEnd.show();
			break;
		}
	}
	
	@Override
	public void pickTime(Calendar cal) {
		
		if(mStartClicked) {
			mStartCal.setTimeInMillis(cal.getTimeInMillis());
			mStartView.setText(StringUtils.formatDateWithoutTime(mStartCal));
			
			if(mStartCal.after(mEndCal)) {
				mEndCal.setTimeInMillis(mStartCal.getTimeInMillis()+KoDataUtil.revertDaysToMills(2));
				mEndView.setText(StringUtils.formatDateWithoutTime(mEndCal));
			}
		}
		else {
			mEndCal.setTimeInMillis(cal.getTimeInMillis());
			mEndView.setText(StringUtils.formatDateWithoutTime(mEndCal));
			
			if(mEndCal.before(mStartCal)) {
				mStartCal.setTimeInMillis(mEndCal.getTimeInMillis()-KoDataUtil.revertDaysToMills(2));
				mStartView.setText(StringUtils.formatDateWithoutTime(mStartCal));
			}
		}
		
		showLoadingDlg();
		mKoControl.assetCheckGetAssetItemList(String.valueOf(mStartCal.getTimeInMillis()), 
											  String.valueOf(mEndCal.getTimeInMillis()));
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			@Override
			public void assetCheckAssetListBack(boolean isSuc, List<KoAssetCheckAssetItem> assetList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					mListAdapter.setData(assetList);
					mListAdapter.notifyDataSetChanged();
					
					//mListParentLayout.setVisibility(View.VISIBLE);
					//mErrorMsgView.setVisibility(View.GONE);
				}
				else {
					//mListParentLayout.setVisibility(View.GONE);
					//mErrorMsgView.setVisibility(View.VISIBLE);
					//mErrorMsgView.setText(msg);
				}
			}
			
		};
	}



}
