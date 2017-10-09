package cn.kol.pes.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KoMyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if(KoPushMsgService.ACTION_PUSH_MSG_SCRAP.equals(arg1.getAction())) {//之前用来接收推送消息的广播接收器
			KoPushMsgService.startSer(arg0);
		}
		else if(Intent.ACTION_BOOT_COMPLETED.equals(arg1.getAction())) {//接收系统启动消息的接收器
			KoPushMsgService.startSer(arg0);
		}
	}

}
