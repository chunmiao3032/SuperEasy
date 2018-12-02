package com.webservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.util.Log;
 




import com.basic.DoneTaskResult;
import com.basic.TaskDO;
import com.common.CommonMethord;
import com.common.Global;

import android.content.SharedPreferences;

public class WebServiceManager {
	  
	//获取所有固定资产
	public static List<TaskDO> GetAllTasks()
	 {
		 List<TaskDO> list = new ArrayList<TaskDO>();
		 
		 try
			{
				Map<String, Object> map = new HashMap<String, Object>();   
				map.put("userName", Global.UserCode);
				map.put("password", Global.Password);
				map.put("deviceId",Global.deviceId);
				map.put("clientVersion", Global.clientVersion); 
		
				SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getAllTask", map);		
				if(so != null)
				{
					String Success = so.getPropertyAsString("Success");
					if(Success.equals("true"))
					{
						SoapObject soap_AssetResults = (SoapObject) so.getProperty("TaskResults");				
						int ic1 = soap_AssetResults.getPropertyCount(); 
						for(int i =0;i<ic1;i++)
						{  
							SoapObject soap_Asset = (SoapObject) soap_AssetResults.getProperty(i);		
							String ID = soap_Asset.getProperty("ID").toString(); 
							
							
							TaskDO ado = new TaskDO();
							ado.ID = ID; 
									
							list.add(ado);
						}
						
					}
				}
			}
		 catch(Exception ex)
		 {
			 return list;
		 }
		return list;
	 }
	
	//获取所有固定资产
    public static Hashtable GetAllTasksHash()
		 {
			 //List<AssetDO> list = new ArrayList<AssetDO>();
			 Hashtable<String, TaskDO> list = new Hashtable<String, TaskDO>();
			 try
				{
					Map<String, Object> map = new HashMap<String, Object>();   
					map.put("userName", Global.UserCode);
					map.put("password", Global.Password);
					map.put("deviceId",Global.deviceId);
					map.put("clientVersion", Global.clientVersion); 
			
					SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getAllTask", map);		
					if(so != null)
					{
						String Success = so.getPropertyAsString("Success");
						if(Success.equals("true"))
						{
							SoapObject soap_AssetResults = (SoapObject) so.getProperty("TaskResults");				
							int ic1 = soap_AssetResults.getPropertyCount(); 
							for(int i =0;i<ic1;i++)
							{  
								SoapObject soap_Asset = (SoapObject) soap_AssetResults.getProperty(i);		
								String ID = soap_Asset.getProperty("ID").toString();
								String AREA_NAME = soap_Asset.getProperty("AREA_NAME").toString(); 
								String HOUSE_NUMBER = soap_Asset.getProperty("HOUSE_NUMBER").toString(); 
								String HOUSE_NAME = soap_Asset.getProperty("HOUSE_NAME").toString(); 
								String HOUSE_ADDRESS = soap_Asset.getProperty("HOUSE_ADDRESS").toString(); 
								String ASSET_CODE = soap_Asset.getProperty("ASSET_CODE").toString(); 
								String IS_ACTIVED = soap_Asset.getProperty("IS_ACTIVED").toString(); 
								String MAKE_DATE = soap_Asset.getProperty("MAKE_DATE").toString();
								String MAKE_USER = soap_Asset.getProperty("MAKE_USER").toString(); 
								String REMARK = soap_Asset.getProperty("REMARK").toString(); 
								
								if(ASSET_CODE == null || ASSET_CODE.length() == 0 || ASSET_CODE.contains("anyType"))
								{
									ASSET_CODE="0";
								}
								
								TaskDO ado = new TaskDO();
								ado.ID = ID;
							    ado.AREA_NAME = AREA_NAME; 
							    ado.HOUSE_NUMBER = HOUSE_NUMBER; 
							    ado.HOUSE_NAME = HOUSE_NAME; 
							    ado.HOUSE_ADDRESS = HOUSE_ADDRESS; 
							    ado.ASSET_CODE = ASSET_CODE; 
							    ado.IS_ACTIVED = IS_ACTIVED; 
							    ado.MAKE_DATE = MAKE_DATE;
							    ado.MAKE_USER  = MAKE_USER; 
							    ado.REMARK  = REMARK; 
										
								list.put(ado.ASSET_CODE,ado);
							}
							
						}
					}
				}
			 catch(Exception ex)
			 {
				 return list;
			 }
			return list;
		 }
  
    
 
