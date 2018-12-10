/** 
 * @Description TODO    ݿ          
 * @Version 1.0
 **/
package com.tomato.fqsdk.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @ClassName OptionsDBHelper
 * @Description TODO    ݿ          
 * @Version 1.0
 **/
public class HJGameDBHelper {
//	private static final boolean D = true;
//	private static final String TAG = "---HJGameDBHelper---";// LogCat
	private static final String DB_LOCALNAME = "HJGames.db";
//	private static final String DB_NAME = Environment
//			.getExternalStorageDirectory().getAbsolutePath()
//			+ "/databases/HJGames.db";//    ݿ   
	private static final String DB_TABLE = "games";//    ݿ    
//	private static final int DB_VERSION = 1;//    ݿ 汾  
//
//	private Context mContext;
	private DBHelper dbHelper;
	private SQLiteDatabase database;
	private AtomicInteger openCounter;
	 private static HJGameDBHelper mInstance;

	private HJGameDBHelper(Context context) {
		// TODO Auto-generated constructor stub
//		this.mContext = context;
		this.openCounter = new AtomicInteger();
		if (SDCardUtils.isAvailable()) {
			try {
				getDatabasePath(DB_LOCALNAME);
				this.dbHelper = new DBHelper(context);
			} catch (Exception e) {	
				this.dbHelper = new DBHelper(context,DB_LOCALNAME);
			}
			
		} else {
		
			this.dbHelper = new DBHelper(context,DB_LOCALNAME);
		}
		
		
		
	}
	  public static HJGameDBHelper getInstance(Context context) {
		    if (mInstance == null) {
		      mInstance = new HJGameDBHelper(context);
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

	public synchronized void open() {
			if ((this.database != null) && (this.database.isOpen())) {
				Log.e("HJ", "Database was opened!");
				return;
			}
			if (this.openCounter.incrementAndGet() == 1) {
				this.database = this.dbHelper.getWritableDatabase();
			}

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
			if (this.openCounter.decrementAndGet() == 0) {
				this.database.close();
			}
	}

	// /**
	// * @          ݿ   ռ䲻   洢  ʱ    Ϊֻ  
	// * @throws SQLiteException
	// **/
	// public boolean open() throws SQLiteException {
	// dbHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
	// try {
	// database = dbHelper.getWritableDatabase();
	// return true;
	// } catch (SQLiteException e) {
	// database = dbHelper.getReadableDatabase();
	// return false;
	// }
	// }
	//
	// /**
	// * @      ر    ݿ 
	// *
	// **/
	// public void close() {
	//
	// if (database != null) {
	// database.close();
	// database = null;
	// }
	// }

	/**
	 * @             һ      
	 **/
	public long insert(HJGame games) {
		// if (queryDataCount()>=3) {
		// queryLast();
		// }
		ContentValues contentValues = new ContentValues();

		contentValues.put(DBHelper.KEY_GAMEID, games.getmGameId());
		contentValues.put(DBHelper.KEY_CHANNEL, games.getmChannel());

		return database.insert(DB_TABLE, null, contentValues);
	}

	/**
	 * @          ɾ  һ      
	 * @param appInfo
	 * @return
	 **/
	public long deleteOne(String channel, String gameid) {

		return database.delete(DB_TABLE, DBHelper.KEY_CHANNEL + "=" + "\""
				+ channel + "\"" + " and " + DBHelper.KEY_GAMEID + "=" + "\""
				+ gameid + "\"", null);
	}

	/**
	 * @     ɾ     ݱ 
	 * @return
	 **/
	public long deleteAllData() {

		return database.delete(DB_TABLE, null, null);
	}

	/**
	 * @        ݰ     ѯһ      
	 * @param
	 * @return
	 **/
	public HJGame queryOne(String channel, String gameid) {
		HJGame games;
		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_CHANNEL
				+ "=" + "\"" + channel + "\"" + " and " + DBHelper.KEY_GAMEID
				+ "=" + "\"" + gameid + "\"", null, null, null, null);
		games = ConvertAll(result)[0];
		result.close();
		return games;
	}

	/**
	 * @       ѯȫ     ݼ ¼    
	 * @return
	 **/
	public int queryDataCount() {
		int count = 0;
		Cursor result = database.query(DB_TABLE, null, null, null, null, null,
				null);
		count = result.getCount();
		result.close();
		return count;
	}

	/**
	 * @       ѯȫ      
	 * @return
	 **/
	public HJGame[] queryAllData() {
		HJGame[] games;
		Cursor result = database.query(DB_TABLE, null, null, null, null, null,
				"channel DESC");
		games = ConvertAll(result);
		result.close();
		return games;
	}

	/**
	 * @        ݰ     ѯһ       Ƿ    
	 * @param
	 * @return
	 **/
	public boolean isOneExist(String channel, String gameid) {
		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_CHANNEL
				+ "=" + "\"" + channel + "\"" + " and " + DBHelper.KEY_GAMEID
				+ "=" + "\"" + gameid + "\"", null, null, null, null);
		// PrintLog.d(this, D,
		// "check a record with username = "+name+" is exist or not");
		if (result.getCount() == 0 || !result.moveToFirst()) {
			// PrintLog.i(this, D, "the record is not exist");
			result.close();
			return false;
		} else {
			// PrintLog.i(this, D, "the record is exist");
			result.close();
			return true;
		}
	}

