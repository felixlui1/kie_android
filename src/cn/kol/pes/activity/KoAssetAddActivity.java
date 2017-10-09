/*-----------------------------------------------------------

-- PURPOSE

--    添加设备点检记录的界面

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
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
import android.widget.ScrollView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoParamItem;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.widget.KoListDialg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoAssetAddActivity extends BaseActivity implements OnClickListener {

	private TextView mEquipNoText;//设备id的textview
	private TextView mCheckTimeView;//展示点检时间
	private TextView mStaffText;//展示点检员工的text
	
	private View mScanAssetIdBtn;//扫描设备号的按钮
	
	private TextView mEquipStatusText;//展示设备状态的text
	
	private TextView mExFixTimeText;//预计维修时间的text
	private TextView mExCompletedTimeText;//预计完成维修时间的text
	
	private EditText mDescEdit;//备注的填写框
	
	private Calendar mCheckTime = Calendar.getInstance();//点检时间
	private Calendar mExFixTime = Calendar.getInstance();//预计开始维修时间
	private Calendar mExCompletedTime = Calendar.getInstance();//预计完成维修时间
	
	private ViewGroup mRepairInfoParent;//维修信息的容器控件
	private ViewGroup mPicParent;//拍摄的照片的父容器
	
	private HorizontalScrollView mPicScrollView;//横向的滑动控件，图片的容器
	
	
	private ImageView mShutPicBtn;//拍照按钮
	
	private List<KoParamItem> mPicListData = new ArrayList<KoParamItem>();//用于存储拍摄的照片数据
	private KoParamItem mSelectedStatus;//设备所选的状态，正常/故障
	private KoAssetGetAssetInfoAdapter mKoAssetGetAssetInfoAdapter;
	
	private String mPicPathDescSb;//照片数据组成字符串，用于上传
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_asset_add_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_add_check_asset);
		
		mSelectedStatus = new KoParamItem("2", getString(R.string.ko_value_asset_fail));
		
		mEquipNoText = (TextView) findViewById(R.id.asset_add_equip_no_text);
		mCheckTimeView = (TextView) findViewById(R.id.asset_add_reset_check_time_edit);
		mStaffText = (TextView) findViewById(R.id.add_asset_staff_name);
		mEquipStatusText = (TextView) findViewById(R.id.asset_add_reset_equip_status_text);
		mDescEdit = (EditText) findViewById(R.id.asset_add_desc_edit);
		
		mExFixTimeText = (TextView) findViewById(R.id.asset_add_ex_fix_time_edit);
		mExCompletedTimeText = (TextView) findViewById(R.id.asset_add_ex_completed_time_edit);
		
		mRepairInfoParent = (ViewGroup) findViewById(R.id.asset_add_repair_info_layout);
		mRepairInfoParent.setVisibility(View.GONE);
		
		mPicParent = (ViewGroup) findViewById(R.id.asset_add_pic_parent_layout);
		
		mShutPicBtn = (ImageView) findViewById(R.id.asset_add_shut_pic_btn);
		
		mPicScrollView = (HorizontalScrollView) findViewById(R.id.asset_add_pic_parent_scroll_view);
		
		//findViewById(R.id.asset_add_reset_equip_status_layout).setOnClickListener(this);//设置选择设备状态的点击事件
		mScanAssetIdBtn = findViewById(R.id.asset_add_equip_no_btn);
		mScanAssetIdBtn.setOnClickListener(this);
		findViewById(R.id.asset_add_sure_btn).setOnClickListener(this);//添加点检数据的“确认”按钮
		mShutPicBtn.setOnClickListener(this);
		
		//mCheckTimeView.setOnClickListener(this);
		mExFixTimeText.setOnClickListener(this);
		mExCompletedTimeText.setOnClickListener(this);
		
		if(CacheUtils.getLoginUserInfo()!=null) {
			mStaffText.setText(CacheUtils.getLoginUserInfo().staffName);
		}
		
		mEquipStatusText.setText(mSelectedStatus.value);
		
		mExFixTime.setTimeInMillis(mCheckTime.getTimeInMillis()+KoDataUtil.revertDaysToMills(1));
		mExCompletedTime.setTimeInMillis(mExFixTime.getTimeInMillis()+KoDataUtil.revertDaysToMills(1));
		
		mCheckTimeView.setText(StringUtils.formatDateTime(mCheckTime));
		mExFixTimeText.setText(StringUtils.formatDateTime(mExFixTime));
		mExCompletedTimeText.setText(StringUtils.formatDateTime(mExCompletedTime));
		
		mRepairInfoParent.setVisibility(View.VISIBLE);
		mScanAssetIdBtn.setEnabled(true);
		
		findViewById(R.id.asset_add_ex_fix_time_layout).setVisibility(View.GONE);
		findViewById(R.id.asset_add_ex_completed_time_layout).setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.asset_add_reset_check_time_edit://重设点检时间
			KoPickTimeDlg dlg = new KoPickTimeDlg(this, mCheckTimeBack, mCheckTime);
			dlg.show();
			break;
			
		case R.id.asset_add_reset_equip_status_layout://改变设备状态
			showStatusListDlg();
			break;
			
		case R.id.asset_add_equip_no_btn://扫描设备号的按钮
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.asset_add_ex_fix_time_edit://重设预计开始维修时间
			KoPickTimeDlg dlgF = new KoPickTimeDlg(this, mExFixTimeBack, mExFixTime);
			dlgF.show(); 
			break;
			
		case R.id.asset_add_ex_completed_time_edit://预计完成维修时间
			KoPickTimeDlg dlgC = new KoPickTimeDlg(this, mExCompletedTimeBack, mExCompletedTime);
			dlgC.show();
			break;
			
		case R.id.asset_add_shut_pic_btn://拍照按钮
			KoCameraActivity.startActForRes(this, CacheUtils.getLoginUserInfo().staffNo);
			break;
			
		case R.id.asset_add_sure_btn://确认添加质量管理计划按钮
			if(mEquipNoText.getText().length()<2 || mKoAssetGetAssetInfoAdapter==null || mKoAssetGetAssetInfoAdapter.assetId==null) {
				DialogUtils.showToast(this, "请先扫入设备");
			}
			else if(mCheckTime.after(mExFixTime)) {
				DialogUtils.showToast(this, R.string.ko_tips_check_time_can_not_after_expect_repair_time);
			}
			else if(mExFixTime.after(mExCompletedTime)) {
				DialogUtils.showToast(this, R.string.ko_tips_expect_repair_time_can_not_after_expect_fix_time);
			}
			else {
				showLoadingDlg(R.string.ko_tips_upload_asset_check);
				add();
			}
			break;
			
		default:
			
			break;
		}
	}
	
	
	private void add() {//请求网络，添加点检信息
		mPicPathDescSb = KoDataUtil.picPathDescListToString(mPicListData);
		
		Calendar nowCal = Calendar.getInstance();
		
		mKoControl.assetCheckAdd(String.valueOf(nowCal.getTimeInMillis()), CacheUtils.getLoginUserInfo().staffNo.trim(), 
								 String.valueOf(nowCal.getTimeInMillis()), CacheUtils.getLoginUserInfo().staffNo.trim(), 
								 mKoAssetGetAssetInfoAdapter.assetId, String.valueOf(mCheckTime.getTimeInMillis()), 
								 mSelectedStatus.name, 
								 String.valueOf(mExFixTime.getTimeInMillis()), 
								 String.valueOf(mExCompletedTime.getTimeInMillis()), 
								 mDescEdit.getText().toString());
	}
	
	//扫描设备号、编辑图片说明和拍照的回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫设备号
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			showLoadingDlg();
			mKoControl.assetGetAssetInfo(dataS);
		}
		else if(requestCode==KoCameraActivity.KEY_REQ_CODE_CAMERA && resultCode==RESULT_OK) {//拍照
			
			String dataS = data.getStringExtra(KoCameraActivity.KEY_RES_INTENT);
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			mPicListData.add(new KoParamItem(dataS, null));
			showPicList();
		}
		else if(requestCode==KoViewPicActivity.REQ_CODE && resultCode==RESULT_OK) {//编辑图片说明
			
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
	
	//显示拍摄的照片微缩图
	private void showPicList() {
		mPicParent.removeAllViews();
		for(KoParamItem picData : mPicListData) {
			if(picData != null) {
				ImageView imgV = new ImageView(this);

                BitmapFactory.Options opts=new BitmapFactory.Options();
                opts.inSampleSize=4;
                Bitmap cbitmap=BitmapFactory.decodeFile(picData.name, opts);
                imgV.setImageBitmap(cbitmap);
				
				imgV.setTag(picData);
				imgV.setOnClickListener(mImgClickListener);
				imgV.setLayoutParams(mShutPicBtn.getLayoutParams());
				mPicParent.addView(imgV);
			}
		}
		
		mPicScrollView.fullScroll(ScrollView.FOCUS_LEFT);
	}
	
	private void removePicView(String picPath) {
		
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
	
	//图标微缩图点击事件，用于触发编辑界面
	private OnClickListener mImgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			KoParamItem picPathDesc = (KoParamItem) v.getTag();
			if(picPathDesc!=null) {
				KoViewPicActivity.startActForRes(KoAssetAddActivity.this, picPathDesc.name, picPathDesc.value);
			}
		}
	};
	
	//编辑点检时间的回调
	private IKoPickTimeDlgBack mCheckTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mCheckTime.setTimeInMillis(cal.getTimeInMillis());
			mCheckTimeView.setText(StringUtils.formatDateTime(mCheckTime));
		}
	};
	
	//重设维修时间的回调
	private IKoPickTimeDlgBack mExFixTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mExFixTime.setTimeInMillis(cal.getTimeInMillis());
			mExFixTimeText.setText(StringUtils.formatDateTime(cal));
		}
	};
	
	//重设维修时间的回调
	private IKoPickTimeDlgBack mExCompletedTimeBack = new IKoPickTimeDlgBack() {
		@Override
		public void pickTime(Calendar cal) {
			mExCompletedTime.setTimeInMillis(cal.getTimeInMillis());
			mExCompletedTimeText.setText(StringUtils.formatDateTime(cal));
		}
	};
	
	//显示状态列表的对话框
	private void showStatusListDlg() {
		List<KoParamItem> dataList = new ArrayList<KoParamItem>();
		dataList.add(new KoParamItem("1", getString(R.string.ko_value_asset_normal)));
		dataList.add(new KoParamItem("2", getString(R.string.ko_value_asset_fail)));
		
		KoStatusListDlg dlg = new KoStatusListDlg(this, mSelectedStatus, dataList);
		dlg.show();
	}
	
	private class KoStatusListDlg extends KoListDialg<KoParamItem> {

		public KoStatusListDlg(Activity context, KoParamItem selectedData, List<KoParamItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoParamItem selectedItem) {
			return selectedItem.value;
		}

		@Override
		public boolean isSelectedObjEquals(KoParamItem selectedData, KoParamItem item) {
			
			if(selectedData!=null && item!=null && selectedData.name!=null && selectedData.name.equals(item.name)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoParamItem selData) {
			
			mEquipStatusText.setText(selData.value);
			mSelectedStatus.name = selData.name;
			mSelectedStatus.value = selData.value;
			
			if(selData!=null && "2".equals(selData.name)) {
				mRepairInfoParent.setVisibility(View.VISIBLE);
				mScanAssetIdBtn.setEnabled(false);
			}else {
				mRepairInfoParent.setVisibility(View.GONE);
				mScanAssetIdBtn.setEnabled(true);
			}
		}
	}

	//网络请求的回调，此界面只是添加点检，所以，实例化KolPesControlBack的时候只复写了点检回调函数
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			//添加设备的数据回调
			@Override
			public void assetCheckAddBack(boolean isSuc, String checkId, String msg) {
				
				LogUtils.e(tag,"assetCheckAddBack():"+isSuc+msg);
				
				if(isSuc) {
					KoUploadPicService.startSer(KoAssetAddActivity.this, checkId, mPicPathDescSb.toString(), false, false);
					KoAssetListActivity.startAct(KoAssetAddActivity.this);
					KoAssetAddActivity.this.finish();
				}
				else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoAssetAddActivity.this, msg);
				}
			}

			//获取设备相关信息的回调
			@Override
			public void assetGetAssetInfoBack(boolean isSuc, String msg, KoAssetGetAssetInfoAdapter adapter) {
				if(isSuc) {
					mKoAssetGetAssetInfoAdapter = adapter;
					mEquipNoText.setText(mKoAssetGetAssetInfoAdapter.tagNumber+" "+mKoAssetGetAssetInfoAdapter.description);
				}else {
					mKoAssetGetAssetInfoAdapter = null;
					DialogUtils.showToast(KoAssetAddActivity.this, msg);
				}
				
				dismissLoadingDlg();
				
			}
		};
	}

	@Override
	public void onBackPressed() {
		KoAssetListActivity.startAct(KoAssetAddActivity.this);
		KoAssetAddActivity.this.finish();
	}

}
