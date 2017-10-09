/*-----------------------------------------------------------

-- PURPOSE

--    KoWeekItem是星期的数据封装类。

-- History

--	  21-April-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

import java.util.Calendar;

import cn.kol.common.util.LogUtils;
import cn.kol.common.util.StringUtils;


public class KoWeekItem extends Item {
	
    private Calendar starCal = Calendar.getInstance();
	private Calendar endCal = Calendar.getInstance();
	
	public String week;
	public String startEnd;
	
	public KoWeekItem() {
		
	}
	
	public void setStart(String start) {
		int startHour = Integer.valueOf(start.substring(0, 2));
		int startMin = Integer.valueOf(start.substring(2, 4));
		
		LogUtils.e("KoWeekItem", "startHour="+startHour+", startMin="+startMin);

		this.starCal.set(Calendar.HOUR_OF_DAY, startHour);
		this.starCal.set(Calendar.MINUTE, startMin);
	}
	
	public void setEnd(String end) {
		int endHour = Integer.valueOf(end.substring(0, 2));
		int endMin = Integer.valueOf(end.substring(2, 4));
		
		LogUtils.e("KoWeekItem", "endHour="+endHour+", endMin="+endMin);
		
		this.endCal.set(Calendar.HOUR_OF_DAY, endHour);
		this.endCal.set(Calendar.MINUTE, endMin);
	}
	
	public boolean isServerInShutDownTime() {
		
		LogUtils.e("KoWeekItem", "starCal="+StringUtils.formatDateTime(starCal)+", endCal="+StringUtils.formatDateTime(endCal));
		LogUtils.e("KoWeekItem", "week="+week+", startEnd="+startEnd);
		
	    Calendar nowCal = Calendar.getInstance();
	    if(nowCal.after(starCal) && nowCal.before(endCal)) {
	    	return true;
	    }
	    return false;
    }
	
}
