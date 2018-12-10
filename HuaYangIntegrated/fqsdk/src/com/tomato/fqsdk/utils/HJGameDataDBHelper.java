/** 
 * @Description TODO    ݿ          
 * @Version 1.0
 **/
package com.tomato.fqsdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * @ClassName OptionsDBHelper
 * @Description TODO    ݿ          
 * @Version 1.0
 **/
public class HJGameDataDBHelper {
//	private static final boolean D = true;
//	private static final String TAG = "---HJGameDBHelper---";// LogCat
    private static final String DB_NAME="HJGameDatas.db";
	private static final String DB_TABLE = "gamedatas";//    ݿ    
//	private static final int DB_VERSION = 1;//    ݿ 汾  
//
//	private Context mContext;
	private DBHelper dbHelper;
	private SQLiteDatabase database;
	private AtomicInteger openCounter;

	 private static HJGameDataDBHelper mInstance;
	public HJGameDataDBHelper(Context context) {
		// TODO Auto-generated constructor stub
		this.dbHelper = new DBHelper(context);
	    this.openCounter = new AtomicInteger();
//		this.mContext = context;
	}
	  public static HJGameDataDBHelper getInstance(Context context) {
		    if (mInstance == null) {
		      mInstance = new HJGameDataDBHelper(context);
		    }
		    return mInstance;
		  }
	public boolean isOpen() {	
		if (database.isOpen()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @          ݿ   ռ䲻   洢  ʱ    Ϊֻ  
	 * @throws SQLiteException
	 **/
	  public synchronized void open() {
		  Environment.getExternalStorageDirectory().canWrite();
		    if ((this.database != null) && (this.database.isOpen())) {
		      Log.e("HJ", "Database was opened!");
		      return;
		    }
		    if (this.openCounter.incrementAndGet() == 1)
		      this.database = this.dbHelper.getWritableDatabase();
		  }
	/**
	 * @      ر    ݿ 
	 * 
	 **/
	  public synchronized void close() {
		    if ((this.database == null) || (!this.database.isOpen())) {
		      Log.e("HJ", "Database was closed!");
		      return;
		    }
		    if (this.openCounter.decrementAndGet() == 0)
		      this.database.close();
		  }


	/**
	 * @             һ      
	 **/
	  public void insert(HJGameData games) {
		    this.database.beginTransaction();
		    ContentValues contentValues = new ContentValues();
			contentValues.put(DBHelper.KEY_BEHAVIOR, games.getBeHavior());
			contentValues.put(DBHelper.KEY_DATA, games.getData());
		    this.database.insert(DB_TABLE, null, contentValues);
		    this.database.setTransactionSuccessful();
		    this.database.endTransaction();
		  }
	/**
	 * @          ɾ  һ      
	 * @param appInfo
	 * @return
	 **/
	public long deleteOne(String id) {

		return database.delete(DB_TABLE, DBHelper.KEY_ID + "=" + "\""
				+ id + "\"", null);
	}

	/**
	 * @     ɾ     ݱ 
	 * @return
	 **/
	public long deleteAllData() {

		return database.delete(DB_TABLE, null, null);
	}

	/**
	 * @       ѯȫ     ݼ ¼    
	 * @return
	 **/
	  public int queryDataCount() {
	    int Cursorcount = -1;
	    try {
	      Cursor mycursor = null;

	      mycursor = this.database.query(DB_TABLE, null, null, 
	        null, null, null, null);
	      if ((mycursor != null) && (!"".equals(mycursor)))
	        Cursorcount = mycursor.getCount();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return Cursorcount;
	  }
	/**
	 * @       ѯȫ      
	 * @return
	 **/
	public HJGameData[] queryAllData() {
		HJGameData[] games;
		Cursor result = database.query(DB_TABLE, null, null, null, null, null,
				"_id DESC");
		games = ConvertAll(result);
		result.close();
		return games;
	}
	 public synchronized List<HJGameData> queryDataWithLimit( int count) {
		    if ((this.database == null) || (!this.database.isOpen())) {
		      Log.e("HJ", "Did you call method 'open' before you call this method?");
		      return null;
		    }
		    Cursor mycursor = this.database.rawQuery("SELECT * FROM " + 
		      DB_TABLE + " limit ?", new String[] { String.valueOf(count) });
		    List<HJGameData> datas = new ArrayList<HJGameDataDBHelper.HJGameData>();
		    int id = 0;
		    String behavior = null;
		    String data = null;
		    if (mycursor != null) {
		      while (mycursor.moveToNext()) {
		         id = mycursor.getInt(mycursor
		          .getColumnIndex("_id"));
		         behavior = mycursor.getString(mycursor
				          .getColumnIndex("behavior"));
		         data = mycursor.getString(mycursor
				          .getColumnIndex("data"));
		         datas.add(new HJGameData(id,behavior, data));
		      }
		    }
		    return datas;
		  }
	  public synchronized void delete(String id) {
		    if ((this.database == null) || (!this.database.isOpen())) {
		      Log.e("HJ", "Did you call method 'open' before you call this method?");
		      return;
		    }
		    if (this.database.isReadOnly()) {
		      Log.e("HJ", "Your memory is not enough!");
		      return;
		    }
		    this.database.delete(DB_TABLE, "_id=?",  new String[] { id });
		  }
	  
//	/**
//	 * @        ݰ     ѯһ       Ƿ    
//	 * @param
//	 * @return
//	 **/
//	public boolean isOneExist(String channel,String gameid) {
//		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_BEHAVIOR
//				+ "=" + "\"" + channel + "\""+" and "+DBHelper.KEY_GAMEID
//				+ "=" + "\"" + gameid + "\"", null, null, null, null);
//		// PrintLog.d(this, D,
//		// "check a record with username = "+name+" is exist or not");
//		if (result.getCount() == 0 || !result.moveToFirst()) {
//			// PrintLog.i(this, D, "the record is not exist");
//			result.close();
//			return false;
//		} else {
//			// PrintLog.i(this, D, "the record is exist");
//			result.close();
//			return true;
//		}
//	}

	private HJGameData[] ConvertAll(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		HJGameData[] games = new HJGameData[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			games[i] = new HJGameData();
			games[i].SetBeHavior(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_BEHAVIOR)));
			games[i].setData(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_DATA)));
			cursor.moveToNext();
		}
		cursor.close();
		return games;
	}

	public class HJGameData {

		private String behavior = "", data = "";
		private int id;
		/**
		 * @Name OptionsDBHelper.UserAccounts
		 **/
		public HJGameData() {
			// TODO Auto-generated constructor stub
		}

		public HJGameData(int id,String behavior, String data) {
			this.id=id;
			this.behavior = behavior;
			this.data = data;
		}

		public String getBeHavior() {
			return behavior;
		}

		public void SetBeHavior(String behavior) {
			this.behavior = behavior;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "behavior:"+behavior+"   data:"+data;
		}

	}

	public class DBHelper extends SQLiteOpenHelper {

		public static final boolean D = true;
		public static final String TAG = "---DBHelper---";// LogCat
		private static final String DB_TABLE = "gamedatas";//    ݿ    
//		private static final int DB_VERSION = 1;//    ݿ 汾  
		private static final String KEY_ID = "_id";
		private static final String KEY_BEHAVIOR = "behavior";
		private static final String KEY_DATA = "data";
		private static final String CREATE_TABLE_SQL = "CREATE TABLE "
				+ DB_TABLE + " (" + KEY_ID
				+ " integer  PRIMARY KEY autoincrement, "+ KEY_BEHAVIOR
				+ " VARCHAR(128) NOT NULL,"+ KEY_DATA
				+ " VARCHAR(65535) NOT NULL);";

//		public DBHelper(Context context, String name, CursorFactory factory,
//				int version) {
//			super(context, name, factory, version);
//		}
	    public DBHelper(Context context) {
	        super(context,DB_NAME, null, 1);
	      }
		@Override
		public void onCreate(SQLiteDatabase db) {
			//   һ  ʹ     ݿ ʱ Զ     
			db.execSQL(CREATE_TABLE_SQL);
		}

		/**
		 * 
		 *          ݿ   Ҫ    ʱ     ã  һ      ɾ   ɵ    ݿ          ת Ƶ  °汾     ݿ    
		 * 
		 **/
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SQL);
			onCreate(db);
			if (D){}
//				Log.i(TAG, "Upgrade");
		}
	}

}
