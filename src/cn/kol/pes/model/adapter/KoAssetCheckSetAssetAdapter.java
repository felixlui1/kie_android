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
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.widget.koCheckBoxView;

public class KoAssetCheckSetAssetAdapter extends BaseAdapter implements View.OnClickListener {
	
	private LayoutInflater mLi;
	private List<KoAssetCheckSetAssetCheckItem> mAssetListData;

	public KoAssetCheckSetAssetAdapter(Context context) {
		mLi = LayoutInflater.from(context);
	}
	
	public void setData(List<KoAssetCheckSetAssetCheckItem> assetList) {
		mAssetListData = assetList;
		this.notifyDataSetChanged();
	}
	
	public List<KoAssetCheckSetAssetCheckItem> getAssetListData() {
		return this.mAssetListData;
	}
	
	public void setAssetAllSelected() {
		for (KoAssetCheckSetAssetCheckItem assetItem : mAssetListData) {
			assetItem.scheduledId = String.valueOf(assetItem.assetId);
		}
		this.notifyDataSetChanged();
	}
	
	public void setAssetAllUnselected() {
		for (KoAssetCheckSetAssetCheckItem assetItem : mAssetListData) {
			if(KoDataUtil.isStringNotNull(assetItem.checkedId) && !"0".equals(assetItem.checkedId)) {
				
			}
			else {
				assetItem.scheduledId = "0";
			}
		}
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
	public KoAssetCheckSetAssetCheckItem getItem(int position) {
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
			convertView = mLi.inflate(R.layout.ko_asset_check_set_asset_check_item_layout, null);
		}
		
		KoAssetCheckSetAssetCheckItem asset = getItem(position);
		
		if(asset != null) {
			TextView assetNameView = (TextView) convertView.findViewById(R.id.asset_check_set_asset_check_asset_name);
			TextView assetTagView = (TextView) convertView.findViewById(R.id.asset_check_set_asset_check_asset_tag);
			//TextView assetFloorView = (TextView) convertView.findViewById(R.id.asset_check_set_asset_check_asset_floor);
			
			TextView assetStatus = (TextView) convertView.findViewById(R.id.asset_check_set_asset_check_status);
			
			koCheckBoxView checkBox = (koCheckBoxView) convertView.findViewById(R.id.asset_check_set_asset_check_asset_check_box);
			checkBox.setOnClickListener(this);
			
			assetNameView.setText(asset.assetDescription);
			assetTagView.setText(asset.assetTagNum);
			//assetFloorView.setText("楼层：2F");
			
			LogUtils.e("KoAssetCheckSetAssetAdapter", "asset.scheduledId="+asset.scheduledId);
			checkBox.setChecked(KoDataUtil.isStringNotNull(asset.scheduledId) && !"0".equals(asset.scheduledId));
			checkBox.setTag(asset);
			
//			if(KoDataUtil.isStringNotNull(asset.checkedId) && !"0".equals(asset.checkedId)) {
//				assetStatus.setText("已点检");
//				checkBox.setClickable(false);
//			}
//			else {
				assetStatus.setText(null);
				checkBox.setClickable(true);
//			}
		}
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_set_asset_check_asset_check_box:
			
			KoAssetCheckSetAssetCheckItem asset = (KoAssetCheckSetAssetCheckItem) v.getTag();
			
			if(KoDataUtil.isStringNotNull(asset.scheduledId) && !"0".equals(asset.scheduledId)) {
				asset.scheduledId = "0";
			}
			else {
				asset.scheduledId = String.valueOf(asset.assetId);
			}
			this.notifyDataSetChanged();
			break;
		}
	}

}
