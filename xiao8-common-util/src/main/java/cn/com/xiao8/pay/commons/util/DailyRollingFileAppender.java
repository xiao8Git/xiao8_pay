package cn.com.xiao8.pay.commons.util;

import org.apache.log4j.Priority;

public class DailyRollingFileAppender extends org.apache.log4j.DailyRollingFileAppender {
	   
	 @Override  
	 public boolean isAsSevereAsThreshold(Priority priority) {  
	  //只判断是否相等，而不判断优先级  
	  return this.getThreshold().equals(priority);  
	 }  
	}  