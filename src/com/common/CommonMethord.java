package com.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//import com.jcraft.jzlib.JZlib;
//import com.jcraft.jzlib.ZInputStream;
//import com.jcraft.jzlib.ZOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.util.Base64;

public class CommonMethord {
		final static int BUFFER_SIZE = 4096; 
	 /**  
	  	
	   * 将String转换成InputStream  
     * @param in  
     * @return  
     * @throws Exception  
     */  
	 	public static InputStream StringTOInputStream(String in) throws Exception{  
         
	        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));  
	        return is;  
	    }  
	      
	    /**  
	     * 将InputStream转换成byte数组  
	     * @param in InputStream  
	     * @return byte[]  
	     * @throws IOException  
	     */  
	    public static byte[] InputStreamTOByte(InputStream in)
	    {  
	          try
	          {
			        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
			        byte[] data = new byte[BUFFER_SIZE];  
			        int count = -1;  
			        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
			            outStream.write(data, 0, count);  
			          
			        data = null;  
			        return outStream.toByteArray();  
	          }
	          catch(Exception ex)
	          {
	        	   return null;
	          }
	    }  

	    public static InputStream ByteToInputStream(byte[] buf)
	    {
	    	InputStream sbs = null;
	    	try
	    	{
	    		sbs = new ByteArrayInputStream(buf); 
	    	}
	    	catch(Exception ex)
	    	{
	    		return sbs;
	    	}
	    	return sbs;
	    }
	    
	    public static Bitmap drawableToBitamp(Drawable drawable)
	    { 
	        BitmapDrawable bd = (BitmapDrawable) drawable;
	        return bd.getBitmap();
	    }
		
	    public static Bitmap Bytes2Bimap_pre(byte[] b){
	        if(b.length!=0){
	                return BitmapFactory.decodeByteArray(b, 0, b.length);
	        }
	        else {
	                return null;
	        }
	    }
	    
		public static Bitmap Bytes2Bimap(byte[] b){
		   Bitmap btp = null;
	       if(b.length!=0)
	       {
	    	   
//	    	   byte[] ns = new byte[b.length];
//	    	   for (int i = 0; i < ns.length; i++) {
//	    	       ns[i] = (byte) (b[i] & 0xff);
//	    	   }
	    	   
//	    	   byte[] bt =new byte[b.length];
//	    	   for(int i = 0;i<b.length;i++)
//	    	   { 
//	    		   byte bbt = (byte) (b[i] - 128);
//	    		   bt[i] = bbt;
//	    	   }
	    	   
//	    	   InputStream is = ByteToInputStream(b);
//	    	   BitmapFactory.Options options=new BitmapFactory.Options();  
//	    	   options.inJustDecodeBounds = false;  
//	    	   options.inSampleSize = 10;     
//	    	   btp =BitmapFactory.decodeStream(is,null,options);  
	    	    
               BitmapFactory.Options opt = new BitmapFactory.Options();  
	    	   opt.inDither=false;    /*不进行图片抖动处理*/
	    	   opt.inPreferredConfig=null;  /*设置让解码器以最佳方式解码*/ 
	    	   /* 下面两个字段需要组合使用 */ 
	    	   opt.inPurgeable = true; 
	    	   opt.inInputShareable = true; 
	    	   opt.inSampleSize = 4;     
	    	   btp = BitmapFactory.decodeByteArray(b, 0, b.length);
	    	   
 
//	    	   InputStream is = ByteToInputStream(b);
//	    	   BitmapFactory.Options opt = new BitmapFactory.Options();    
//	    	   opt.inPreferredConfig = Bitmap.Config.RGB_565;     
//	    	   opt.inPurgeable = true;    
//	    	   opt.inInputShareable = true; 
//	    	   opt.inSampleSize = 8;   
//	    	   btp = BitmapFactory.decodeStream(is,null,opt); 
	    	    
//	    	BitmapFactory.Options opts = new BitmapFactory.Options();    
//	    	opts.inSampleSize = 4;  
//	    	byte[] bitmapArray = Base64.decode(b, Base64.DEFAULT);
//	    	btp = BitmapFactory.decodeByteArray(bitmapArray, 0, b.length,opts); 
 	    	
	    	 
	    	// 回收  
	    	//btp.recycle();  
	    	    
	       }
	       return btp;
	   }
		 
		
		   
		public static byte[] Bitmaptobyte(Bitmap bm) 
	   {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
		    return baos.toByteArray();   
	   }
	   
		public static Drawable BitmapToDrawable(Bitmap bmp)
	   {
		   Drawable drawable =new BitmapDrawable(bmp);
		   return drawable;
	   }

		static ByteArrayInputStream bais;
		 // 将byte[]转换成InputStream
		 public static InputStream Byte2InputStream(byte[] b) 
		 {
			  bais = new ByteArrayInputStream(b); 
			  return bais;
		 }

		 // 将InputStream转换成byte[]
		 public static  byte[] InputStream2Bytes(InputStream is) 
		 {
			  String str = "";
			  byte []readByte = new byte[1024];
			  int readCount = -1;
			  try {
			   while ((readCount = is.read(readByte,0,1024)) != -1) {
			    str += new String(readByte).trim();
			   }
			   return str.getBytes();
			  } catch (Exception e) {
			   e.printStackTrace();
			  }
			  return null;
		 }

		 // 将Bitmap转换成InputStream
		 public static  InputStream Bitmap2InputStream(Bitmap bm) 
		 {
			  baos = new ByteArrayOutputStream();
			  bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);
			  InputStream is = new ByteArrayInputStream(baos.toByteArray());
			  return is;
		 }

		 // 将Bitmap转换成InputStream
		 public static  InputStream Bitmap2InputStream(Bitmap bm, int quality) 
		 {
			  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			  bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
			  InputStream is = new ByteArrayInputStream(baos.toByteArray());
			  return is;
		 }
		 // 将InputStream转换成Bitmap
		 public static  Bitmap InputStream2Bitmap(InputStream is) 
		 {
//			 BitmapFactory.Options opts = new BitmapFactory.Options();   
//			 opts.inSampleSize = 2;  
			 return BitmapFactory.decodeStream(is);
		 }

		 // Drawable转换成InputStream
		 public  static InputStream Drawable2InputStream(Drawable d) 
		 {
			  Bitmap bitmap = drawable2Bitmap(d);
			  return Bitmap2InputStream(bitmap);
		 }

		 // InputStream转换成Drawable
		 public static  Drawable InputStream2Drawable(InputStream is) 
		 {
			  Bitmap bitmap = InputStream2Bitmap(is);
			  return bitmap2Drawable(bitmap);
		 }

		 // Drawable转换成byte[]
		 public  static byte[] Drawable2Bytes(Drawable d) 
		 {
			  Bitmap bitmap = drawable2Bitmap(d);
			  return Bitmap2Bytes(bitmap);
		 }

		 // byte[]转换成Drawable
		 public static  Drawable Bytes2Drawable(byte[] b) 
		 {
			  Bitmap bitmap = Bytes2Bitmap(b);
			  return bitmap2Drawable(bitmap);
		 }

		 static ByteArrayOutputStream baos;
		 // Bitmap转换成byte[]
		 public  static byte[] Bitmap2Bytes(Bitmap bm) 
		 { 
			  baos = new ByteArrayOutputStream(); 
			  bm.compress(Bitmap.CompressFormat.PNG, 30, baos);
			  return baos.toByteArray();
		 }

		 // byte[]转换成Bitmap
		 public  static Bitmap Bytes2Bitmap(byte[] b) 
		 {
			  if (b.length != 0) {
			   return BitmapFactory.decodeByteArray(b, 0, b.length);
			  }
			  return null;
		 }

		 // Drawable转换成Bitmap
		 public static Bitmap drawable2Bitmap(Drawable drawable) 
		 {
			  Bitmap bitmap = Bitmap
			    .createBitmap(
			      drawable.getIntrinsicWidth(),
			      drawable.getIntrinsicHeight(),
			      drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
			        : Bitmap.Config.RGB_565);
			  Canvas canvas = new Canvas(bitmap);
			  drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
			    drawable.getIntrinsicHeight());
			  drawable.draw(canvas);
			  return bitmap;
		 }

		 // Bitmap转换成Drawable
		 public static  Drawable bitmap2Drawable(Bitmap bitmap) 
		 {
			  BitmapDrawable bd = new BitmapDrawable(bitmap);
			  Drawable d = (Drawable) bd;
			  return d;
		 }

		 /**
		  * 压缩数据
		  * 
		  * @param object
		  * @return
		  * @throws IOException
		  */
