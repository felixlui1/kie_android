/*-----------------------------------------------------------

-- PURPOSE

--    点检设备列表的界面

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoAssetListAdapter;
import cn.kol.pes.model.item.KoAssetCheckItem;


public class KoAssetListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private ListView mAssetListView;//设备列表的view
	private KoAssetListAdapter mListAdapter;//设备列表的数据适配器
	
	private ViewGroup mListParentLayout;//列表的父容器
	private TextView mErrorMsgView;//错误信息的view
	
	public static void startAct(Context context) {
		Intent i = new Intent(context, KoAssetListActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_list_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_check_asset_error_list);
		
		mAssetListView = (ListView) findViewById(R.id.asset_list_list_view);
		mListAdapter = new KoAssetListAdapter(this);
		mAssetListView.setAdapter(mListAdapter);
		mAssetListView.setOnItemClickListener(this);
		
		mListParentLayout = (ViewGroup) findViewById(R.id.asset_list_parent_layout);
		mErrorMsgView = (TextView) findViewById(R.id.asset_list_error_msg_text_view);
		
		mListParentLayout.setVisibility(View.GONE);
		mErrorMsgView.setVisibility(View.GONE);
		
		findViewById(R.id.asset_list_add_new_btn).setOnClickListener(this);//添加新设备按钮的点击事件
		
		mKoControl.getAssetCheckErrorList();//获取故障的设备列表
		showLoadingDlg();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.asset_list_add_new_btn://添加新设备
			KoAssetAddActivity.startAct(this, KoAssetAddActivity.class);
			this.finish();
			break;
			
		default:
			break;
		}
	}
	
	//点了设备列表的某一项的回调
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		KoAssetCheckItem checkItem = (KoAssetCheckItem) arg0.getAdapter().getItem(arg2);
		if(checkItem != null) {
			CacheUtils.setSelectedAssetCheckItem(checkItem);
			KoAssetRepairActivity.startAct(KoAssetListActivity.this, String.valueOf(checkItem.checkId));
			KoAssetListActivity.this.finish();
		}
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			//获取设备点检数据的回调
			@Override
			public void assetCheckGetErrorListBack(boolean isSuc, String msg, List<KoAssetCheckItem> assetList) {
				dismissLoadingDlg();
				if(isSuc) {
					mListAdapter.setData(assetList);
					mListAdapter.notifyDataSetChanged();
					
					mListParentLayout.setVisibility(View.VISIBLE);
					mErrorMsgView.setVisibility(View.GONE);
				}
				else {
					mListParentLayout.setVisibility(View.GONE);
					mErrorMsgView.setVisibility(View.VISIBLE);
					mErrorMsgView.setText(msg);
				}
			}
			
		};
	}

}
