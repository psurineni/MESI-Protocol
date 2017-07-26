


import java.util.List;

public class MESICacheController extends CacheController{

	public MESICacheController(Cache cache,Bus bus) {
		this.cache = cache;
		this.bus = bus;
	}
	protected void load(long tag) {
		
		
		CacheLine newCacheLine = new CacheLine();
		newCacheLine.setTag(tag);
		
		
		CacheLine loadedCacheLine = null;

		synchronized (bus) {
			
			List<CacheLine> cacheLines = bus.getCacheLineCopiesFromOtherL1Caches(this.cache, tag);
			
			if (cacheLines.isEmpty()) {

				
				l2AccessCount++;

				
				loadedCacheLine = bus.getCacheLineFromL2(tag);
				loadedCacheLine.setCacheState(CacheState.EXCLUSIVE);
				newCacheLine.setCacheState(CacheState.EXCLUSIVE);
				System.out.println("loading cache from Memory, Status"+CacheState.EXCLUSIVE);
			} else {

				System.out.println("Loading Cache from other L1 caches,Status:SHARED");
				loadedCacheLine = cacheLines.get(0);
				if (loadedCacheLine.getCacheState() == CacheState.MODIFIED) {
					store(tag);
				}
				bus.markCacheLineShared(tag);
			}
			
			newCacheLine.setValues(loadedCacheLine.getValues());
		}
		
		cache.getCacheLines().put(tag, newCacheLine);
	}
	
}
