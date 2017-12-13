package com.cte.credit.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;



import org.codehaus.xfire.XFireException;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.fault.XFireFault;

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
	   //XFireRuntimeException>XFireFault>XFireException>SocketTimeoutException>
	   if(ex instanceof XFireRuntimeException){
		   if(ex.getCause()!=null&&ex.getCause() instanceof XFireFault){
			   Throwable ex_XfireFault=ex.getCause();
			   if(ex_XfireFault.getCause()!=null&&ex_XfireFault.getCause() instanceof XFireException){
				   Throwable ex_SocketTimeout=ex_XfireFault.getCause();
				   if(ex_SocketTimeout.getCause()!=null&&ex_SocketTimeout.getCause() instanceof SocketTimeoutException){
					   return true;
				   }
			   }
		   }
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
