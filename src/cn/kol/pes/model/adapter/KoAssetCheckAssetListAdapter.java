/*-----------------------------------------------------------

-- PURPOSE

--   选择点检设备列表的Adapter。

-- History

--	  26-Feb-16  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.activity.KoAssetCheckSetCheckAssetActivity;
import cn.kol.pes.model.item.KoAssetCheckAssetItem;

public class KoAssetCheckAssetListAdapter extends BaseAdapter implements View.OnClickListener {
	
	private LayoutInflater mLi;
	private List<KoAssetCheckAssetItem> mAssetListData;
	private Context mContext;

	public KoAssetCheckAssetListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
		mContext = context;
	}
	
	public void setData(List<KoAssetCheckAssetItem> assetList) {
		mAssetListData = assetList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mAssetListData != null) {
			return mAssetListData.size();
		}
		return 0;
	}

	@Override
	public KoAssetCheckAssetItem getItem(int position) {
		if(mAssetListData != null) {
			return mAssetListData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLi.inflate(R.layout.ko_asset_check_asset_list_item_layout, null);
		}
		
		KoAssetCheckAssetItem asset = getItem(position);
		
		if(asset != null) {
			TextView dateView = (TextView) convertView.findViewById(R.id.asset_check_asset_item_date);
			TextView lightInfoView = (TextView) convertView.findViewById(R.id.asset_check_asset_item_light_info);
			TextView nightInfoView = (TextView) convertView.findViewById(R.id.asset_check_asset_item_night_info);

			lightInfoView.setOnClickListener(this);
			nightInfoView.setOnClickListener(this);
			
			lightInfoView.setTag(asset);
			nightInfoView.setTag(asset);
			
			dateView.setText(asset.checkDate);
			lightInfoView.setText(mContext.getText(R.string.ko_value_day_work_and_asset_number)+String.valueOf(asset.assetTotalNum)+mContext.getText(R.string.ko_value_asset_need_check)+String.valueOf(asset.assetNeedCheckNumLight)+mContext.getText(R.string.ko_value_asset_already_checked)+String.valueOf(asset.assetCheckedNumLight));
			nightInfoView.setText(mContext.getText(R.string.ko_value_night_work_and_asset_number)+String.valueOf(asset.assetTotalNum)+mContext.getText(R.string.ko_value_asset_need_check)+String.valueOf(asset.assetNeedCheckNumNight)+mContext.getText(R.string.ko_value_asset_already_checked)+String.valueOf(asset.assetCheckedNumNight));
		}
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_asset_item_light_info:
			KoAssetCheckAssetItem assetL = (KoAssetCheckAssetItem) v.getTag();
			KoAssetCheckSetCheckAssetActivity.startAct(mContext, assetL.checkDate, "1");
			break;
			
		case R.id.asset_check_asset_item_night_info:
			KoAssetCheckAssetItem assetN = (KoAssetCheckAssetItem) v.getTag();
			KoAssetCheckSetCheckAssetActivity.startAct(mContext, assetN.checkDate, "2");
			break;
		}
	}

}
