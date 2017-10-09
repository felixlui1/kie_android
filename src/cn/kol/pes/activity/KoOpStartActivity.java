/*-----------------------------------------------------------

-- PURPOSE

--    开始工序的界面

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoAssetCheckItem;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.widget.KoJobInfoView;
import cn.kol.pes.widget.KoOpStartAssetView;
import cn.kol.pes.widget.KoOpStartAssetView.IKoOpStartAssetBack;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoOpStartActivity extends BaseActivity implements OnClickListener, IKoPickTimeDlgBack, IKoOpStartAssetBack {

	private KoJobInfoView mMainInfoView;//展示工单各项信息的view
	private EditText mInputQuantity;//投入数的填写框
	private TextView mStartDateTime;//展示开启工序时间的view
	
	private TextView mStaffView;//展示登录员工的view
	
	private Button mResetTimeBtn;//重设开启工序时间的按钮
	private Button mStartOpBtn;//开启工序的按钮
	
	private Calendar mOpStartCal = Calendar.getInstance();//开启工序的时间的数据
	
	private int mMaxQuan = 0;//最大可投入数
	private KoOpStartAssetView mAssetView;//加工设备的封装view
	private ViewGroup mAssetParentLayout;//加工设备view的父容器
	
	private long mTimeBuffer;//最大可提前的时间
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_op_start_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_start_op);
		mAssetParentLayout = (ViewGroup) findViewById(R.id.op_start_asset_parent_layout);
		mMainInfoView = (KoJobInfoView) findViewById(R.id.gongxu_start_main_info_view);
		mMainInfoView.setData(this, CacheUtils.getSelectedJob(), CacheUtils.getSelectedOp(), "");
		
		mInputQuantity = (EditText) findViewById(R.id.gx_start_input_quentity);
		mStartDateTime = (TextView) findViewById(R.id.gx_start_start_date_time);
		mStaffView = (TextView) findViewById(R.id.op_start_staff_name);
		
		mResetTimeBtn = (Button) findViewById(R.id.gx_start_reset_date_time_btn);
		mStartOpBtn = (Button) findViewById(R.id.gx_start_op_start_btn);
		
		mResetTimeBtn.setOnClickListener(this);
		mStartOpBtn.setOnClickListener(this);
		
		mStartDateTime.setText(StringUtils.formatDateTime(mOpStartCal));
		
		KoLoginAdapter staff = CacheUtils.getLoginUserInfo();
		if(staff != null) {
			mStaffView.setText(staff.staffNo+"-"+staff.staffName);
		}
		
		check();
	}
	
	private void check() {//检查是否可以开启这个工序
		showLoadingDlg();
		KoJobItem job = CacheUtils.getSelectedJob();
		KoOpItem op = CacheUtils.getSelectedOp();
		mKoControl.opCheckBeforeStart(job.wipEntityId, job.wipEntityName, op.standardOperationCode, job.curOperationId, job.commonRoutingSequenceId);
	}
	
	private void getMaxQuan() {//获取最大可投入数
		showLoadingDlg();
		KoJobItem job = CacheUtils.getSelectedJob();
		KoOpItem op = CacheUtils.getSelectedOp();
		
		if(job!=null && op!=null) {
			mKoControl.opGetMaxQuan(job.wipEntityName, job.wipEntityId, 
									job.commonRoutingSequenceId, op.standardOperationCode, job.curOperationId,
									CacheUtils.getLoginUserInfo().isCanJumpOp()?"Y":"N");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gx_start_reset_date_time_btn://重设开启工序的时间
			KoPickTimeDlg dlg = new KoPickTimeDlg(this, this, mOpStartCal);
			dlg.show();
			break;
			
		case R.id.gx_start_op_start_btn://开启工序
			if(CacheUtils.getWeekMap()!=null && 
				CacheUtils.getWeekMap().get(KoDataUtil.getWeekInEnglish())!=null &&
				CacheUtils.getWeekMap().get(KoDataUtil.getWeekInEnglish()).isServerInShutDownTime()) {
				DialogUtils.showToast(this, R.string.ko_tips_server_in_shut_time);
			}
			else if(mInputQuantity.getText().length()<1 || 
			   !KoDataUtil.isValidNumber(mInputQuantity.getText().toString()) ||
			   Integer.valueOf(mInputQuantity.getText().toString())<=0) {
				
				DialogUtils.showToast(this, R.string.ko_tips_wrong_input_quan);
			}
			else if(!isAssetsOk()) {
				DialogUtils.showToast(this, R.string.ko_tips_scan_asset_id_first);
			}
			else if(Integer.valueOf(mInputQuantity.getText().toString()) > mMaxQuan) {
				DialogUtils.showToast(this, R.string.ko_tips_input_quan_much_than_max_quan);
			}
			else {
				KoLoginAdapter staff = CacheUtils.getLoginUserInfo();
				KoJobItem job = CacheUtils.getSelectedJob();
				KoOpItem op = CacheUtils.getSelectedOp();
				
				mKoControl.opStart(staff.staffNo, staff.staffNo, 
								   job.wipEntityId, job.wipEntityName, op.standardOperationCode, job.curOperationId,
								   mInputQuantity.getText().toString(),
								   getAssetIdForParam(), String.valueOf(mOpStartCal.getTimeInMillis()),
								   job.commonRoutingSequenceId, 
								   CacheUtils.getLoginUserInfo().isCanJumpOp()?"Y":"N");
				
				showLoadingDlg(R.string.ko_title_op_starting);
			}
			break;
			
		default:
			break;
		}
	}
	

	@Override
	public void scanAssetClick(KoOpStartAssetView assetView) {//加工设备封装view的扫码回调
		mAssetView = assetView;
		KoCaptureActivity.startActForRes(this);
	}
	
	@Override
	public void pickTime(Calendar cal) {//重设开启工序时间的回调
		Calendar nowCal = Calendar.getInstance();
		
		Calendar minCal = Calendar.getInstance();
		minCal.setTimeInMillis(nowCal.getTimeInMillis()-mTimeBuffer);
		
		
		if(cal.after(nowCal) || cal.before(minCal)) {
			DialogUtils.showToast(this, R.string.ko_tips_start_time_cant_after_now);
		}else {
			mOpStartCal.setTimeInMillis(cal.getTimeInMillis());
			mStartDateTime.setText(StringUtils.formatDateTime(mOpStartCal));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//加工设备号的扫码回调
			String dataS = data.getStringExtra("res");
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			mAssetView.setAssetTagNum(dataS);
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		
		return new KolPesControlBack() {

			@Override
			public void getOpCheckBeforeStartBack(boolean isSuc, String timeBuffer, String msg) {//检测是否可开启工序的数据回调
				if(isSuc) {
					LogUtils.e(tag, "timeBuffer="+timeBuffer);
					mTimeBuffer = Integer.valueOf(timeBuffer)*60*1000;
					String opCode = CacheUtils.getSelectedOp().standardOperationCode;
					if(opCode!=null && opCode.startsWith("T")) {
						mTimeBuffer = mTimeBuffer*10000;
					}
					getMaxQuan();
				}else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoOpStartActivity.this, msg);
					KoMainActivity.startAct(KoOpStartActivity.this);
					KoOpStartActivity.this.finish();
				}
			}
			
			//获取最大可投入数的数据回调
			@Override
			public void opGetMaxQuanBack(boolean isSuc, String maxQuan, List<KoAssetCheckItem> opAssetList, String msg) {
				if(isSuc) {
					mMaxQuan = Integer.valueOf(maxQuan);
					mInputQuantity.setText(maxQuan);
					mMainInfoView.setData(KoOpStartActivity.this, CacheUtils.getSelectedJob(), CacheUtils.getSelectedOp(), maxQuan);
					showAssetViews(opAssetList);
				}
				else {
					mMaxQuan = 0;
					mInputQuantity.setText("0");
					DialogUtils.showToast(KoOpStartActivity.this, msg);
				}
				dismissLoadingDlg();
			}

			//开启工序的数据回调
			@Override
			public void opStartBack(boolean isSuc, final String transId, String msg) {
				dismissLoadingDlg();
				if(isSuc) {
					//showOpStartedDlg(transId);
					KoMainActivity.startAct(KoOpStartActivity.this);
					KoOpStartActivity.this.finish();
				}else {
					DialogUtils.showToast(KoOpStartActivity.this, msg);
					getMaxQuan();//开始工单失败后，要刷新一下最大剩余数。因为界面在此停留的这段时间，可能有别的生产线有生产
				}
			}
		};
	}
	
	//显示加工设备
	private void showAssetViews(List<KoAssetCheckItem> opAssetList) {
		mAssetParentLayout.removeAllViews();
		if(opAssetList!=null && opAssetList.size()>0) {
			int maxAsset = opAssetList.size()>=3 ? 3:opAssetList.size();
			for(int i=0; i<maxAsset; i++) {
				mAssetParentLayout.addView(new KoOpStartAssetView(this, opAssetList, this));
			}
		}
	}
	
	//判断加工设备是否合法
	private boolean isAssetsOk() {
		int assetNum = mAssetParentLayout.getChildCount();
		if(assetNum == 0) {
			return true;
		}
		else if(assetNum == 1) {
			KoOpStartAssetView childOne = (KoOpStartAssetView) mAssetParentLayout.getChildAt(0);
			return childOne.isAssetInputedOk();
		}
		else {
			boolean isInputedAtLeastOneAsset = false;
			for(int i=0; i<assetNum; i++) {
				KoOpStartAssetView child = (KoOpStartAssetView) mAssetParentLayout.getChildAt(i);
				if(child.isAssetInputedOk()) {
					isInputedAtLeastOneAsset = true;
					break;
				}
			}
			if(isInputedAtLeastOneAsset) {
				List<String> assetIdList = getAssetIdForParam();
				for(String assetId : assetIdList) {
					int fre = Collections.frequency(assetIdList, assetId);
					if(fre>1) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
	}
	
	//获取加工设备的id，用于网络传参数
	private List<String> getAssetIdForParam() {
		List<String> assetIdList = new ArrayList<String>();
		for(int i=0; i<mAssetParentLayout.getChildCount(); i++) {
			KoOpStartAssetView child = (KoOpStartAssetView) mAssetParentLayout.getChildAt(i);
			if(child.isAssetInputedOk()) {
				assetIdList.add(child.getAssetId());
			}
		}
		return assetIdList;
	}

	@Override
	public void onBackPressed() {
		KoMainActivity.startAct(this);
		this.finish();
	}

}
