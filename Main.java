
import java.io.File;
import java.util.*;


public class Main {
	
	public Main(){
				}

	public static void main(String[] args) throws Exception{
		int l1CacheSize = 250;
		final CacheProtocol protocol = CacheProtocol.MESI;
		Bus bus = new Bus();
		Cache l2Cache = new Cache(-1,Level.L2);
		bus.setL2Cache(l2Cache);
		
		node nde = new node(bus,protocol,l1CacheSize);
		node nde2 = new node(bus,protocol,l1CacheSize);
		node nde3 = new node(bus,protocol,l1CacheSize);
		File inputFile = new File("C:/Users/prash/workspace/MESI/src/input");
		HashMap<Integer,String> instructionsMap =new HashMap<Integer,String>();
		Scanner sc;
		
		int i=1;
		sc = new Scanner(inputFile);
		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			instructionsMap.put(i, line);
			i++;
		
			}
		System.out.println(instructionsMap.toString());
		for(String l:instructionsMap.values()){
			String[] subIns=l.split(",");
			//System.out.println(subIns.length);
			
			for(String ins:subIns){
			System.out.println("Executing Instruction {"+ins+"}");
			String[] innerList=ins.split(" ");
			
			if(innerList[0].contains("R")){
				
				if(innerList[0].contains("1")){
					nde.Read(Long.parseLong(innerList[1]));
					//node.address=Long.parseLong(innerList[1]);
				}
				else if(innerList[0].contains("2")){
					nde2.Read(Long.parseLong(innerList[1]));
					//nde2.address=Long.parseLong(innerList[1]);
				}
				else if(innerList[0].contains("3")){
					nde3.Read(Long.parseLong(innerList[1]));
				}
			
		}
			else if(innerList[0].contains("W")){
				if(innerList[0].contains("1")){
					nde.Write(Long.parseLong(innerList[1]), Integer.parseInt(innerList[2]));
				}
				else if(innerList[0].contains("2")){
					nde2.Write(Long.parseLong(innerList[1]), Integer.parseInt(innerList[2]));
				}
				else if (innerList[0].contains("3")){
					nde3.Write(Long.parseLong(innerList[1]), Integer.parseInt(innerList[2]));
				}
			}
			}
			
			
			
		sc.close();
	}
}
}