	//提交盘点结果
	public static String SubmitDoTask(String TaskID,String oldAssetCode,String newAssetCode, 
			String closeSineCode1, String closeSineCode2, String degreeNumber, String remark,
			String GlobalUser, String GlobalUserName,
			String GlobalPass, String GlobalVersion, String GlobalDeviceID)
    {   
		String Success = null;
        Map<String, Object> map = new HashMap<String, Object>(); 
        map.put("TaskID", TaskID);
        map.put("oldAssetCode", oldAssetCode);
        map.put("newAssetCode", newAssetCode);
        map.put("closeSineCode1", closeSineCode1);
        map.put("closeSineCode2", closeSineCode2);
        map.put("degreeNumber", degreeNumber);
        map.put("remark", remark); 
        if(Global.UserCode.trim().length() > 0)
        {
        	map.put("userName", Global.UserCode); 
        }
        else
        {
        	map.put("userName", GlobalUser); 
        }
        
        if(Global.Password.trim().length() > 0)
        {
        	map.put("password", Global.Password);
        }
        else
        {
        	map.put("password", GlobalPass);
        }
        
        if(Global.deviceId.trim().length() > 0)
        {
        	map.put("deviceId",Global.deviceId);
        }
        else
        {
        	map.put("deviceId",GlobalDeviceID);
        }
        
        if(Global.clientVersion.trim().length() > 0)
        {
        	map.put("clientVersion", Global.clientVersion); 
        }
        else
        {
        	map.put("clientVersion", GlobalVersion); 
        }
		
		SoapPrimitive so = SoapControl.ExecuteWebMethod("Submit", map , Global.SendDataTime);		
		if(so != null)
		{ 
			Success = so.toString(); 
		}
         
        return Success;
    }
 
	//获取日期
	public static String GetServerDate()
	{ 
		Time t=new Time(); 
		t.setToNow(); // 取得系统时间。  
		String year = String.valueOf(t.year).substring(0,2); 
		return year;
	}
	
