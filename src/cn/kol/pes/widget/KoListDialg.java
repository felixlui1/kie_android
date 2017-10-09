/*-----------------------------------------------------------

-- PURPOSE

--    封装的列表对话框类。用到它的有选择工序对话框、选择故障类型对话框等

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import java.util.List;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.kol.pes.R;

public abstract class KoListDialg<E> extends KoCommomDialogFatherClass implements OnItemClickListener {
	
	private ViewGroup mRootLayout;
	private ListView mListView;
	private E mSelectedData;
	private List<E> mListData;
	private ListDlgAdapter mListAdapter;
	private LayoutInflater mLi;
	
	public abstract String getStringToShowFromObj(E selectedItem);//返回的String就是列表的item要显示的信息
	public abstract boolean isSelectedObjEquals(E selectedData, E item);//判断两个值是否相等，用于默认选中某个item
	public abstract void selectedItemData(E selData);//点击某个item后的回调

	public KoListDialg(Activity context, E selectedData, List<E> listData) {
		super(context);

		mSelectedData = selectedData;
		mListData = listData;
		
		mLi = LayoutInflater.from(context);
		mRootLayout = (ViewGroup) mLi.inflate(R.layout.ko_list_dialog, null);
		mListView = (ListView) mRootLayout.findViewById(R.id.list_dialog_list_view);
		mListAdapter = new ListDlgAdapter();
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(this);
		
		mListView.setSelection(getSelectionPosition(selectedData, listData));
		
		this.setContentView(mRootLayout);
	}
	
	private int getSelectionPosition(E selectedData, List<E> listData) {
		if(selectedData!=null && listData!=null && listData.size()>0) {
			for(int i=0; i<listData.size(); i++) {
				E item = listData.get(i);
				if(isSelectedObjEquals(selectedData, item)) {
					return i;
				}
			}
		}
		
		return 0;
	}
	
	protected void setAllListData(E selectedData, List<E> listData) {
		mSelectedData = selectedData;
		mListData = listData;
		mListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		E e = (E) arg0.getAdapter().getItem(arg2);
		if(e != null) {
			selectedItemData(e);
			mSelectedData = e;
			mListAdapter.notifyDataSetChanged();
			this.dismiss();
		}
	}

	private class ListDlgAdapter extends BaseAdapter {
		
		public ListDlgAdapter() {
			
		}

		@Override
		public int getCount() {
			if(mListData!=null) {
				return mListData.size();
			}
			return 0;
		}

		@Override
		public E getItem(int position) {
			if(mListData!=null) {
				return mListData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			E selectedItem = getItem(position);
			
			if(convertView == null) {
				convertView = mLi.inflate(R.layout.ko_list_dialog_item_layout, null);
			}
			
			if(selectedItem != null) {
				TextView tv = (TextView) convertView.findViewById(R.id.list_dialog_text_view);
				tv.setText(Html.fromHtml(getStringToShowFromObj(selectedItem)));
				
				ImageView imgView = (ImageView) convertView.findViewById(R.id.list_dialog_img_view);
				
				if(isSelectedObjEquals(selectedItem, mSelectedData)) {
					imgView.setImageResource(R.drawable.ko_checkbox_selected);
				}else {
					imgView.setImageResource(R.drawable.ko_checkbox_unselected);
				}
			}
			
			return convertView;
		}
		
	}
}
