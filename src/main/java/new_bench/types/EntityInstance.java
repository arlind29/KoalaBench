package new_bench.types;

import java.util.HashMap;

import new_bench.util.SchemaFilters;

public class EntityInstance {
    private boolean projectAll = true;

    protected String headers[];
    protected String values[];
    protected String[] types;
    protected String relationName; 
    protected String prefix = ""; // a possible prefix for attribute names
    protected HashMap<String, Boolean> projMap = new HashMap<String, Boolean>(); // projection map
    
    public String[] headers(){return headers;} 
    public String[] values(){return values;}
    public String[] types(){return types;}
    public HashMap<String, Boolean> getProjectionMap(){return projMap;}
    public String getRelationName(){return relationName;}
    
    public void setProjection(String[] projHeaders){
        projMap = new HashMap<String, Boolean>();
        for (String header: projHeaders){ projMap.put(header, true);}
        int count = 0;
        for (String header: headers){if (projMap.get(header)!=null) count++; }
        if (count < projMap.size()) {
            System.out.println("Wrong header in projection of "+relationName); System.exit(-1);
        }
        projectAll=false;   
    }
    
    public void setPrefix(String prefix){this.prefix = prefix; } 
    
    protected  EntityInstance(String relationName){this.relationName = relationName;}
    
    public EntityInstance(String relationName, String[] headers, String[] types, String values[]){
        this.relationName = relationName; 
        this.headers = headers; 
        this.types = types; 
        this.values = values; 
    }
    
    public String toLine(SchemaFilters filters) {
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName); 
        String out = "";  int count =0; 
        for (int i=0; i<values.length; i++){
            if (projectAll || projMap.get(headers[i])!=null) {
                out += (filter==null || filter.get(headers[i])!=null) ? addQuotes(values[i], types[i]) : addQuotes("", types[i]);
                count++;
                if ((projectAll || projMap.size()>count) && i!=headers.length-1) out += "|";
            } 
        }
        return out; 
    }
    
    public String toJson(SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName); 
        String out= "{"; int count = 0; 
        for (int i=0; i<headers.length; i++){ 
            if (projectAll || projMap.get(headers[i])!=null) {
                if (filter==null || filter.get(headers[i])!=null){
                    out+= ( prefix+headers[i]+":" +addQuotes(values[i], types[i])) ;
                    count++; out+=", ";
                }
            }
        }       
        out = out.trim().replaceAll("\\,$", "");  
        return out+ "}"; 
    }
    
    public String toXML(SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName); 
        String out = "<"+relationName+">\n"; 
        for (int i=0; i<headers.length; i++){
            if (projectAll || projMap.get(headers[i])!=null) 
                if (filter==null || filter.get(headers[i])!=null)
                    out+="\t<"+prefix+headers[i]+">" + addQuotes(values[i],types[i]) +"</"+headers[i]+">\n";
        }
        return out+ "</"+relationName+">\n";
    }
    
    public String toCSV(String separator, SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName); 
        String out = ""; int count =0;  
        for (int i=0; i<values.length; i++){
            if (projectAll || projMap.get(headers[i])!=null) {
                out += (filter==null || filter.get(headers[i])!=null)  ? addQuotes(values[i],types[i]) : addQuotes("", types[i]);
                count++; 
                if ((projectAll || projMap.size()>count) && i!=headers.length-1) out += separator; 
            }
        }
        return out; 
        //return String.join(separator, values);  
    }
    
    private String addQuotes(String val, String type){
        if (type.equals("n")) return val; else return "\""+val+"\"";
    }
    

}
