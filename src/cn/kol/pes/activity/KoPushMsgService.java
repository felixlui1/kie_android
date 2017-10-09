package cn.kol.pes.activity;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Xml;
import android.util.Xml.Encoding;
import android.widget.RemoteViews;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.R;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.NetworkManager.OnHttpDownloadListener;
import cn.kol.pes.model.item.KoPushMsgItem;
import cn.kol.pes.model.param.KoPushMsgParams;
import cn.kol.pes.model.parser.KoPushMsgListParser;
import cn.kol.pes.model.parser.adapter.KoPushMsgAdapter;
import cn.kol.pes.service.BasicSimpleService;
import cn.kol.pes.service.IService;
import cn.kol.pes.service.ServiceFactory;

public class KoPushMsgService extends Service implements OnDataParseListener, OnHttpDownloadListener {

	private int mGetMsgListServiceId = -1;
	private BasicSimpleService mBaseService;
	private KoPushMsgAdapter mKoPushMsgListAdapter;
	
	private MsgReceiver mMsgReceiver;
	
	public static final String ACTION_PUSH_MSG_SCRAP = "cn.kol.pes.action_push_msg_scrap";
	private static final String KEY_LAST_NOTIFY_MSG_TRANS_ID = "key_last_notify_msg_trans_id";
	
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	
	private static final int ALARM_HIGH_FREQUENCY = 1;
	private static final int ALARM_LOW_FREQUENCY = 60;
	
	public static void startSer(Context con) {
		//con.startService(new Intent(con, KoPushMsgService.class));
	}
	
//	public static void stopSer(Context con) {
//		con.stopService(new Intent(con, KoPushMsgService.class));
//	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		try {
			mMsgReceiver = new MsgReceiver();
			IntentFilter inF = new IntentFilter(ACTION_PUSH_MSG_SCRAP);
			this.registerReceiver(mMsgReceiver, inF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mBaseService = (BasicSimpleService) ServiceFactory.instant().createService(IService.SIMPLE);
		startAlarm(ALARM_HIGH_FREQUENCY);
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	public static int getTransIdLastNotify(Context con) {
		return con.getSharedPreferences(KEY_LAST_NOTIFY_MSG_TRANS_ID, Context.MODE_PRIVATE).getInt(KEY_LAST_NOTIFY_MSG_TRANS_ID, 0);
	}
	
	public static void setTransIdLastNotify(String transId, Context con) {
		SharedPreferences sp = con.getSharedPreferences(KEY_LAST_NOTIFY_MSG_TRANS_ID, Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putInt(KEY_LAST_NOTIFY_MSG_TRANS_ID, Integer.valueOf(transId));
		ed.commit();
		
		LogUtils.e("KoPushMsgService", "setTransIdLastNotify:"+transId);
	}
	
    private void getPushMsgList(String staffNo, String transId) {
    	mGetMsgListServiceId = mBaseService.getDataFromService(new KoPushMsgParams(staffNo, transId, true), this, this);
    }
    
    private void showNotice(KoPushMsgItem pushMsg) {
    	
    	RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.ko_push_msg_notice_layout);    
        contentView.setTextViewText(R.id.push_msg_notice_title, pushMsg.title); 
        contentView.setTextViewText(R.id.push_msg_notice_msg, pushMsg.msg); 
    	
    	mNotification = new Notification(R.drawable.ko_app_icon, pushMsg.title, System.currentTimeMillis());  
        mNotification.flags = Notification.FLAG_AUTO_CANCEL|Notification.FLAG_ONLY_ALERT_ONCE;
        mNotification.contentView = contentView;
        mNotification.defaults = Notification.DEFAULT_ALL;
        
        Intent intent = new Intent(this, KoPushMsgListActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        mNotification.contentIntent = pi;
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        //mNotificationManager.cancel(KEY_NOTICE_ID);
        mNotificationManager.notify((int) System.currentTimeMillis(), mNotification);
    }

	@Override
	public void onHttpDownLoadDone(int taskId, String result) {
		if(taskId == mGetMsgListServiceId) {
			if(mKoPushMsgListAdapter.isSuccess()) {
				List<KoPushMsgItem> msgList = mKoPushMsgListAdapter.getList();
				if(msgList!=null && msgList.size()>0) {
					setTransIdLastNotify(msgList.get(0).transId, this);
					showNotice(msgList.get(0));
				}
			}
		}
	}

	@Override
	public String onDataParse(int id, InputStream is) {
		if(id == mGetMsgListServiceId) {
			try {
				mKoPushMsgListAdapter = new KoPushMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoPushMsgListParser(mKoPushMsgListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return OnDataParseListener.SUCCESS;
	}
	
	private class MsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(ACTION_PUSH_MSG_SCRAP.equals(intent.getAction())) {
				getPushMsgList("", String.valueOf(getTransIdLastNotify(KoPushMsgService.this)));
				
				Calendar cal = Calendar.getInstance();
				if(cal.get(Calendar.HOUR_OF_DAY)>19 || cal.get(Calendar.HOUR_OF_DAY)<8) {
					stopAlarm();
					startAlarm(ALARM_LOW_FREQUENCY);
				}else {
					stopAlarm();
					startAlarm(ALARM_HIGH_FREQUENCY);
				}
				
				LogUtils.e("MsgReceiver:cal.get(Calendar.HOUR_OF_DAY)=", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
			}
		}
	}
	
	private void startAlarm(int min) {
		Intent intent = new Intent(ACTION_PUSH_MSG_SCRAP);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
		
		AlarmManager alarmManager = (AlarmManager) this.getSystemService(Activity.ALARM_SERVICE);
        
		alarmManager.setRepeating(AlarmManager.RTC, 
	    						  calendar.getTimeInMillis() + 1000*60*min, 
	                              1000*60*min, 
	                              pendingIntent);
	}
	
	private void stopAlarm() {
        Intent intent = new Intent(ACTION_PUSH_MSG_SCRAP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

	@Override
	public void onDestroy() {
		try {
			this.unregisterReceiver(mMsgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stopAlarm();
		super.onDestroy();
	}
}
