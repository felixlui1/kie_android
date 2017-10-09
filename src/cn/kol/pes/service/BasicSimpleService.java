/*-----------------------------------------------------------

-- PURPOSE

--    BasicSimpleService是联系网络请求参数类（cn.pes.model.param包中的类）和解析器适配类（cn.pes.model.parser.adapter中的类）的类。
--	      由于我们这个应用里面请求较少，且结构差异不大，所以，我们只现实了一个BasicSimpleService， 将OnDataParseListener放到外面，将所有的解析和回调放到KolPesNetReqControl中

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.service;

import android.content.Context;
import cn.kol.pes.model.NetworkManager;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.NetworkManager.OnHttpDownloadListener;
import cn.kol.pes.model.item.Item;
import cn.kol.pes.model.param.KoHttpParams;

public class BasicSimpleService implements IService<Item> {
	
	int mDownloadTaskID;
	Context mContext;
	
	@Override
	public int getDataFromService(KoHttpParams param, OnHttpDownloadListener httpListener, OnDataParseListener parseListener) {
		
		mDownloadTaskID = NetworkManager.instance().getXML(NetworkManager.getTask(param, httpListener, parseListener));
		return mDownloadTaskID;
	}

	@Override
	public void setContext(Context context) {
		mContext = context;
	}
	
}
