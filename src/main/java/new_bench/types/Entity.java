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

import new_bench.util.SchemaFilters;

public interface Entity
{
    long getRowNumber();
    
    //TpchEntityInstance entity = null; 
    
    String[] headers(); 
    String[] values();
    String[] types();
    
    //public void setProjection(String[] proHeaders);  
    
    public String toLine(SchemaFilters filters);
    public String toJson(SchemaFilters filters);
    public String toXML(SchemaFilters filters);
    public String toElasticSearchJson(SchemaFilters filters); 
    public String toCSV(String valueSeparator, SchemaFilters filters);
    
}