	private HJGame[] ConvertAll(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		HJGame[] games = new HJGame[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			games[i] = new HJGame();
			games[i].setmChannel(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_CHANNEL)));
			games[i].setmGameId(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_GAMEID)));
			cursor.moveToNext();
		}
		cursor.close();
		return games;
	}

	public class HJGame {

		private String mChannel = "", mGameId = "";

		/**
		 * @Name OptionsDBHelper.UserAccounts
		 **/
		public HJGame() {
			// TODO Auto-generated constructor stub
		}

		public HJGame(String mChannel, String mGameId, boolean lastacc,
				boolean rempwd, boolean autolog, String time) {
			this.mChannel = mChannel;
			this.mGameId = mGameId;
		}

		public String getmChannel() {
			return mChannel;
		}

		public void setmChannel(String mChannel) {
			this.mChannel = mChannel;
		}

		public void setmGameId(String mGameId) {
			this.mGameId = mGameId;
		}

		public String getmGameId() {
			return this.mGameId;
		}
	}

//	private String GetDbName() {
//		if (SDCardUtils.isAvailable()) {
//			return DB_NAME;
//		} else {
//			return DB_LOCALNAME;
//		}
//	}
	  /**  
     *       ݿ ·            ڣ  򴴽          
     *   
     * @param name  
     * @param mode  
     * @param factory  
	 * @throws IOException 
     */  
    public File getDatabasePath(String name) throws IOException {  
        //  ж  Ƿ    sd    
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED  
                .equals(android.os.Environment.getExternalStorageState());  
        if (!sdExist) {//          ,  
            Log.e("SD      ", "SD       ڣ      SD  ");  
            return null;  
        } else {//          
                //   ȡsd  ·    
            String dbDir = android.os.Environment.getExternalStorageDirectory()  
                    .getAbsolutePath();  
            dbDir += "/databases";//    ݿ     Ŀ¼  
            String dbPath = dbDir + "/" + name;//    ݿ ·    
            //  ж Ŀ¼ Ƿ   ڣ        򴴽   Ŀ¼  
            File dirFile = new File(dbDir);  
            if (!dirFile.exists())  
                dirFile.mkdirs();  
  
            //    ݿ  ļ  Ƿ񴴽  ɹ   
            boolean isFileCreateSuccess = false;  
            //  ж  ļ  Ƿ   ڣ        򴴽    ļ   
            File dbFile = new File(dbPath);  
            if (!dbFile.exists()) {  
//                try {  
                    isFileCreateSuccess = dbFile.createNewFile();//      ļ   
//                } catch (IOException e) {  
//                    // TODO Auto-generated catch block  
//                    e.printStackTrace();  
//                }  
            } else  
                isFileCreateSuccess = true;  
  
            //        ݿ  ļ       
            if (isFileCreateSuccess)  
                return dbFile;  
            else  
                return null;  
        }  
    }  
	public class DBHelper extends SQLiteOpenHelper {

		public static final boolean D = true;
		public static final String TAG = "---DBHelper---";// LogCat

		private static final String DB_TABLE = "games";//    ݿ    

//		private static final int DB_VERSION = 1;//    ݿ 汾  
		private static final String KEY_ID = "_id";
		private static final String KEY_GAMEID = "gameid";
		private static final String KEY_CHANNEL = "channel";
		private static final String CREATE_TABLE_SQL = "CREATE TABLE "
				+ DB_TABLE + " (" + KEY_ID
				+ " integer  PRIMARY KEY autoincrement, " + KEY_GAMEID
				+ " VARCHAR(32) NOT NULL," + KEY_CHANNEL
				+ " VARCHAR(32) NOT NULL);";
		// public DBHelper(Context context, String name, CursorFactory factory,
		// int version) {
		// super(context, GetDbName(), factory, version);
		// }
		public DBHelper(Context context) {
			super(new DbContext(context), DB_LOCALNAME, null, 1);
		}
		public DBHelper(Context context,String dbname) {
			super(context, dbname, null, 1);
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
			if (D) {
			}
			// Log.i(TAG, "Upgrade");
		}
	}

}
