/*-----------------------------------------------------------

-- PURPOSE

--    BaseControl是网络请求类的父类.由于 此应用的网络请求数量相对较少，所以我们采用一个帮助类管理所有网络请求。每个界面调用自己需要的请求即可。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.controller;

import android.app.Activity;


public abstract class BaseControl {

    protected Activity mActivityContext;

    public BaseControl() {
    }
    
    //传入调用这个帮助类的Activity的上下文
    public void setContextToControl(Activity act) {
        mActivityContext = act;
    }

}