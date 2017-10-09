package cn.kol.pes.widget.picktime;

import java.util.Calendar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.kol.pes.R;
import cn.kol.pes.widget.KoCommomDialogFatherClass;

public class KoPickTimeDlg extends KoCommomDialogFatherClass {
	
	public interface IKoPickTimeDlgBack {
		public void pickTime(Calendar cal);
	}
	
	private WheelMain wheelMain;
	private Button mOkBtn;
    private Button mCancelBtn;
    private IKoPickTimeDlgBack mIKoPickTimeDlgBack;

	public KoPickTimeDlg(Activity context, IKoPickTimeDlgBack back, Calendar initCal) {
		super(context);
		
		mIKoPickTimeDlgBack = back;
		LayoutInflater inflater=LayoutInflater.from(context);
		ViewGroup parentView = (ViewGroup) inflater.inflate(R.layout.ko_common_dialog_layout, null);
		
		final View timepickerview=inflater.inflate(R.layout.ko_time_picker, null);
		ScreenInfo screenInfo = new ScreenInfo(context);
		wheelMain = new WheelMain(timepickerview, true);
		wheelMain.screenheight = screenInfo.getHeight();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(initCal.getTimeInMillis());

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

		ViewGroup pickParent = (ViewGroup) parentView.findViewById(R.id.common_dialog_add_view_parent);
		pickParent.setVisibility(View.VISIBLE);
		pickParent.addView(timepickerview);
		
		TextView title = (TextView) parentView.findViewById(R.id.common_dialog_title_text_view);
		title.setText("选择日期时间");
		title.setVisibility(View.VISIBLE);
		
		mOkBtn = (Button) parentView.findViewById(R.id.common_dialog_ok_button);
        mCancelBtn = (Button) parentView.findViewById(R.id.common_dialog_cancel_button);
        parentView.findViewById(R.id.common_dialog_msg_text_view).setVisibility(View.GONE);
        
        mOkBtn.setOnClickListener(mClickListener);
        mCancelBtn.setOnClickListener(mClickListener);
        
        setContentView(parentView);
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mIKoPickTimeDlgBack==null) {
            	KoPickTimeDlg.this.dismiss();
                return;
            }
            
            switch(v.getId()) {
            case R.id.common_dialog_ok_button:
            	mIKoPickTimeDlgBack.pickTime(wheelMain.getTimeCal());
                break;
            case R.id.common_dialog_cancel_button:
            	
                break;

            }
            KoPickTimeDlg.this.dismiss();
        }
    };
}
