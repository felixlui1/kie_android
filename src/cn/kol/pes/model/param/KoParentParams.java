/*-----------------------------------------------------------

-- PURPOSE

--    参数类的父类.此父类中暂时实现了语言设置

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.param;

import cn.kol.common.util.KoCommonUtil;


public class KoParentParams extends KoHttpParams {
	
	public KoParentParams(){
		
		setParam("lang", KoCommonUtil.getLanguage());
	}
}
