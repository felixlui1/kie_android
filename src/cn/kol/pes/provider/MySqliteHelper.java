package cn.kol.pes.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import cn.kol.common.util.LogUtils;


public class MySqliteHelper extends SQLiteOpenHelper {

	private static String TAG = "MySqliteHelper";
	public static String TABLE_GONG_DAN= "kol_gong_dan";

	
	
	private String mCreateSQL = "create table if not exists "+TABLE_GONG_DAN+"(id text)";

	
	public MySqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(mCreateSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    
	    LogUtils.e(TAG, "oldVersion="+oldVersion+", newVersion="+newVersion);
	    
	    db.execSQL(mCreateSQL);

        
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

		db.execSQL(mCreateSQL);
		
		super.onOpen(db);
	}
	
	

}
