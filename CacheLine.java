


public class CacheLine {
	
	
	private long tag;
	private long[] values = new long[8];
	private CacheState cacheState;
            public long getTag() {
		return tag;
	}

	public void setTag(long tag) {
		this.tag = tag;
	}

	public long getValue(int index) {
		return values[index];
	}

	public void setValues(long[] values) {
		this.values = values;
	}

	public CacheState getCacheState() {
		return cacheState;
	}

	public void setCacheState(CacheState cacheState) {
		this.cacheState = cacheState;
	}
	
	@Override
	public String toString() {
		return tag+" , "+values+" , "+cacheState;
	}

	public long[] getValues() {
		return values;
	}
	
}


