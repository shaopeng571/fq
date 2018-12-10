/** 
 * @Description TODO 数据库操作辅助类
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
 * @Description TODO 数据库操作辅助类
 * @Version 1.0
 **/
public class HJAccountDBHelper {
	private static final boolean D = true;
//	private static final String TAG = "---OptionsDBHelper---";// LogCat
	private static final String DB_LOCALNAME = "HJAccount.db";
//	private static final String DB_NAME = Environment
//			.getExternalStorageDirectory().getAbsolutePath()
//			+ "/databases/HJAccount.db";// 数据库名
	private static final String DB_TABLE = "accounts";// 数据库表 ?
//	private static final int DB_VERSION = 1;// 数据库版本号
//
//	private Context mContext;
	private DBHelper dbHelper;
	private SQLiteDatabase database;
	private AtomicInteger openCounter;
	 private static HJAccountDBHelper mInstance;
	private HJAccountDBHelper(Context context) {
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
	
//		try {
//			this.dbHelper = new DBHelper(context);
//			open();
//			close();
//		} catch (Exception e) {
//			this.dbHelper = new DBHelper(context,DB_LOCALNAME);
//		}
		
	}
	  public static HJAccountDBHelper getInstance(Context context) {
		    if (mInstance == null) {
		      mInstance = new HJAccountDBHelper(context);
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
	 * @功能 关闭数据 ?
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
	// * @功能 打开数据 ? 空间不够存储的时候设为只 ?
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
	// * @功能 关闭数据 ?
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
	 * @功能 更新选中账号记录
	 * @param appInfo
	 * @return
	 **/
	public void updateState(String name) {
		if (name != null) {
			database.execSQL("UPDATE " + DB_TABLE + " SET "
					+ DBHelper.KEY_LAST_ACC + " = 1 WHERE "
					+ DBHelper.KEY_USERNAME + " = " + "\"" + name + "\";");
			database.execSQL("UPDATE " + DB_TABLE + " SET "
					+ DBHelper.KEY_LAST_ACC + " = 0 WHERE "
					+ DBHelper.KEY_USERNAME + " <> " + "\"" + name + "\";");// 将其它的置为0
		} else {
			database.execSQL("UPDATE " + DB_TABLE + " SET "
					+ DBHelper.KEY_LAST_ACC + " = 0 ;");// 将其它的置为0
		}

	}

	/**
	 * @功能 向表中添加一条数 ?
	 **/
	public long insert(UserAccount userAccount) {
		if (queryDataCount() >= 5) {
			queryLast();
		}
		ContentValues contentValues = new ContentValues();

		contentValues.put(DBHelper.KEY_USERNAME, userAccount.getUserName());
		contentValues.put(DBHelper.KEY_PASSWORD, userAccount.getPassWord());
		contentValues.put(DBHelper.KEY_TIME, userAccount.getmTime());
		contentValues.put(DBHelper.KEY_REMB_PWD, (userAccount.getRembPwd()) ? 1
				: 0);
		contentValues.put(DBHelper.KEY_AUTO_LOG, (userAccount.getAutoLog()) ? 1
				: 0);
		contentValues.put(DBHelper.KEY_LAST_ACC, (userAccount.getLastAcc()) ? 1
				: 0);

		return database.insert(DB_TABLE, null, contentValues);
	}

	/**
	 * @功能 向表中删除一条数 ?
	 * @param appInfo
	 * @return
	 **/
	public long deleteOne(String name) {
		if (isOneExist(name)) {
			return database.delete(DB_TABLE, DBHelper.KEY_USERNAME + "=" + "\""
					+ name + "\"", null);
		}
		return (Long) null;
	}

	/**
	 * @功能 删除数据 ?
	 * @return
	 **/
	public long deleteAllData() {

		return database.delete(DB_TABLE, null, null);
	}

	/**
	 * @功能 获得上一次登录的账号
	 * @param
	 * @return
	 **/
	public UserAccount queryLastAcc() {
		UserAccount userAccount = null;
		Cursor result = database
				.query(DB_TABLE, null, DBHelper.KEY_LAST_ACC + "=" + "\""
						+ String.valueOf(1) + "\"", null, null, null, null);
		// Log.e(TAG, "queryOneData()---result:" + result.getColumnCount());
		if (result.getCount() > 0) {
			userAccount = ConvertAll(result)[0];
		}
		result.close();
		return userAccount;
	}

	/**
	 * @功能 查询时间 ?早的数据并删 ?
	 * @param
	 * @return
	 **/
	public void queryLast() {
		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_TIME, null,
				null, null, DBHelper.KEY_TIME);
		if (result != null) {
			while (result.moveToFirst()) {
				String name = result.getString(result
						.getColumnIndex(DBHelper.KEY_USERNAME));
				deleteOne(name);
				break;
			}
		}
		result.close();
	}

	/**
	 * @功能 根据包名查询 ?条数 ?
	 * @param
	 * @return
	 **/
	public UserAccount queryOne(String name) {
		UserAccount userAccount;
		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_USERNAME
				+ "=" + "\"" + name + "\"", null, null, null, null);
		userAccount = ConvertAll(result)[0];
		result.close();
		return userAccount;
	}

