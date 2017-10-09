/*-----------------------------------------------------------

-- PURPOSE

--    查询工单界面中，展示工序信息的子View

-- History

--	  15-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoOpStartedItem;

public class KoJobViewOpInfoItemView extends LinearLayout {

	private KoOpStartedItem mData;
	private View mLineView;
	
	public KoJobViewOpInfoItemView(Context context) {
		this(context, null, null, null);
	}
	
	public KoJobViewOpInfoItemView(Context context, KoOpStartedItem opData, View.OnClickListener click, View.OnLongClickListener longClick) {
		super(context, null);

		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_job_view_op_info_item_layout, this);


		TextView opCode = (TextView) rootView.findViewById(R.id.job_view_op_info_item_op_code);
		TextView opDesc = (TextView) rootView.findViewById(R.id.job_view_op_info_item_op_desc);
		
		TextView wipName = (TextView) rootView.findViewById(R.id.job_view_op_info_item_wip_name);
		
		TextView startDate = (TextView) rootView.findViewById(R.id.job_view_op_info_op_start_date);
		TextView endDate = (TextView) rootView.findViewById(R.id.job_view_op_info_item_op_end_date);
		
		TextView lastUpdateDateStaff = (TextView) rootView.findViewById(R.id.job_view_op_info_item_last_update_time_staff);
		
		TextView trxQuan = (TextView) rootView.findViewById(R.id.job_view_op_info_item_trx_quantity);
		TextView scrapQuan = (TextView) rootView.findViewById(R.id.job_view_op_info_item_scrap_quantity);
		TextView assetInfo = (TextView) rootView.findViewById(R.id.job_view_op_info_item_asset_info);
		
		mLineView = findViewById(R.id.job_view_op_info_item_line);
		
		mData = opData;
		this.setTag(opData);
		
		
		if(opData!=null) {
			opCode.setText(opData.fmOperationCode);
			opDesc.setText(opData.opDesc);
			wipName.setText(opData.wipEntityName);
			startDate.setText(opData.opStartDate);
			
			if(opData.opEndDate!=null && opData.opEndDate.length()>1) {
				endDate.setText(opData.opEndDate);
				scrapQuan.setText(opData.scrapQuantity);
			}else {
				endDate.setText(R.string.ko_title_uncomplete);
				scrapQuan.setText(R.string.ko_title_unfill);
				endDate.setTextColor(context.getResources().getColor(R.color.sd_color_red));
				this.setOnClickListener(click);
			}
			
			this.setOnLongClickListener(longClick);
			
			lastUpdateDateStaff.setText(opData.lastUpdateBy+" * "+opData.lastUpdateDate);
			trxQuan.setText(opData.trxQuantity);
			assetInfo.setText(opData.assettagNumber+" "+opData.assetDesc);
		}
		
	}
	
	public void setLineShow(boolean isShow) {
		mLineView.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	public KoOpStartedItem getData() {
		return mData;
	}
	
}
