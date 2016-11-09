package cn.com.xiao8.pay.alipay.redis.impl;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class KetamaRedisLocator {

	private TreeMap<Long, String> ketamaServers;
	private HashAlgorithm hashAlg;
	private int numReps = 160;
	
	public KetamaRedisLocator(List<String> list, HashAlgorithm alg){	
		hashAlg = alg;
		ketamaServers = new TreeMap<Long, String>();
		this.build(list);
	}
	
	public void build(List<String> list){
		for(String server : list){
			for (int i = 0; i < numReps / 4; i++) {
				byte[] digest = hashAlg.computeMd5(server + i);
				for (int h = 0; h < 4; h++) {
					long k = (long) (digest[3 + h * 4] & 0xFF) << 24
							| (long) (digest[2 + h * 4] & 0xFF) << 16
							| (long) (digest[1 + h * 4] & 0xFF) << 8
							| digest[h * 4] & 0xFF;
					ketamaServers.put(k, server);
				}
			}			
		}
	}
	
	public String getServerByKey(final String key){
		long hash = this.hashAlg.hash(key);
		String rv = getSessionByHash(hash);
		return rv;
	}
	
	public String getSessionByHash(final long hash){
		final String rv;
		Long key = hash;
		if(!ketamaServers.containsKey(key)){
			SortedMap<Long, String> tailMap = ketamaServers.tailMap(key);
			if(tailMap.isEmpty()) {
				key = ketamaServers.firstKey();
			} else {
				key = tailMap.firstKey();
			}			
		}
		rv = ketamaServers.get(key);
		return rv;
	}
}

