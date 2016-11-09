package cn.com.xiao8.pay.alipay.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisAccessor {

	/*过期时间单位是秒*/
	public void hset(String key, String field, Object value, int expiresTime);
	
	public Object hget(String key, String field);
	
	/*过期时间单位是秒*/
	public void set(String key, Object value, int expiresTime);
	
	public Long setnx(String key, Object value, int expiresTime);
	
	public Object get(String key);
	
	public String getCounterValue(String key);
	
	/*过期时间单位是秒*/
	public long incr(String key, long number, int expiresTime);
	
	/*过期时间单位是秒*/
    public void expire(String key, int expiration);

    public Set<String> listDataMap(String key, int num);

    public void listadd(String key, String infoKey);

    public void deleteList(String key, List<String> keys);
    
    public void sadd(String key, String member, int expiresTime) ;
    
    public List<String> spop(String key, int num);
    
    public void delKeys(List<String> keyList);
    
    public void delKey(String key);
    
    public Map getJedisPool(String key);
    
    public void releaseJedisPool(Map poolMap);
    
    /**
     * 传jedis的pool进来批量操作，要在外面释放连接池资源
     * @param poolMap
     * @param key
     * @param value
     * @param expiresTime
     */
    public void setWithPool(Map poolMap, String key, Object value, int expiresTime);
    
    /**
     * 传jedis的pool进来批量操作，要在外面释放连接池资源
     * @param poolMap
     * @param key
     * @param member
     * @param expiresTime
     */
    public void saddWithPool(Map poolMap, String key, String member, int expiresTime);
}
