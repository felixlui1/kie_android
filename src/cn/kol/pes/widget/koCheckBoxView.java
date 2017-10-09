/*-----------------------------------------------------------

-- PURPOSE

--    自己封装的CheckBox，这个相对美观些

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.pes.R;

public class koCheckBoxView extends LinearLayout {

	private boolean mIsChecked = false;
	private ImageView mImgView;//左边的方框图标
	private TextView mTextView;//右边的文字说明
	
	public koCheckBoxView(Context context) {
		this(context, null);
	}
	
	public koCheckBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_check_box_view_layout, this);
		mImgView = (ImageView) rootView.findViewById(R.id.chech_box_img_view);
		mTextView = (TextView) rootView.findViewById(R.id.check_box_text_view);
	}
	
	public void setText(String s) {
		mTextView.setText(s);
	}
	
	public void setText(int id) {
		mTextView.setText(id);
	}
	
	public void setChecked(boolean isChecked) {//设置选中状态
		mIsChecked = isChecked;
		if(isChecked) {
			mImgView.setImageResource(R.drawable.ko_checked);
		}else {
			mImgView.setImageResource(R.drawable.ko_uncheck);
		}
	}
	
	public boolean getIsChecked() {//获取选中状态
		return mIsChecked;
	}
}
