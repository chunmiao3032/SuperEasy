package com.phone.supereasy;

import java.io.Serializable;
import java.util.List;

import com.basic.TaskDO;
import com.manager.DbManager_Data;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class TaskQueryActivity extends Activity {

	//***********************全局变量***********************
	List<TaskDO> taskList;
	//***************************************************
	ProgressDialog loginDialog;
	EditText etAreaName, etHouseNumber, etHouseName, etHouseAddress,
			etAssetCode;
	Button btQuery, btClear, btScanAssetCode;
	CheckBox chkDone,chkNew;

	DbManager_Data db;

	Handler cwjHandler_getCnt = null;
	final Runnable mUpdateResults_getCnt = new Runnable() {
		public void run() {
			updateUI_getCnt();
		}

		private void updateUI_getCnt() {

			if(taskList != null && taskList.size() > 0)
			{
				ProgressBarCancel();
				Intent intent = new Intent(TaskQueryActivity.this, TaskQueryResultActivity.class);
				intent.putExtra("list", (Serializable)taskList); 
				startActivity(intent);
			}
			else
			{
				 Toast.makeText(getApplication(), "没有符合条件的数据！", 2000).show();
				 ProgressBarCancel();
			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskquery);
		
		cwjHandler_getCnt = new Handler(getApplication().getMainLooper());
		init();
	}

	private void init() {
		etAreaName = (EditText) findViewById(R.id.etAreaName);
		etHouseNumber = (EditText) findViewById(R.id.etHouseNumber);
		etHouseName = (EditText) findViewById(R.id.etHouseName);
		etHouseAddress = (EditText) findViewById(R.id.etHouseAddress);
		etAssetCode = (EditText) findViewById(R.id.etAssetCode);
		btQuery = (Button) findViewById(R.id.btQuery);
		btClear = (Button) findViewById(R.id.btClear);
		btScanAssetCode = (Button) findViewById(R.id.btScanAssetCode);
		chkDone = (CheckBox)findViewById(R.id.chkDone);
		chkNew = (CheckBox)findViewById(R.id.chkNew);
		
		btQuery.setOnClickListener(btQuery_click);
		btClear.setOnClickListener(bClear_click);
		btScanAssetCode.setOnClickListener(btScanAssetCode_click);
	}

	private OnClickListener btQuery_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				loadProgressBar();
				new Thread() {
					@Override
					public void run() {
						getData();
						cwjHandler_getCnt.post(mUpdateResults_getCnt);
					}
				}.start();
			} catch (Exception ex) {
				Toast.makeText(getApplication(), "异常:" + ex.getMessage(), 2000)
						.show();
			}
		}
	};
	
	private OnClickListener bClear_click = new OnClickListener() {

		@Override
		public void onClick(View v) { 
			etAreaName.setText("");
			etHouseNumber.setText("");
			etHouseName.setText("");
			etHouseAddress.setText("");
			etAssetCode.setText("");
		}
	
	};

	private OnClickListener btScanAssetCode_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(TaskQueryActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 1);

		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
 
		if (requestCode == 1 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etAssetCode.setText(scanResult);
		}
		
	}
	
	
	private void getData() {
		String stretAreaName = etAreaName.getText().toString();
		String stretHouseNumber = etHouseNumber.getText().toString();
		String stretHouseName = etHouseName.getText().toString();
		String stretHouseAddress = etHouseAddress.getText().toString();
		String stretAssetCode = etAssetCode.getText().toString();

		db = new DbManager_Data(getApplication());
		int iDone = 0;//0=未完成,1=已完成,2=全部
		if(chkDone.isChecked())
		{ 
			iDone = 1;
		} 
		int iNew = 2;// 0 = 更换,1=新装,2=全部
		if(chkNew.isChecked())
		{
			iNew= 1;
		}
		
		db.UpdateTaskAll();
		
		taskList = db.GetTaskList(
						stretAreaName, stretHouseNumber,
						stretHouseName, stretHouseAddress, 
						stretAssetCode,iDone,iNew);
		
	}

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("正在检索数据请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	private void ProgressBarCancel() {
		loginDialog.cancel();
		if (loginDialog != null && loginDialog.isShowing()) {
			loginDialog.dismiss();
		}
	}

}
