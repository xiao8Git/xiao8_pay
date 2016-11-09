package cn.com.xiao8.pay.alipay.redis.impl;

import cn.com.xiao8.pay.alipay.redis.RedisAccessor;
import cn.com.xiao8.pay.alipay.redis.Serializer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;



@Service
public class RedisAccessorImpl implements RedisAccessor, InitializingBean {

    private JedisPoolConfig poolConfig;

    private Map<String, JedisPool> poolMap;

    private KetamaRedisLocator locator;

    @Value("${redis.maxtotal}")
    private int maxTotal = 500;

    private int maxIdle = 20;

    private boolean blockWhenExhausted = false;

    private boolean testOnBorrow = true;

    @Value("${redis.server}")
    private String servers;
    
    @Value("${redis.password}")
    private String password;

    private Serializer serializer = new JdkSerializer();

    private Log log = LogFactory.getLog(RedisAccessorImpl.class);

    public static final String JEDIS_POOL="jedisPool";
    public static final String JEDIS="jedis";
    
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (this.poolConfig == null) {
                this.poolConfig = new JedisPoolConfig();
                this.poolConfig.setMaxTotal(maxTotal);
                this.poolConfig.setMaxIdle(maxIdle);
                this.poolConfig.setBlockWhenExhausted(blockWhenExhausted);
                this.poolConfig.setTestOnBorrow(testOnBorrow);
            }
            if (poolMap == null) {
                poolMap = new HashMap<String, JedisPool>();
            }

            List<String> serverList = getAddresses(this.servers);

            for (String server : serverList) {
            	JedisPool pool = null;
            	if(StringUtils.isNotBlank(password)){
            		pool = new JedisPool(this.poolConfig, server.split(":")[0], Integer.parseInt(server.split(":")[1]),0,password);
            	}else{
            		pool = new JedisPool(this.poolConfig, server.split(":")[0], Integer.parseInt(server.split(":")[1]));
            	}
                this.poolMap.put(server, pool);
            }

