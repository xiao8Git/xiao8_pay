package cn.com.xiao8.pay.alipay.redis.impl;

public class CacheAccessException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
    
    public CacheAccessException(String msg){
       super(msg);
    }
    public CacheAccessException(String msg,Exception ex){
       super(msg,ex);
       
    }
}
