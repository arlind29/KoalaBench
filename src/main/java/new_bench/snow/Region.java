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
package new_bench.snow;

import static com.google.common.base.Preconditions.checkNotNull;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;

public class Region extends AbstractEntity {
    private final long regionKey;
    private final String name;

    public static String[] headers = {"regionKey","region_name"}; 
    public static String types[] = {"n","s","s"};
    
    public Region(long rowNumber, long regionKey, String name)
    {
    	super(rowNumber);
    	String[] values = new String[2];
    	this.relationName = "Region";

    	values[0] = "" + (this.regionKey = regionKey);
        values[1] = this.name = checkNotNull(name, "name is null");
        entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber(){  return rowNumber;   }

    public long getRegionKey(){  return regionKey;   }

    public String getName(){  return name;   }

}
