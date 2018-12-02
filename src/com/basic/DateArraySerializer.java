package com.basic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.common.CommonMethord;

public class DateArraySerializer extends Vector<Date> implements KvmSerializable {
    
    String NameSpace = "http://www.ln.crb.com/CrbLN/CRBPdaLNService/"; 

    @Override
    public Object getProperty(int arg0) {
            return this.get(arg0);
    }

    @Override
    public int getPropertyCount() {
            return this.size();
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        arg2.name = "string";
        arg2.type = PropertyInfo.STRING_CLASS;
        arg2.namespace = NameSpace;
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
    		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((Date) arg1)); 
    		Date dt = CommonMethord.StrToDate(sdf);
            this.add(dt);
    }

}
