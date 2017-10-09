/*-----------------------------------------------------------

-- PURPOSE

--    查看工单的界面

-- History

--	  15-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.item.KoJobItem;
import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;
import cn.kol.pes.widget.KoJobViewOpInfoItemView;

public class KoViewJobActivity extends BaseActivity implements OnClickListener {

	private TextView mWipName;//工单名称的view
	private TextView mSaId;//展示saId的view
	private TextView mWipInputQuan;//展示该工单的投入数
	
	private ViewGroup mOpParentLayout;//工序信息控件的父容器
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_job_view_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_view_job);

		mWipName = (TextView) findViewById(R.id.job_view_wip_name);
		mSaId = (TextView) findViewById(R.id.job_view_sa_id);
		mWipInputQuan = (TextView) findViewById(R.id.job_view_input_quan);
		mOpParentLayout = (ViewGroup) findViewById(R.id.job_view_op_list_parent);
		
		getOpListByWip();//根据工单id获取工序信息
	}
	
	private void getOpListByWip() {//通过工单号获取工序列表
		KoJobItem job = CacheUtils.getSelectedJob();
		if(job != null) {
			mWipName.setText(job.wipEntityName);
			mSaId.setText(job.saItem);
			mWipInputQuan.setText(String.valueOf(job.incompleteQuantity));
			
			showLoadingDlg();
			mKoControl.getOpMoveAllList(job.wipEntityId);
		}
	}
	
	private void showOpList(List<KoOpStartedItem> opList) {//展示工序列表
		mOpParentLayout.removeAllViews();
		if(opList!=null && opList.size()>0) {
			for(int i=0; i<opList.size(); i++) {
				KoOpStartedItem opData = opList.get(i);
				KoJobViewOpInfoItemView opView = new KoJobViewOpInfoItemView(this, opData, mOpItemClick, mOpItemLongClick);
				opView.setLineShow(i<(opList.size()-1));
				mOpParentLayout.addView(opView);
			}
			mOpParentLayout.setVisibility(View.VISIBLE);
		}else {
			mOpParentLayout.setVisibility(View.GONE);
		}
	}
	
	private View.OnClickListener mOpItemClick = new OnClickListener() {//工序某项单击的回调。进入结束工序界面
		
		@Override
		public void onClick(View v) {
			KoOpStartedItem opData = (KoOpStartedItem) v.getTag();
			if(opData!=null && opData.opEndDate!=null && opData.opEndDate.trim().length()==0) {
				KoOpEndActivity.startAct(KoViewJobActivity.this, opData.transactionId, opData.jobObj.wipEntityId, opData.jobObj.organizationId, opData.fmOperationCode, opData.opDesc, opData.trxQuantity, opData.assettagNumber, opData.creationDate);
				KoViewJobActivity.this.finish();
			}
		}
	};
	
	private OnLongClickListener mOpItemLongClick = new OnLongClickListener() {//长按弹出删除工序的对话框
		
		@Override
		public boolean onLongClick(final View v) {
			
			final KoOpStartedItem opItem = (KoOpStartedItem) v.getTag();
			
			if("1".equals(opItem.interfaced)) {
				DialogUtils.showToast(KoViewJobActivity.this, R.string.ko_tips_uploaded_op_cant_delete);
			}
			else {
				KoCommonDialog dlg = KoCommonDialog.getDlgAndShow(KoViewJobActivity.this, new CommonDlgClick() {
					
					@Override
					public void onOkBack() {
						
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
			}
			return false;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			
		default:
			break;
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		
		return new KolPesControlBack() {

			//获取该工单的所有工序的数据回调
			@Override
			public void opMoveAllListBack(boolean isSuc, List<KoOpStartedItem> opList, String msg) {
				if(isSuc) {
					showOpList(opList);
				}else {
					mOpParentLayout.setVisibility(View.GONE);
					DialogUtils.showToast(KoViewJobActivity.this, msg);
				}
				dismissLoadingDlg();
			}
			
			//删除工序的数据回调
			@Override
			public void getOpDeleteBack(boolean isSuc, String msg) {
				if(isSuc) {
					getOpListByWip();
				}else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoViewJobActivity.this, msg);
				}
			}

		};
	}
	
	@Override
	public void onBackPressed() {
		KoMainActivity.startAct(this);
		this.finish();
	}
	
}
