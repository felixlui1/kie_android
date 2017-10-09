/*-----------------------------------------------------------

-- PURPOSE

--    封装的开始工序界面的设备view

-- History

--	  03-Jan-15  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoAssetCheckItem;

public class KoOpStartAssetView extends LinearLayout {
	
	public interface IKoOpStartAssetBack {
		public void scanAssetClick(KoOpStartAssetView assetView);
	}
	

	private KoAssetCheckItem mAssetSelected = null;
	private List<KoAssetCheckItem> mOpAssetList;
	private TextView mAssetNoTextView;
	private Context mCon;
	
	public KoOpStartAssetView(Context context, List<KoAssetCheckItem> opAssetList, final IKoOpStartAssetBack ioOpStartAssetBack) {
		super(context, null);
		mOpAssetList = opAssetList;
		mCon = context;
		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_op_start_asset_layout, this);
		mAssetNoTextView = (TextView) rootView.findViewById(R.id.op_start_start_equip_no);
		
		rootView.findViewById(R.id.op_start_camera_btn_equip_no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ioOpStartAssetBack.scanAssetClick(KoOpStartAssetView.this);
			}
		});
	}
	
	public void setAssetTagNum(String tagNum) {
		boolean isContains = false;
		for(KoAssetCheckItem asset : mOpAssetList) {
			if(asset!=null && asset.assetTagNum!=null && asset.assetTagNum.equals(tagNum)) {
				mAssetSelected = asset;
				isContains = true;
				break;
			}
		}
		
		if(isContains) {
			mAssetNoTextView.setText(mAssetSelected.assetTagNum+" "+mAssetSelected.assetOpDscr);
		}else {
			mAssetSelected = null;
			mAssetNoTextView.setText("");
			DialogUtils.showToast(mCon, R.string.ko_tips_asset_not_exists_or_not_belong_to_this_op);
		}
	}
	
	public String getAssetId() {
		return mAssetSelected.assetId;
	}
	
	public boolean isAssetInputedOk() {
		if(mAssetSelected!=null && mAssetSelected.assetTagNum!=null && mAssetSelected.assetTagNum.length()>1) {
			return true;
		}
		return false;
	}
	
}
