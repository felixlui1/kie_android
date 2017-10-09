/*-----------------------------------------------------------

-- PURPOSE

--   设备点检列表的Adapter。

-- History

--	  09-Sep-14  LiZheng  Created.

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
import cn.kol.pes.model.item.KoAssetCheckItem;

public class KoAssetListAdapter extends BaseAdapter {
	
	private LayoutInflater mLi;
	private List<KoAssetCheckItem> mAssetListData;
	private String mCheckIdLable = "";
	
	public KoAssetListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
		mCheckIdLable = context.getString(R.string.ko_title_asset_check_id);
	}
	
	public void setData(List<KoAssetCheckItem> assetList) {
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
	public KoAssetCheckItem getItem(int position) {
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
			convertView = mLi.inflate(R.layout.ko_asset_list_item_layout, null);
		}
		
		KoAssetCheckItem asset = getItem(position);
		
		if(asset != null) {
			TextView assetName = (TextView) convertView.findViewById(R.id.asset_item_asset_name);
			TextView staff = (TextView) convertView.findViewById(R.id.asset_item_check_staff);
			TextView checkTime = (TextView) convertView.findViewById(R.id.asset_item_check_time);
			TextView exFixTime = (TextView) convertView.findViewById(R.id.asset_item_expect_fix_time);
			TextView exCompletedTime = (TextView) convertView.findViewById(R.id.asset_item_expect_completed_time);
			
			assetName.setText(asset.assetName+" ( "+mCheckIdLable+asset.assetTagNum+" ) ");
			staff.setText(String.valueOf(asset.createdBy)+"-"+asset.createdByName);
			checkTime.setText(asset.checkTime);
			exFixTime.setText(asset.estRepairStart);
			exCompletedTime.setText(asset.estRepairEnd);
		}
		
		return convertView;
	}

}
