package com.phone.supereasy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.basic.DoneTaskResult;
import com.basic.TaskDO;
import com.manager.DbManager_Data;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DoneTaskQueryResultActivity extends Activity {

	// ***********************全局变量***********************
	List<DoneTaskResult> taskList;
	SimpleAdapter mSimpleAdapter;
	DbManager_Data db;
	// ***************************************************
	ListView lv;
	TextView tvCnt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donetaskqueryresult);
		lv = (ListView) findViewById(R.id.lv);
	 
		tvCnt = (TextView)findViewById(R.id.tvCnt);

		taskList = (List<DoneTaskResult>) getIntent().getSerializableExtra("list");
		initialListView(taskList);
		
		tvCnt.setText(taskList.size() + "条");
	}

//	@Override
//	protected void onResume() {
//		//super.onResume();
//
//	}

	private void initialListView(List<DoneTaskResult> list) {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		int i = 1;
		for (DoneTaskResult task : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("tvIndex", i);
			map.put("tvID", task.ID);
			map.put("tvAreaName", task.AREA_NAME);
			map.put("tvHouseName", task.HOUSE_NAME);
			map.put("tvHouseNo", task.HOUSE_NUMBER);
			map.put("tvHouseAddress", task.HOUSE_ADDRESS);
			map.put("tvPreAssetCode", task.PRE_ASSET_CODE);
			map.put("tvDeregssNumber", task.DEGRESS_NUMBER);
			map.put("tvAssetCode", task.ASSET_CODE);
			map.put("tvCloseSineCode1", task.CLOSE_SINE_CODE1);
			map.put("tvCloseSineCode2", task.CLOSE_SINE_CODE2);
			map.put("tvRemark", task.REMARK);
			map.put("tvMakeUser", task.MAKE_USER);
			map.put("tvMakeDate", task.MAKE_DATE);
 
			listItem.add(map);
			i++;
		}
		mSimpleAdapter = new SimpleAdapter(
				this,
				listItem,// 需要绑定的数据
				R.layout.activity_donetaskqueryresult_item, 
				new String[] { 
						"tvIndex","tvID",
						"tvAreaName","tvHouseName", "tvHouseNo", 
						"tvHouseAddress", 
						"tvPreAssetCode", "tvDeregssNumber",
						"tvAssetCode" ,"tvCloseSineCode1","tvCloseSineCode2",
						"tvRemark","tvMakeUser","tvMakeDate"},
				new int[] { 
						R.id.tvIndex,R.id.tvID, 
						R.id.tvAreaName,R.id.tvHouseName, R.id.tvHouseNo,
						 R.id.tvHouseAddress,
						R.id.tvPreAssetCode, R.id.tvDegressNumber,
						R.id.tvAssetCode,R.id.tvCloseSineCode1,R.id.tvCloseSineCode2,
						R.id.tvRemark,R.id.tvMakeUser,R.id.tvMakeDate
				});
		lv.setAdapter(mSimpleAdapter);

	}

	 

	 

}
