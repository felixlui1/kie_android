/*-----------------------------------------------------------

-- PURPOSE

--   推送消息列表的Adapter。

-- History

--	  19-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoPushMsgItem;

public class KoPushMsgListAdapter extends BaseAdapter {
	
	private LayoutInflater mLi;
	private List<KoPushMsgItem> mOpListData;
	
	public KoPushMsgListAdapter(Context context) {
		mLi = LayoutInflater.from(context);
	}
	
	public void setData(List<KoPushMsgItem> assetList) {
		mOpListData = assetList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(mOpListData != null) {
			return mOpListData.size();
		}
		return 0;
	}

	@Override
	public KoPushMsgItem getItem(int position) {
		if(mOpListData != null) {
			return mOpListData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mLi.inflate(R.layout.ko_push_msg_list_item_layout, null);
		}
		
		KoPushMsgItem msgData = getItem(position);
		
		if(msgData != null) {
			TextView title = (TextView) convertView.findViewById(R.id.push_msg_item_title);
			TextView msg = (TextView) convertView.findViewById(R.id.push_msg_item_msg);

			title.setText(msgData.title);
			msg.setText(msgData.msg);
		}
		
		return convertView;
	}

}
