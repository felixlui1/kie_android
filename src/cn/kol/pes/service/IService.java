/*-----------------------------------------------------------

-- PURPOSE

--    BasicSimpleService是联系网络请求参数类（cn.pes.model.param包中的类）和解析器适配类（cn.pes.model.parser.adapter中的类）的类的上层接口

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.service;

import android.content.Context;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.NetworkManager.OnHttpDownloadListener;
import cn.kol.pes.model.param.KoHttpParams;


public interface IService<E> {
	
	static final int SIMPLE = 000001;
	
	int getDataFromService(KoHttpParams param, OnHttpDownloadListener httpListener, OnDataParseListener parseListener);

	void setContext(Context context);

}
