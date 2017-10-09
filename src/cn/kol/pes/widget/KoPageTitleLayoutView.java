/*-----------------------------------------------------------

-- PURPOSE

--    每个页面的标题，封装成了一个View，方便使用

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.pes.R;

public class KoPageTitleLayoutView extends LinearLayout{

	private LinearLayout mLayout;
	private TextView mName;
	private ViewGroup mSetLeftView;
	private View mClickableView;
	
	public KoPageTitleLayoutView(Context context) {
		this(context ,null);
	}
	
	public KoPageTitleLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.ko_page_title_layout, this, true);
		mName = (TextView) mLayout.findViewById(R.id.title_bar_title);
		mSetLeftView = (ViewGroup) mLayout.findViewById(R.id.title_bar_set_left_view);
		mClickableView = mLayout.findViewById(R.id.title_bar_clickable_img);
	}
	
	public void setLeft() {
		mSetLeftView.setVisibility(View.VISIBLE);
	}
	
	public void setMyClickable() {
		mClickableView.setVisibility(View.VISIBLE);
	}
	
	public void setMyClickListener(OnClickListener listener) {
		mName.setOnClickListener(listener);
	}

	public void setTitle(String name) {
		mName.setText(name);
	}
	
	public void setTitle(int nameId) {
		mName.setText(nameId);
	}
}