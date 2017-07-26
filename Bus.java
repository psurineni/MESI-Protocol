
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bus {
	private Cache l2Cache = null;
	private List<Cache> l1Caches = new ArrayList<Cache>();

	public void addNewL1Cache(Cache cache) {
		l1Caches.add(cache);
	}
	public List<Cache> getL1Caches() {
		return l1Caches;
	}
	public Cache getL2Cache() {
		return l2Cache;
	}
	public void setL2Cache(Cache l2Cache) {
		this.l2Cache = l2Cache;
	}

	public synchronized void invalidateCacheLine(long tag) {
		for(Cache l1Cache: l1Caches){
			CacheLine cacheLine = l1Cache.getCacheLines().get(tag);
			if(cacheLine != null){
				cacheLine.setCacheState(CacheState.INVALID);
			}
		}
	}
	public void markCacheLineShared(long tag) {
		for(Cache l1Cache: l1Caches){
			CacheLine cacheLine = l1Cache.getCacheLines().get(tag);
			if(cacheLine != null){
				cacheLine.setCacheState(CacheState.SHARED);
			}
		}
	}


public List<CacheLine> getCacheLineCopiesFromOtherL1Caches(Cache cache,long tag) {
		List<CacheLine> cacheLines = new ArrayList<CacheLine>();
		for (Cache l1Cache :l1Caches) {
			if(l1Cache == cache)continue;
			CacheLine cacheLine = l1Cache.getCacheLine(tag);
			if(cacheLine != null){
				if(cacheLine.getCacheState() != CacheState.INVALID){
					cacheLines.add(cacheLine);
				}
			}
		}
		return cacheLines;
	}

	public CacheLine getCacheLineFromL2(long tag) {
		CacheLine loadedCacheLine;
		if (l2Cache.getCacheLine(tag) == null) {
			long [] values = new long [8]; 
			for(int i = 0;i<8;i++){
				values[i] = new Random().nextInt(10000) + 10000;
			}
			CacheLine cacheLine = new CacheLine();
			cacheLine.setCacheState(CacheState.EXCLUSIVE);
			cacheLine.setTag(tag);
			cacheLine.setValues(values);
			l2Cache.getCacheLines().put(tag, cacheLine);
		}
		loadedCacheLine = l2Cache.getCacheLine(tag);
		return loadedCacheLine;
	}




}



