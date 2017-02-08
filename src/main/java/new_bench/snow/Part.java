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
import new_bench.types.TpchMoney;

public class Part extends AbstractEntity
{
    private final long partKey;
    private final String name;
    private final String manufacturer;
    private final String brand;
    private final String type;
    private final long size;
    private final String container;
    private final TpchMoney retailPrice;
    private final String comment;

    static String headers[] = {"partKey", "name", "manufacturer", "brand", "type", "size", "container", "retailPrice", "comment"}; 
    public static String types[] = {"n","s","s","s", "s", "n", "s", "m", "s"};
    
    public Part(long rowNumber,
            long partKey,
            String name,
            String manufacturer,
            String brand,
            String type,
            long size,
            String container,
            long retailPrice,
            String comment)
    {
    	super(rowNumber);
    	String[] values = new String[9];
    	this.relationName = "Part"; 
    	
        values[0] = "" + (this.partKey = partKey);
        values[1] = this.name = checkNotNull(name, "name is null");
        values[2] = this.manufacturer = checkNotNull(manufacturer, "manufacturer is null");
        values[3] = this.brand = checkNotNull(brand, "brand is null");
        values[4] = this.type = checkNotNull(type, "type is null");
        values[5] = "" + (this.size = size);
        values[6] = this.container = checkNotNull(container, "container is null");
        values[7] = "" + (this.retailPrice = new TpchMoney(retailPrice));
        values[8] = this.comment = checkNotNull(comment, "comment is null");   
    	entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber(){  return rowNumber;   }

    public long getPartKey(){  return partKey;   }

    public String getName(){  return name;   }

    public String getManufacturer(){  return manufacturer;   }

    public String getBrand(){  return brand;   }

    public String getType(){  return type;   }

    public long getSize(){  return size;   }

    public String getContainer(){  return container;   }

    public double getRetailPrice(){  return retailPrice.getValue() / 100.0;   }

    public long getRetailPriceInCents(){  return retailPrice.getValue();   }

    public String getComment(){  return comment;   }
   

    public static void main(String argz[]){
    	Part p = new Part(1,1,"a","a","a","a",1,"a",1,"a");
    	//System.out.println(p.values); 
    	System.out.println(p); 
    	System.out.println(p.toLine(null));
    	System.out.println(p.toJson(null));
    	System.out.println(p.toXML(null));
    	System.out.println(p.toCSV(",", null));
    }
}
