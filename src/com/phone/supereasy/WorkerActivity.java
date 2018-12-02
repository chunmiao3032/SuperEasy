package com.phone.supereasy;

import java.util.ArrayList;
import java.util.List;

import com.basic.DoTaskDO;
import com.manager.DbManager_Data;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WorkerActivity extends Activity {

	Button btAssetCode, btCloseSineNumber1, btCloseSineNumber2, btSave,
			btClear, btNext;
	EditText etAreaName, etHouseNumber, etHouseName, etHouseAddress,
			etPreAssetCode, etDegressNumber, etAssetCode, etCloseSineNumber1,
			etCloseSineNumber2, etRemark;
	TextView textViewID;
	/******************************************************/
	// ************* 全局变量*****************
	// 任务信息
	String tvID;
	String tvAreaName;
	String tvHouseNumber;
	String tvHouseName;
	String tvHouseAddress;
	String tvAssetCode;
	String Position;
	String Add;// 是否是追加

	DbManager_Data db;
	// 任务完成数据
	List<DoTaskDO> doneTaskList = new ArrayList<DoTaskDO>();// 记录已录入的数据
	String _currentDoTaskID = null;// 当前已完成任务的id
	int idx = 0;// 临时变量

	/******************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker);

		init();

		if (Add.equals("0")) {
			db = new DbManager_Data(getApplication());
			doneTaskList = db.GetDoTaskByTaskID(tvID);
			if (doneTaskList != null && doneTaskList.size() > 0) {
				DoTaskDO doTask = doneTaskList.get(0);
				_currentDoTaskID = doTask.ID;
				etDegressNumber.setText(doTask.DEGRESS_NUMBER);
				etAssetCode.setText(doTask.ASSET_CODE);
				etCloseSineNumber1.setText(doTask.CLOSE_SINE_CODE1);
				etCloseSineNumber2.setText(doTask.CLOSE_SINE_CODE2);
				etRemark.setText(doTask.REMARK);
				if (doneTaskList.size() > 1) {
					this.btNext.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void init() {
		Intent intent1 = getIntent();
		tvID = intent1.getStringExtra("tvID");
		tvAreaName = intent1.getStringExtra("tvAreaName");
		tvHouseNumber = intent1.getStringExtra("tvHouseNumber");
		tvHouseName = intent1.getStringExtra("tvHouseName");
		tvHouseAddress = intent1.getStringExtra("tvHouseAddress");
		tvAssetCode = intent1.getStringExtra("tvAssetCode");
		Position = intent1.getStringExtra("Position");
		Add = intent1.getStringExtra("Add");

		textViewID = (TextView) findViewById(R.id.tvID);
		etAreaName = (EditText) findViewById(R.id.etAreaName);
		etHouseNumber = (EditText) findViewById(R.id.etHouseNumber);
		etHouseName = (EditText) findViewById(R.id.etHouseName);
		etHouseAddress = (EditText) findViewById(R.id.etHouseAddress);
		etPreAssetCode = (EditText) findViewById(R.id.etPreAssetCode);

		textViewID.setText(tvID);
		etAreaName.setText(tvAreaName);
		etHouseNumber.setText(tvHouseNumber);
		etHouseName.setText(tvHouseName);
		etHouseAddress.setText(tvHouseAddress);
		etPreAssetCode.setText(tvAssetCode);

		etDegressNumber = (EditText) findViewById(R.id.etDegressNumber);
		etAssetCode = (EditText) findViewById(R.id.etAssetCode);
		etCloseSineNumber1 = (EditText) findViewById(R.id.etCloseSineCode1);
		etCloseSineNumber2 = (EditText) findViewById(R.id.etCloseSineCode2);
		etRemark = (EditText) findViewById(R.id.etRemark);

		btAssetCode = (Button) findViewById(R.id.btAssetCode);
		btCloseSineNumber1 = (Button) findViewById(R.id.btCloseSineCode1);
		btCloseSineNumber2 = (Button) findViewById(R.id.btCloseSineCode2);
		btSave = (Button) findViewById(R.id.btSave);
		btClear = (Button) findViewById(R.id.btClear);
		btNext = (Button) findViewById(R.id.btNext);

		btSave.setOnClickListener(btSave_OnClick);
		btClear.setOnClickListener(btClear_OnClick);
		btAssetCode.setOnClickListener(btAssetCode_OnClick);
		btCloseSineNumber1.setOnClickListener(btCloseSineNumber1_OnClick);
		btCloseSineNumber2.setOnClickListener(btCloseSineNumber2_OnClick);
		btNext.setOnClickListener(btNext_OnClick);
	}

	// 保存
	private OnClickListener btSave_OnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			String strDegressNumber = etDegressNumber.getText().toString();
			String strAssetCode = etAssetCode.getText().toString();
			String strCloseSineNumber1 = etCloseSineNumber1.getText()
					.toString();
			String strCloseSineNumber2 = etCloseSineNumber2.getText()
					.toString();
			String strRemark = etRemark.getText().toString();

			// 无原资产编号=新装
			if (tvAssetCode.trim().equals("0")
					|| tvAssetCode.trim().length() == 0) {
				if (strDegressNumber.trim().length() > 0
						&& !strDegressNumber.trim().equals("0")) {
					Toast.makeText(getApplication(), "新装表【有功总示数】不需要填写！", 2)
							.show();
					return;
				}
			}
			if (strDegressNumber == null
					|| strDegressNumber.trim().length() == 0) {
				Toast.makeText(getApplication(), "【有功总示数】不可以为空！", 2).show();
				return;
			}
			if (strAssetCode.trim().length() == 0) {
				Toast.makeText(getApplication(), "新资产编号】需要填写！", 2).show();
				return;
			}
			if (strCloseSineNumber2.trim().length() == 0
					&& strCloseSineNumber1.trim().length() == 0) {
				Toast.makeText(getApplication(), "【封印号】需要至少填写1个！", 2).show();
				return;
			}
			// 新添加数据
			if (_currentDoTaskID == null || _currentDoTaskID.length() == 0) {
				DoTaskDO dotask = new DoTaskDO();
				dotask.TASK_ID = tvID;
				dotask.ASSET_CODE = strAssetCode;
				dotask.DEGRESS_NUMBER = strDegressNumber;
				dotask.CLOSE_SINE_CODE1 = strCloseSineNumber1;
				dotask.CLOSE_SINE_CODE2 = strCloseSineNumber2;
				dotask.REMARK = strRemark;

				db = new DbManager_Data(getApplication());
				if (db.InsertDoTask(dotask)) {
					Toast.makeText(getApplication(), "保存成功!", 2).show();
					Intent intent = new Intent();
					intent.putExtra("TASK_ID", tvID);
					intent.putExtra("Position", Position);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(getApplication(), "保存失败，请重试!", 2).show();
				}
			} else// 修改已存在的新装数据
			{
				db.UpdateDoTaskValues(_currentDoTaskID, tvAssetCode,
						strDegressNumber, strAssetCode, strCloseSineNumber1,
						strCloseSineNumber2, strRemark);
				Toast.makeText(getApplication(), "修改成功!", 2).show();
				finish();
			}

		}
	};

	// 清除
	// 清除
	private OnClickListener btClear_OnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			etDegressNumber.setText("");
			etAssetCode.setText("");
			etCloseSineNumber1.setText("");
			etCloseSineNumber2.setText("");
			etRemark.setText("");
		}
	};
	// 清除
	// 下一条
	private OnClickListener btNext_OnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (doneTaskList != null && doneTaskList.size() > 0) {
				if (idx >= doneTaskList.size()) {
					idx = 0;
				}
				DoTaskDO doTask = doneTaskList.get(idx);
				_currentDoTaskID = doTask.ID;
				etDegressNumber.setText(doTask.DEGRESS_NUMBER);
				etAssetCode.setText(doTask.ASSET_CODE);
				etCloseSineNumber1.setText(doTask.CLOSE_SINE_CODE1);
				etCloseSineNumber2.setText(doTask.CLOSE_SINE_CODE2);
				etRemark.setText(doTask.REMARK);
				idx++;
			}
		}
	};

	// 资产编码扫码
	private OnClickListener btAssetCode_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(WorkerActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 1);

		}
	};
	// 封印号 1 扫码
	private OnClickListener btCloseSineNumber1_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(WorkerActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 2);

		}
	};
	// 封印号 2 扫码
	private OnClickListener btCloseSineNumber2_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(WorkerActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 3);

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
		if (requestCode == 2 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etCloseSineNumber1.setText(scanResult);
		}
		if (requestCode == 3 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etCloseSineNumber2.setText(scanResult);
		}
	}

}
