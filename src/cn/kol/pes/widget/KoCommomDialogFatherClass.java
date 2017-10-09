/*-----------------------------------------------------------

-- PURPOSE

--    自己封装的对话框类的父类，主要是封装了去掉标题和边框的属性。复写show()的目的是让对话框的宽度较好地适应具体设备的屏幕

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.WindowManager;
import cn.kol.pes.R;

public class KoCommomDialogFatherClass extends Dialog{
    
    private Activity mContext;
    protected LayoutInflater mLi;

    public KoCommomDialogFatherClass(Activity context) {
        super(context, R.style.sd_date_dialog_theme);
        mContext = context;
        
        mLi = LayoutInflater.from(context);
    }
    
    @Override
    public void show() {
        super.show();
        
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (mContext.getWindowManager().getDefaultDisplay().getWidth());
        this.getWindow().setAttributes(lp);
    }

    protected int getMyWidth() {
    	 WindowManager.LayoutParams lp = this.getWindow().getAttributes();
         return lp.width = (int) (mContext.getWindowManager().getDefaultDisplay().getWidth());
    }
    
}
