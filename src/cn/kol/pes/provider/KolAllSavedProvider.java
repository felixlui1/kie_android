package cn.kol.pes.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import cn.kol.pes.KolApplication;

public class KolAllSavedProvider extends ContentProvider {
	
	private SQLiteDatabase mDB;
	private MySqliteHelper mHelp;
	
	public static Uri URI_GONG_DAN = Uri.parse("content://cn.kol.pes.all_saved/gong_dan");
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int mRes = 0;
		if(uri.equals(URI_GONG_DAN)) {
			mRes = mDB.delete(MySqliteHelper.TABLE_GONG_DAN, selection, selectionArgs);
		}
		
		return mRes;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	

	@Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
	    
	    int length = 0;
	    
	    if(uri.equals(URI_GONG_DAN)) {
	        try {
	            length = values.length;
                mDB.beginTransaction();
                for(ContentValues item : values) {
                    mDB.insert(MySqliteHelper.TABLE_GONG_DAN, null, item);
                }
                mDB.setTransactionSuccessful();
            }
	        catch (Exception e) {
                e.printStackTrace();
            }
	        finally {
                mDB.endTransaction();
            }
        }
	    
        return length;
    }

    @Override
	public Uri insert(Uri uri, ContentValues values) {
		if(uri.equals(URI_GONG_DAN)) {
			mDB.insert(MySqliteHelper.TABLE_GONG_DAN, null, values);
		}
		
		return uri;
	}

	@Override
	public boolean onCreate() {
		mHelp = new MySqliteHelper(this.getContext(), "favorites.db", null, KolApplication.VERSION_CODE);

		mDB = mHelp.getWritableDatabase();
		
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor mCur = null;
		if(uri.equals(URI_GONG_DAN)) {
			mCur = mDB.query(MySqliteHelper.TABLE_GONG_DAN, projection, selection, selectionArgs, null, null, sortOrder);
		}
		
		return mCur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	    if(uri.equals(URI_GONG_DAN)) {
            return mDB.update(MySqliteHelper.TABLE_GONG_DAN, values, selection, selectionArgs);
        }
	    
	    return 0;
	}

}
