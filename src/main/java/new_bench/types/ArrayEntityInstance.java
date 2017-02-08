/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package new_bench.types;

import java.util.HashMap;

import new_bench.util.SchemaFilters;


public class ArrayEntityInstance extends EntityInstance 
{   
    protected KeyValue[] keyValues; 
    
    public ArrayEntityInstance(String attributeName, KeyValue[] keyValues){
        super(attributeName); 
        headers = new String[]{relationName}; 
        types =  new String[]{"a"}; 
        values = new String[]{keyValues.toString()};
        this.keyValues = keyValues; 
    }
    
 
    @Override
    public String toLine(SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName);
        String out =  (filter==null || filter.get(keyValues[0])!=null) ? keyValues[0].toLine() : "";
        for (int i=1; i<keyValues.length; i++){
            out+="|"+keyValues[i].toLine(); 
        }
        out = out.trim().replaceAll("^\\|", "");
        return out; 
    }
    
    @Override
    public String toCSV(String separator, SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName);
        String out =  (filter==null || filter.get(keyValues[0])!=null) ? keyValues[0].toCSV() : "";
        for (int i=1; i<keyValues.length; i++){
            out+=  separator + ((filter==null || filter.get(keyValues[i])!=null) ? separator+keyValues[i].toCSV() : ""); 
        }
        return out; 
    }
    
    @Override
    public String toXML(SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName);
        String out = "<"+relationName+">\n"; 
        for (KeyValue kv: keyValues)
            out+=  (filter==null || filter.get(kv.key)!=null) ? kv.toXML() : ""; 
        return out + "</"+relationName+">"; 
    }
    @Override
    public String toJson(SchemaFilters filters){
    	HashMap<String, Boolean> filter = filters.getEntityFilter(relationName);
        String out = "{" + relationName+": {";   
        for (KeyValue kv: keyValues){
            if ((filter==null || filter.get(kv.key)!=null)){
                out +=  kv.toJson();
                out += ",";
            }
        }
        out = out.trim().replaceAll("\\,$", "");
        return out + "}}";  
    }
    
    
}
