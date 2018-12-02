package com.phone.supereasy;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.basic.DoTaskDO;
import com.basic.TaskDO;
import com.manager.DbManager_Data;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DataSyncActivity extends Activity {
	// ------------------------------------------

	// ------------------------------------------
	Button btnDownload;
	ProgressDialog loginDialog;
	TextView tvMsg;
	String errMsg = "";

	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			updateUI();
		}
	};

	private void updateUI() {
		loginDialog.cancel();
		if (loginDialog != null && loginDialog.isShowing()) {
			loginDialog.dismiss();
		}
		tvMsg.setText(errMsg);
		Toast.makeText(getApplication(), errMsg, 15000).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datasync);

		cwjHandler = new Handler(getApplication().getMainLooper());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		init();
	}

	private void init() {
		btnDownload = (Button) findViewById(R.id.btnDownload);
		btnDownload.setOnClickListener(btnDownload_Click);
		tvMsg = (TextView) findViewById(R.id.tvMsg);
	}

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("�����ϴ��������Ե�...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	OnClickListener btnDownload_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {

			try {
				new AlertDialog.Builder(DataSyncActivity.this)
						.setTitle("ϵͳ��ʾ")
						.setMessage(
								"ȷ��Ҫִ������ͬ��������\r\n1.���Ƚ���������ɵ������ϴ���������.\r\n2.ɾ��������������ݡ�\r\n3.�ӷ���������δ��ɵ���������.")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										loadProgressBar();
										new Thread() {
											public void run() {
												errMsg = "";
												DoUploadWork();
												DoDownLoadWork();
												cwjHandler.post(mUpdateResults);
											}

										}.start();

									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();

			} catch (Exception e) {
				e.toString();
			}

		}
	};

	private void DoUploadWork() {
		try {
			// ȫ�ֱ���
			String GlobalUser = "";
			String GlobalUserName = "";
			String GlobalPass = "";
			String GlobalVersion = "";
			String GlobalDeviceID = "";
			try {
				// ��ȡȫ�ֱ���
				SharedPreferences remdname = getSharedPreferences("Global",
						Activity.MODE_PRIVATE);
				GlobalUser = remdname.getString("GlobalUser", "");
				GlobalUserName = remdname.getString("GlobalUserName", "");
				GlobalPass = remdname.getString("GlobalPass", "");
				GlobalVersion = remdname.getString("GlobalVersion", "");
				GlobalDeviceID = remdname.getString("GlobalDeviceID", "");
			} catch (Exception ex) {

			}
			// �ϴ����������
			DbManager_Data db = new DbManager_Data(getApplication());
			Cursor allDoTask = db.GetAllTask(1);
			if (allDoTask == null) {
				errMsg += "û��������Ҫ�ϴ���\n\r";
				return;
			}
			int ic = allDoTask.getCount();
			if (ic == 0) {
				errMsg += "û��������Ҫ�ϴ���\n\r";
				return;
			}

			List<String> list_ids = new ArrayList<String>();

			for (allDoTask.moveToFirst(); !allDoTask.isAfterLast(); allDoTask
					.moveToNext()) {
				int iid = allDoTask.getColumnIndex("ID");
				String id = allDoTask.getString(iid);
				list_ids.add(id);
			}

			int iSuccCnt = 0;
			for (String id : list_ids) {
				List<DoTaskDO> taskList = db.GetDoTask(id);
				if (taskList != null && taskList.size() > 0) {
					for (DoTaskDO task : taskList) {

						try {
							String rlt = WebServiceManager.SubmitDoTask(
									task.TASK_ID, task.PRE_ASSET_CODE,
									task.ASSET_CODE, task.CLOSE_SINE_CODE1,
									task.CLOSE_SINE_CODE2, task.DEGRESS_NUMBER,
									task.REMARK, GlobalUser, GlobalUserName,
									GlobalPass, GlobalVersion, GlobalDeviceID);
							if (rlt == null || !rlt.equals("success")) {

								errMsg += "TaskID[" + task.ID + "]" + rlt
										+ "\n\r";
								continue;
							} else {
								db.DeleteDoTask(task.ID);
								iSuccCnt++;
								// db.UpdateDoTask(task);
							}
						} catch (Exception ex) {
							errMsg += "�ϴ��쳣: [task_id =" + task.TASK_ID + "]"
									+ ex.getMessage() + "\n\r";
						}
					}
				}
			}
			errMsg += "�ϴ�:���ݳɹ� " + iSuccCnt + "��!\n\r";
		} catch (Exception ex) {
			errMsg += "�ϴ��쳣: " + ex.getMessage() + "\n\r";
		}
	}

	private void DoDownLoadWork() {
		try {
			DbManager_Data db_data = new DbManager_Data(getApplication());
			Hashtable<String, TaskDO> allTasks = WebServiceManager
					.GetAllTasksHash();

			if ((allTasks == null) || (allTasks.size() == 0)) {
				return;
			}

			db_data.DeleteTask(null);

			Enumeration e = allTasks.elements(); // ����value
			while (e.hasMoreElements()) {
				TaskDO taskdo = (TaskDO) e.nextElement();
				db_data.InsertTask(taskdo);
			}
			errMsg += "����:��" + allTasks.size() + "�����ݣ�\n\r";
		} catch (Exception ex) {
			errMsg += "�����쳣:" + ex.getMessage() + "\n\r";
		}
	}

}
