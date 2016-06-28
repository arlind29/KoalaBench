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

public abstract class AbstractMultiEntity implements Entity 
{
	protected  final long rowNumber;
	public long getRowNumber(){return rowNumber;}
	
	protected EntityInstance[] entities; 
	
	protected String relationName; 
	protected HashMap<String, Boolean> projMap = new HashMap<String, Boolean>(); // projection map
	
	public String[] headers(){
		int headerSize = 0; int i=0; 
		for (EntityInstance entity: entities){headerSize+=entity.headers.length;}
		String[] allHeaders = new String[headerSize];
		for (EntityInstance entity: entities){
			for (String header: entity.headers){ allHeaders[i] = header; i++;	}
		} return allHeaders; 
	} 
	public String[] types(){
		int typesSize = 0; int i=0; 
		for (EntityInstance entity: entities){typesSize+=entity.types.length;}
		String[] allTypes = new String[typesSize];
		for (EntityInstance entity: entities){
			for (String type: entity.types){ allTypes[i] = type; i++;	}
		} return allTypes; 
	} 
	public String[] values(){
		int valuesSize = 0; int i=0; 
		for (EntityInstance entity: entities){valuesSize+=entity.headers.length;}
		String[] allValues = new String[valuesSize];
		for (EntityInstance entity: entities){
			for (String value: entity.headers){ allValues[i] = value; i++;	}
		} return allValues; 
	} 

	public String getRelationName(){return relationName;}
    
    
	public AbstractMultiEntity(long rowNumber, String relationName, EntityInstance[] entities){
		this.rowNumber = rowNumber;
		this.relationName = relationName; 
		this.entities = entities; 
	}
	
        
    @Override
    public String toLine(){
    	String out = entities[0].toLine();  
    	for (int i=1; i<entities.length; i++){
    		out+="|"+entities[i].toLine(); 
    	}
    	return out; 
    }
    @Override
    public String toCSV(String separator){
    	String out = entities[0].toCSV(separator);  
    	for (int i=1; i<entities.length; i++){
    		out+=separator+entities[i].toCSV(separator); 
    	}
    	return out; 
    }
    @Override
    public String toXML(){
    	String out = "<"+relationName+">\n"; 
    	for (EntityInstance entity: entities)
    		out+=entity.toXML(); 
    	return out + "</"+relationName+">"; 
    }
    @Override
    public String toJson(){
    	String str = entities[0].toJson().trim();  
    
    	String out = "{" + str.substring(1, str.length() -1); 
    	for (int i=1; i<entities.length; i++){
    		str = entities[i].toJson().trim(); 
    		out += "," + str.substring(1, str.length() -1); 
    	}
    	return out + "}";  
    }
    
    @Override
    public String toElasticSearchJson(){
    	String str = "{\"index\": {_index: \"tpch\", _type: \""+ this.relationName + "\", _id: "+ this.rowNumber + "}}";
    	return str + "\n" + this.toJson();  
    }
    
}
