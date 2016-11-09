package cn.com.xiao8.pay.alipay.redis.impl;

import cn.com.xiao8.pay.alipay.redis.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class JdkSerializer implements Serializer {

	@Override
	public Object deserialize(byte[] bytes) throws Exception {
		if(bytes == null){
			return null;
		}
		Object obj = null;          
        ByteArrayInputStream bis = new ByteArrayInputStream (bytes);       
        ObjectInputStream ois = new ObjectInputStream (bis);       
        obj = ois.readObject();     
        ois.close();  
        bis.close();  
      
        return obj; 
	}

	@Override
	public byte[] serialize(Object object) throws Exception {
		if(object == null){
			return null;
		}		
        byte[] bytes = null;     
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);    
        oos.writeObject(object);       
        oos.flush();        
        bytes = bos.toByteArray ();     
        oos.close();        
        bos.close();

        return bytes; 
	}

}
