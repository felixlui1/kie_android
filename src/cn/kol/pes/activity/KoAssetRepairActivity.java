/*-----------------------------------------------------------

-- PURPOSE

--    更新维修信息的界面

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoAssetMachineFailItem;
import cn.kol.pes.model.item.KoChangedPartItem;
import cn.kol.pes.model.item.KoParamItem;
import cn.kol.pes.widget.KoAssetCheckChangedPartItemView;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.KoAssetCheckChangedPartItemView.IKoAssetCheckChangePartsCallBack;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoAssetRepairActivity extends BaseActivity implements OnClickListener, IKoAssetCheckChangePartsCallBack {

	private TextView mEquipNoText;//展示设备号的view
	private TextView mStaffText;//展示员工姓名的view
	private TextView mFailDescTextView;//故障信息的view
	
	private TextView mActRepairStartTimeText;//实际开始维修时间的view
	private TextView mActRepairEndTimeText;//实际结束维修时间的view
	
	private EditText mDescEdit;//填写备注信息的输入框
	private EditText mDownTimeEdit;//停机时间的显示框

	
	private Calendar mCheckTimeCal = Calendar.getInstance();//点检时间
	private Calendar mActRepairStartTime = Calendar.getInstance();//实际开始维修时间
	private Calendar mActRepairEndTime = Calendar.getInstance();//实际完成维修时间
	
	private ViewGroup mPicParent;//拍摄的照片的父容器
	
	private HorizontalScrollView mPicScrollView;//拍摄的照片的横向滑动容器
	
	private ImageView mShutPicBtn;//拍照按钮
	
	private List<KoChangedPartItem> mPartList;
	private ViewGroup mChangedPartsParentLayout;
	
	private List<KoParamItem> mPicListData = new ArrayList<KoParamItem>();
	
	private static final String KEY_CHECK_ID = "key_check_id";
	
	public static void startAct(Context context, String checkId) {
		Intent i = new Intent(context, KoAssetRepairActivity.class);
		i.putExtra(KEY_CHECK_ID, checkId);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_repair_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_asset_repair_title);
		
		mEquipNoText = (TextView) findViewById(R.id.asset_repair_equip_no_text);
		mStaffText = (TextView) findViewById(R.id.add_asset_staff_name);
		
		mFailDescTextView = (TextView) findViewById(R.id.asset_repair_fail_desc_edit);
		
		mDownTimeEdit = (EditText) findViewById(R.id.asset_repair_reset_down_time_edit);
		mDescEdit = (EditText) findViewById(R.id.asset_repair_repair_desc_edit);
		
		mActRepairStartTimeText = (TextView) findViewById(R.id.asset_repair_act_fix_time_edit);
		mActRepairEndTimeText = (TextView) findViewById(R.id.asset_repair_act_completed_time_edit);
		
		mPicParent = (ViewGroup) findViewById(R.id.asset_repair_pic_parent_layout);
		
		mShutPicBtn = (ImageView) findViewById(R.id.asset_repair_shut_pic_btn);
		
		mPicScrollView = (HorizontalScrollView) findViewById(R.id.asset_repair_pic_parent_scroll_view);
		
		findViewById(R.id.asset_repair_changed_parts_add_changed_part).setOnClickListener(this);
		
		findViewById(R.id.asset_repair_sure_btn).setOnClickListener(this);//为 确定维修的按钮添加点击事件
		
		mChangedPartsParentLayout = (ViewGroup) findViewById(R.id.asset_repair_changed_parts_parent_layout);
		
		mChangedPartsParentLayout.addView(new KoAssetCheckChangedPartItemView(this, this));
		
		View checkRemarksParentLayout = findViewById(R.id.asset_repair_check_remarks_parent);
		TextView checkRemarksView = (TextView) findViewById(R.id.asset_repair_check_remarks);
		
		mShutPicBtn.setOnClickListener(this);
		
		mActRepairStartTimeText.setOnClickListener(this);
		mActRepairEndTimeText.setOnClickListener(this);
		mFailDescTextView.setOnClickListener(this);
		
		if(CacheUtils.getLoginUserInfo()!=null) {
			mStaffText.setText(CacheUtils.getLoginUserInfo().staffName);
		}
		
		//mActRepairEndTime.setTimeInMillis(mActRepairStartTime.getTimeInMillis()+1000*60*60*1);//结束维修时间默认比开始维修时间晚2小时
		
		//mActRepairStartTimeText.setText(StringUtils.formatDateTime(mActRepairStartTime));
		mActRepairEndTimeText.setText(StringUtils.formatDateTime(mActRepairEndTime));
		
		KoAssetCheckItem dataItem = CacheUtils.getSelectedAssetCheckItem();
		
		if(dataItem != null) {
			mEquipNoText.setText(dataItem.assetName);
			if(KoDataUtil.isStringNotNull(dataItem.checkRemarks)) {
				checkRemarksView.setText(dataItem.checkRemarks);
				checkRemarksParentLayout.setVisibility(View.VISIBLE);
			}
			else {
				checkRemarksParentLayout.setVisibility(View.GONE);
			}
			
			mCheckTimeCal = KoDataUtil.convertStringToCal(dataItem.checkTime);
			int hour = (int) ((mActRepairEndTime.getTimeInMillis()-mCheckTimeCal.getTimeInMillis())/(1000*60*60));
			mDownTimeEdit.setText(String.valueOf(hour));
			
			showLoadingDlg();
			mKoControl.getAssetMachineFailList(dataItem.assetTagNum);//获取机器的错误状态列表
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.asset_repair_fail_desc_edit://显示故障列表
			showStatusListDlg();
			break;
		
		case R.id.asset_repair_act_fix_time_edit://重设实际开始维修时间
			KoPickTimeDlg dlgF = new KoPickTimeDlg(this, mExFixTimeBack, mActRepairStartTime);
			dlgF.show();
			break;
			
		case R.id.asset_repair_act_completed_time_edit://重设实际完成维修时间
			KoPickTimeDlg dlgC = new KoPickTimeDlg(this, mExCompletedTimeBack, mActRepairEndTime);
			dlgC.show();
			break;
			
		case R.id.asset_repair_shut_pic_btn://拍照
			KoCameraActivity.startActForRes(this, CacheUtils.getLoginUserInfo().staffNo);
			break;
			
		case R.id.asset_repair_changed_parts_add_changed_part:
			mChangedPartsParentLayout.addView(new KoAssetCheckChangedPartItemView(this, this));
			break;
			
		case R.id.asset_repair_sure_btn://确定维修的按钮。实际传数据
			String failDesc = mFailDescTextView.getText().toString();
			String downTime = mDownTimeEdit.getText().toString();
			
			if(failDesc.length()<2) {
				DialogUtils.showToast(this, R.string.ko_tips_repair_select_fail_type);
			}
			else if(downTime.length()<1) {
				DialogUtils.showToast(this, R.string.ko_tips_repair_input_down_time);
			}
			else if(!KoDataUtil.isValidNumber(downTime)) {
				DialogUtils.showToast(this, R.string.ko_tips_wrong_repair_input_down_time);
			}
			else if(mActRepairStartTimeText.getText().toString().trim().length()==0) {
				DialogUtils.showToast(this, "请输入开始维修时间");
			}
			else if(mActRepairStartTime.after(mActRepairEndTime)) {
				DialogUtils.showToast(this, R.string.ko_tips_repair_start_time_is_after_end_time);
			}
			else if(!isChangedPartsDataOk()) {
				DialogUtils.showToast(this, "请输入更换的部件");
			}
			else {
				showLoadingDlg(R.string.ko_tips_upload_asset_repair);
				repair();
			}
			break;
			
		default:
			
			break;
		}
	}
	
	private String mPicPathDescSb;
	
	private void repair() {//请求网络，更新维修数据
		mPicPathDescSb = KoDataUtil.picPathDescListToString(mPicListData);
		
		Calendar nowCal = Calendar.getInstance();
		
		KoAssetCheckItem dataItem = CacheUtils.getSelectedAssetCheckItem();
		
		mKoControl.assetRepair(getIntent().getStringExtra(KEY_CHECK_ID), 
								String.valueOf(nowCal.getTimeInMillis()), 
								CacheUtils.getLoginUserInfo().staffNo, 
								String.valueOf(mActRepairStartTime.getTimeInMillis()), 
								String.valueOf(mActRepairEndTime.getTimeInMillis()), 
								mDownTimeEdit.getText().toString(), 
								mDescEdit.getText().toString(), 
								mSelectedFailData.lookupCode,
								getChangedPartString(),
								dataItem.opCode);
	}
	
	private boolean isChangedPartsDataOk() {
		int count = mChangedPartsParentLayout.getChildCount();
		for(int i=0; i<count; i++) {
			KoAssetCheckChangedPartItemView itemV = (KoAssetCheckChangedPartItemView) mChangedPartsParentLayout.getChildAt(i);
			
			if(!itemV.isDataOk()){
				return false;
			}
		}
		return true;
	}
	
	private String getChangedPartString() {
		StringBuilder sb = new StringBuilder();
		int count = mChangedPartsParentLayout.getChildCount();
		for(int i=0; i<count; i++) {
			KoAssetCheckChangedPartItemView itemV = (KoAssetCheckChangedPartItemView) mChangedPartsParentLayout.getChildAt(i);
			
			sb.append(itemV.getValue());
			if(i<count-1) {
				sb.append("@");
			}
		}
		return sb.toString();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫描设备号的回调
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			mEquipNoText.setText(dataS);
		}
		else if(requestCode==KoCameraActivity.KEY_REQ_CODE_CAMERA && resultCode==RESULT_OK) {//拍照的回调
			
			String dataS = data.getStringExtra(KoCameraActivity.KEY_RES_INTENT);
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			mPicListData.add(new KoParamItem(dataS, null));
			showPicList();
		}
		else if(requestCode==KoViewPicActivity.REQ_CODE && resultCode==RESULT_OK) {//编辑图片说明的回调
			
			if(KoViewPicActivity.KEY_OPERATION_DELETE.equals(data.getStringExtra(KoViewPicActivity.KEY_PIC_OPERATION))) {
				removePicView(data.getStringExtra(KoViewPicActivity.KEY_PIC_FILE_PATH));
			}
			else if(KoViewPicActivity.KEY_OPERATION_SAVE.equals(data.getStringExtra(KoViewPicActivity.KEY_PIC_OPERATION))) {
				KoParamItem p = new KoParamItem(data.getStringExtra(KoViewPicActivity.KEY_PIC_FILE_PATH), 
												data.getStringExtra(KoViewPicActivity.KEY_PIC_FILE_DESC));
				
				removePicView(p.name);
				mPicListData.add(p);
			}

			showPicList();
		}
	}
	
	private void showPicList() {//显示拍摄的照片
		mPicParent.removeAllViews();
		for(KoParamItem picData : mPicListData) {
			if(picData != null) {
				ImageView imgV = new ImageView(this);

                BitmapFactory.Options opts=new BitmapFactory.Options();//获取缩略图显示到屏幕上
                opts.inSampleSize=4;
                Bitmap cbitmap=BitmapFactory.decodeFile(picData.name, opts);
                imgV.setImageBitmap(cbitmap);
				
				imgV.setTag(picData);
				imgV.setOnClickListener(mImgClickListener);
				imgV.setLayoutParams(mShutPicBtn.getLayoutParams());
				mPicParent.addView(imgV);
			}
		}
		
		mPicScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
	}
	
	private void removePicView(String picPath) {//删除某张不需要的照片
		try {
			File picFile = new File(picPath);
			picFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(KoParamItem picData : mPicListData) {
			if(picData != null && picPath!=null) {
				if(picPath.equals(picData.name)) {
					mPicListData.remove(picData);
					return;
				}
			}
		}
	}
	
	//图片点击的回调
	private OnClickListener mImgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			KoParamItem picPathDesc = (KoParamItem) v.getTag();
			if(picPathDesc!=null) {
				KoViewPicActivity.startActForRes(KoAssetRepairActivity.this, picPathDesc.name, picPathDesc.value);
			}
		}
	};
	
	//重设修理时间的回调
	private IKoPickTimeDlgBack mExFixTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mActRepairStartTime.setTimeInMillis(cal.getTimeInMillis());
			mActRepairStartTimeText.setText(StringUtils.formatDateTime(cal));
		}
	};
	
	//重设结束修理时间的回调
	private IKoPickTimeDlgBack mExCompletedTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mActRepairEndTime.setTimeInMillis(cal.getTimeInMillis());
			mActRepairEndTimeText.setText(StringUtils.formatDateTime(cal));
			
			int hour = (int) ((mActRepairEndTime.getTimeInMillis()-mCheckTimeCal.getTimeInMillis())/(1000*60*60));
			mDownTimeEdit.setText(String.valueOf(hour));
		}
	};
	
	private KoAssetMachineFailItem mSelectedFailData;
	private List<KoAssetMachineFailItem> mMachineFailList;
	
	private void showStatusListDlg() {//显示设备故障列表
		if(mMachineFailList!=null && mMachineFailList.size()>0) {
			KoStatusListDlg dlg = new KoStatusListDlg(this, mSelectedFailData, mMachineFailList);
			dlg.show();
		}
	}
	
	private class KoStatusListDlg extends KoListDialg<KoAssetMachineFailItem> {

		public KoStatusListDlg(Activity context, KoAssetMachineFailItem selectedData, List<KoAssetMachineFailItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoAssetMachineFailItem selectedItem) {
			return selectedItem.meaning;
		}

		@Override
		public boolean isSelectedObjEquals(KoAssetMachineFailItem selectedData, KoAssetMachineFailItem item) {
			
			if(selectedData!=null && item!=null && selectedData.lookupCode!=null && selectedData.lookupCode.equals(item.lookupCode)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoAssetMachineFailItem selData) {
			mSelectedFailData = selData;
			mFailDescTextView.setText(mSelectedFailData.meaning);
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			@Override
			public void assetGetMachineFailListBack(boolean isSuc, String msg, List<KoAssetMachineFailItem> failList) {//获取到了设备故障列表数据
				dismissLoadingDlg();
				if(isSuc) {
					mMachineFailList = failList;
					if(mMachineFailList!=null && mMachineFailList.size()>0) {
						mSelectedFailData = mMachineFailList.get(0);
						
					}
				}else {
					DialogUtils.showToast(KoAssetRepairActivity.this, msg);
				}
			}

			@Override
			public void assetCheckRepairBack(boolean isSuc, String msg) {//提交网络维修数据后回调
				dismissLoadingDlg();
				if(isSuc) {
					KoUploadPicService.startSer(KoAssetRepairActivity.this, getIntent().getStringExtra(KEY_CHECK_ID), mPicPathDescSb.toString(), true, false);
					KoAssetListActivity.startAct(KoAssetRepairActivity.this);
					KoAssetRepairActivity.this.finish();
				}
				else {
					DialogUtils.showToast(KoAssetRepairActivity.this, msg);
				}
			}
			
			@Override
			public void assetCheckGetChangedPartsBack(boolean isSuc, List<KoChangedPartItem> partList, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					KoAssetRepairActivity.this.mPartList = partList;
					KoChangedPartsListDlg dlg = new KoChangedPartsListDlg(KoAssetRepairActivity.this, null, partList);
					dlg.show();
				}
				else {
					DialogUtils.showToast(KoAssetRepairActivity.this, msg);
				}
			}
		};
	}

	@Override
	public void onBackPressed() {//点击手机后退按钮的回调
		KoAssetListActivity.startAct(KoAssetRepairActivity.this);
		KoAssetRepairActivity.this.finish();
	}
	
	private class KoChangedPartsListDlg extends KoListDialg<KoChangedPartItem> {

		public KoChangedPartsListDlg(Activity context, KoChangedPartItem selectedData, List<KoChangedPartItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoChangedPartItem selectedItem) {
			return selectedItem.name;
		}

		@Override
		public boolean isSelectedObjEquals(KoChangedPartItem selectedData, KoChangedPartItem item) {
			if(selectedData!=null && item!=null && selectedData.name.equals(item.name)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoChangedPartItem selData) {
			if(mSelectedChangedPartView != null) {
				mSelectedChangedPartView.setValue(selData.name);
			}
		}
	}
	
	private KoAssetCheckChangedPartItemView mSelectedChangedPartView;
	
	@Override
	public void changedPartViewClicked(KoAssetCheckChangedPartItemView v) {
		mSelectedChangedPartView = v;
		
		if(this.mPartList != null) {
			KoChangedPartsListDlg dlg = new KoChangedPartsListDlg(KoAssetRepairActivity.this, null, mPartList);
			dlg.show();
		}
		else {
			KoAssetCheckItem dataItem = CacheUtils.getSelectedAssetCheckItem();
			if(dataItem != null) {
				mKoControl.assetCheckGetChangedPartsList(dataItem.opCode);
			}
		}
	}
	
	@Override
	public void changedPartViewDeleteClicked(KoAssetCheckChangedPartItemView v) {
		mChangedPartsParentLayout.removeView(v);
	}
	
}
