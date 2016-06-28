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
    public String toLine(){
    	String out = keyValues[0].toLine();  
    	for (int i=1; i<keyValues.length; i++){
    		out+="|"+keyValues[i].toLine(); 
    	}
    	return out; 
    }
    @Override
    public String toCSV(String separator){
    	String out = keyValues[0].toCSV(separator);  
    	for (int i=1; i<keyValues.length; i++){
    		out+=separator+keyValues[i].toCSV(separator); 
    	}
    	return out; 
    }
    @Override
    public String toXML(){
    	String out = "<"+relationName+">\n"; 
    	for (KeyValue kv: keyValues)
    		out+=kv.toXML(); 
    	return out + "</"+relationName+">"; 
    }
    @Override
    public String toJson(){
    	String out = "{" + relationName+": {"; 
    	out +=  keyValues[0].toJson();  
    	for (int i=1; i<keyValues.length; i++){
    		out += "," + keyValues[i].toJson() ;  
    	}
    	return out + "}}";  
    }
    
    public static void main(String argz[]){
    	KeyValue kv1 = new KeyValue("p", "a", "s"); 
    	KeyValue kv2 = new KeyValue("p", "a", "s"); 
    	KeyValue kv3 = new KeyValue("p", "a", "s"); 
    	KeyValue kv4 = new KeyValue("p", "a", "s"); 
    	
    	ArrayEntityInstance a = new ArrayEntityInstance( "name", new KeyValue[]{kv1, kv2, kv3, kv4}); 
    	System.out.println(a.toJson());
    	System.out.println(a.toXML());
    	System.out.println(a.toLine());
    }
    
}
