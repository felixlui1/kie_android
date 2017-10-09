/*-----------------------------------------------------------

-- PURPOSE

--    登陆后的界面，也是主界面。
--	  

-- Historyb

--	  10-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.Constants;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoOpStartedListAdapter;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpItem;
import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoListDialg;

public class KoMainActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	private EditText mJobText;//显示工单名称的view
	private TextView mOpText;//显示工序名称的view
	private String mCodeType = "";//判断扫码输入的是工单还是工序的数据标识
	private List<KoOpItem> mOpList;//工序列表的数据
	private View mRouteSeqBtn;//扫描输入工序的按钮
	private KoOpItem mSelectedOp;//已经选择的工序数据
	private KoOpItem mCurOp;//当前应该加工的工序数据
	private View mSearchWipBtn;	//根据已输入工单信息搜索工序列表的按钮
	
	private ListView mOpListView;//展示未完成工序列表的view
	private KoOpStartedListAdapter mListAdapter;//未完成工序列表的数据适配器
	private ViewGroup mListParentLayout;//列表的父容器
	
	private List<KoOpStartedItem> mStartedOpList = null;//未完成工序列表的数据
	private boolean mIsCurOpReallyCompleted = true;//工单附带的当前工序是否真的加工完成

	public static void startAct(Context context) {
		Intent i = new Intent(context, KoMainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_main_activity);//此行代码必须写在super.onCreate(savedInstanceState)前面，
												  //因为在父类的onCreate(savedInstanceState)中，实例化了mTitleView，
												  //这需要首先设置setContentView
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_app_name);

		mJobText = (EditText) findViewById(R.id.main_job_id_edit);
		mOpText = (TextView) findViewById(R.id.main_select_op_edit);
		
		mSearchWipBtn = findViewById(R.id.main_job_id_search_btn);
		mSearchWipBtn.setOnClickListener(this);
		
		mRouteSeqBtn = findViewById(R.id.main_camera_btn_op);
		
		TextView staffName = (TextView) findViewById(R.id.main_staff_name);
		
		KoLoginAdapter staff = CacheUtils.getLoginUserInfo();//从缓存中读取登录员工信息,这也是本应用中各个界面交换信息的一种方式
		if(staff!=null && staff.staffNo!=null && staff.staffName!=null) {//判断信息是否为空，如果可用，则显示操作员工信息
			staffName.setText(staff.staffNo.trim()+"-"+staff.staffName.trim());
		}
		mJobText.setOnClickListener(this);
		mOpText.setOnClickListener(this);
		findViewById(R.id.main_camera_btn_job).setOnClickListener(this);//开启扫码界面的按钮
		mRouteSeqBtn.setOnClickListener(this);
		findViewById(R.id.main_start_op_btn).setOnClickListener(this);//开启工序的按钮
		findViewById(R.id.main_end_op_btn).setOnClickListener(this);//完成工序的按钮
		findViewById(R.id.main_search_job_btn).setOnClickListener(this);//查看工单的按钮
		findViewById(R.id.main_check_asset_btn).setOnClickListener(this);//点检设备的按钮
		
		mRouteSeqBtn.setEnabled(false);
		mOpText.setEnabled(false);
		
		mOpListView = (ListView) findViewById(R.id.main_op_started_list_list_view);
		mListAdapter = new KoOpStartedListAdapter(this);
		mOpListView.setAdapter(mListAdapter);
		mOpListView.setOnItemClickListener(this);
		mOpListView.setOnItemLongClickListener(this);
		
		mListParentLayout = (ViewGroup) findViewById(R.id.main_op_started_list_parent_layout);
		mListParentLayout.setVisibility(View.GONE);
		
		getJobOpListAndtxnList();
		
	}
	
	private void getJobOpListAndtxnList() {//根据已经缓存的工单获取一下工单数据，因为数据有可能需要刷新
		showLoadingDlg();
		if(CacheUtils.getSelectedJob()!=null) {
			String wipName = CacheUtils.getSelectedJob().wipEntityName;
			mJobText.setText(wipName);
			getJob(wipName);
		}
		else {
			mKoControl.getStartedOpList(CacheUtils.getLoginUserInfo().staffNo);//根据登录员工获取已开启的未完成工序
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.main_job_id_search_btn://搜索工单列表
			if(mJobText.getText().length()>1) {
				showLoadingDlg(R.string.ko_title_loading_job);
				getJob(mJobText.getText().toString());
			}
			else {
				DialogUtils.showToast(this, R.string.ko_tips_input_job_num);
			}
			break;
		
		case R.id.main_camera_btn_job://扫码输入工单
			mCodeType = "main_camera_btn_job";
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.main_camera_btn_op://扫码输入工序
			mCodeType = "main_camera_btn_op";
			KoCaptureActivity.startActForRes(this);
			break;
			
		case R.id.main_start_op_btn://打开“开启工序”界面
			String job = mJobText.getText().toString();
			String op = mOpText.getText().toString();
			if(job.length()==0 || job.equals(getString(R.string.ko_title_scan_to_input_job_num)) || CacheUtils.getSelectedJob()==null) {
				DialogUtils.showToast(this, R.string.ko_tips_input_job_num_first);
			}
			else if(op.length()==0 || op.equals(getString(R.string.ko_title_scan_to_input_op)) || mSelectedOp==null) {
				DialogUtils.showToast(this, R.string.ko_tips_input_one_op_first);
			}
			else {
				if(isOpGoToPreSeq(mSelectedOp, mCurOp)) {//判断是否回跳了工序
					KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(KoMainActivity.this, new KoCommonDialog.CommonDlgClick() {
						
						@Override
						public void onOkBack() {
							CacheUtils.setSelectedOp(mSelectedOp);
							KoOpStartActivity.startAct(KoMainActivity.this, KoOpStartActivity.class);
						}
						
						@Override
						public void onCancelBack() {
							
						}
					}, R.string.ko_tips_sure_goto_pre_op_seq);
					dlg.setOkCancelBtn(true, true);
				}
				else {
					CacheUtils.setSelectedOp(mSelectedOp);
					KoOpStartActivity.startAct(this, KoOpStartActivity.class);
				}
			}
			break;
			
		case R.id.main_end_op_btn://开启“完成工序”界面
			KoOpStartedListActivity.startAct(this, KoOpStartedListActivity.class);
			break;
			
		case R.id.main_search_job_btn://打开“查看工单”界面
			String job2 = mJobText.getText().toString();
			if(job2.length()==0 || job2.equals(getString(R.string.ko_title_scan_to_input_job_num)) || CacheUtils.getSelectedJob()==null) {
				DialogUtils.showToast(this, R.string.ko_tips_input_job_num_first);
			}else {
				KoViewJobActivity.startAct(this, KoViewJobActivity.class);
			}
			break;
			
		case R.id.main_job_id_edit:
			
			break;
			
		case R.id.main_select_op_edit://显示工单对应的工序列表对话框
			showOpList(mOpList, mSelectedOp);
			break;
			
		case R.id.main_check_asset_btn://打开“点检设备”界面
			KoAssetMainActivity.startAct(this, KoAssetMainActivity.class);
			break;
		}
	}
	
	private boolean isOpGoToPreSeq(KoOpItem selectedOp, KoOpItem curOp) {//判断是否回跳了工序
		
		if(selectedOp!=null && curOp!=null) {
			if(selectedOp.operationSeqNum < curOp.operationSeqNum) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isOpJumpSeq(KoOpItem selectedOp, KoOpItem curNextOp) {//判断是否跳过了一些工序
		
		if(selectedOp!=null && curNextOp!=null) {
			if(selectedOp.operationSeqNum > curNextOp.operationSeqNum) {
				return true;
			}
		}
		
		return false;
	}
	
	//接收扫码工单和工序的回调，收到工单号后，立即查询工单并获取相应工序。接收到工序号后立即从已有的工序中筛选出该工序。
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			
			if("main_camera_btn_job".equals(mCodeType)) {//工单回调
				if(dataS!=null && dataS.length()>1) {
					getJob(dataS);
					showLoadingDlg(R.string.ko_title_loading_job);
				}
				else {
					clearJobAndOpWhenInputWrongWip();
					DialogUtils.showToast(this, R.string.ko_tips_wrong_job_num);
				}
			}
			else if("main_camera_btn_op".equals(mCodeType)) {//工序回调
				KoOpItem opData = getRouteSeqById(dataS);
				if(opData != null) {
					mSelectedOp = opData;
					mOpText.setText(opData.standardOperationCode +" "+ opData.operationDescription);
				}else {
					DialogUtils.showToast(this, R.string.ko_tips_there_no_such_routing_code_in_this_gong_dan);
				}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	//网络请求的回调，需要哪个回调，就复写哪个函数
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			
			//获取的工单数据的回调
			@Override
			public void getJobBack(boolean isSuc, List<KoJobItem> jobList, String msg) {
				if(isSuc) {//如果请求成功，则显示相关数据
					if(jobList.size()>0) {
						if(jobList.size()>1) {
							showJobList(jobList, jobList.get(0));
						}else {
							thingsTodoWhenJobListBack(jobList.get(0));
						}
					}
				}else {//如果失败，清除相关的显示和数据
					clearJobAndOpWhenInputWrongWip();
					dismissLoadingDlg();
					DialogUtils.showToast(KoMainActivity.this, msg);
				}
			}

			//工序数据的回调
			@Override
			public void getOpBack(boolean isSuc, List<KoOpItem> opList, String msg) {
				if(isSuc) {
					mOpList = opList;
					mRouteSeqBtn.setEnabled(true);
					mOpText.setEnabled(true);
					
					KoJobItem job = CacheUtils.getSelectedJob();
					if(job!=null && job.curOperationId!=null && job.curOperationId.length()>0) {
						for(KoOpItem op : opList) {
							if(op!=null && op.standardSequenceId!=null && op.standardSequenceId.equals(job.curOperationId)) {
								mSelectedOp = op;
								mOpText.setText(mSelectedOp.standardOperationCode+" "+mSelectedOp.operationDescription);
								mCurOp = op;
								break;
							}
						}
						
						if("0".equals(job.curOperationId)) {
							DialogUtils.showToast(KoMainActivity.this, R.string.ko_tips_this_job_already_finished);
						}
					}
					
					LogUtils.e(tag, "mOpList.size()="+mOpList.size());
				}
				else {
					DialogUtils.showToast(KoMainActivity.this, msg);
				}

				mKoControl.getStartedOpList(CacheUtils.getSelectedJob().wipEntityId);//获取到工序数据后，开始请求未完成工序列表
			}
			
			//已开启工序列表的回调
			@Override
			public void opStartedListBack(boolean isSuc, List<KoOpStartedItem> opList, boolean isOpCompleted, String curWorkingOpCode, String msg) {
				dismissLoadingDlg();
				mIsCurOpReallyCompleted = isOpCompleted;
				if(isSuc) {
					mStartedOpList = opList;
					mListAdapter.setData(opList);
					mListAdapter.notifyDataSetChanged();
					
					if(mStartedOpList.size()>0) {
						mListParentLayout.setVisibility(View.VISIBLE);
					}else {
						mListParentLayout.setVisibility(View.GONE);
					}
					
					if(mOpList!=null && mStartedOpList.size()>0) {//存在未完成工序时的情况
						for(KoOpItem op : mOpList) {
							if(op!=null && op.standardOperationCode!=null && op.standardOperationCode.equals(mStartedOpList.get(0).fmOperationCode)) {
								mSelectedOp = op;
								mOpText.setText(mSelectedOp.standardOperationCode+" "+mSelectedOp.operationDescription);
								break;
							}
						}
					}
					else if(mOpList!=null && !isOpCompleted && curWorkingOpCode.length()>0) {//没有未完成工序，但是这个工单的某个工序却又没有足额完成
						for(KoOpItem op : mOpList) {
							if(op!=null && op.standardOperationCode!=null && op.standardOperationCode.equals(curWorkingOpCode)) {
								mSelectedOp = op;
								mOpText.setText(mSelectedOp.standardOperationCode+" "+mSelectedOp.operationDescription);
								mCurOp = op;
								break;
							}
						}
					}
				}
				else {
					mStartedOpList = null;
					mListParentLayout.setVisibility(View.GONE);
				}
			}

			//删除已经开启工序的回调
			@Override
			public void getOpDeleteBack(boolean isSuc, String msg) {
				if(isSuc) {
					getJobOpListAndtxnList();//成功删除某个工序后要重新刷新未完成工序列表
				}else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoMainActivity.this, msg);
				}
			}
			
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//点击某个未完成工序的回调，打开完成工序界面
		KoOpStartedItem opItem = (KoOpStartedItem) arg0.getAdapter().getItem(arg2);
		if(opItem != null) {
			CacheUtils.setSelectedJob(opItem.jobObj);
			KoOpEndActivity.startAct(KoMainActivity.this, opItem.transactionId, opItem.jobObj.wipEntityId, opItem.jobObj.organizationId, opItem.fmOperationCode, opItem.opDesc, opItem.trxQuantity, opItem.assettagNumber, opItem.creationDate);
		}
	}
	
	@Override
	public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int position, long arg3) {//长按未完成工序，删除
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			
			@Override
			public void onOkBack() {
				KoOpStartedItem opItem = (KoOpStartedItem) arg0.getAdapter().getItem(position);
				if(opItem != null) {
					showLoadingDlg(R.string.ko_title_is_deleting_op);
					mKoControl.opDeleteAnOp(opItem.transactionId);
				}
			}
			
			@Override
			public void onCancelBack() {
			}
		}, R.string.ko_title_sure_delete_op);
		dlg.setOkCancelBtn(true, true);
		return true;
	}
	
	private void clearJobAndOpWhenInputWrongWip() {//输入错误的工单号时，进行必要的清理
		mJobText.setText("");
		mOpText.setText("");
		mSelectedOp = null;
	}
	
	private void thingsTodoWhenJobListBack(KoJobItem job) {//请求工单数据成功后，显示相应的工单工序数据
		mJobText.setText(job.wipEntityId+" "+job.wipEntityName);
		mKoControl.getOp(job.commonRoutingSequenceId);
		showLoadingDlg(R.string.ko_title_loading_operation);
		mJobText.setText(job.wipEntityName);
		
		CacheUtils.setSelectedJob(job);
	}
	
	//通过工序ID获取相应工序信息
	private KoOpItem getRouteSeqById(String id) {
		
		if(mOpList!=null && id!=null) {
			for(KoOpItem seq : mOpList) {
				if(seq!=null && seq.standardOperationCode.equals(id)) {//T000
					return seq;
				}
			}
		}
		return null;
	}
	
	//根据工单号查询工单
	private void getJob(String wipId) {
		mOpText.setText("");
		mSelectedOp = null;
		mKoControl.getJobList(wipId);
	}
	
	//显示选择工序的对话框
	private void showOpList(List<KoOpItem> opList, KoOpItem selData) {
		KoOpListDlg dlg = new KoOpListDlg(this, selData, opList);
		dlg.show();
	}
	
	//显示选择工单的对话框
	private void showJobList(List<KoJobItem> jobList, KoJobItem selData) {
		KoJobListDlg dlg = new KoJobListDlg(this, selData, jobList);
		dlg.show();
	}
	
	//预留的让用户在列表中选择工单的对话框
	public class KoJobListDlg extends KoListDialg<KoJobItem> {

		public KoJobListDlg(Activity context, KoJobItem selectedData, List<KoJobItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoJobItem selectedItem) {
			return selectedItem.wipEntityName;
		}

		@Override
		public boolean isSelectedObjEquals(KoJobItem selectedData, KoJobItem item) {
			if(selectedData!=null && item!=null && selectedData.wipEntityId!=null && selectedData.wipEntityId.equals(item.wipEntityId)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoJobItem selData) {
			thingsTodoWhenJobListBack(selData);
		}
	}
	
	//工序对话框的封装类
	public class KoOpListDlg extends KoListDialg<KoOpItem> {

		public KoOpListDlg(Activity context, KoOpItem selectedData, List<KoOpItem> listData) {
			super(context, selectedData, listData);
		}

		@Override
		public String getStringToShowFromObj(KoOpItem selectedItem) {
			return selectedItem.standardOperationCode+" "+selectedItem.operationDescription;
		}

		@Override
		public boolean isSelectedObjEquals(KoOpItem selectedData, KoOpItem item) {
			
			if(selectedData!=null && item!=null && selectedData.standardOperationCode!=null && 
			   selectedData.standardOperationCode.equals(item.standardOperationCode)) {
				return true;
			}
			return false;
		}

		@Override
		public void selectedItemData(KoOpItem selData) {

			if((mStartedOpList!=null && mStartedOpList.size()>0 && !selData.standardOperationCode.equals(mStartedOpList.get(0).fmOperationCode)) 
			   || !mIsCurOpReallyCompleted) {
				DialogUtils.showToastShort(KoMainActivity.this, R.string.ko_tips_new_op_cant_start_when_there_is_uncomplete_one);
			}
			else if(isOpGoToPreSeq(selData, mCurOp) || isOpJumpSeq(selData, mCurOp)) {
				if((isOpGoToPreSeq(selData, mCurOp) && CacheUtils.getLoginUserInfo().isCanJumpOp()) ||
				    isOpJumpSeq(selData, mCurOp)) {
					mOpText.setText(selData.standardOperationCode+" "+selData.operationDescription);
					mSelectedOp = selData;
					DialogUtils.showToastShort(KoMainActivity.this, R.string.ko_tips_op_jumped_by_high_level_staff);
				}
				else {
					mOpText.setText(mCurOp.standardOperationCode+" "+mCurOp.operationDescription);
					mSelectedOp = mCurOp;
					DialogUtils.showToastShort(KoMainActivity.this, R.string.ko_tips_op_no_right_to_jump_op);
				}
			}
			else if("-1".equals(CacheUtils.getSelectedJob().curOperationId)) {
				DialogUtils.showToastShort(KoMainActivity.this, R.string.ko_tips_op_this_job_has_already_done);
			}
			else {
				mOpText.setText(selData.standardOperationCode+" "+selData.operationDescription);
				mSelectedOp = selData;
			}
		}
	}

	//按了back健的回调，提示用户是否退出应用
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			
			@Override
			public void onOkBack() {
				CacheUtils.clearAllCache();
				KoMainActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
				
			}
		}, getString(R.string.ko_title_sure_exit_app));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

}
