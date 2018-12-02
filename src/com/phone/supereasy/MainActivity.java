package com.phone.supereasy;

import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button btDowork, btDownload,btQuery;//,btUpload

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

	}

	private void init() {
		btDowork = (Button) findViewById(R.id.btDowork);
		btDownload = (Button) findViewById(R.id.btDownload);
		btQuery = (Button)findViewById(R.id.btQuery);

		btDowork.setOnClickListener(btDowork_Click);
		btDownload.setOnClickListener(btDataSync_Click);
		btQuery.setOnClickListener(btQuery_Click);
	}

	// 数据录入
	private OnClickListener btDowork_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					TaskQueryActivity.class);
			startActivity(intent);
		}
	};

	// 数据同步
	private OnClickListener btDataSync_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					DataSyncActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener btQuery_Click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					DoneTaskQueryActivity.class);
			startActivity(intent);
		}
	};
	

}
