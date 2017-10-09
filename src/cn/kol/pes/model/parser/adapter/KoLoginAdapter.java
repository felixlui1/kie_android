/*-----------------------------------------------------------

-- PURPOSE

--    登录的适配类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.parser.adapter;

import cn.kol.common.util.LogUtils;


public class KoLoginAdapter extends DefaultBasicAdapter {

    public String staffNo;
    public String staffName;
    public String cardNo;
    public String levelCode;
    
    //该员工是否可以跳工序
    public boolean isCanJumpOp() {
    	LogUtils.e("isCanJumpOp()", "levelCode="+levelCode+"=");
    	return "S".equals(levelCode) || "s".equals(levelCode);
    }
}
