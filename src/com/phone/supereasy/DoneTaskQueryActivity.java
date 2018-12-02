package com.phone.supereasy;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import com.basic.DoneTaskResult;
import com.basic.TaskDO;
import com.common.Global;
import com.manager.DbManager_Data;
import com.webservice.WebServiceManager;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DoneTaskQueryActivity extends Activity {

	//***********************全局变量***********************
	List<DoneTaskResult> taskList;
	//***************************************************
	
	private final static String TAG="TimeDate";  
    //获取日期格式器对象  
	DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	//获取一个日历对象  
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);  
 
 
	ProgressDialog loginDialog;
	EditText etAreaName, etHouseNumber, 
	        etHouseName, etHouseAddress,
			etPreAssetCode,etDegressNumber,
			etAssetCode,etCloseSineCode1,
			etCloseSineCode2,etRemark,
			etMakeUser;
	TextView etDateFrom,etDateTo;
	Button btQuery, btClear, 
	btScanPreAssetCode,btScanAssetCode,
		btScanCloseSineCode1,btScanCloseSineCode2;

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
				Intent intent = new Intent(DoneTaskQueryActivity.this, DoneTaskQueryResultActivity.class);
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
		 // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_donetaskquery);
		
		cwjHandler_getCnt = new Handler(getApplication().getMainLooper());
		init();
	}

	private void init() {
		etAreaName = (EditText) findViewById(R.id.etAreaName);
		etHouseNumber = (EditText) findViewById(R.id.etHouseNumber);
		etHouseName = (EditText) findViewById(R.id.etHouseName);
		etHouseAddress = (EditText) findViewById(R.id.etHouseAddress);
		etPreAssetCode = (EditText) findViewById(R.id.etPreAssetCode);
		etDegressNumber = (EditText)findViewById(R.id.etDegressNumber);
		etAssetCode = (EditText) findViewById(R.id.etAssetCode);
		etCloseSineCode1 = (EditText) findViewById(R.id.etCloseSineCode1);
		etCloseSineCode2 = (EditText) findViewById(R.id.etCloseSineCode2);
		etRemark = (EditText) findViewById(R.id.etRemark);
		etMakeUser = (EditText) findViewById(R.id.etMakeUser);
		etDateFrom = (TextView) findViewById(R.id.etDateFrom);
		etDateTo = (TextView) findViewById(R.id.etDateTo); 
		
		btQuery = (Button) findViewById(R.id.btQuery);
		btClear = (Button) findViewById(R.id.btClear);		
		btScanPreAssetCode = (Button) findViewById(R.id.btScanPreAssetCode);
		btScanAssetCode = (Button) findViewById(R.id.btScanAssetCode);
		btScanCloseSineCode1 = (Button) findViewById(R.id.btScanCloseSineCode1);
		btScanCloseSineCode2 = (Button) findViewById(R.id.btScanCloseSineCode2);
		
		btQuery.setOnClickListener(btQuery_click);
		btClear.setOnClickListener(bClear_click);
		btScanPreAssetCode.setOnClickListener(btScanPreAssetCode_click);
		btScanAssetCode.setOnClickListener(btScanAssetCode_click);
		btScanCloseSineCode1.setOnClickListener(btScanCloseSineCode1_click);
		btScanCloseSineCode2.setOnClickListener(btScanCloseSineCode2_click);
		
		etMakeUser.setText(Global.UserName);
		
		//设置日期
		etDateFrom.setClickable(true); 
		etDateTo.setClickable(true);  
		etDateFrom.setOnClickListener(new OnClickListener(){    
             @Override    
             public void onClick(View v){        
                 DatePickerDialog  dateDlg = new DatePickerDialog(DoneTaskQueryActivity.this,  
                         dFrom,  
                         dateAndTime.get(Calendar.YEAR),  
                         dateAndTime.get(Calendar.MONTH),  
                         dateAndTime.get(Calendar.DAY_OF_MONTH));  
                
                 dateDlg.show();    
          }  
        });
		etDateTo.setOnClickListener(new OnClickListener(){    
            @Override    
            public void onClick(View v){        
                DatePickerDialog  dateDlg = new DatePickerDialog(DoneTaskQueryActivity.this,  
                        dTo,  
                        dateAndTime.get(Calendar.YEAR),  
                        dateAndTime.get(Calendar.MONTH),  
                        dateAndTime.get(Calendar.DAY_OF_MONTH));  
               
                dateDlg.show();    
         }  
       });
		upDateFrom();    
		upDateTo();    
		 
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
			etPreAssetCode.setText("");
			etDegressNumber.setText("");
			etAssetCode.setText("");
			etCloseSineCode1.setText("");
			etCloseSineCode2.setText("");
			etRemark.setText("");
			etMakeUser.setText("");
		}
	
	};

	private OnClickListener btScanPreAssetCode_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(DoneTaskQueryActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 1);

		}
	};
	
	private OnClickListener btScanAssetCode_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(DoneTaskQueryActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 2);

		}
	};
	
	
	private OnClickListener btScanCloseSineCode1_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(DoneTaskQueryActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 3);

		}
	};
	
	private OnClickListener btScanCloseSineCode2_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent openCameraIntent = new Intent(DoneTaskQueryActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 4);

		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
 
		if (requestCode == 1 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etPreAssetCode.setText(scanResult);
		}
		else if (requestCode == 2 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etAssetCode.setText(scanResult);
		}
		else if (requestCode == 3 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etCloseSineCode1.setText(scanResult);
		}
		else if (requestCode == 4 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			etCloseSineCode2.setText(scanResult);
		}
		
	}
	
	
	private void getData() {
		String AreaName = etAreaName.getText().toString();
		String HouseNumber = etHouseNumber.getText().toString();
		String HouseName = etHouseName.getText().toString();
		String HouseAddress = etHouseAddress.getText().toString();
		String oldAssetCode = etPreAssetCode.getText().toString();
		String degreeNumber = etDegressNumber.getText().toString();
		String newAssetCode = etAssetCode.getText().toString();
		String closeSineCode1 = etCloseSineCode1.getText().toString();
		String closeSineCode2 = etCloseSineCode2.getText().toString();
		String remark = etRemark.getText().toString();
		String makeUserCode = etMakeUser.getText().toString();
		String dateFrom = etDateFrom.getText().toString();
		String dateTo = etDateFrom.getText().toString();

		db = new DbManager_Data(getApplication());
		
		taskList = WebServiceManager.GetAllDoneTasksHash(
				AreaName, HouseNumber, HouseName, 
				HouseAddress, oldAssetCode, newAssetCode, 
				closeSineCode1, closeSineCode2, degreeNumber, 
				remark, makeUserCode,dateFrom,dateTo);
		
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
	
	//当点击DatePickerDialog控件的设置按钮时，调用该方法  
    DatePickerDialog.OnDateSetListener dFrom = new DatePickerDialog.OnDateSetListener()  
    {  
        @Override  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            //修改日历控件的年，月，日  
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致  
            dateAndTime.set(Calendar.YEAR, year);  
            dateAndTime.set(Calendar.MONTH, monthOfYear);  
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);      
            //将页面TextView的显示更新为最新时间  
            upDateFrom();     
              
        }          
    };  
    //当点击DatePickerDialog控件的设置按钮时，调用该方法  
    DatePickerDialog.OnDateSetListener dTo = new DatePickerDialog.OnDateSetListener()  
    {  
        @Override  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            //修改日历控件的年，月，日  
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致  
            dateAndTime.set(Calendar.YEAR, year);  
            dateAndTime.set(Calendar.MONTH, monthOfYear);  
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);      
            //将页面TextView的显示更新为最新时间  
            upDateTo();     
              
        }          
    };  
      
    private void upDateFrom() {  
        etDateFrom.setText(fmtDate.format(dateAndTime.getTime()));  
        }  
    
    private void upDateTo() {  
        etDateTo.setText(fmtDate.format(dateAndTime.getTime()));  
        }  

}
