/*-----------------------------------------------------------

-- PURPOSE

--    推送消息列表的界面

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;
import cn.kol.pes.model.adapter.KoPushMsgListAdapter;
import cn.kol.pes.model.item.KoPushMsgItem;

public class KoPushMsgListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private ListView mMsgListView;
	private KoPushMsgListAdapter mListAdapter;
	
	private ViewGroup mListParentLayout;
	private TextView mErrorMsgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_push_msg_list_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_push_msg_list);
		
		mMsgListView = (ListView) findViewById(R.id.push_msg_list_list_view);
		mListAdapter = new KoPushMsgListAdapter(this);
		mMsgListView.setAdapter(mListAdapter);
		
		mListParentLayout = (ViewGroup) findViewById(R.id.push_msg_list_parent_layout);
		mErrorMsgView = (TextView) findViewById(R.id.push_msg_list_error_msg_text_view);
		
		mListParentLayout.setVisibility(View.GONE);
		mErrorMsgView.setVisibility(View.GONE);
		
		showLoadingDlg();
		mKoControl.getPushMsgList("", String.valueOf(KoPushMsgService.getTransIdLastNotify(this)), false);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
	
	@Override
	protected KolPesControlBack initControlBack() {
		return new KolPesControlBack() {
			@Override
			public void getPushMsgListBack(boolean isSuc, List<KoPushMsgItem> msgList, String msg) {
				dismissLoadingDlg();
				if(isSuc && msgList!=null && msgList.size()>0) {
					mListAdapter.setData(msgList);
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
			
		};
	}

}
