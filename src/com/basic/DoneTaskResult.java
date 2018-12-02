package com.basic;

import java.io.Serializable;

public class DoneTaskResult  implements Serializable{
	public String ID;
    public String AREA_NAME;//台区	
    public String HOUSE_NUMBER;//户号	
    public String HOUSE_NAME;//户名	
    public String HOUSE_ADDRESS;//地址	
    public String PRE_ASSET_CODE;//旧资产编号
   
	public String DEGRESS_NUMBER;//原电度数 
	public String ASSET_CODE;//新资产编号
	public String CLOSE_SINE_CODE1;//封印号1	
	public String CLOSE_SINE_CODE2;//封印号2	
	public String REMARK;//备注
    public String MAKE_USER;//创建人
    public String MAKE_DATE;//创建时间
} 