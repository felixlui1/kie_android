/*-----------------------------------------------------------

-- PURPOSE

--    质量收集计划数据项列表的适配类

-- History

--	  09-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.kol.pes.model.item.KoParamItem;
import cn.kol.pes.model.item.KoQaNeedFillItem;

public class KoQaListNeedFillAdapter extends ItemAdapter<KoQaNeedFillItem> {
	
	public boolean mIsLastSeq = false;
	public String incompleteQuan;
	public String minStartTime;
	public String maxEndTime;
	
	public String timeBufferOpEnd;
	public String scrapQuanTotal;
	
	private List<KoParamItem> mChildPlanIdList = new ArrayList<KoParamItem>();
	
	public void addPlanIDToChildPlanIdList(String childPlanId, String childPlanName) {
		mChildPlanIdList.add(new KoParamItem(childPlanId, childPlanName));
	}
	
	public List<KoParamItem> getChildPlanIdList() {
		return mChildPlanIdList;
	}
	
}
