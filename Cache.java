
import java.util.HashMap;
import java.util.Map;

public class Cache {

	private Map<Long, CacheLine> cacheLines;
	private int cacheSize;
	private Level level;

	public Cache(int size, Level level) {
		cacheLines = new HashMap<Long, CacheLine>();
		this.cacheSize = size;
		this.level = level;
	}
	public CacheLine getCacheLine(long tag) {
		return cacheLines.get(tag);
	}
	
	public Map<Long, CacheLine> getCacheLines() {
		return cacheLines;
	}


	public int getCacheSize() {
		return cacheSize;
	}
	public Level getLevel() {
		return level;
	}
	
}
