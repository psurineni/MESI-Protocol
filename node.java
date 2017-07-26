

public class node{
	CacheController cacheController;


public node(Bus bus,CacheProtocol protocol,int cacheSize) {
	
		Cache l1Cache = new Cache(cacheSize,Level.L1);
		bus.addNewL1Cache(l1Cache);
		if(protocol == CacheProtocol.MESI)
			cacheController = new MESICacheController(l1Cache, bus);
		
	}
	
public void Read(long address){
	
	cacheController.read(address);
	
	
}

public void Write(long address,int value){
	cacheController.write(address, value);
	
}




}
