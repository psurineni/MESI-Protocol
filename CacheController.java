
import java.util.HashMap;
import java.util.Map;

public abstract class CacheController {

	protected Bus bus;

	protected Cache cache;

	protected Map<Long, Long> cacheLineUsage = new HashMap<Long, Long>();

	long readHits = 0;

	long writeHits = 0;

	long readMiss = 0;

	long writeMiss = 0;

	long totalRequests = 0;

	long l2AccessCount = 0;

	protected long[] splitAddress(long address) {
		
		String addr = String.valueOf(address);
		
		 String t=addr.substring(0,5);
		 String i=addr.substring(5,7);
		 long tag=Long.parseLong(t);
		 long index=Long.parseLong(i);
		System.out.println("tag %% index"+tag+"|"+index);
		return new long[] { tag, index };
	}

	protected void incrementCacheLineUsage(long tag) {

		if (!cacheLineUsage.containsKey(tag)) {
			cacheLineUsage.put(tag, 0L);
		}
		cacheLineUsage.put(tag, cacheLineUsage.get(tag) + 1);
	}

	private boolean cacheLineNotPresent(long tag) {
		return (cache.getCacheLines().get(tag) == null)
				|| (cache.getCacheLines().get(tag).getCacheState() == CacheState.INVALID);
	}

	public long read(long addr) {
		
		//System.out.println(addr);
		long[] tagNIndex = splitAddress(addr);

		long tag = tagNIndex[0];

		int index = (int) tagNIndex[1];

		if (cacheLineNotPresent(tag)) {
			readMiss++;
			//System.out.println("Read miss, load the cache line from L1 caches or Memory ");
			load(tag);
		} else {
			System.out.println("Read hit");
			readHits++;
		}

		incrementCacheLineUsage(tag);

		CacheLine cacheLine = cache.getCacheLines().get(tag);
		System.out.println("Read value " + cacheLine.getValue(index));
		
		return cacheLine.getValue(index);
	}

	public void write(long addr, int value) {

		long[] tagNIndex = splitAddress(addr);

		long tag = tagNIndex[0];

		int index = (int) tagNIndex[1];

		if (cacheLineNotPresent(tag)) {

			writeMiss++;
			System.out.println("Write Miss");

			if (cache.getCacheLines().size() >= cache.getCacheSize() && cache.getLevel() == Level.L1) {

				moveLRUEntryToL2();
			}

			load(tag);
			//System.out.println("load cache line from other L1 caches or Memory");
		} else {

			writeHits++;
			System.out.println("Write Hit");
		}

		CacheLine cacheLine = cache.getCacheLines().get(tag);

		long[] values = cacheLine.getValues();
		values[index] = value;
		cacheLine.setValues(values);
		

		if (cacheLine.getCacheState() == CacheState.SHARED) {
			bus.invalidateCacheLine(tag);
			System.out.println("Cache Status before write " + cacheLine.getCacheState());
		}

		cacheLine.setCacheState(CacheState.MODIFIED);

		incrementCacheLineUsage(tag);
		System.out.println("Cache Status after write " + cacheLine.getCacheState());
	}

	protected void moveLRUEntryToL2() {

		if (cache == bus.getL2Cache()) {
			return;
		}
		long minUsedAddrTag = -1;
		long minCount = Long.MAX_VALUE;

		for (Map.Entry<Long, Long> entry : cacheLineUsage.entrySet()) {
			if (entry.getValue() < minCount) {
				minUsedAddrTag = entry.getKey();
			}
		}

		store(minUsedAddrTag);
		cache.getCacheLines().remove(minUsedAddrTag);
		cacheLineUsage.remove(minUsedAddrTag);
	}

	protected boolean store(long tag) {

		CacheLine cacheLine = cache.getCacheLines().get(tag);

		if (cacheLine != null && (cacheLine.getCacheState() != CacheState.INVALID)) {

			l2AccessCount++;
			synchronized (bus) {

				bus.getL2Cache().getCacheLine(tag).setValues(cacheLine.getValues());
			}
			return true;
		} else {
			return false;
		}
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Map<Long, Long> getCacheLineUsage() {
		return cacheLineUsage;
	}

	public void setCacheLineUsage(Map<Long, Long> cacheLineUsage) {
		this.cacheLineUsage = cacheLineUsage;
	}

	public long getReadHits() {
		return readHits;
	}

	public void setReadHits(long readHits) {
		this.readHits = readHits;
	}

	public long getWriteHits() {
		return writeHits;
	}

	public void setWriteHits(long writeHits) {
		this.writeHits = writeHits;
	}

	public long getReadMiss() {
		return readMiss;
	}

	public void setReadMiss(long readMiss) {
		this.readMiss = readMiss;
	}

	public long getWriteMiss() {
		return writeMiss;
	}

	public void setWriteMiss(long writeMiss) {
		this.writeMiss = writeMiss;
	}

	public long getTotalRequests() {
		return totalRequests;
	}

	public void setTotalRequests(long totalRequests) {
		this.totalRequests = totalRequests;
	}

	public long getL2AccessCount() {
		return l2AccessCount;
	}

	public void setL2AccessCount(long l2AccessCount) {
		this.l2AccessCount = l2AccessCount;
	}

	protected abstract void load(long tag);

}
