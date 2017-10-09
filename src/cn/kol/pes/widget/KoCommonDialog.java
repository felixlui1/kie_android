/*-----------------------------------------------------------

-- PURPOSE

--    常用的确认对话框的封装类

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import cn.kol.pes.R;

public class KoCommonDialog extends KoCommomDialogFatherClass {
    
    public interface CommonDlgClick {
        public void onOkBack();
        public void onCancelBack();
        //public void onMsgClick();
    }
    
    private Button mOkBtn;
    private Button mCancelBtn;
    private TextView mMsgTextView;
    private CommonDlgClick mCommonDlgClick;
    
    private View mBtnDistenserView;
    private ViewGroup mAddViewParentLayout;
    private TextView mDlgTitleView;
    private View mDlgTitleLine;
    
    
    public static KoCommonDialog getDlgAndShow(Activity context, CommonDlgClick listener, String msg) {
        KoCommonDialog dlg = new KoCommonDialog(context);
        dlg.setMyOnClickListener(listener);
        dlg.setMsg(msg);
        dlg.show();
        
        return dlg;
    }
    
    public static KoCommonDialog getDlgAndShow(Activity context, CommonDlgClick listener, final int msgId) {
        KoCommonDialog dlg = new KoCommonDialog(context);
        dlg.setMyOnClickListener(listener);
        if(msgId>0) {
            dlg.setMsg(context.getString(msgId));
        } else {
            dlg.setMsg(null);
        }
        dlg.show();
        
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        lp.width = (int) (context.getWindowManager().getDefaultDisplay().getWidth());
        dlg.getWindow().setAttributes(lp);
        
        return dlg;
    }
    
    public static KoCommonDialog getDlgAndShow(Activity context, CommonDlgClick listener, final int msgId, final int okBtnTextId, final int cancelBtnTextId) {
        KoCommonDialog dlg = new KoCommonDialog(context);
        dlg.setMyOnClickListener(listener);
        
        if(msgId>0) {
            dlg.setMsg(context.getString(msgId));
        } else {
            dlg.setMsg(null);
        }
        
        String okText = null;
        String cancelText = null;
        if(okBtnTextId>0) {
            okText = context.getString(okBtnTextId);
        }
        if(cancelBtnTextId>0) {
            cancelText = context.getString(cancelBtnTextId);
        }
        
        dlg.setBtnText(okText, cancelText);

        dlg.show();

        return dlg;
    }

    public KoCommonDialog(Activity context) {
        super(context);
        
        LayoutInflater li = LayoutInflater.from(context);
        ViewGroup parentView = (ViewGroup) li.inflate(R.layout.ko_common_dialog_layout, null);

        mOkBtn = (Button) parentView.findViewById(R.id.common_dialog_ok_button);
        mCancelBtn = (Button) parentView.findViewById(R.id.common_dialog_cancel_button);
        mMsgTextView = (TextView) parentView.findViewById(R.id.common_dialog_msg_text_view);

        mOkBtn.setOnClickListener(mClickListener);
        mCancelBtn.setOnClickListener(mClickListener);
        //mMsgTextView.setOnClickListener(mClickListener);
        
        mBtnDistenserView = parentView.findViewById(R.id.common_dialog_button_distenser);
        mAddViewParentLayout = (ViewGroup) parentView.findViewById(R.id.common_dialog_add_view_parent);
        mDlgTitleView = (TextView) parentView.findViewById(R.id.common_dialog_title_text_view);
        mDlgTitleLine = parentView.findViewById(R.id.common_dialog_title_text_line);
        mDlgTitleView.setVisibility(View.GONE);
        mDlgTitleLine.setVisibility(View.GONE);
        
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCommonDlgClick.onCancelBack();
            }
        });
        
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.setContentView(parentView);
    }
    
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mCommonDlgClick==null) {
                KoCommonDialog.this.dismiss();
                return;
            }
            
            switch(v.getId()) {
            case R.id.common_dialog_ok_button:
                mCommonDlgClick.onOkBack();
                break;
            case R.id.common_dialog_cancel_button:
                mCommonDlgClick.onCancelBack();
                break;
            case R.id.common_dialog_msg_text_view:
                //mCommonDlgClick.onMsgClick();
                break;
            }
            KoCommonDialog.this.dismiss();
        }
    };
    
    public void setMyOnClickListener(CommonDlgClick listener) {
        mCommonDlgClick = listener;
    }
    
    public void setMsg(String msg) {
        if(msg!=null) {
            mMsgTextView.setText(msg);
        } else {
            mMsgTextView.setVisibility(View.GONE);
        }
    }
    
    public void setTitle(String title) {
    	mDlgTitleView.setText(title);
    	mDlgTitleView.setVisibility(View.VISIBLE);
        mDlgTitleLine.setVisibility(View.VISIBLE);
    }
    
    public void addViewToMyGroup(View child) {
    	mAddViewParentLayout.addView(child);
    	mAddViewParentLayout.setVisibility(View.VISIBLE);
    }
    
    public void setOkCancelBtn(boolean isShowOk, boolean isShowCancel) {
    	if(isShowOk) {
            mOkBtn.setVisibility(View.VISIBLE);
        }else {
            mOkBtn.setVisibility(View.GONE);
        }
        
        if(isShowCancel) {
            mCancelBtn.setVisibility(View.VISIBLE);
        }else {
            mCancelBtn.setVisibility(View.GONE);
        }
        
        if(isShowOk && isShowCancel) {
        	mBtnDistenserView.setVisibility(View.VISIBLE);
        }else {
        	mBtnDistenserView.setVisibility(View.GONE);
        }
    }
    
    public void setBtnText(String okText, String cancelText) {
        if(okText != null) {
            mOkBtn.setText(okText);
        }else {
            mOkBtn.setVisibility(View.GONE);
        }
        
        if(cancelText != null) {
            mCancelBtn.setText(cancelText);
            mBtnDistenserView.setVisibility(View.VISIBLE);
        }else {
            mCancelBtn.setVisibility(View.GONE);
            mBtnDistenserView.setVisibility(View.GONE);
        }
    }
    
    public void setBtnText(int okText, int cancelText) {
        if(okText > 0) {
            mOkBtn.setText(okText);
        }else {
            mOkBtn.setVisibility(View.GONE);
        }
        
        if(cancelText > 0) {
            mCancelBtn.setText(cancelText);
            mBtnDistenserView.setVisibility(View.VISIBLE);
        }else {
            mCancelBtn.setVisibility(View.GONE);
            mBtnDistenserView.setVisibility(View.GONE);
        }
    }
}