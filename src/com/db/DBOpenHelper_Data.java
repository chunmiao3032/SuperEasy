package com.db;

import java.util.Date;

//import com.basic.AssetDO;
import com.common.Global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
  
public class DBOpenHelper_Data extends SQLiteOpenHelper {

	public DBOpenHelper_Data(Context context) {
		super(context, Global._ConnString4Data, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 
		db.execSQL("CREATE TABLE IF NOT EXISTS Task ("
				+ "AREA_NAME TEXT, "
				+ "ID INTEGER, "
				+ "HOUSE_NUMBER TEXT, "
				+ "HOUSE_NAME TEXT, "
				+ "HOUSE_ADDRESS TEXT, "
				+ "ASSET_CODE TEXT, " 
				+ "IS_ACTIVED TEXT, "
				+ "MAKE_DATE TEXT, "
				+ "MAKE_USER TEXT, "
				+ "REMARK TEXT,"
				+ "DO_STATUS TEXT,"
				+ "DO_USER TEXT,"
				+ "DO_DATE TEXT"
				+ ");");	 
		
		db.execSQL("CREATE TABLE IF NOT EXISTS DoTask ("
				+ "ID INTEGER PRIMARY KEY autoincrement, "
				+"TASK_ID INTEGER,"
				+ "PRE_ASSET_CODE TEXT, " 
				+ "ASSET_CODE TEXT, " 
				+ "DEGRESS_NUMBER TEXT,"
				+ "CLOSE_SINE_CODE1 Text," 
				+ "CLOSE_SINE_CODE2 Text," 
				+ "REMARK TEXT,"
				+ "UPLOAD_STATUS Text" 
				+ ");");	 
	
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Task");
		db.execSQL("DROP TABLE IF EXISTS DoTask"); 
		onCreate(db);
	}


} 