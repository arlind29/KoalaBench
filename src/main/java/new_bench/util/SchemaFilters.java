package new_bench.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap; 
import java.util.Vector;

public class SchemaFilters {

	HashMap<String, ChanceFilter> filters = new HashMap<String, ChanceFilter>(); 
	
	public static SchemaFilters readFromFile(String filename){
		SchemaFilters sch = new SchemaFilters(); 
		BufferedReader br = null; String line; 
		try {
			br = new BufferedReader(new FileReader(filename));
			
		    while ((line = br.readLine()) != null){
		    	sch.readFilter(line); 
		    }	    
		    br.close();
		}catch(Exception e){e.printStackTrace();}		
		
		return sch;
	}
	
	
	private void readFilter(String line){
		String parts[] = line.split("\\s*\\,\\s*"); 
		if (parts.length <= 2) return; 
		
		HashMap<String, Boolean> filter = new HashMap<String, Boolean>(); 
		for (int i=2; i<parts.length; i++)
			filter.put(parts[i], true); 
		
		double chance = 0;
		String entity = parts[0].toLowerCase();
	
		try{chance = Double.parseDouble(parts[1]);}catch(Exception e){}
		ChanceFilter chf = filters.get(entity); 
		if (chf==null) {
			chf = new ChanceFilter(entity);
			this.filters.put(entity, chf);
		}
				
		chf.addFilter(chance, filter);			
		 
	}
	
	public static boolean stays(HashMap<String, Boolean> filter, String key){
		if (filter==null) return true;
		Boolean keep = filter.get(key);
		if (keep==null) return false; 
		return false; 
	}
	
	public HashMap<String, Boolean> getEntityFilter(String entityName){
		ChanceFilter chf = filters.get(entityName.toLowerCase()) ; 
		if (chf==null) return null; 
		return chf.getFilter(); 
	}
	
}

class ChanceFilter{
	Vector<Double> chances = new Vector<Double>(); 
	Vector<HashMap<String, Boolean>> filters = new Vector<HashMap<String, Boolean>>(); 
	
	String entity; 
	double totalChance = 0; 
	
	public ChanceFilter(String entity){this.entity = entity; }
	
	public void addFilter(double chance, HashMap<String, Boolean> filter){
		chances.add(chance); filters.add(filter);
		totalChance += chance; 
		if (totalChance>100) { 
			System.out.println("Maximum of 100% distribution exceeded on "+ entity);
			System.exit(-1);
		}
	}
	
	public HashMap<String, Boolean> getFilter(){
		double rnd = Math.random() * 100;
		int min = 0; int max = 0;
		int i; boolean found = false; 
		for (i=0; i<chances.size(); i++){
			max+=chances.elementAt(i); 
			if (rnd>min && rnd<=max ){found = true; break;}
			min = max; 
		}
		if (found) return filters.elementAt(i); 
		else return null; 
		
	}
	
}