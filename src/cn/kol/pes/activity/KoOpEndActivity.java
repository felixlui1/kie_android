/*-----------------------------------------------------------

-- PURPOSE

--    结束工序的界面

-- History

--	  12-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import cn.kol.pes.model.item.KoQaNeedFillItem;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.widget.KoChildPlanView;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoJobInfoView;
import cn.kol.pes.widget.KoQaItemView;
import cn.kol.pes.widget.KoQaItemView.QaItemDerivedOneBack;
import cn.kol.pes.widget.picktime.KoPickTimeDlg;
import cn.kol.pes.widget.picktime.KoPickTimeDlg.IKoPickTimeDlgBack;

public class KoOpEndActivity extends BaseActivity implements OnClickListener, IKoPickTimeDlgBack {

	private KoJobInfoView mMainInfoView;//显示工单相关信息的封装view
	private TextView mEndDateTime;//展示完成工序的时间
	
	private ViewGroup mScrapQuanParentView;//坏品数量的父容器
	
	private TextView mStaffView;//展示登录员工信息的view
	
	private EditText mScrapQuanEdit;//输入坏品数量的文本框
	private Button mResetTimeBtn;//重设完成时间的按钮
	private Button mEndOpBtn;//完成工序的按钮
	
	private ViewGroup mChildPlanIdParentView;//添加子质量管理计划按钮的父容器
	
	private ViewGroup mQaParentView;//质量管理计划的父容器
	private ViewGroup mChildQaParentView;//子质量管理计划的父容器
	
	private Calendar mOpEndCal = Calendar.getInstance();//完成工序的时间
	
	private String mTransId;
	
	private ImageView mShutPicBtn;//拍照的按钮
	private ViewGroup mPicParent;//图片的父容器
	private HorizontalScrollView mPicScrollView;//拍摄图片横向移动的view
	private List<KoParamItem> mPicListData = new ArrayList<KoParamItem>();
	
	private View mAddQaInfoBtn;//添加质量管理计划的按钮
	private boolean mIsThereStandardQaList = false;//该工序是否有一个质量管理收集计划
	private String mChildPlanId = null;//子计划id数据
	
	private KoQaItemView mClickedScanQaItemView;//质量收集计划的某项能通过扫码输入时，要有一个引用，在回调时通过这个引用把扫描到的数据填写到这个item中
	
	private long mTimeBuffer;//最大可提前的时间
	private int mScrapQuanTotal;//当完成一个工序的最后一个拆分加工时，需要知道这个工序之前所有拆分的坏品数之和
	private boolean mIsLastSeq = false;
	
	private static final String KEY_KEY_TRAINS_ID = "key_key_trains_id";
	private static final String KEY_WIP_ID = "key_wip_id";
	private static final String KEY_ORG_ID = "key_org_id";
	private static final String KEY_OP_CODE = "key_op_code";
	private static final String KEY_OP_DESC = "key_op_desc";
	private static final String KEY_OP_TRX_QUAN = "key_op_trx_quan";
	private static final String KEY_OP_ASSET_TAG_NUM = "key_op_asset_tag_num";
	private static final String KEY_OP_START_TIME = "key_op_start_time";
	
	public static void startAct(Context context, String trainsId, String wipId, String orgId, String opCode, String opDesc, String trxQuan, String assetTagNum, String opStartTime) {
		Intent i = new Intent(context, KoOpEndActivity.class);
		i.putExtra(KEY_KEY_TRAINS_ID, trainsId);
		i.putExtra(KEY_WIP_ID, wipId);
		i.putExtra(KEY_ORG_ID, orgId);
		i.putExtra(KEY_OP_CODE, opCode);
		i.putExtra(KEY_OP_DESC, opDesc);
		i.putExtra(KEY_OP_TRX_QUAN, trxQuan);
		i.putExtra(KEY_OP_ASSET_TAG_NUM, assetTagNum);
		i.putExtra(KEY_OP_START_TIME, opStartTime);
		context.startActivity(i);
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_op_end_activity);
		super.onCreate(savedInstanceState);
		
		mTransId = getIntent().getStringExtra(KEY_KEY_TRAINS_ID);//其他界面传进来的transaction_id
		
		mTitleView.setTitle(R.string.ko_title_end_op);//设置标题
		
		mScrapQuanParentView = (ViewGroup) findViewById(R.id.op_end_scrap_quantity_parent_view);

		mMainInfoView = (KoJobInfoView) findViewById(R.id.op_end_main_info_view);

		mEndDateTime = (TextView) findViewById(R.id.op_end_end_date_time);
		mStaffView = (TextView) findViewById(R.id.op_start_staff_name);
		mScrapQuanEdit = (EditText) findViewById(R.id.op_end_scrap_quantity);
		mScrapQuanEdit.addTextChangedListener(new TextWatcher() {//坏品数量有变化时的回调
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String sS = s.toString().trim();
				if(sS.length()==0) {
					sS = "0";
				}
				
				if(KoDataUtil.isValidNumber(sS)) {//当数据格式合法时，更新质量管理计划各项中的坏品数量这一项
					updatePassQtyInQaView(Integer.valueOf(sS));
				}
			}
		});
		
		mResetTimeBtn = (Button) findViewById(R.id.op_end_reset_date_time_btn);
		mEndOpBtn = (Button) findViewById(R.id.op_end_op_end_btn);
		
		mChildPlanIdParentView = (ViewGroup) findViewById(R.id.op_end_child_plan_id_parent_layout);
		
		mQaParentView = (ViewGroup) findViewById(R.id.op_end_qa_parent_layout);
		mChildQaParentView = (ViewGroup) findViewById(R.id.op_end_child_qa_parent_layout);
		
		mPicParent = (ViewGroup) findViewById(R.id.op_end_pic_parent_layout);
		mShutPicBtn = (ImageView) findViewById(R.id.op_end_shut_pic_btn);
		mPicScrollView = (HorizontalScrollView) findViewById(R.id.op_end_pic_parent_scroll_view);
		
		mAddQaInfoBtn = findViewById(R.id.op_end_add_qa_info_btn);//为添加质量收集计划按钮添加点击事件
		
		mResetTimeBtn.setOnClickListener(this);
		mShutPicBtn.setOnClickListener(this);
		mEndOpBtn.setOnClickListener(this);
		mAddQaInfoBtn.setOnClickListener(this);
		
		mEndDateTime.setText(StringUtils.formatDateTime(mOpEndCal));
		
		KoLoginAdapter staff = CacheUtils.getLoginUserInfo();
		if(staff != null) {
			mStaffView.setText(staff.staffNo+"-"+staff.staffName);
		}
		
		Intent i = getIntent();
		
		mMainInfoView.setData(this, CacheUtils.getSelectedJob(), 
								i.getStringExtra(KEY_OP_CODE), 
								i.getStringExtra(KEY_OP_DESC),
								i.getStringExtra(KEY_OP_TRX_QUAN));//为展示工单信息的view填入数据
		
		showLoadingDlg();
		mKoControl.getQaListNeedFill(i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE), i.getStringExtra(KEY_ORG_ID));//请求质量收集计划
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.op_end_reset_date_time_btn://重设完成工序时间
			KoPickTimeDlg dlg = new KoPickTimeDlg(this, this, mOpEndCal);
			dlg.show();
			break;
			
		case R.id.op_end_shut_pic_btn://拍照
			KoCameraActivity.startActForRes(this, CacheUtils.getLoginUserInfo().staffNo);
			break;
			
		case R.id.op_end_op_end_btn://完成工序
			String scrQuan = mScrapQuanEdit.getText().toString();
			boolean isDerived = isHaveDerivedOneQaItem();
			
			if(CacheUtils.getWeekMap()!=null && 
					CacheUtils.getWeekMap().get(KoDataUtil.getWeekInEnglish())!=null &&
					CacheUtils.getWeekMap().get(KoDataUtil.getWeekInEnglish()).isServerInShutDownTime()) {
				
					DialogUtils.showToast(this, R.string.ko_tips_server_in_shut_time);
			}
			else if(scrQuan.length()==0) {
				DialogUtils.showToast(this, R.string.ko_tips_input_scrap_quan);
			}
			else if(!KoDataUtil.isValidNumber(scrQuan)) {
				DialogUtils.showToast(this, R.string.ko_tips_wrong_scrap_quan);
			}
			else if(!mIsLastSeq && Integer.valueOf(scrQuan)>Integer.valueOf(getIntent().getStringExtra(KEY_OP_TRX_QUAN))) {
				DialogUtils.showToast(this, R.string.ko_tips_scrap_quan_can_not_more_than_trx_quan);
			}
			else if(mIsLastSeq && !isHaveQaItem() && Integer.valueOf(scrQuan)>Integer.valueOf(getIntent().getStringExtra(KEY_OP_TRX_QUAN))) {
				DialogUtils.showToast(this, R.string.ko_tips_scrap_quan_can_not_more_than_trx_quan);
			}
//			else if(mIsLastSeq && 
//					Integer.valueOf(scrQuan)>(Integer.valueOf(getIntent().getStringExtra(KEY_OP_TRX_QUAN))+mScrapQuanTotal)) {
//				DialogUtils.showToast(this, R.string.ko_tips_scrap_quan_can_not_more_than_scrap_and_trx_quan);
//			}
			else if(isDerived && 
					(Integer.valueOf(scrQuan)<mScrapQuanTotal || 
					 Integer.valueOf(scrQuan)>(mScrapQuanTotal+Integer.valueOf(getIntent().getStringExtra(KEY_OP_TRX_QUAN))))) {
				
				DialogUtils.showToast(this, R.string.ko_tips_your_scrap_quan_is_wrong);
			}
			else if(mQaParentView.getChildCount()>0 || mChildQaParentView.getChildCount()>0) {//如果有质量管理项目要填，则首先提交质量管理数据
				if(isQalistViewFillOk()) {
					if(isDerived) {
						float scrapPercent = Float.valueOf(scrQuan)/Float.valueOf(CacheUtils.getSelectedJob().incompleteQuantity);
						LogUtils.e(tag, "scrapPercent="+scrapPercent);
						if(scrapPercent>0.1) {
							KoCommonDialog dlgScrap = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
								@Override
								public void onOkBack() {
									uploadQaData();
								}
								
								@Override
								public void onCancelBack() {
								}
							}, R.string.ko_tips_scrap_number_too_big);
							dlgScrap.setOkCancelBtn(true, true);
							dlgScrap.show();
						}else {
							uploadQaData();
						}
					}
					else {
						uploadQaData();
					}
				}
			}
			else {//如没有质量管理数据，直接结束工序
				endOpReq();
			}
			break;
			
		case R.id.op_end_add_qa_info_btn://添加质量收集计划
			showLoadingDlg();
			Intent i = getIntent();
			mKoControl.getQaListManualAdd(i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE));
			break;
			
		case KoChildPlanView.CHILD_PLAN_ID_VIEW_ID://添加质量收集子计划
			if(mQaParentView.getChildCount() > 0) {
				KoChildPlanView childView = (KoChildPlanView) v;
				LogUtils.e(tag, "KoChildPlanView.CHILD_PLAN_ID_VIEW_ID"+childView.getPlanId());
				showLoadingDlg();
				Intent i2 = getIntent();
				mKoControl.getQaListByPlanId(childView.getPlanId(), i2.getStringExtra(KEY_WIP_ID), i2.getStringExtra(KEY_OP_CODE));
				mChildPlanId = childView.getPlanId();
			}else {
				DialogUtils.showToast(this, R.string.ko_tips_add_qa_item_first);
			}
			break;
			
		default:
			break;
		}
	}
	
	private void uploadQaData() {
		String qaNvList = getQaListStringFromViews(false);
		String qaChildNvList = getQaListStringFromViews(true);
		
		Intent i = getIntent();
		showLoadingDlg(R.string.ko_title_op_triggering);
		mKoControl.submitQaData(CacheUtils.getLoginUserInfo().staffNo, 
								i.getStringExtra(KEY_WIP_ID), 
								i.getStringExtra(KEY_OP_CODE), 
								mTransId, 
								qaNvList, qaChildNvList, !mIsThereStandardQaList, mChildPlanId);
	}
	
	@Override
	public void pickTime(Calendar cal) {//重设时间的回调
		
		Calendar nowCal = Calendar.getInstance();
		Calendar minCal = Calendar.getInstance();
		minCal.setTimeInMillis(nowCal.getTimeInMillis()-mTimeBuffer);
		
		if(nowCal.after(cal) && minCal.before(cal)) {
			mOpEndCal.setTimeInMillis(cal.getTimeInMillis());
			mEndDateTime.setText(StringUtils.formatDateTime(mOpEndCal));
			
			updateOpEndTimeIfExists(mOpEndCal);
		}else {
			DialogUtils.showToast(this, R.string.ko_tips_end_time_cant_after_now);
		}
	}
	
	private void updateOpEndTimeIfExists(Calendar opEndTime) {//质量收集计划项中有时候有完成工序时间这一项，所以要搜索更新一下
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView view = (KoQaItemView) mQaParentView.getChildAt(i);
			view.updateOpEndTime(opEndTime);
		}
		for(int i=0; i<mChildQaParentView.getChildCount(); i++) {
			KoQaItemView view = (KoQaItemView) mChildQaParentView.getChildAt(i);
			view.updateOpEndTime(opEndTime);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode==KoCameraActivity.KEY_REQ_CODE_CAMERA && resultCode==RESULT_OK) {//拍照
			
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
		else if(requestCode==KoCaptureActivity.KEY_REQ_CODE_ZXING && resultCode==RESULT_OK) {//扫码结果的回调
			
			String dataS = data.getStringExtra(KoCaptureActivity.KEY_RES_INTENT);
			LogUtils.e(tag, "onActivityResult():dataS="+dataS);
			mClickedScanQaItemView.setQaValue(dataS);
		}
		
	}
	
	//显示质量收集计划view
	private void showQaItemViews(List<KoQaNeedFillItem> qaList, boolean isChild, String incompleteQuan, String minQuan, String maxQuan) {
		if(isChild) {
			mChildQaParentView.removeAllViews();
		}else {
			mQaParentView.removeAllViews();
			mChildQaParentView.removeAllViews();
		}

		String wipId = CacheUtils.getSelectedJob().wipEntityName;//getIntent().getStringExtra(KEY_WIP_ID);
		int startQuantity = CacheUtils.getSelectedJob().startQuantity;
		String trxQuan = getIntent().getStringExtra(KEY_OP_TRX_QUAN);
		String saItem =  CacheUtils.getSelectedJob().saItem;
		String opCode = getIntent().getStringExtra(KEY_OP_CODE);
		String opName = getIntent().getStringExtra(KEY_OP_DESC);
		String assetTagNum = getIntent().getStringExtra(KEY_OP_ASSET_TAG_NUM);
		String opStartTime = getIntent().getStringExtra(KEY_OP_START_TIME);
		
		//Calendar thisOpStartCal = KoDataUtil.convertStringToCal(opStartTime);
		Calendar minStartCal = KoDataUtil.convertStringToCal(minQuan);
		Calendar maxEndCal = KoDataUtil.isCanConvertStringToCal(maxQuan)?KoDataUtil.convertStringToCal(maxQuan):null;
		
		LogUtils.e(tag, "maxQuan="+maxQuan+KoDataUtil.isCanConvertStringToCal(maxQuan));
		
		if(qaList != null) {
			for(KoQaNeedFillItem qa : qaList) {
				if(qa != null) {
					KoQaItemView qaItemView = new KoQaItemView(this, qa, wipId, incompleteQuan, trxQuan, 
																saItem, opCode, opName, assetTagNum, 
																CacheUtils.getLoginUserInfo(), 
																minStartCal, 
																maxEndCal, 
																mQaItemListener,mQaItemDerivedOneBack);
					if(isChild) {
						mChildQaParentView.addView(qaItemView);
					}else {
						mQaParentView.addView(qaItemView);
					}
				}
			}
		}

		if(isHaveDerivedOneQaItem()) {
			mScrapQuanEdit.setEnabled(false);
			mScrapQuanEdit.setText(String.valueOf(getDerivedOneQaItemTotalSum()));
		}
		
		updatePassQtyAndScrapQty();
	}
	
	//显示子质量管理计划的view
	private void showChildPlanIdViews(List<KoParamItem> childPlanIdList) {
		if(childPlanIdList!=null && childPlanIdList.size()>0) {
			for(KoParamItem childPlanId : childPlanIdList) {
				if(childPlanId != null) {
					KoChildPlanView planIdView = new KoChildPlanView(this, childPlanId);
					planIdView.setOnClickListener(this);
					mChildPlanIdParentView.addView(planIdView);
				}
			}
		}
	}
	
	//某个质量收集计划项是否设置了derivied_flag
	private boolean isHaveDerivedOneQaItem() {
		
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView qaView = (KoQaItemView) mQaParentView.getChildAt(i);
			if(qaView!=null && qaView.isDerivedOne()) {
				return true;
			}
		}
		
		for(int j=0; j<mChildQaParentView.getChildCount(); j++) {
			KoQaItemView qaChildView = (KoQaItemView) mQaParentView.getChildAt(j);
			if(qaChildView!=null && qaChildView.isDerivedOne()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isHaveQaItem() {
		return mQaParentView.getChildCount()>0 || mChildQaParentView.getChildCount()>0;
	}
	
	//将设置了flag的质量收集计划项的值全部加起来
	private int getDerivedOneQaItemTotalSum() {
		int total = 0;
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView qaView = (KoQaItemView) mQaParentView.getChildAt(i);
			if(qaView!=null && qaView.isDerivedOne() && qaView.isValueValid()) {
				total = total+Integer.valueOf(qaView.getQaValue());
			}
		}
		
		for(int j=0; j<mChildQaParentView.getChildCount(); j++) {
			KoQaItemView qaChildView = (KoQaItemView) mChildQaParentView.getChildAt(j);
			if(qaChildView!=null && qaChildView.isDerivedOne() && qaChildView.isValueValid()) {
				total = total+Integer.valueOf(qaChildView.getQaValue());
			}
		}
		
		return total;
	}
	
	//设置了derived_flag的质量计划项，里面的数据改变时，同样要回调出来，更新坏品数量
	private QaItemDerivedOneBack mQaItemDerivedOneBack = new QaItemDerivedOneBack() {
		@Override
		public void derivedOneValueback(int value) {
			updatePassQtyAndScrapQty();
		}
	};
	
	//更新坏品数量
	private void updatePassQtyAndScrapQty() {
		int scrapTotal = getDerivedOneQaItemTotalSum();
		mScrapQuanEdit.setText(String.valueOf(scrapTotal));
		updatePassQtyInQaView(scrapTotal);
	}
	
	//质量收集计划项点了扫描按钮的回调
	private View.OnClickListener mQaItemListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			KoQaItemView qaView = (KoQaItemView) v.getTag();
			if(qaView != null) {
				mClickedScanQaItemView = qaView;
				KoCaptureActivity.startActForRes(KoOpEndActivity.this);
			}
		}
	};
	
	//更新质量管理计划中的合格数
	private void updatePassQtyInQaView(int scrapTotal) {
		
		for(int i=0; i<mQaParentView.getChildCount(); i++) {
			KoQaItemView view = (KoQaItemView) mQaParentView.getChildAt(i);
			view.updateOpPassQty(scrapTotal);
		}
		for(int i=0; i<mChildQaParentView.getChildCount(); i++) {
			KoQaItemView view = (KoQaItemView) mChildQaParentView.getChildAt(i);
			view.updateOpPassQty(scrapTotal);
		}
	}
	
	//将动态展示的质量收集栏位整理成一个字符串
	private String getQaListStringFromViews(boolean isChild) {
		ViewGroup parentV = mQaParentView;
		if(isChild) {
			parentV = mChildQaParentView;
		}
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<parentV.getChildCount(); i++) {
			
			KoQaItemView qaV = (KoQaItemView) parentV.getChildAt(i);
			
			if(qaV.isValueValid() && !qaV.isNullCharNotMandatory()) {//简单判断数据格式，如果错误，直接弹出提示并且返回null
				sb.append(qaV.getCharId()).append(",").append(qaV.getResultColumnName()).append(",").append(qaV.getQaValue());
				if(i<(parentV.getChildCount()-1)) {
					sb.append("@");
				}
			}
			else if(qaV.isNullCharNotMandatory()) {
				
			}
			else {
				return null;
			}
		}
		LogUtils.e(tag, "getQaListStringFromViews:"+sb);
		return sb.toString();
	}
	
	//判断质量收集计划数据项是否都填写正确
	private boolean isQalistViewFillOk() {
		
		if(mQaParentView.getChildCount()>0) {
			for(int i=0; i<mQaParentView.getChildCount(); i++) {
				KoQaItemView qaV = (KoQaItemView) mQaParentView.getChildAt(i);
				if(!qaV.isValueValid()) {
					return false;
				}
			}
		}
		
		if(mChildQaParentView.getChildCount()>0) {
			for(int j=0; j<mChildQaParentView.getChildCount(); j++) {
				KoQaItemView qaChildV = (KoQaItemView) mChildQaParentView.getChildAt(j);
				if(!qaChildV.isValueValid()) {
					return false;
				}
			}
		}
			
		return true;
	}
	
	//请求网络，真正地完成一个工序
	private void endOpReq() {
		Intent i = getIntent();
		int scrapQuan = Integer.valueOf(mScrapQuanEdit.getText().toString());
		if(isHaveDerivedOneQaItem() && 
		   (scrapQuan<mScrapQuanTotal || scrapQuan>(mScrapQuanTotal+Integer.valueOf(i.getStringExtra(KEY_OP_TRX_QUAN))))) {
			
			DialogUtils.showToast(this, R.string.ko_tips_your_scrap_quan_is_wrong);
		}
		else {
			showLoadingDlg(R.string.ko_title_op_ending);
			
			mKoControl.opEnd(mTransId,
							 isHaveDerivedOneQaItem()?String.valueOf(scrapQuan-mScrapQuanTotal):mScrapQuanEdit.getText().toString(), 
					         String.valueOf(Calendar.getInstance().getTimeInMillis()), 
					         CacheUtils.getLoginUserInfo().staffNo, i.getStringExtra(KEY_ORG_ID), i.getStringExtra(KEY_WIP_ID), i.getStringExtra(KEY_OP_CODE));
		}
	}
	
	//某些工单不显示添加质量收集计划按钮
	private boolean isInSomeWipThatNotShowAddQaInfoBtn() {
		String wipEntityName = CacheUtils.getSelectedJob().wipEntityName;
		
		if(wipEntityName!=null && wipEntityName.length()>3) {
			String wip = wipEntityName.substring(0, 2);
			LogUtils.e(tag, "isInSomeWipThatNotShowAddQaInfoBtn():wip="+wip);
			if("TB".equals(wip) || "TL".equals(wip) || "T1".equals(wip) || "TZ".equals(wip)) {// || "IT".equals(wip)
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		
		return new KolPesControlBack() {
			//质量管理信息的回调
			@Override
			public void qaListNeedFillBack(boolean isSuc, boolean isLastSeq, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, List<KoParamItem> childPlanIdList, String msg, String timeBuffer, String scrapQuanTotal) {
				if(isSuc) {
					LogUtils.e(tag, "timeBuffer="+timeBuffer);
					mTimeBuffer = Integer.valueOf(timeBuffer)*60*1000;
					String opCode = getIntent().getStringExtra(KEY_OP_CODE);
					if(opCode!=null && opCode.startsWith("T")) {
						mTimeBuffer = mTimeBuffer*10000;
					}
					mScrapQuanTotal = Integer.valueOf(scrapQuanTotal);
					mIsLastSeq = isLastSeq;
					
					if((qaList!=null && qaList.size()>0) && isLastSeq) {
						mIsThereStandardQaList = true;
						showQaItemViews(qaList, false, incompleteQuan, minStart, maxEnd);
					}
					else if(!isInSomeWipThatNotShowAddQaInfoBtn() && isLastSeq) {
						mAddQaInfoBtn.setVisibility(View.VISIBLE);
					}
					
					if(!isInSomeWipThatNotShowAddQaInfoBtn() && isLastSeq) {
						showChildPlanIdViews(childPlanIdList);
					}
					
//					if(isLastSeq) {
//						mScrapQuanParentView.setVisibility(View.VISIBLE);
//					}
				}
				else {
					DialogUtils.showToast(KoOpEndActivity.this, msg);
				}
				
				dismissLoadingDlg();
			}
			
			//根据planid添加的子计划的数据回调
			@Override
			public void qaListByPlanIdBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, String msg) {
				if(isSuc) {
					showQaItemViews(qaList, true, incompleteQuan, minStart, maxEnd);
				}else {
					DialogUtils.showToast(KoOpEndActivity.this, msg);
				}
				dismissLoadingDlg();
			}

			//提交质量管理数据的回调
			@Override
			public void submitQaDataBack(boolean isSuc, String msg) {
				if(isSuc) {
					endOpReq();
				}else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoOpEndActivity.this, msg);
				}
			}

			//结束工序的回调
			@Override
			public void opEndBack(boolean isSuc, String transId, String msg) {
				if(isSuc) {
					KoUploadPicService.startSer(KoOpEndActivity.this, transId, KoDataUtil.picPathDescListToString(mPicListData), true, true);
					KoMainActivity.startAct(KoOpEndActivity.this);
					KoOpEndActivity.this.finish();
				}else {
					DialogUtils.showToast(KoOpEndActivity.this, msg);
				}
				dismissLoadingDlg();
			}

			//手动添加的质量收集计划的数据回调上
			@Override
			public void qaListManualAddBack(boolean isSuc, String incompleteQuan, String minStart, String maxEnd, List<KoQaNeedFillItem> qaList, String msg) {
				if(isSuc) {
					if(qaList!=null && qaList.size()>0) {
						showQaItemViews(qaList, false, incompleteQuan, minStart, maxEnd);
					}
				}else {
					DialogUtils.showToast(KoOpEndActivity.this, msg);
				}
				dismissLoadingDlg();
			}
			
		};
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
	
	//图标微缩图点击事件，用于触发编辑界面
	private OnClickListener mImgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			KoParamItem picPathDesc = (KoParamItem) v.getTag();
			if(picPathDesc!=null) {
				KoViewPicActivity.startActForRes(KoOpEndActivity.this, picPathDesc.name, picPathDesc.value);
			}
		}
	};
	
	//移除拍的照片
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
	
	@Override
	public void onBackPressed() {
		KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(this, new CommonDlgClick() {
			@Override
			public void onOkBack() {
				KoMainActivity.startAct(KoOpEndActivity.this);
				KoOpEndActivity.this.finish();
			}
			
			@Override
			public void onCancelBack() {
			}
		}, getString(R.string.ko_title_sure_exit_op_end_activity));
		
		dlg.setOkCancelBtn(true, true);
		dlg.show();
	}

}