	/**
	 * @功能 查询全部数据记录数量
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
	 * @功能 查询全部数据
	 * @return
	 **/
	public UserAccount[] queryAllData() {
		UserAccount[] userAccounts;
		Cursor result = database.query(DB_TABLE, null, null, null, null, null,
				"time DESC");
		userAccounts = ConvertAll(result);
		result.close();
		return userAccounts;
	}

	/**
	 * @功能 根据包名更新 ?条数 ?
	 * @param
	 * @return long
	 **/
	public long updateOne(String name, UserAccount userAccount, String Time) {

		// PrintLog.d(this, D, "update a record with username = "+name);

		ContentValues contentValues = new ContentValues();

		contentValues.put(DBHelper.KEY_USERNAME, userAccount.getUserName());
		contentValues.put(DBHelper.KEY_PASSWORD, userAccount.getPassWord());
		contentValues.put(DBHelper.KEY_REMB_PWD, (userAccount.getRembPwd()) ? 1
				: 0);
		contentValues.put(DBHelper.KEY_AUTO_LOG, (userAccount.getAutoLog()) ? 1
				: 0);
		contentValues.put(DBHelper.KEY_LAST_ACC, (userAccount.getLastAcc()) ? 1
				: 0);
		contentValues.put(DBHelper.KEY_TIME, userAccount.getmTime());
		return database.update(DB_TABLE, contentValues, DBHelper.KEY_USERNAME
				+ "=" + "\"" + name + "\"", null);
	}