	//获取所有已完成任务
    public static List<DoneTaskResult> GetAllDoneTasksHash(
    		 String AreaName, String HouseNumber,
 			 String HouseName, String HouseAddress,
             String oldAssetCode, String newAssetCode, 
             String closeSineCode1, String closeSineCode2, 
             String degreeNumber, String remark,
             String makeUserCode,String dateFrom,String dateTo
    		)
		 { 
    	 	 List<DoneTaskResult> list = new ArrayList<DoneTaskResult>();
			 try
				{
					Map<String, Object> map = new HashMap<String, Object>();   
					map.put("AreaName", AreaName);
					map.put("HouseNumber", HouseNumber);
					map.put("HouseName", HouseName);
					map.put("HouseAddress", HouseAddress);
					map.put("oldAssetCode", oldAssetCode);
					map.put("newAssetCode", newAssetCode);
					map.put("closeSineCode1", closeSineCode1);
					map.put("closeSineCode2", closeSineCode2);
					map.put("degreeNumber", degreeNumber);
					map.put("remark", remark);
					map.put("makeUserCode", makeUserCode); 
					
					map.put("userName", Global.UserCode);
					map.put("password", Global.Password);
					map.put("deviceId",Global.deviceId);
					map.put("clientVersion", Global.clientVersion); 
					
					map.put("dateFrom", dateFrom);
					map.put("dateTo", dateTo);
			
					SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getAllDoneTask", map);		
					if(so != null)
					{
						String Success = so.getPropertyAsString("Success");
						if(Success.equals("true"))
						{
							SoapObject soap_AssetResults = (SoapObject) so.getProperty("DoneTaskResults");				
							int ic1 = soap_AssetResults.getPropertyCount(); 
							for(int i =0;i<ic1;i++)
							{  
								SoapObject soap_Asset = (SoapObject) soap_AssetResults.getProperty(i);		
								String ID = soap_Asset.getProperty("ID").toString();
								String AREA_NAME = soap_Asset.getProperty("AREA_NAME").toString(); 
								String HOUSE_NUMBER = soap_Asset.getProperty("HOUSE_NUMBER").toString(); 
								String HOUSE_NAME = soap_Asset.getProperty("HOUSE_NAME").toString(); 
								String HOUSE_ADDRESS = soap_Asset.getProperty("HOUSE_ADDRESS").toString(); 
								String PRE_ASSET_CODE = soap_Asset.getProperty("PRE_ASSET_CODE").toString(); 
								String DEGRESS_NUMBER = soap_Asset.getProperty("DEGRESS_NUMBER").toString();
								String ASSET_CODE = soap_Asset.getProperty("ASSET_CODE").toString();
								String CLOSE_SINE_CODE1 = soap_Asset.getProperty("CLOSE_SINE_CODE1").toString();
								String CLOSE_SINE_CODE2 = soap_Asset.getProperty("CLOSE_SINE_CODE2").toString();
								
								String MAKE_DATE = soap_Asset.getProperty("MAKE_DATE").toString();
								String MAKE_USER = soap_Asset.getProperty("MAKE_USER").toString(); 
								String REMARK = soap_Asset.getProperty("REMARK").toString(); 
								
								if(PRE_ASSET_CODE == null || PRE_ASSET_CODE.length() == 0 || PRE_ASSET_CODE.contains("anyType"))
								{
									ASSET_CODE="0";
								}
								if(REMARK == null || REMARK.length() == 0 || REMARK.contains("anyType"))
								{
									REMARK="";
								}
								if(DEGRESS_NUMBER == null || DEGRESS_NUMBER.length() == 0 || DEGRESS_NUMBER.contains("anyType"))
								{
									DEGRESS_NUMBER="0";
								}
								if(CLOSE_SINE_CODE1 == null || CLOSE_SINE_CODE1.length() == 0 || CLOSE_SINE_CODE1.contains("anyType"))
								{
									CLOSE_SINE_CODE1="";
								}
								if(CLOSE_SINE_CODE2 == null || CLOSE_SINE_CODE2.length() == 0 || CLOSE_SINE_CODE2.contains("anyType"))
								{
									CLOSE_SINE_CODE2="";
								}
								if(MAKE_USER == null || MAKE_USER.length() == 0 || MAKE_USER.contains("anyType"))
								{
									MAKE_USER="";
								}
								if(MAKE_DATE == null || MAKE_DATE.length() == 0 || MAKE_DATE.contains("anyType"))
								{
									MAKE_DATE="";
								}
								
								DoneTaskResult ado = new DoneTaskResult();
								ado.ID = ID;
							    ado.AREA_NAME = AREA_NAME; 
							    ado.HOUSE_NUMBER = HOUSE_NUMBER; 
							    ado.HOUSE_NAME = HOUSE_NAME; 
							    ado.HOUSE_ADDRESS = HOUSE_ADDRESS; 
							    ado.PRE_ASSET_CODE = PRE_ASSET_CODE;
							    ado.DEGRESS_NUMBER = DEGRESS_NUMBER;
							    ado.ASSET_CODE = ASSET_CODE; 
							    ado.CLOSE_SINE_CODE1 = CLOSE_SINE_CODE1; 
							    ado.CLOSE_SINE_CODE2 = CLOSE_SINE_CODE2; 
							  
							    ado.MAKE_DATE = MAKE_DATE;
							    ado.MAKE_USER  = MAKE_USER; 
							    ado.REMARK  = REMARK; 
										
								list.add(ado);
							}
							
						}
					}
				}
			 catch(Exception ex)
			 {
				 return list;
			 }
			return list;
		 }
  
	
	
	
	 
	 
}
