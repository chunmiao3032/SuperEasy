package com.phone.supereasy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.phone.supereasy.R;
import com.basic.BaseActivity;
import com.common.Global;
import com.manager.DeviceManager;
import com.webservice.SoapControl;

public class LoginActivity extends BaseActivity {
	Button btlogin;
	Button btexit;
	EditText cmbUserCode;
	EditText txtPassword;
	CheckBox chkRemember;
	TextView TxtVersion;
	Button btCheckVersion;
	private String version = "0.00";
	private ProgressDialog loginDialog;

	boolean set_lvleft = false;// 左下菜单显示隐藏

	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			LoginSystem();

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		cwjHandler = new Handler();
		try {

			btCheckVersion = (Button) findViewById(R.id.btCheckVersion);
			cmbUserCode = (EditText) findViewById(R.id.cmbUserCode);
			txtPassword = (EditText) findViewById(R.id.txtPassword);
			btlogin = (Button) findViewById(R.id.btlogin);
			btexit = (Button) findViewById(R.id.btexit);
			chkRemember = (CheckBox) findViewById(R.id.chkRemember);
			TxtVersion = (TextView) findViewById(R.id.TxtVersion);

			btlogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					getDeviceId(getApplicationContext());
					DoLogin(true);
				}
			});

			btexit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			btCheckVersion.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					checkAppVersion();
				}
			});

			String version = this.getPackageManager().getPackageInfo(
					"com.phone.supereasy", 0).versionName;
			TxtVersion.setText(version);
			Global.Version = version;
			saveGlobalMember(null, null, null, version, null);
			// try
			// {
			// checkAppVersion();
			// }
			// catch(Exception ex)
			// {
			// Toast.makeText(getApplication(), "异常：" + ex.getMessage(),
			// 2000).show();
			// }

		} catch (Exception ex) {
			Toast.makeText(getApplication(), "登录异常:" + ex.getMessage(), 2000)
					.show();
		}

	}

	// 检查程序更新
	private void checkAppVersion() {
		// 获取版本信息
		try {
			String version = this.getPackageManager().getPackageInfo(
					"com.phone.supereasy", 0).versionName;
			Global.Version = version;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appName", "SalesPDA");
			map.put("deviceId", Global.deviceId);
			SoapPrimitive sp = SoapControl.ExecuteWebMethod("GetLastVer", map,
					Global.SendDataTime);

			if (sp != null) {
				String ver_Server = sp.toString().trim();
				// float retValue = Float.parseFloat(sp.toString().trim());
				if (CompareVersion(ver_Server, version) <= 0) {
					Toast.makeText(getApplication(), "当前版本已经是最新，无需升级。", 2000)
							.show();
					return;
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginActivity.this)
							.setTitle("系统提示")
							.setMessage("检测到当前存在新版本，是否升级?")
							.setCancelable(false)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											downFile();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					builder.show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "获取程序版本号完毕！", 2000)
						.show();
			}
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
			Toast.makeText(getApplicationContext(), "获取程序版本异常！", 2000).show();
		}
	}

	void downFile() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("正在下载更新,请稍后...");
		loginDialog.setCanceledOnTouchOutside(false);
		loginDialog.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(Global.updateFileString);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(
								Environment.getExternalStorageDirectory(),
								"SuperEasy.apk");
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {

							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {

							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					update();
					loginDialog.cancel();
				} catch (ClientProtocolException e) {
					loginDialog.cancel();
					e.printStackTrace();

				} catch (IOException e) {
					loginDialog.cancel();
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.toString() + "/SuperEasy.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static int CompareVersion(String ver1, String ver2) {
		String[] strArray = ver1.split("\\.");
		String[] strArray2 = ver2.split("\\.");
		for (int i = 0; i < Math.min(strArray.length, strArray2.length); i++) {
			int num2 = Integer.valueOf(strArray[i]);
			int num3 = Integer.valueOf(strArray2[i]);
			if (num2 > num3) {
				return 1;
			}
			if (num2 < num3) {
				return -1;
			}
		}
		return 0;
	}

	private void DoLogin(boolean isOnline) {
		final String userCode = cmbUserCode.getText().toString();
		final String text = txtPassword.getText().toString();
		boolean isPwdRemember = chkRemember.isChecked();
		if (userCode == null || userCode.length() == 0) {
			this.cmbUserCode.setFocusable(true);
			Toast.makeText(getApplicationContext(), "用户名不能为空。", 2000).show();
		}
		if (text == null || text.length() == 0) {
			this.txtPassword.setFocusable(true);
			Toast.makeText(getApplicationContext(), "密码不能为空。", 2000).show();
		}
		saveLogin(userCode, text);

		loadProgressBar();
		new Thread() {
			public void run() {
				LoginThead(userCode, text);
				cwjHandler.post(mUpdateResults);
			}

		}.start();

	}

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("正在登录请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	private void LoginSystem() {

		if (isLogin) {
			try {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
			} catch (Exception e) {
				e.toString();
			}
		} else {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle("系统提示")
					// 设置对话框标题
					.setMessage("登录失败！" + ErrMsg)
					// 设置显示的内容
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {// 添加确定按钮
								public void onClick(DialogInterface dialog,
										int which) {// 确定按钮的响应事件

								}
							}).show();// 在按键响应事件中显示此对话框
		}

	}

	// 记录用户名密码
	private void saveLogin(String userCode, String text) {
		boolean isPwdRemember = chkRemember.isChecked();
		SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
		if (isPwdRemember) {
			SharedPreferences.Editor edit = remdname.edit();
			edit.putString("name", userCode);
			edit.putString("pass", text);
			edit.putBoolean("check", true);
			edit.commit();
		} else {
			SharedPreferences.Editor edit = remdname.edit();
			edit.putString("name", "");
			edit.putString("pass", "");
			edit.putBoolean("check", false);
			edit.commit();
		}
	}

	/**
	 * 保存登录信息
	 * 
	 * @param GlobalUser
	 * @param GlobalPass
	 * @param GlobalVersion
	 * @param GlobalDeviceID
	 */
	private void saveGlobalMember(String GlobalUser, String GlobalUserName,
			String GlobalPass, String GlobalVersion, String GlobalDeviceID) {
		try {
			SharedPreferences remdname = getSharedPreferences ("Global", Activity.MODE_PRIVATE);
			SharedPreferences.Editor edit = remdname.edit();
			if (GlobalUser != null && GlobalUser.trim().length() > 0) {
				edit.putString("GlobalUser", GlobalUser);
			}
			if (GlobalUserName != null && GlobalUserName.trim().length() > 0) {
				edit.putString("GlobalUserName", GlobalUserName);
			}
			if (GlobalPass != null && GlobalPass.trim().length() > 0) {
				edit.putString("GlobalPass", GlobalPass);
			}
			if (GlobalVersion != null && GlobalVersion.trim().length() > 0) {
				edit.putString("GlobalVersion", GlobalVersion);
			}
			if (GlobalDeviceID != null && GlobalDeviceID.trim().length() > 0) {
				edit.putString("GlobalDeviceID", GlobalDeviceID);
			}
			edit.commit();
		} catch (Exception ex) {
		}
	}

	// 从配置文件中读取用户名密码
	@Override
	protected void onResume() {
		SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
		cmbUserCode.setText(remdname.getString("name", ""));
		txtPassword.setText(remdname.getString("pass", ""));
		boolean cheked = remdname.getBoolean("check", false);
		if (cheked) {
			chkRemember.setChecked(true);
		} else {
			chkRemember.setChecked(false);
		}
		super.onResume();
	}

	boolean isLogin = false;
	String ErrMsg = null;
	String ID = null;
	String IsAdmin = null;

	private void LoginThead(String userCode, String password) {
		try {
			// string userName, string password, string deviceId, string
			// clientVersion, int dbServer
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", userCode);
			map.put("password", password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("Login",
					map);

			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					String ID = so.getPropertyAsString("ID");
					String UserCode = so.getPropertyAsString("UserCode");
					String UserName = so.getPropertyAsString("UserName");
					String Password = so.getPropertyAsString("Password");
					String Remark = so.getPropertyAsString("Remark");

					Global.ID = ID;
					Global.UserCode = UserCode;
					Global.UserName = UserName;
					Global.Password = Password;
					Global.Remark = Remark;

					saveGlobalMember(UserCode, UserName, Password, null, null);

					isLogin = true;
				} else {
					ErrMsg = so.getPropertyAsString("ErrMsg");
					ID = so.getPropertyAsString("ID");
					isLogin = false;
				}
			}
		} catch (Exception ex) {
			String ErrMsg = "异常:" + ex.getMessage();
			isLogin = false;

		}

	}

	public void getDeviceId(Context context) {
		if (Global.deviceId == null || Global.deviceId.equals("")
				|| Global.deviceId.length() == 0) {
			// 读取序号
			final TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Global.deviceId = "" + tm.getDeviceId();
			saveGlobalMember(null, null, null, null, Global.deviceId);
		}
	}

}
