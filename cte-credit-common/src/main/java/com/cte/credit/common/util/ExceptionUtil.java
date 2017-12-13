package com.cte.credit.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import com.alibaba.fastjson.JSONException;

public class ExceptionUtil {
	/**
	 * 判断ex异常是否是超时异常：SocketTimeoutException
	 * @param ex
	 * @return
	 */
   public static boolean isTimeoutException(Exception ex){  
	   if(ex == null ) return false;
	   String exeMsg = ex.getMessage();
	   if(exeMsg != null && exeMsg.
			   toLowerCase().indexOf("sockettimeout") > -1){
		   return true;
	   }
	   exeMsg = ex.toString(); 
	   if(exeMsg != null && exeMsg.toLowerCase().
			   indexOf("sockettimeout") > -1){
		   return true;
	   }
	   if(exeMsg.toLowerCase().indexOf("send message") > -1){
		   return true;
	   }
	   if(ex instanceof  JSONException){
		   return true;
	   }
	   return false;
   }
   /**
    * 获取异常时堆栈信息
    * @param Exception ex
    * @return String
    * */
   public static String getTrace(Exception ex) {
       StringWriter stringWriter= new StringWriter();
       PrintWriter writer= new PrintWriter(stringWriter);
       ex.printStackTrace(writer);
       StringBuffer buffer= stringWriter.getBuffer();
       return buffer.toString();
   }
}
