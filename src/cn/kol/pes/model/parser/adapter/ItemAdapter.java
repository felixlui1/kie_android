/*-----------------------------------------------------------

-- PURPOSE

--    XML数据有的是列表，有的是单节点数据。而且数据封装类中没有封装XML的code和message信息.所以通过一个适配类，来适配解析器和数据封装类。
--	  ItemAdapter是实现了对列表数据处理的适配类的父类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import cn.kol.pes.model.item.Item;

public class ItemAdapter<E extends Item> extends BasicAdapter<E> {
	String count;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	public void clear() {
		list.clear();
	}
	
}