	/**
	 * @功能 根据包名查询 ?条数据是否存 ?
	 * @param
	 * @return
	 **/
	public boolean isOneExist(String name) {
		Cursor result = database.query(DB_TABLE, null, DBHelper.KEY_USERNAME
				+ "=" + "\"" + name + "\"", null, null, null, null);
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

	private UserAccount[] ConvertAll(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (D)
			// Log.e(TAG, "ConvertToAppInfo()---resultCounts:" + resultCounts);
			if (resultCounts == 0 || !cursor.moveToFirst()) {
				return null;
			}
		UserAccount[] userAccounts = new UserAccount[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			userAccounts[i] = new UserAccount();
			userAccounts[i].setUserName(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_USERNAME)));
			userAccounts[i].setPassWord(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_PASSWORD)));
			userAccounts[i].setmTime(cursor.getString(cursor
					.getColumnIndex(DBHelper.KEY_TIME)));
			userAccounts[i].setRembPwd(((byte) (cursor.getInt(cursor
					.getColumnIndex(DBHelper.KEY_REMB_PWD))) == 1) ? true
					: false);
			userAccounts[i].setAutoLog(((byte) (cursor.getInt(cursor
					.getColumnIndex(DBHelper.KEY_AUTO_LOG))) == 1) ? true
					: false);
			userAccounts[i].setLastAcc(((byte) (cursor.getInt(cursor
					.getColumnIndex(DBHelper.KEY_LAST_ACC))) == 1) ? true
					: false);
			cursor.moveToNext();
		}
		cursor.close();
		return userAccounts;
	}

	public class UserAccount {

		private String mUserName = "", mPassWord = "", mTime = "";
		private boolean mLastAcc = false, mRembPwd = true, mAutoLog = false;

		/**
		 * @Name OptionsDBHelper.UserAccounts
		 **/
		public UserAccount() {
			// TODO Auto-generated constructor stub
		}

		public UserAccount(String username, String password, boolean lastacc,
				boolean rempwd, boolean autolog, String time) {
			this.mUserName = username;
			this.mPassWord = password;
			this.mRembPwd = rempwd;
			this.mAutoLog = autolog;
			this.mLastAcc = lastacc;
			this.mTime = time;
		}

		public String getmTime() {
			return mTime;
		}

		public void setmTime(String mTime) {
			this.mTime = mTime;
		}

		public void setUserName(String un) {
			this.mUserName = un;
		}

		public String getUserName() {
			return this.mUserName;
		}

		public void setPassWord(String pwd) {
			this.mPassWord = pwd;
		}

		public String getPassWord() {
			return this.mPassWord;
		}

		public void setRembPwd(boolean rem) {
			this.mRembPwd = rem;
		}

		public boolean getRembPwd() {
			return this.mRembPwd;
		}

		public void setAutoLog(boolean auto) {
			this.mAutoLog = auto;
		}

		public boolean getAutoLog() {
			return this.mAutoLog;
		}

		public void setLastAcc(boolean la) {
			this.mLastAcc = la;
		}

		public boolean getLastAcc() {
			return this.mLastAcc;
		}

		@Override
		public String toString() {
			String str = "账号:" + this.mUserName + "," + "密码:" + this.mPassWord
					+ "," + "记住密码:" + this.mRembPwd + "," + "自动登录:"
					+ this.mAutoLog + "," + "是否为上次账 ?:" + this.mLastAcc;
			return str;
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
     * 获得数据库路径，如果不存在，则创建对象对 ?  
     *   
     * @param name  
     * @param mode  
     * @param factory  
	 * @throws IOException 
     */  
    public File getDatabasePath(String name) throws IOException {  
        // 判断是否存在sd ?  
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED  
                .equals(android.os.Environment.getExternalStorageState());  
        if (!sdExist) {// 如果不存 ?,  
            Log.e("SD卡管理：", "SD卡不存在，请加载SD ?");  
            return null;  
        } else {// 如果存在  
                // 获取sd卡路 ?  
            String dbDir = android.os.Environment.getExternalStorageDirectory()  
                    .getAbsolutePath();  
            dbDir += "/databases";// 数据库所在目 ?  
            String dbPath = dbDir + "/" + name;// 数据库路 ?  
            // 判断目录是否存在，不存在则创建该目录  
            File dirFile = new File(dbDir);  
            if (!dirFile.exists())  
                dirFile.mkdirs();  
  
            // 数据库文件是否创建成 ?  
            boolean isFileCreateSuccess = false;  
            // 判断文件是否存在，不存在则创建该文件  
            File dbFile = new File(dbPath);  
            if (!dbFile.exists()) {  
//                try {  
                    isFileCreateSuccess = dbFile.createNewFile();// 创建文件  
//                } catch (IOException e) {  
//                    // TODO Auto-generated catch block  
//                    e.printStackTrace();  
//                }  
            } else  
                isFileCreateSuccess = true;  
  
            // 返回数据库文件对 ?  
            if (isFileCreateSuccess)  
                return dbFile;  
            else  
                return null;  
        }  
    }  
	public class DBHelper extends SQLiteOpenHelper {

		public static final boolean D = true;
		public static final String TAG = "---DBHelper---";// LogCat

		private static final String DB_TABLE = "accounts";// 数据库表 ?

//		private static final int DB_VERSION = 1;// 数据库版本号

		private static final String KEY_USERNAME = "UserName";
		private static final String KEY_PASSWORD = "PassWord";
		private static final String KEY_REMB_PWD = "RembPwd";
		private static final String KEY_AUTO_LOG = "AutoLog";
		private static final String KEY_LAST_ACC = "LastAcc";
		private static final String KEY_TIME = "time";
		private static final String CREATE_TABLE_SQL = "CREATE TABLE "
				+ DB_TABLE + " (" + KEY_USERNAME
				+ " VARCHAR(32) NOT NULL PRIMARY KEY, " + KEY_PASSWORD
				+ " VARCHAR(32) NOT NULL, " + KEY_REMB_PWD
				+ " BIT(1) DEFAULT 1, " + KEY_AUTO_LOG + " BIT(1) DEFAULT 0, "
				+ KEY_TIME + " VARCHAR(32) NOT NULL, " + KEY_LAST_ACC
				+ " BIT(1) DEFAULT 0);";

		public DBHelper(Context context) {
			super(new DbContext(context), DB_LOCALNAME, null, 1);
		}
		public DBHelper(Context context,String dbname) {
			super(context, dbname, null, 1);
		}
		// public DBHelper(Context context, String name, CursorFactory factory,
		// int version) {
		// super(context, GetDbName(), factory, version);
		// }

		@Override
		public void onCreate(SQLiteDatabase db) {
			// 第一个使用数据库时自动建 ?
			db.execSQL(CREATE_TABLE_SQL);
		}

		/**
		 * 
		 * 函数在数据库 ?要升级时被调用，  ?般用来删除旧的数据库表，并将数据转移到新版本的数据库表中
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
