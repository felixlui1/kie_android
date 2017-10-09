package cn.kol.pes.activity;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.kol.common.util.DialogUtils;
import cn.kol.common.util.KoDataUtil;
import cn.kol.pes.R;
import cn.kol.pes.controllerback.KolPesControlBack;

public class SettingsActivity extends BaseActivity implements OnClickListener {

	private TextView mStatus;
	private EditText mPwdView;
	private Button mChangeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ko_settings_activity);
		super.onCreate(savedInstanceState);
		
		mTitleView.setTitle(R.string.ko_title_settings);
		
		mStatus = (TextView) findViewById(R.id.gx_settings_status);
		
		mStatus.setText(KoDataUtil.isUseTestUrl() ? "测试" : "正式");
		
		mChangeBtn = (Button) findViewById(R.id.gx_settings_change_btn);
		mChangeBtn.setOnClickListener(this);
		
		mPwdView = (EditText) findViewById(R.id.gx_settings_password_edit);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gx_settings_change_btn:
			if(mPwdView.getText().toString().equals("pes")) {
				KoDataUtil.toggleSetIsUseTestUrl();
				mStatus.setText(KoDataUtil.isUseTestUrl() ? "测试" : "正式");
			}
			else {
				DialogUtils.showToast(this, R.string.ko_tips_password_wrong);
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected KolPesControlBack initControlBack() {
		// TODO Auto-generated method stub
		return null;
	}
}
