package new_bench.types;

import java.util.HashMap;


public class KeyValue {
	protected String key;
	protected String value;
	protected String type; 
	
	public KeyValue(String key, String value, String type){
		this.key = key; this.value = value; this.type = type; 
	}
	
    public String toLine() {
    	System.out.println("ERROR: KeyValue cannot be used with this method"); 
    	System.exit(-1);;
    	return ""; 
    }
    
    public String toJson(){
    	return key + ": " + addQuotes(value, type);
    }
    
    public String toXML(){
    	return "<"+key + ">" + addQuotes(value, type) + "</"+key + ">";
    }
    
    public String toCSV(){
    	System.exit(-1);;
    	return ""; 
    }
    
    private String addQuotes(String val, String type){
    	if (type.equals("n")) return val; else return "\""+val+"\"";
    }
	

}