//		 public static byte[] jzlib(byte[] object) {
//		  byte[] data = null;
//		  try {
//		   ByteArrayOutputStream out = new ByteArrayOutputStream();
//		   ZOutputStream zOut = new ZOutputStream(out,
//		     JZlib.Z_DEFAULT_COMPRESSION);
//		   DataOutputStream objOut = new DataOutputStream(zOut);
//		   objOut.write(object);
//		   objOut.flush();
//		   zOut.close();
//		   data = out.toByteArray();
//		   out.close();
//		  } catch (IOException e) {
//		   e.printStackTrace();
//		  }
//		  return data;
//		 }
		 /**
		  * 解压被压缩的数据
		  * 
		  * @param object
		  * @return
		  * @throws IOException
		  */
//		 public static byte[] unjzlib(byte[] object) {
//		  byte[] data = null;
//		  try {
//		   ByteArrayInputStream in = new ByteArrayInputStream(object);
//		   ZInputStream zIn = new ZInputStream(in);
//		   byte[] buf = new byte[1024];
//		   int num = -1;
//		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		   while ((num = zIn.read(buf, 0, buf.length)) != -1) {
//		    baos.write(buf, 0, num);
//		   }
//		   data = baos.toByteArray();
//		   baos.flush();
//		   baos.close();
//		   zIn.close();
//		   in.close();
//		   
//		  } catch (IOException e) {
//		   e.printStackTrace();
//		  }
//		  return data;
//		 }

			//计算图片的缩放值
		 public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		     final int height = options.outHeight;
		     final int width = options.outWidth;
		     int inSampleSize = 1;

		     if (height > reqHeight || width > reqWidth) {
		              final int heightRatio = Math.round((float) height/ (float) reqHeight);
		              final int widthRatio = Math.round((float) width / (float) reqWidth);
		              inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		     }
		         return inSampleSize;
		 }
		 
		 static BitmapFactory.Options options;
		 // 根据路径获得图片并压缩，返回bitmap用于显示
		 public static Bitmap getSmallBitmap(byte[] btPhoto) { 
	         options = new BitmapFactory.Options();
	         options.inJustDecodeBounds = true;
		     options.inSampleSize = calculateInSampleSize(options, 300, 200);
		     options.inJustDecodeBounds = false; 
		     
		     //byte[] btPhoto = Bitmap2Bytes(photo);
		     return BitmapFactory.decodeByteArray(btPhoto, 0, btPhoto.length,options); 
		 }

		 /** 
		  * base64转为bitmap 
		  * @param base64Data 
		  * @return 
		  */  
		 public static Bitmap base64ToBitmap(String base64Data) {  
		     byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
		     return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);  
		 } 

		 /**
		  * base64ToByte
		  * @param base64Data
		  * @return
		  */
		 public static byte[] base64ToByte(String base64Data) {  
		     byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);  
		     return bytes;  
		 } 

		 public static boolean isValidDate(String str) {
		      boolean convertSuccess=true; 
		       SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		       try { 
		          format.setLenient(false);
		          try {
					format.parse(str);
				} catch (java.text.ParseException e) { 
					e.printStackTrace();
					convertSuccess=false;
				}
		       } 
		       catch (ParseException e) {
		           // e.printStackTrace();
		    	   // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
		           convertSuccess=false;
		       } 
		       return convertSuccess;
		}
		 
		 /**
		 * 日期转换成字符串
		 * @param date 
		 * @return str
		 */
		 public static String DateToStr(Date date) {
		   
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String str = format.format(date);
		    return str;
		 } 

		 /**
		 * 字符串转换成日期
		 * @param str
		 * @return date
		 */
		 public static Date StrToDate(String str) {
		   
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date date = null;
		    try {
		    		try {
						date = format.parse(str); 
					} catch (java.text.ParseException e) { 
						e.printStackTrace();
					}
		    	} 
		    	catch (ParseException e) 
			    {
			    	e.printStackTrace();
			    }  
		    return date;
		 }
		 
		 /**
		 * 字符串转换成日期
		 * @param str
		 * @return date
		 */
		 public static Date StrToDateShort(String str) {
		   
		    SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		    Date date = null;
		    try {
		    		try {
						date = format.parse(str); 
					} catch (java.text.ParseException e) { 
						e.printStackTrace();
					}
		    	} 
		    	catch (ParseException e) 
			    {
			    	e.printStackTrace();
			    }  
		    return date;
		 }
		 
}
