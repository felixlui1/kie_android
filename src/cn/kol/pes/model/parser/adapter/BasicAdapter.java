/*-----------------------------------------------------------

-- PURPOSE

--    XML数据有的是列表，有的是单节点数据。而且数据封装类中没有封装XML的code和message信息.所以通过一个适配类，来适配解析器和数据封装类。
--	  BasicAdapter是实现了对列表数据处理的适配类的父类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import java.util.ArrayList;
import java.util.List;

public class BasicAdapter<E> extends DefaultBasicAdapter{
	List<E> list;
	
	public BasicAdapter(){
		list = new ArrayList<E>();
	}
	
	public void initList(){
		list = new ArrayList<E>();
	}
	public int size(){
		if(list==null)
			return 0;
		return list.size();
	}
	public E get(int position){
		if(list==null)
			return null;
		return list.get(position);
	}
	public void add(E object){
		if(list==null) {
			list = new ArrayList<E>();
		}
		list.add(object);
	}
	public List<E> getList(){
		return list;
	}
	public void setList(List<E> list){
		this.list = list;
	}
}
