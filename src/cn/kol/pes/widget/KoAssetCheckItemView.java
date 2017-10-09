package cn.kol.pes.widget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.item.KoCleanLevelItem;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoAssetCheckItemView extends LinearLayout implements OnClickListener {

	private TextView mTitleView;
	
	private ViewGroup mInfoViewLayout;
	private TextView mInfoView;
	private TextView mChkCycleView;
	private Button mBtnText;
	
	private ViewGroup mInfoEditLayout;
	private EditText mInfoEdit;
	private Button mBtnEdit;
	
	private ViewGroup mAnomalyLayout;
	private koCheckBoxView mAnomalyCheckY;
	private koCheckBoxView mAnomalyCheckN;
	
	private ViewGroup mBoolLayout;
	private koCheckBoxView mBoolCheckY;
	private koCheckBoxView mBoolCheckN;
	
	private Calendar mSelectedTime = Calendar.getInstance();
	
	private Activity mContext;
	
	private KoCleanLevelItem mSelectedCleanLevel = null;
	
	private KoAssetCheckCheckItem mCheckItem;
	
	public KoAssetCheckItemView(Activity context, KoAssetCheckCheckItem checkItem) {
		
		super(context, null);
		this.mContext = context;
		this.mCheckItem = checkItem;
		
		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_asset_check_item_view, this);
		
		mTitleView = (TextView) rootView.findViewById(R.id.asset_check_item_title_view);
		
		mInfoViewLayout = (ViewGroup) rootView.findViewById(R.id.asset_check_item_scan_text_layout);
		mInfoView = (TextView) rootView.findViewById(R.id.asset_check_item_info_text);
		mInfoView.setOnClickListener(this);
		mChkCycleView = (TextView) rootView.findViewById(R.id.asset_check_item_title_chk_view);
		mBtnText = (Button) rootView.findViewById(R.id.asset_check_item_text_btn);
		mBtnText.setOnClickListener(this);
		
		mInfoEditLayout = (ViewGroup) rootView.findViewById(R.id.asset_check_item_scan_edit_layout);
		mInfoEdit = (EditText) rootView.findViewById(R.id.asset_check_item_info_edit);
		mBtnEdit = (Button) rootView.findViewById(R.id.asset_check_item_edit_btn);
		mBtnEdit.setOnClickListener(this);
		
		mAnomalyLayout = (ViewGroup) rootView.findViewById(R.id.asset_check_item_anomaly_layout);
		mAnomalyCheckY = (koCheckBoxView) rootView.findViewById(R.id.asset_check_item_anomaly_y);
		mAnomalyCheckN = (koCheckBoxView) rootView.findViewById(R.id.asset_check_item_anomaly_n);
		mAnomalyCheckY.setOnClickListener(this);
		mAnomalyCheckN.setOnClickListener(this);
		mAnomalyCheckY.setText(context.getString(R.string.ko_title_pass));
		mAnomalyCheckN.setText(context.getString(R.string.ko_title_fail));
		
		mBoolLayout = (ViewGroup) rootView.findViewById(R.id.asset_check_item_bool_layout);
		mBoolCheckY = (koCheckBoxView) rootView.findViewById(R.id.asset_check_item_bool_y);
		mBoolCheckN = (koCheckBoxView) rootView.findViewById(R.id.asset_check_item_bool_n);
		mBoolCheckY.setOnClickListener(this);
		mBoolCheckN.setOnClickListener(this);
		mBoolCheckY.setText(context.getString(R.string.ko_title_y));
		mBoolCheckN.setText(context.getString(R.string.ko_title_n));
		
		initView(checkItem.queryType, checkItem.checkResult);
		
		mTitleView.setText(checkItem.queryText);
		
		if("12".equals(checkItem.chkCycle)) {
			mChkCycleView.setText(null);
			mChkCycleView.setVisibility(View.GONE);
		}
		else {
			mChkCycleView.setText("点检周期:"+checkItem.chkCycle+"小时"+",上次点检时间:"+checkItem.lastChkTime);
			mChkCycleView.setVisibility(View.VISIBLE);
		}
	}
	
