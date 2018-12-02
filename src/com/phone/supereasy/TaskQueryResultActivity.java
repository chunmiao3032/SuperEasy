package com.phone.supereasy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class TaskQueryResultActivity extends Activity {

	// ***********************ȫ�ֱ���***********************
	List<TaskDO> taskList;
	SimpleAdapter mSimpleAdapter;
	DbManager_Data db;
	// ***************************************************
	ListView lv;
	TextView tvCnt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskqueryresult);
		lv = (ListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(lv_Item_Click);
		tvCnt = (TextView)findViewById(R.id.tvCnt);

		taskList = (List<TaskDO>) getIntent().getSerializableExtra("list");
		initialListView(taskList);
		
		tvCnt.setText(taskList.size() + "��");
	}

//	@Override
//	protected void onResume() {
//		//super.onResume();
//
//	}

	private void initialListView(List<TaskDO> list) {
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		int i = 1;
		for (TaskDO task : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("tvIndex", i);
			map.put("tvID", task.ID);
			map.put("tvAreaName", task.AREA_NAME);
			map.put("tvHouseName", task.HOUSE_NAME);
			map.put("tvHouseNo", task.HOUSE_NUMBER);
			map.put("tvHouseAddress", task.HOUSE_ADDRESS);
			map.put("tvAssetCode", task.ASSET_CODE);
			map.put("tvDoStatus", task.DO_STATUS);
			listItem.add(map);
			i++;
		}
		mSimpleAdapter = new SimpleAdapter(
				this,
				listItem,// ��Ҫ�󶨵�����
				R.layout.activity_taskqueryresult_item, 
				new String[] { 
						"tvIndex",
						"tvID",
						"tvAreaName","tvHouseName", "tvHouseNo", 
						"tvHouseAddress", 
						"tvAssetCode", "tvDoStatus" },
				new int[] { 
						R.id.tvIndex,
						R.id.tvID, 
						R.id.tvAreaName,R.id.tvHouseName, R.id.tvHouseNo,
						 R.id.tvHouseAddress,
						R.id.tvAssetCode, R.id.tvDoStatus });
		lv.setAdapter(mSimpleAdapter);

	}

	OnItemClickListener lv_Item_Click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {

			HashMap<String, String> map = (HashMap<String, String>) lv
					.getItemAtPosition(arg2);
			final String tvID = map.get("tvID");
			final String tvAreaName = map.get("tvAreaName");
			final String tvHouseNumber = map.get("tvHouseNo");
			final String tvHouseName = map.get("tvHouseName");
			final String tvHouseAddress = map.get("tvHouseAddress");
			final String tvAssetCode = map.get("tvAssetCode");
			String tvDoStatus = map.get("tvDoStatus");

			// δ�������
			if (tvDoStatus == null || tvDoStatus.trim().length() == 0) {
				DoWork(tvID, tvAreaName, tvHouseNumber, tvHouseName,
						tvHouseAddress, tvAssetCode, arg2, 0);
			} else// ���������
			{
				String[] menu;
				if (tvAssetCode == null || tvAssetCode.equals("")|| tvAssetCode.trim().equals("0") || tvAssetCode.trim().length() == 0)// ��װ��
				{
					menu = new String[] { "�༭����", "ɾ��¼������", "׷��¼��" };
				} else// ����
				{
					menu = new String[] { "�༭����", "ɾ��¼������" };
				}
				new AlertDialog.Builder(TaskQueryResultActivity.this)
						.setTitle("��ѡ��")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(menu, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:// �༭
											DoWork(tvID, tvAreaName,
													tvHouseNumber, tvHouseName,
													tvHouseAddress,
													tvAssetCode, arg2, 0);
											break;
										case 1:// ɾ��¼������
											db = new DbManager_Data(getApplication());
											db.DeleteDoTask(tvID);
											db.UpdateTaskNull(tvID);
											HashMap<String, String> hp = (HashMap<String, String>) mSimpleAdapter
													.getItem(arg2);
											if (hp.get("tvDoStatus").toString()
													.length() > 0) {
												hp.put("tvDoStatus", "");
												mSimpleAdapter.notifyDataSetChanged();
											}
											break;
										case 2:// ׷��¼��
											DoWork(tvID, tvAreaName,
													tvHouseNumber, tvHouseName,
													tvHouseAddress,
													tvAssetCode, arg2, 1);
											break;

										}

										dialog.dismiss();
									}
								}).setNegativeButton("ȡ��", null).show();

			}

		}
	};

	/**
	 * ��ת���༭����
	 * @param tvID
	 * @param tvAreaName
	 * @param tvHouseNumber
	 * @param tvHouseName
	 * @param tvHouseAddress
	 * @param tvAssetCode
	 * @param Position listview�ĵ�ǰ�к�
	 * @param Add �༭=0,����(��У��doTask��)=1
	 */
	private void DoWork(String tvID, String tvAreaName, String tvHouseNumber,
			String tvHouseName, String tvHouseAddress, String tvAssetCode,
			int Position, int Add) {
		Intent intent = new Intent(TaskQueryResultActivity.this,
				WorkerActivity.class);
		intent.putExtra("tvID", tvID);
		intent.putExtra("tvAreaName", tvAreaName);
		intent.putExtra("tvHouseNumber", tvHouseNumber);
		intent.putExtra("tvHouseName", tvHouseName);
		intent.putExtra("tvHouseAddress", tvHouseAddress);
		intent.putExtra("tvAssetCode", tvAssetCode);
		intent.putExtra("Position", String.valueOf(Position));
		intent.putExtra("Add", String.valueOf(Add));
		startActivityForResult(intent,1);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		try {
			if (requestCode == 1 && resultCode == RESULT_OK) {
				String Task_ID = data.getStringExtra("TASK_ID");
				String strPosition = data.getStringExtra("Position");
				if (Task_ID.trim().length() > 0 && strPosition.length() > 0) {
					HashMap<String, String> hp = (HashMap<String, String>) mSimpleAdapter
							.getItem(Integer.parseInt(strPosition));
					
					String doStatus = hp.get("tvDoStatus");
					if (doStatus == null || doStatus.toString().length() == 0) {
						hp.put("tvDoStatus", "�����");
						mSimpleAdapter.notifyDataSetChanged();
					}
				}
			}
		} catch (Exception ex) {
			Toast.makeText(getApplication(), "�쳣" + ex.getMessage(), 2000)
					.show();
		}
	}

}
