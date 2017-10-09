/*-----------------------------------------------------------

-- PURPOSE

--    封装的子质量收集计划的点击按钮

-- History

--	  06-Mar-15  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.model.item.KoParamItem;

public class KoChildPlanView extends LinearLayout {

	public static final int CHILD_PLAN_ID_VIEW_ID = 0x88665;
	private TextView mTextView;
	private String mPlanId;
	
	public KoChildPlanView(Context context, KoParamItem planIdName) {
		super(context, null);

		LayoutInflater li = LayoutInflater.from(context);
		ViewGroup rootView = (ViewGroup) li.inflate(R.layout.ko_child_plan_id_view_layout, this);
		mTextView = (TextView) rootView.findViewById(R.id.child_plan_id_text_view);
		
		this.setId(CHILD_PLAN_ID_VIEW_ID);
		mPlanId = planIdName.name;
		setPlanIdText(context.getString(R.string.ko_title_child_plan_id)+planIdName.value);
	}
	
	public void setPlanIdText(String s) {
		mTextView.setText(s);
	}
	
	public String getPlanId() {
		return mPlanId;
	}
	
	
}
