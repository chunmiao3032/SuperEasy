package com.manager;

import com.common.Global;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceManager {
  
    public static void getDeviceId(Context context)
    {
    	if(Global.deviceId == null || Global.deviceId.equals("") || Global.deviceId.length() == 0)
    	{
	    	//∂¡»°–Ú∫≈
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Global.deviceId = "" + tm.getDeviceId(); 
    	} 
    }
	
}
