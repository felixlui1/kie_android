/*-----------------------------------------------------------

-- PURPOSE

--    开始一个工序时滚动对话框的封装类

-- History

--	  24-Oct-14  LiZheng Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import cn.kol.pes.R;

public class KoOpStartedDialog extends KoCommomDialogFatherClass {
    
    public interface CommonDlgClick {
        public void onOkBack();
        public void onCancelBack();
    }
    
    private Button mOkBtn;
    private View mProgressView;
    private CommonDlgClick mCommonDlgClick;
    
    private float mAlpha = 1;

    
    public static KoOpStartedDialog getDlgAndShow(Activity context, CommonDlgClick listener) {
        KoOpStartedDialog dlg = new KoOpStartedDialog(context);
        dlg.setMyOnClickListener(listener);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(false);
        dlg.show();
        
        return dlg;
    }
    
    public KoOpStartedDialog(Activity context) {
        super(context);
        
        LayoutInflater li = LayoutInflater.from(context);
        ViewGroup parentView = (ViewGroup) li.inflate(R.layout.ko_op_started_dialog_layout, null);

        mOkBtn = (Button) parentView.findViewById(R.id.op_started_dialog_complete_btn);
        mProgressView =  parentView.findViewById(R.id.op_started_dialog_progress_view);
        
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible  
        animation.setDuration(500); // duration - half a second  
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate  
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely  
        animation.setRepeatMode(Animation.REVERSE); //   
        mProgressView.setAnimation(animation);

        mOkBtn.setOnClickListener(mClickListener);
        
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCommonDlgClick.onCancelBack();
            }
        });
        
        this.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_BACK) {
					 mCommonDlgClick.onCancelBack();
				}
				return true;
			}
		});
        
        this.setCancelable(true);
        this.setContentView(parentView);
    }
    
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressView.setAlpha(mAlpha);
			mHandler.sendEmptyMessageDelayed(1, 200);
			mAlpha = mAlpha - 0.1f;
			if(mAlpha<0.2) {
				
			}
			super.handleMessage(msg);
		}
    	
    };
    
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mCommonDlgClick==null) {
                KoOpStartedDialog.this.dismiss();
                return;
            }
            
            switch(v.getId()) {
            case R.id.op_started_dialog_complete_btn:
                mCommonDlgClick.onOkBack();
                break;
            }
            KoOpStartedDialog.this.dismiss();
        }
    };
    
    public void setMyOnClickListener(CommonDlgClick listener) {
        mCommonDlgClick = listener;
    }
    
}