//	public KoAssetCheckItemView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		
//	}
	
	private void initView(String dataType, String historyResult) {
		if("numeric".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(GONE);
			mInfoEditLayout.setVisibility(VISIBLE);
			mAnomalyLayout.setVisibility(GONE);
			mBoolLayout.setVisibility(GONE);
			
			mInfoEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			mBtnEdit.setVisibility(GONE);
			
			mInfoEdit.setText(historyResult);
		}
		else if("clean_level".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(VISIBLE);
			mInfoEditLayout.setVisibility(GONE);
			mAnomalyLayout.setVisibility(GONE);
			mBoolLayout.setVisibility(GONE);
			
			mBtnText.setVisibility(GONE);
			
			
			
			KoCleanLevelItem levelData = getCleanLevelDataByValue(historyResult);
			if(levelData != null) {
				mInfoView.setText(levelData.name);
			}
			mSelectedCleanLevel = levelData;
		}
		else if("anomaly".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(GONE);
			mInfoEditLayout.setVisibility(GONE);
			mAnomalyLayout.setVisibility(VISIBLE);
			mBoolLayout.setVisibility(GONE);
			
			if("1".equals(historyResult)) {
				mAnomalyCheckY.setChecked(true);
				mAnomalyCheckN.setChecked(false);
				this.mCheckItem.checkResult = "1";
			}
			else if("2".equals(historyResult)) {
				mAnomalyCheckY.setChecked(false);
				mAnomalyCheckN.setChecked(true);
				this.mCheckItem.checkResult = "2";
			}
			else {
				mAnomalyCheckY.setChecked(false);
				mAnomalyCheckN.setChecked(false);
				this.mCheckItem.checkResult = null;
			}
		}
		else if("boolean".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(GONE);
			mInfoEditLayout.setVisibility(GONE);
			mAnomalyLayout.setVisibility(GONE);
			mBoolLayout.setVisibility(VISIBLE);
			
			if("Y".equals(historyResult)) {
				mBoolCheckY.setChecked(true);
				mBoolCheckN.setChecked(false);
				this.mCheckItem.checkResult = "Y";
			}
			else if("N".equals(historyResult)) {
				mBoolCheckY.setChecked(false);
				mBoolCheckN.setChecked(true);
				this.mCheckItem.checkResult = "N";
			}
			else {
				mBoolCheckY.setChecked(false);
				mBoolCheckN.setChecked(false);
				this.mCheckItem.checkResult = null;
			}
		}
		else if("time".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(VISIBLE);
			mInfoEditLayout.setVisibility(GONE);
			mAnomalyLayout.setVisibility(GONE);
			mBoolLayout.setVisibility(GONE);
			
			mBtnText.setVisibility(VISIBLE);
			mBtnText.setText("设置时间");
			
			mInfoView.setEnabled(false);
			mInfoView.setText(historyResult);
			this.mCheckItem.checkResult = historyResult;
		}
		else if("text".equals(dataType.toLowerCase())) {
			mInfoViewLayout.setVisibility(GONE);
			mInfoEditLayout.setVisibility(VISIBLE);
			mAnomalyLayout.setVisibility(GONE);
			mBoolLayout.setVisibility(GONE);
			
			mInfoEdit.setInputType(InputType.TYPE_CLASS_TEXT);
			mBtnEdit.setVisibility(VISIBLE);
			
			mInfoEdit.setText(historyResult);
			this.mCheckItem.checkResult = historyResult;
		}
	}
	
	public boolean isInputedValue() {
		KoAssetCheckCheckItem checkData = getCheckValueItem();
		return KoDataUtil.isStringNotNull(checkData.checkResult);
	}
	
	public boolean isInputedValueOk() {
		KoAssetCheckCheckItem checkData = getCheckValueItem();
		if(KoDataUtil.isStringNotNull(checkData.checkResult)) {
			if("numeric".equals(checkData.queryType)) {
				if(KoDataUtil.isValidFloatNumber(checkData.checkResult)) {
					return true;
				}
				else {
					DialogUtils.showToast(mContext, checkData.queryText+mContext.getResources().getString(R.string.ko_tips_must_be_right_number));
					return false;
				}
			}
//			else if("anomaly".equals(checkData.queryType.toLowerCase())) {
//				if(!mAnomalyCheckY.getIsChecked() && !mAnomalyCheckN.getIsChecked()) {
//					DialogUtils.showToast(mContext, "请勾选"+checkData.queryText);
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
//			else if("boolean".equals(checkData.queryType.toLowerCase())) {
//				if(!mBoolCheckY.getIsChecked() && !mBoolCheckN.getIsChecked()) {
//					DialogUtils.showToast(mContext, "请勾选"+checkData.queryText);
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
			else {
				return true;
			}
		}
		else {
			//DialogUtils.showToast(mContext, "请填写"+checkData.queryText);
			return true;
		}
	}
	
	public KoAssetCheckCheckItem getCheckValueItem() {
		if("numeric".equals(this.mCheckItem.queryType)) {
			this.mCheckItem.checkResult = mInfoEdit.getText().toString();
		}
		else if("text".equals(this.mCheckItem.queryType.toLowerCase())) {
			this.mCheckItem.checkResult = mInfoEdit.getText().toString();
		}
		else if("time".equals(this.mCheckItem.queryType.toLowerCase())) {
			this.mCheckItem.checkResult = mInfoView.getText().toString();
		}

		return this.mCheckItem;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.asset_check_item_info_text:
			showCleanLevelDlg();
			break;
			
		case R.id.asset_check_item_text_btn:
			KoPickTimeDlg dlg = new KoPickTimeDlg(mContext, mSelectTimeBack, mSelectedTime);
			dlg.show();
			break;
			
		case R.id.asset_check_item_edit_btn:
			
			break;
			
		case R.id.asset_check_item_anomaly_y:
			mAnomalyCheckY.setChecked(true);
			mAnomalyCheckN.setChecked(false);
			this.mCheckItem.checkResult = "1";
			break;
			
		case R.id.asset_check_item_anomaly_n:
			mAnomalyCheckY.setChecked(false);
			mAnomalyCheckN.setChecked(true);
			this.mCheckItem.checkResult = "2";
			break;
			
		case R.id.asset_check_item_bool_y:
			mBoolCheckY.setChecked(true);
			mBoolCheckN.setChecked(false);
			this.mCheckItem.checkResult = "Y";
			break;
			
		case R.id.asset_check_item_bool_n:
			mBoolCheckY.setChecked(false);
			mBoolCheckN.setChecked(true);
			this.mCheckItem.checkResult = "N";
			break;
		
		}
	}
	
	private IKoPickTimeDlgBack mSelectTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mSelectedTime.setTimeInMillis(cal.getTimeInMillis());
			mInfoView.setText(StringUtils.formatDateTime(mSelectedTime));
			mCheckItem.checkResult = StringUtils.formatDateTime(mSelectedTime);
		}
	};
	
	private void showCleanLevelDlg() {
		List<KoCleanLevelItem> dataList = getCleanLevelListData();
		KoCleanLevelListDlg dlg = new KoCleanLevelListDlg(mContext, mSelectedCleanLevel, dataList);
		dlg.show();
	}
	
	private List<KoCleanLevelItem> getCleanLevelListData() {
		List<KoCleanLevelItem> dataList = new ArrayList<KoCleanLevelItem>();
		dataList.add(new KoCleanLevelItem("不合格",1));
		dataList.add(new KoCleanLevelItem("较差",2));
		dataList.add(new KoCleanLevelItem("合格",3));
		dataList.add(new KoCleanLevelItem("清洁",4));
		dataList.add(new KoCleanLevelItem("很清洁",5));
		return dataList;
	}
	
	private KoCleanLevelItem getCleanLevelDataByValue(String cleanLevel) {
		
		try {
			int levelInt = Integer.valueOf(cleanLevel);
			
			List<KoCleanLevelItem> dataList = getCleanLevelListData();
			for (KoCleanLevelItem koCleanLevelItem : dataList) {
				if(koCleanLevelItem != null && koCleanLevelItem.level == levelInt) {
					return koCleanLevelItem;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private class KoCleanLevelListDlg extends KoListDialg<KoCleanLevelItem> {

		public KoCleanLevelListDlg(Activity context, KoCleanLevelItem selectedData, List<KoCleanLevelItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoCleanLevelItem selectedItem) {
			return selectedItem.name;
		}

		@Override
		public boolean isSelectedObjEquals(KoCleanLevelItem selectedData, KoCleanLevelItem item) {
			if(selectedData!=null && item!=null && selectedData.level==item.level) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoCleanLevelItem selData) {
			mSelectedCleanLevel = selData;
			mInfoView.setText(selData.name);
			mCheckItem.checkResult = String.valueOf(selData.level);
		}
	}
}
