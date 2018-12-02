package com.basic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
 
import com.manager.DbManager_Data;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class BaseActivity extends Activity {
	  
	public Button bttopmenu;//”“…œ≤Àµ•
	 
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	public List<EditText> et=new ArrayList<EditText>();
	private boolean softKeyboard=false; 
	public DbManager_Data db_data = null;
	
	//public DBManager dbManager;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
     
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		 
		allActivity.add(this); 
		init();
		//softKeyBord();
	}

	protected void init() {  
		db_data =new DbManager_Data(this);
	}
	
	public static Activity getActivityByName(String name) {
		for (Activity ac : allActivity) {
			if (ac.getClass().getName().indexOf(name) > 0) {
				return ac;
			} else {
				return null;
			}
		}
		return null;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//Util.closeDataBase(dbManager);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart(); 
			//dbManager=new DBManager(this); 
	}
	
	public void softKeyBord() {
		if (softKeyboard==false) {
			int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			Method setShowSoftInputOnFocus = null;
			for (int i = 0; i < et.size(); i++) {
				try {
					if (sdk >= 14 && sdk < 16) {
						setShowSoftInputOnFocus = et
								.get(i)
								.getClass()
								.getMethod("setSoftInputShownOnFocus",
										boolean.class);
					} else if (sdk < 14) {
						et.get(i).setInputType(InputType.TYPE_NULL);
					} else {
						setShowSoftInputOnFocus = et
								.get(i)
								.getClass()
								.getMethod("setShowSoftInputOnFocus",
										boolean.class);
					}
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (setShowSoftInputOnFocus != null) {
				setShowSoftInputOnFocus.setAccessible(true);
			}
			for (int i = 0; i < et.size(); i++) {
				try {
					setShowSoftInputOnFocus.invoke(et.get(i), false);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	
	
}
