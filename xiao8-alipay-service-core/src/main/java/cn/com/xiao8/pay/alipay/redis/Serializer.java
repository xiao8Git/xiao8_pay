package cn.com.xiao8.pay.alipay.redis;

public interface Serializer {

	public byte[] serialize(Object object) throws Exception;
	
	public Object deserialize(byte[] bytes) throws Exception;
}
