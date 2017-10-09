/*-----------------------------------------------------------

-- PURPOSE

--    质量收集计划的数据项，包含一个左标题和右面的EditView

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoQaNeedFillItem;
import cn.kol.pes.model.item.KoQaValueItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoQaItemView extends LinearLayout {
	
	public interface QaItemDerivedOneBack {
		public void derivedOneValueback(int value);
	}
	
	private static final String tag = "KoQaItemView";

	private TextView mLableView;
	private EditText mValueView;
	private View mScanView;
	private View mTimeResetBtn;
	
	private Activity mCon;
	private KoQaNeedFillItem mQaData;
	private Calendar mOpEndCal = Calendar.getInstance();
	private KoQaValueItem mSelectedQaValueItem;
	
	private int mIncompleteQuan;
	private Calendar mMinStartCal = Calendar.getInstance();
	private Calendar mMaxEndCal = Calendar.getInstance();
	
	private IKoPickTimeDlgBack mTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mOpEndCal.setTimeInMillis(cal.getTimeInMillis());
			setQaValue(KoDataUtil.formatDateTime(mOpEndCal));
		}
	};
	
	
	public KoQaItemView(final Activity context, KoQaNeedFillItem qa, String wipId, String incompleteQuan, String trxQuan, String saItem, String opCode, String opName, String assetTagNum, KoLoginAdapter staffInfo, Calendar opStartTime, Calendar opEndTime, OnClickListener click, QaItemDerivedOneBack back) {
		super(context, null);
		mCon = context;
		mIncompleteQuan = Integer.valueOf(incompleteQuan);
		
		mMinStartCal.setTimeInMillis(opStartTime.getTimeInMillis());
		
		if(opEndTime!=null) {
			mMaxEndCal.setTimeInMillis(opEndTime.getTimeInMillis());
		}
		else {
			mMaxEndCal = null;
		}
		
		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_qa_item_view_layout, this);
		mValueView = (EditText) rootView.findViewById(R.id.qa_item_value_view);
		mLableView = (TextView) rootView.findViewById(R.id.qa_item_view_title_view);
		
		mTimeResetBtn = rootView.findViewById(R.id.qa_item_reset_date_time_btn);
		mTimeResetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KoPickTimeDlg dlg = new KoPickTimeDlg(context, mTimeBack, mOpEndCal);
				dlg.show();
			}
		});
		
		mScanView = findViewById(R.id.qa_item_scan_view);
		mScanView.setTag(KoQaItemView.this);
		mScanView.setOnClickListener(click);
		
		setQaData(qa, wipId, incompleteQuan, trxQuan, saItem, opCode, opName, assetTagNum, staffInfo, opStartTime, opEndTime, back);
	}
	
	private void setQaData(KoQaNeedFillItem qa, String wipId, String incompleteQuan,  String trxQuan, String saItem, String opCode, String opName, String assetTagNum, KoLoginAdapter staffInfo, Calendar opStartTime, Calendar opEndTime, final QaItemDerivedOneBack back) {
		mQaData = qa;
		
		String iconMustInput = isMustInputChar() ? " *":"";
		mLableView.setText(mQaData.prompt+iconMustInput);
		
		final List<KoQaValueItem> qaValueList = qa.getQaValueList();
		if(qaValueList!=null && qaValueList.size()>0) {
			hideScanBtn();
			mValueView.setEnabled(true);
			mValueView.setFocusable(false);
//			mSelectedQaValueItem = qaValueList.get(0);
//			setQaValue(mSelectedQaValueItem.shortCode);
			
			mValueView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					KoQaValueListDlg dlg = new KoQaValueListDlg(mCon, mSelectedQaValueItem, qaValueList);
					dlg.show();
				}
			});
		}
		else if(qa.charId!=null && qa.charId.equals("10")) {
			mValueView.setText(saItem);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("19")) {
			mValueView.setText(wipId);
			this.setVisibility(View.GONE);
			hideScanBtn();
		}
		else if(qa.charId!=null && (qa.charId.equals("45")||qa.charId.equals("553"))) {
			mValueView.setText(opCode);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("148")) {
			mValueView.setText(String.valueOf(incompleteQuan));
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("200")) {
			mValueView.setText(CacheUtils.getLoginUserInfo().staffNo);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("250")) {
			mValueView.setText(assetTagNum);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("268")) {
			mValueView.setText(staffInfo.staffNo);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("315")) {
			mValueView.setText(staffInfo.staffName);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("373")) {
			mValueView.setText(opName);
			hideScanBtn();
		}
		else if(qa.charId!=null && (qa.charId.equals("473")||qa.charId.equals("4"))) {//合格数，我决定通过客户端计算得出
			mValueView.setText(String.valueOf(0));
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("476")) {
			mValueView.setText(incompleteQuan);//trxQuan
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("933")) {
			mValueView.setText(CacheUtils.getLoginUserInfo().staffNo);
			hideScanBtn();
		}
		else if(qa.charId!=null && (qa.charId.equals("936") || qa.charId.equals("949"))) {
			mValueView.setText(incompleteQuan);
			hideScanBtn();
		}
		else if(qa.charId!=null && qa.charId.equals("993")) {
			mValueView.setText(assetTagNum);
			hideScanBtn();
		}
		
		
		if(mQaData.datatypeMeaning!=null && "Date and Time".equals(mQaData.datatypeMeaning)) {
			if(qa.charId!=null && (qa.charId.equals("248")||qa.charId.equals("946"))) {//开始时间
				mValueView.setText(StringUtils.formatDateTime(opStartTime));
				mOpEndCal.setTimeInMillis(opStartTime.getTimeInMillis());
			}
			else if(qa.charId!=null && (qa.charId.equals("2147483632")||qa.charId.equals("947"))) {//结束时间
//				if (opEndTime!=null) {
//					mOpEndCal.setTimeInMillis(opEndTime.getTimeInMillis());
//				}
				mValueView.setText(StringUtils.formatDateTime(mOpEndCal));
			}
			else {//进入系统时间的其它的情况
				mValueView.setText(StringUtils.formatDateTime(mOpEndCal));
				mTimeResetBtn.setVisibility(View.VISIBLE);
			}
			hideScanBtn();
		}
		
		if(mQaData.datatypeMeaning!=null && "Number".equals(mQaData.datatypeMeaning)) {//数据类型是数字时，设置输入模式为电话号码
			mValueView.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		else if(mQaData.prompt!=null && mQaData.prompt.contains("号")) {//当输入项中含有“号”这个字时，一般会输入非汉字字符,所以输入模式设为可见密码
			mValueView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		}
		
		if(isDerivedOne()) {
			mScanView.setEnabled(false);
			mScanView.setVisibility(View.GONE);
			mValueView.setHint("0");
			mValueView.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					String sS = s.toString().trim();
					if(sS.length()==0) {
						sS = "0";
					}
					
					if(KoDataUtil.isValidNumber(sS)) {
						back.derivedOneValueback(Integer.valueOf(sS));
					}
				}
			});
		}
		
		if("1".equals(qa.readOnlyFlag)) {
			hideScanBtn();
			if(mValueView.getText().toString().length()==0 && isMustInputChar()) {
				mValueView.setText("none");
			}
		}
	}
	
	public boolean isNullCharNotMandatory() {//空的且非必填字段
		if(mValueView.getText().toString().trim().length()==0 && !isMustInputChar()) {
			return true;
		}
		
		return false;
	}
	
	private boolean isMustInputChar() {
		LogUtils.e(tag, "isMustInputChar:mQaData.mandatoryFlag="+mQaData.mandatoryFlag);
		if("1".equals(mQaData.mandatoryFlag)) {
			return true;
		}
		return false;
	}
	
	private void hideScanBtn() {
		mValueView.setEnabled(false);
		mScanView.setEnabled(false);
		mScanView.setVisibility(View.GONE);
	}
	
	public void updateOpEndTime(Calendar opEndTime) {
		
		if(mQaData.charId!=null && (mQaData.charId.equals("2147483632")||mQaData.charId.equals("947"))) {	
			if (mMaxEndCal!=null) {
				mOpEndCal.setTimeInMillis((mMaxEndCal.after(opEndTime) ? mMaxEndCal:opEndTime).getTimeInMillis());
			}else {
				mOpEndCal.setTimeInMillis(opEndTime.getTimeInMillis());
			}
			
			mValueView.setText(StringUtils.formatDateTime(mOpEndCal));
		}
	}
	
	public void updateOpPassQty(int scrpQuan) {
		if(mQaData.charId!=null && (mQaData.charId.equals("473") || mQaData.charId.equals("936"))) {
			mValueView.setText(String.valueOf(mIncompleteQuan-scrpQuan));
		}
	}
	
	public String getQaValue() {
		String vS = mValueView.getText().toString().trim();
		if(isDerivedOne() && vS.length()==0) {
			return "0";
		}
		if(mQaData.datatypeMeaning!=null && "Date and Time".equals(mQaData.datatypeMeaning)) {
			return KoDataUtil.formatDateTime(mOpEndCal);
			//return String.valueOf(KoDataUtil.revertMillsToDaysDouble(mOpEndCal.getTimeInMillis()));//返回day为单位的整数，因为后面触发器有用day为变量进行计算的
		}
		return vS;
	}
	
	public void setQaValue(String value) {
		mValueView.setText(value);
	}
	
	public String getCharId() {
		return mQaData.charId;
	}
	
	public String getResultColumnName() {
		return mQaData.resultColumnName;
	}
	
	public boolean isValueValid() {//简单判断输入的数据是否合法
		if(mQaData.datatypeMeaning!=null && "Number".equals(mQaData.datatypeMeaning)) {

			if(getQaValue().length()==0 && isMustInputChar()) {
				DialogUtils.showToast(mCon, mCon.getText(R.string.ko_tips_input_value_should_be_input)+mQaData.prompt);
				return false;
			}
			else if(getQaValue().length()>0 && isDerivedOne() && !KoDataUtil.isValidNumber(getQaValue())) {
				DialogUtils.showToast(mCon, mQaData.prompt+mCon.getText(R.string.ko_tips_input_value_should_be_number));
				return false;
			}
			else if(getQaValue().length()>0 && !KoDataUtil.isValidFloatNumber(getQaValue())) {
				DialogUtils.showToast(mCon, mQaData.prompt+mCon.getText(R.string.ko_tips_input_value_should_be_number_or_float));
				return false;
			}
			else {
				return true;
			}
		}
		else if(getQaValue().length()==0 && isMustInputChar()) {
			DialogUtils.showToast(mCon, mCon.getText(R.string.ko_tips_input_value_should_be_input)+mQaData.prompt);
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean isDerivedOne() {
		if(mQaData!=null && mQaData.derivedFlag!=null && "1".equals(mQaData.derivedFlag)) {
			return true;
		}
		return false;
	}
	
	public class KoQaValueListDlg extends KoListDialg<KoQaValueItem> {

		public KoQaValueListDlg(Activity context, KoQaValueItem selectedData, List<KoQaValueItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoQaValueItem selectedItem) {
			return selectedItem.shortCode+" -- "+selectedItem.description;
		}

		@Override
		public boolean isSelectedObjEquals(KoQaValueItem selectedData, KoQaValueItem item) {
			
			if(selectedData!=null && item!=null && selectedData.shortCode!=null && 
			   selectedData.shortCode.equals(item.shortCode)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoQaValueItem selData) {
			mSelectedQaValueItem = selData;
			setQaValue(mSelectedQaValueItem.shortCode);
		}
	}
	
}
