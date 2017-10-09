/*-----------------------------------------------------------

-- PURPOSE

--    已开启工序列表的界面

-- History

--	  1-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.DialogUtils;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoOpStartedListAdapter;
import cn.kol.pes.model.item.KoOpStartedItem;
import cn.kol.pes.widget.KoCommonDialog;
import cn.kol.pes.widget.KoCommonDialog.CommonDlgClick;


public class KoOpStartedListActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {

	private ListView mOpListView;//已开启工序列表的view
	private KoOpStartedListAdapter mListAdapter;//已开启工序的数据适配器
	
	private ViewGroup mListParentLayout;//已开启列表的父控件
	private TextView mErrorMsgView;//显示报错信息的view
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_op_started_list_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_op_started_list);
		
		mOpListView = (ListView) findViewById(R.id.op_started_list_list_view);
		mListAdapter = new KoOpStartedListAdapter(this);
		mOpListView.setAdapter(mListAdapter);
		mOpListView.setOnItemClickListener(this);
		mOpListView.setOnItemLongClickListener(this);
		
		mListParentLayout = (ViewGroup) findViewById(R.id.op_started_list_parent_layout);
		mErrorMsgView = (TextView) findViewById(R.id.op_started_list_error_msg_text_view);
		
		mListParentLayout.setVisibility(View.GONE);
		mErrorMsgView.setVisibility(View.GONE);
		
		mKoControl.getStartedOpList(CacheUtils.getLoginUserInfo().staffNo);//获取已开启的工序
		showLoadingDlg();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//单击而完成列表某项的回调，进入结束工序界面
		KoOpStartedItem opItem = (KoOpStartedItem) arg0.getAdapter().getItem(arg2);
		if(opItem != null) {
			CacheUtils.setSelectedJob(opItem.jobObj);
			KoOpEndActivity.startAct(KoOpStartedListActivity.this, opItem.transactionId,opItem.jobObj.wipEntityId,opItem.jobObj.organizationId, opItem.fmOperationCode, opItem.opDesc, opItem.trxQuantity, opItem.assettagNumber, opItem.creationDate);
			KoOpStartedListActivity.this.finish();
		}
	}
	
	@Override
	public boolean onItemLongClick(final AdapterView<?> arg0, View arg1, final int position, long arg3) {//长按某一项，弹出删除工序的对话框
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
		return false;
	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {

			//获取已经开启工序的数据回调
			@Override
			public void opStartedListBack(boolean isSuc, List<KoOpStartedItem> opList, boolean isOpCompleted, String curWorkingOpCode, String msg) {
				dismissLoadingDlg();
				if(isSuc && opList!=null && opList.size()>0) {
					mListAdapter.setData(opList);
					mListAdapter.notifyDataSetChanged();
					
					mListParentLayout.setVisibility(View.VISIBLE);
					mErrorMsgView.setVisibility(View.GONE);
				}
				else {
					mListParentLayout.setVisibility(View.GONE);
					mErrorMsgView.setVisibility(View.VISIBLE);
					mErrorMsgView.setText(msg);
				}
			}
			
			//删除工序的数据回调
			@Override
			public void getOpDeleteBack(boolean isSuc, String msg) {
				if(isSuc) {
					mKoControl.getStartedOpList(CacheUtils.getLoginUserInfo().staffNo);
				}else {
					dismissLoadingDlg();
					DialogUtils.showToast(KoOpStartedListActivity.this, msg);
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