            this.locator = new KetamaRedisLocator(serverList, HashAlgorithm.KETAMA_HASH);
        } catch (Exception e) {
            log.error("RedisAccessorImpl.afterPropertiesSet",e);
            throw new CacheAccessException(e.getMessage(), e);
        }

    }

    private static List<String> getAddresses(String s) {
        if (s == null) {
            throw new NullPointerException("Null host list");
        }
        if (s.trim().equals("")) {
            throw new IllegalArgumentException("No hosts in list:  ``" + s + "''");
        }
        ArrayList<String> servers = new ArrayList<String>();
        // 支持“，”替代空格
//        s.replaceAll(";", " ");
        StringUtils.replace(s, ";", " ");
        //
        for (String hoststuff : s.split(" ")) {
            int finalColon = hoststuff.lastIndexOf(':');
            if (finalColon < 1) {
                throw new IllegalArgumentException("Invalid server ``" + hoststuff + "'' in list:  " + s);
            }
            servers.add(hoststuff);
        }

        return servers;
    }

    /*过期时间单位是秒*/
    @Override
    public void hset(String key, String field, Object value, int expiresTime) {
        JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key.getBytes(), field.getBytes(), this.serializer.serialize(value));
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl hset", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Object hget(String key, String field) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            Object obj = null;
            jedis = jedisPool.getResource();
            byte[] result = jedis.hget(key.getBytes(), field.getBytes());
            if (result != null) {
                obj = this.serializer.deserialize(result);
            }
            return obj;
        } catch (Exception e) {
            log.error("RedisAccessorImpl hget", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Object get(String key) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            Object obj = null;
            jedis = jedisPool.getResource();
            byte[] result = jedis.get(key.getBytes());
            if (result != null) {
                obj = this.serializer.deserialize(result);
            }
            return obj;
        } catch (Exception e) {
            log.error("RedisAccessorImpl get", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /*过期时间单位是秒*/
    @Override
    public long incr(String key, long number, int expiresTime) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long result = jedis.incrBy(key, number);
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
            return result;
        } catch (Exception e) {
            log.error("RedisAccessorImpl incr", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /*过期时间单位是秒*/
    @Override
    public void set(String key, Object value, int expiresTime) {
        JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key.getBytes(), this.serializer.serialize(value));
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl set", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    
    /*过期时间单位是秒*/
    @Override
    public Long setnx(String key,Object value,int expiresTime){
    	JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setnx(key.getBytes(), this.serializer.serialize(value));
            if (result==1L && expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl set", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    	return result;
    }
    
    /*过期时间单位是秒*/
    @Override
    public void setWithPool(Map poolMap, String key, Object value, int expiresTime) {

        try {
            JedisPool jedisPool = (JedisPool)poolMap.get(JEDIS_POOL);
            Jedis jedis =  (Jedis)poolMap.get(JEDIS);
            jedis.set(key.getBytes(), this.serializer.serialize(value));
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl setWithPool", e);
            //throw new CacheAccessException(e.getMessage(), e);
        }
    }
    
    public Map getJedisPool(String key){
    	Map resultMap = new HashMap();
    	JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            resultMap.put(JEDIS_POOL,jedisPool);
            resultMap.put(JEDIS,jedis);
        } catch (Exception e) {
            log.error("RedisAccessorImpl getJedisPool", e);
            throw new CacheAccessException(e.getMessage(), e);
        } 
    	return resultMap;
    }
   
    public void releaseJedisPool(Map poolMap){
    	if(poolMap!=null){
    		try {
    			JedisPool jedisPool = (JedisPool)poolMap.get(JEDIS_POOL);
    			Jedis jedis = (Jedis)poolMap.get(JEDIS);
    			if(jedisPool!=null&&jedis!=null){
    				 jedisPool.returnResource(jedis);
    			}
    		}catch (Exception e){
                log.error("RedisAccessorImpl releaseJedisPool", e);
                throw new CacheAccessException(e.getMessage(), e);
    		}
    	}
    }
    
    /*过期时间单位是秒*/
    @Override
    public void sadd(String key, String member, int expiresTime) {
        JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.sadd(key, member);
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl sadd", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    
    @Override
    public void saddWithPool(Map poolMap, String key, String member, int expiresTime) {

        try {
            JedisPool jedisPool = (JedisPool)poolMap.get(JEDIS_POOL);
            Jedis jedis =  (Jedis)poolMap.get(JEDIS);
            jedis.sadd(key, member);
            if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl sadd", e);
           // throw new CacheAccessException(e.getMessage(), e);
        } 
    }

    @Override
    public List<String> spop(String key,int num) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
        	List<String> result = new ArrayList();
            jedis = jedisPool.getResource();
            for(int i=0;i<num;i++) {
            	String redisValue=jedis.spop(key);
            	if(redisValue!=null) {
            		result.add(redisValue);
            	}else {
            		break;
            	}
            }
            return result;
        } catch (Exception e) {
            log.error("RedisAccessorImpl spop", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    
    @Override
    public void delKeys(List<String> keyList) {
    	if(keyList==null||keyList.size()<=0)  return;
        JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(UUID.randomUUID().toString()));//随机产生一个KEY
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for(int i=0;i<keyList.size();i++) {
            	jedis.del(keyList.get(i));
            }
        } catch (Exception e) {
            log.error( "RedisAccessorImpl delKeys", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    
    @Override
    public void delKey(String key) {
    	if(key==null)  return;
        JedisPool jedisPool = poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            log.error("RedisAccessorImpl delKey", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    
    
    @Override
    public String getCounterValue(String key) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            log.error( "RedisAccessorImpl getCounterValue", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /*过期时间单位是秒*/
    @Override
    public void expire(String key, int expiration) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, expiration);
        } catch (Exception e) {
            log.error("RedisAccessorImpl expire", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Set<String> listDataMap(String key, int num) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Set<String> listKey = jedis.zrange(key, 0, num);
            return listKey;
        } catch (Exception e) {
            log.error("RedisAccessorImpl listDataMap", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


    @Override
    public void listadd(String key, String infoKey) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zadd(key, (new Date()).getTime(), infoKey);
        } catch (Exception e) {
            log.error("RedisAccessorImpl listadd", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void deleteList(String key, List<String> keys) {
        JedisPool jedisPool = this.poolMap.get(this.locator.getServerByKey(key));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (String member : keys) {
                //先删除List，再删除对应的值
                jedis.zrem(key, member);
                jedis.del(member);
            }
        } catch (Exception e) {
            log.error("RedisAccessorImpl deleteList", e);
            throw new CacheAccessException(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}