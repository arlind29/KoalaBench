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

public class Nation extends AbstractEntity{
    private final long nationKey;
    private final String name;
    private final long regionKey;

    public static String headers[] = {"nationKey","nation_name","regionKey"};
    public static String types[] = {"n","s","n","s"};

    
    public Nation(long rowNumber, long nationKey, String name, long regionKey, String comment)
    {
    	super(rowNumber);
    	String[] values = new String[3];
    	this.relationName = "Nation";
    	
        values[0] = "" + (this.nationKey = nationKey);
        values[1] = this.name = checkNotNull(name, "name is null");
        values[2] = "" + (this.regionKey = regionKey);
        entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber(){  return rowNumber;  }

    public long getNationKey(){  return nationKey;  }

    public String getName(){  return name;  }

    public long getRegionKey(){  return regionKey;  }

}
