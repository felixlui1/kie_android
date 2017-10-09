/*-----------------------------------------------------------

-- PURPOSE

--   已启动工序列表的Adapter。

-- History

--	  1-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoOpStartedItem;

public class KoOpStartedListAdapter extends BaseAdapter {
	
	private LayoutInflater mLi;
	private List<KoOpStartedItem> mOpListData;
	
	public KoOpStartedListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
	}
	
	public void setData(List<KoOpStartedItem> assetList) {
		mOpListData = assetList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mOpListData != null) {
			return mOpListData.size();
		}
		return 0;
	}

	@Override
	public KoOpStartedItem getItem(int position) {
		if(mOpListData != null) {
			return mOpListData.get(position);
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
			convertView = mLi.inflate(R.layout.ko_op_started_list_item_layout, null);
		}
		
		KoOpStartedItem opData = getItem(position);
		
		if(opData != null) {
			TextView opCode = (TextView) convertView.findViewById(R.id.op_started_item_op_code);
			TextView opDesc = (TextView) convertView.findViewById(R.id.op_started_item_op_desc);
			
			TextView wipName = (TextView) convertView.findViewById(R.id.op_started_item_wip_name);
			
			TextView startDate = (TextView) convertView.findViewById(R.id.op_started_item_op_start_date);
			
			TextView lastUpdateDateStaff = (TextView) convertView.findViewById(R.id.op_started_item_last_update_time_staff);
			
			TextView trxQuan = (TextView) convertView.findViewById(R.id.op_started_item_trx_quantity);
			TextView assetInfo = (TextView) convertView.findViewById(R.id.op_started_item_asset_info);
			
			Button pctBtn = (Button) convertView.findViewById(R.id.op_started_item_pct_button);
			pctBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
			
			opCode.setText(opData.fmOperationCode);
			opDesc.setText(opData.opDesc);
			wipName.setText(opData.wipEntityName);
			startDate.setText(opData.opStartDate);
			
			lastUpdateDateStaff.setText(opData.lastUpdateBy+" * "+opData.lastUpdateDate);
			
			trxQuan.setText(opData.trxQuantity);
			assetInfo.setText(opData.assettagNumber+" "+opData.assetDesc);
		}
		
		return convertView;
	}

}
