/*-----------------------------------------------------------

-- PURPOSE

--    已开启工序列表的适配类

-- History

--	  1-Nov-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import cn.kol.pes.model.item.KoOpStartedItem;

public class KoGetOpStartedListAdapter extends ItemAdapter<KoOpStartedItem> {
	public boolean isOpCompleted;
	public String curWorkingOpCode;
}
