/*-----------------------------------------------------------

-- PURPOSE

--    BasicService是联系网络请求参数类（cn.pes.model.param包中的类）和解析器适配类（cn.pes.model.parser.adapter中的类）的父类。
--	      继承实现它的类可以实例化一个OnDataParseListener，进而将解析器封装到这个类里面。由于我们这个应用里面请求较少，且结构差异不大，所以，我们只现实
--    了一个BasicSimpleService， 将OnDataParseListener放到外面，将所有的解析和回调放到KolPesNetReqControl中

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.service;

import java.io.InputStream;

import android.content.Context;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.item.Item;
import cn.kol.pes.model.parser.adapter.BasicAdapter;


public abstract class BasicService<E extends Item, T extends BasicAdapter<?>> implements IService<E>, OnDataParseListener {
	
	int mDownloadTaskID;
	T adapter;
	Context mContext;
	
	
	@Override
	public String onDataParse(int id, InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
	}
}
