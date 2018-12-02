package com.manager;

import java.util.ArrayList;
import java.util.List;

import com.basic.DoTaskDO;
import com.basic.TaskDO;
import com.common.CommonMethord;
import com.db.DBOpenHelper_Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager_Data {
	// Fields
	private String _ConnString4Data;
	private static DbManager_Data _Instance;

	private DBOpenHelper_Data helper;
	private SQLiteDatabase database;
	private Cursor cursor;
	private Cursor cursorBatchNo;

	public DbManager_Data(Context context) {

		helper = new DBOpenHelper_Data(context);
		database = helper.getWritableDatabase();
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public Cursor getcursorBatchNo() {
		return cursor;
	}

	public void setcursorBatchNo(Cursor cursor) {
		this.cursorBatchNo = cursor;
	}

	public DBOpenHelper_Data getHelper() {
		return helper;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public Cursor GetAllTask() {

		if (database == null) {
			// database = helper.getWritableDatabase();
		}
		if (!database.isOpen()) {
			// database = helper.getWritableDatabase();
		}
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database.rawQuery("select id from Task", null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * 获取所有任务
	 * 
	 * @param iflag
	 *            0=所有，1=已完成,3=未完成
	 * @return
	 */
	public Cursor GetAllTask(int iflag) {

		if (database == null) {
			// database = helper.getWritableDatabase();
		}
		if (!database.isOpen()) {
			// database = helper.getWritableDatabase();
		}
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		String sql = "select id from Task where 1=1 ";
		if (iflag == 0)// 所有
		{
			sql += "";
		} else if (iflag == 1)// 已完成
		{
			sql += " and DO_STATUS is not null";
		} else if (iflag == 2)// 未完成
		{
			sql += " AND DO_STATUS is null";
		}
		cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}

	public Cursor GetAllDoTask() {

		if (database == null) {
			// database = helper.getWritableDatabase();
		}
		if (!database.isOpen()) {
			// database = helper.getWritableDatabase();
		}
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database
				.rawQuery(
						"select id from DoTask where upload_status = null or upload_status = ''",
						null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}

	public Cursor GetTasks() {
		if (database == null) {
			// database = helper.getWritableDatabase();
		}
		if (!database.isOpen()) {
			// database = helper.getWritableDatabase();
		}
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database
				.rawQuery(
						"select "
								+ "ID, AREA_NAME, HOUSE_NUMBER, HOUSE_NAME, HOUSE_ADDRESS, ASSET_CODE, "
								+ "IS_ACTIVED, MAKE_DATE, MAKE_USER, REMARK, DO_STATUS, DO_USER, DO_DATE "
								+ "from Tasks where DO_STATUS == null or DO_STATUS == ''",
						null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}

	public void InsertTask(TaskDO task) {
		database.execSQL(
				"insert into Task "
						+ "("
						+ "ID, AREA_NAME, HOUSE_NUMBER, HOUSE_NAME, HOUSE_ADDRESS, ASSET_CODE, "
						+ "IS_ACTIVED, MAKE_DATE, MAKE_USER, REMARK" + ")"
						+ "values (?, ?, ?, ?, ?, ?, ?,?,?,?)", new Object[] {
						task.ID, task.AREA_NAME, task.HOUSE_NUMBER,
						task.HOUSE_NAME, task.HOUSE_ADDRESS, task.ASSET_CODE,
						task.IS_ACTIVED, task.MAKE_DATE, task.MAKE_USER,
						task.REMARK });

	}

	public boolean InsertDoTask(DoTaskDO dotask) {
		// 开启事务
		database.beginTransaction();
		try {
			database.execSQL(
					"insert into DoTask "
							+ "("
							+ "ID, TASK_ID,PRE_ASSET_CODE, DEGRESS_NUMBER, ASSET_CODE, CLOSE_SINE_CODE1,CLOSE_SINE_CODE2, REMARK"
							+ ")" + "values (?, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { dotask.ID, dotask.TASK_ID,
							dotask.PRE_ASSET_CODE, dotask.DEGRESS_NUMBER,
							dotask.ASSET_CODE, dotask.CLOSE_SINE_CODE1,
							dotask.CLOSE_SINE_CODE2, dotask.REMARK });

			database.execSQL(
					"update Task " + "set do_status=? " + "where id=?",
					new Object[] { "已完成", dotask.TASK_ID });
			
			UpdateTaskAll();

			// 设置事务标志为成功，当结束事务时就会提交事务
			database.setTransactionSuccessful();
		} catch (Exception e) { 
				return false;
		} finally {
			// 结束事务
			database.endTransaction();
			return true;
		}
	}
	
	/**
	 * 更新任务表为已完成，参照所有已完成的任务数据中的task_id
	 */
	public void UpdateTaskAll()
	{
		database.execSQL(
				"update task set do_status = '已完成' " +
				"where id in( " +
				"      select task_id from doTask " +
				")"
				); 
	}

	public void UpdateTask(DoTaskDO dotask) {
		database.execSQL("update Task " + "set do_status=? " + "where id=?",
				new Object[] { "已完成", dotask.TASK_ID });
	}

	public void UpdateTaskNull(String id) {
		database.execSQL("update Task " + "set do_status=? " + "where id=?",
				new Object[] { null, id });
	}

	/*
	 * 修改doTask表的上传状态
	 */
	public void UpdateDoTask(DoTaskDO task) {
		database.execSQL("update DoTask " + "set upload_status=? "
				+ "where id=?", new Object[] { "已上传", task.ID });
	}

	/*
	 * 修改doTask表内容
	 */
	public void UpdateDoTaskValues(String id, String PRE_ASSET_CODE,
			String DEGRESS_NUMBER, String ASSET_CODE, String CLOSE_SINE_CODE1,
			String CLOSE_SINE_CODE2, String REMARK) {
		database.execSQL(
				"update DoTask "
						+ "set PRE_ASSET_CODE=?, DEGRESS_NUMBER=?, ASSET_CODE=?, CLOSE_SINE_CODE1=?,CLOSE_SINE_CODE2=?, REMARK=? "
						+ "where id=?", new Object[] { PRE_ASSET_CODE,
						DEGRESS_NUMBER, ASSET_CODE, CLOSE_SINE_CODE1,
						CLOSE_SINE_CODE2, REMARK, id });
	}

	public void DeleteTask(String id) {
		if (id == null) {
			database.execSQL("delete from Task");
		} else {
			database.execSQL("delete from Task where id=?", new Object[] { id });
		}
	}

	public void DeleteDoTask(String id) {
		if (id == null)// 删全部
		{
			database.execSQL("delete from DoTask");
		} else {
			database.execSQL("delete from DoTask where TASK_ID = '" + id + "'");
		}
	}

	public TaskDO GetTask(String AssetCode) {
		TaskDO task = null;
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database.rawQuery("select * from task where ASSET_CODE='"
				+ AssetCode + "'", null);
		if (cursor.moveToFirst()) {
			String ID = cursor.getString(cursor.getColumnIndex("id"));
			String AREA_NAME = cursor.getString(cursor
					.getColumnIndex("AREA_NAME"));
			String HOUSE_NUMBER = cursor.getString(cursor
					.getColumnIndex("HOUSE_NUMBER"));
			String HOUSE_NAME = cursor.getString(cursor
					.getColumnIndex("HOUSE_NAME"));
			String HOUSE_ADDRESS = cursor.getString(cursor
					.getColumnIndex("HOUSE_ADDRESS"));
			String ASSET_CODE = cursor.getString(cursor
					.getColumnIndex("ASSET_CODE"));
			String IS_ACTIVED = cursor.getString(cursor
					.getColumnIndex("IS_ACTIVED"));
			String MAKE_DATE = cursor.getString(cursor
					.getColumnIndex("MAKE_DATE"));
			String MAKE_USER = cursor.getString(cursor
					.getColumnIndex("MAKE_USER"));
			String REMARK = cursor.getString(cursor.getColumnIndex("REMARK"));
			String DO_STATUS = cursor.getString(cursor
					.getColumnIndex("DO_STATUS"));
			String DO_USER = cursor.getString(cursor.getColumnIndex("DO_USER"));
			String DO_DATE = cursor.getString(cursor.getColumnIndex("DO_DATE"));

			task = new TaskDO();
			task.ID = ID;
			task.AREA_NAME = AREA_NAME;
			task.HOUSE_NUMBER = HOUSE_NUMBER;
			task.HOUSE_NAME = HOUSE_NAME;
			task.HOUSE_ADDRESS = HOUSE_ADDRESS;
			task.ASSET_CODE = ASSET_CODE;
			task.IS_ACTIVED = IS_ACTIVED;
			task.MAKE_DATE = MAKE_DATE;
			task.MAKE_USER = MAKE_USER;
			task.REMARK = REMARK;
			task.DO_STATUS = DO_STATUS;
			task.DO_USER = DO_USER;
			task.DO_DATE = DO_DATE;

			cursor.close();
			return task;
		} else {
			cursor.close();
			return task;
		}

	}

	/**
	 * 获取sqlite数据库中的数据
	 * 
	 * @param AreaName
	 * @param HouseNumber
	 * @param HouseName
	 * @param HouseAddress
	 * @param AssetCode
	 * @param iStatus
	 *            0=未完成,1=已完成,2=全部
	 * @param iType
	 *            0 = 更换,1=新装,2=全部
	 * @return
	 */
	public List<TaskDO> GetTaskList(String AreaName, String HouseNumber,
			String HouseName, String HouseAddress, String AssetCode,
			int iStatus, int iType) {
		List<TaskDO> taskList = new ArrayList<TaskDO>();
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		String sql = "select * from task where 1=1 ";
		if (iStatus == 0)// 未完成
		{
			sql += " and (DO_STATUS is null or DO_STATUS = '')";
		} else if (iStatus == 1)// 已完成
		{
			sql += " and (DO_STATUS is not null or DO_STATUS != '')";
		}

		if (iType == 0)// 更换
		{
			sql += " and (ASSET_CODE != null or ASSET_CODE != '')";
		} else if (iType == 1)// 新装
		{
			sql += " and (ASSET_CODE = null or ASSET_CODE = '' or ASSET_CODE = '0')";
		}

		if (AreaName != null && AreaName.length() > 0) {
			sql += " and AREA_NAME like '%" + AreaName + "%'";
		}
		if (HouseNumber != null && HouseNumber.length() > 0) {
			sql += " and HOUSE_NUMBER like '%" + HouseNumber + "%'";
		}
		if (HouseName != null && HouseName.length() > 0) {
			sql += " and HOUSE_NAME like '%" + HouseName + "%'";
		}
		if (HouseAddress != null && HouseAddress.length() > 0) {
			sql += " and HOUSE_ADDRESS like '%" + HouseAddress + "%'";
		}
		if (AssetCode != null && AssetCode.length() > 0) {
			sql += " and ASSET_CODE like '%" + AssetCode + "%'";
		}

		sql += " order by AREA_NAME,ID limit 0,500";
		cursor = database.rawQuery(sql, null);
		if (cursor == null) {
			return null;
		}
		// [Area_Name, ID, House_Number, House_Name, House_Address, Asset_Code,
		// Is_Actived, Make_Date, Make_user, Remark, Do_Status, Do_User,
		// Do_Date]
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String ID = cursor.getString(cursor.getColumnIndex("ID"));
			String AREA_NAME = cursor.getString(cursor
					.getColumnIndex("AREA_NAME"));
			String HOUSE_NUMBER = cursor.getString(cursor
					.getColumnIndex("HOUSE_NUMBER"));
			String HOUSE_NAME = cursor.getString(cursor
					.getColumnIndex("HOUSE_NAME"));
			String HOUSE_ADDRESS = cursor.getString(cursor
					.getColumnIndex("HOUSE_ADDRESS"));
			String ASSET_CODE = cursor.getString(cursor
					.getColumnIndex("ASSET_CODE"));
			String IS_ACTIVED = cursor.getString(cursor
					.getColumnIndex("IS_ACTIVED"));
			String MAKE_DATE = cursor.getString(cursor
					.getColumnIndex("MAKE_DATE"));
			String MAKE_USER = cursor.getString(cursor
					.getColumnIndex("MAKE_USER"));
			String REMARK = cursor.getString(cursor.getColumnIndex("REMARK"));
			String DO_STATUS = cursor.getString(cursor
					.getColumnIndex("DO_STATUS"));
			String DO_USER = cursor.getString(cursor.getColumnIndex("DO_USER"));
			String DO_DATE = cursor.getString(cursor.getColumnIndex("DO_DATE"));

			TaskDO task = new TaskDO();
			task.ID = ID;
			task.AREA_NAME = AREA_NAME;
			task.HOUSE_NUMBER = HOUSE_NUMBER;
			task.HOUSE_NAME = HOUSE_NAME;
			task.HOUSE_ADDRESS = HOUSE_ADDRESS;
			task.ASSET_CODE = ASSET_CODE;
			task.IS_ACTIVED = IS_ACTIVED;
			task.MAKE_DATE = MAKE_DATE;
			task.MAKE_USER = MAKE_USER;
			task.REMARK = REMARK;
			task.DO_STATUS = DO_STATUS;
			task.DO_USER = DO_USER;
			task.DO_DATE = DO_DATE;

			taskList.add(task);
		}
		cursor.close();
		return taskList;
	}

	public List<DoTaskDO> GetDoTask(String id) {

		List<DoTaskDO> doTasklist = new ArrayList<DoTaskDO>();

		String sql = "select * from dotask where TASK_ID='" + id + "'";
		cursor = database.rawQuery(sql, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String ID = cursor.getString(cursor.getColumnIndex("ID"));
			String TASK_ID = cursor.getString(cursor.getColumnIndex("TASK_ID"));
			String Pre_Asset_Code = cursor.getString(cursor
					.getColumnIndex("PRE_ASSET_CODE"));
			String ASSET_CODE = cursor.getString(cursor
					.getColumnIndex("ASSET_CODE"));
			String Degress_Number = cursor.getString(cursor
					.getColumnIndex("DEGRESS_NUMBER"));
			String Close_Sine_Code1 = cursor.getString(cursor
					.getColumnIndex("CLOSE_SINE_CODE1"));
			String Close_Sine_Code2 = cursor.getString(cursor
					.getColumnIndex("CLOSE_SINE_CODE2"));
			String REMARK = cursor.getString(cursor.getColumnIndex("REMARK"));

			DoTaskDO task = null;
			task = new DoTaskDO();
			task.ID = ID;
			task.TASK_ID = TASK_ID;
			task.ASSET_CODE = ASSET_CODE;
			task.REMARK = REMARK;
			task.PRE_ASSET_CODE = Pre_Asset_Code;
			task.DEGRESS_NUMBER = Degress_Number;
			task.CLOSE_SINE_CODE1 = Close_Sine_Code1;
			task.CLOSE_SINE_CODE2 = Close_Sine_Code2;
			doTasklist.add(task);
		}
		cursor.close();
		return doTasklist;
	}

	public List<DoTaskDO> GetDoTaskByTaskID(String task_id) {
		List<DoTaskDO> dotasklist = new ArrayList<DoTaskDO>();
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database.rawQuery("select * from DoTask where TASK_ID='"
				+ task_id + "'", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String ID = cursor.getString(cursor.getColumnIndex("ID"));

			String TASK_ID = cursor.getString(cursor.getColumnIndex("TASK_ID"));
			String Pre_Asset_Code = cursor.getString(cursor
					.getColumnIndex("PRE_ASSET_CODE"));
			String ASSET_CODE = cursor.getString(cursor
					.getColumnIndex("ASSET_CODE"));
			String Degress_Number = cursor.getString(cursor
					.getColumnIndex("DEGRESS_NUMBER"));
			String Close_Sine_Code1 = cursor.getString(cursor
					.getColumnIndex("CLOSE_SINE_CODE1"));
			String Close_Sine_Code2 = cursor.getString(cursor
					.getColumnIndex("CLOSE_SINE_CODE2"));
			String REMARK = cursor.getString(cursor.getColumnIndex("REMARK"));

			DoTaskDO task = new DoTaskDO();
			task.ID = ID;
			task.TASK_ID = TASK_ID;
			task.ASSET_CODE = ASSET_CODE;
			task.REMARK = REMARK;
			task.PRE_ASSET_CODE = Pre_Asset_Code;
			task.DEGRESS_NUMBER = Degress_Number;
			task.CLOSE_SINE_CODE1 = Close_Sine_Code1;
			task.CLOSE_SINE_CODE2 = Close_Sine_Code2;
			dotasklist.add(task);
		}

		cursor.close();
		return dotasklist;
	}

	public Cursor GetTask_cur(String assetCode) {
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database.rawQuery("select * from Task where ASSET_CODE='"
				+ assetCode + "'", null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}

	}

}
