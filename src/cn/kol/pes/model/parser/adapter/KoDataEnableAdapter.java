/*-----------------------------------------------------------

-- PURPOSE

--    判断服务器是否可用的适配类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import java.util.HashMap;

import cn.kol.pes.model.item.KoWeekItem;


public class KoDataEnableAdapter extends DefaultBasicAdapter {

	private HashMap<String, KoWeekItem> mWeekList = new HashMap<String, KoWeekItem>();
	
	public void addWeek(String week, KoWeekItem weekData) {
		mWeekList.put(week, weekData);
	}
	
	public HashMap<String, KoWeekItem> getWeekMap() {
		return mWeekList;
	}
}
