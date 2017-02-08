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

public class PartSupplier extends AbstractEntity{
//    private final long rowNumber;
    private final long partKey;
    private final long supplierKey;
    private final long availableQuantity;
    private final TpchMoney supplyCost;
    private final String comment;

    public static String headers[] = {"partKey","supplierKey","availableQuantity","supplyCost","comment"};   
    public static String types[] = {"n","n","n","m", "s"};
    
    public PartSupplier(long rowNumber, long partKey, long supplierKey, long availableQuantity, long supplyCost, String comment)
    {
    	super(rowNumber);
    	String[] values = new String[5];
    	this.relationName = "PartSupplier";
    	
        values[0] = "" + (this.partKey = partKey);
        values[1] = "" + (this.supplierKey = supplierKey);
        values[2] = "" + (this.availableQuantity = availableQuantity);
        values[3] = "" + (this.supplyCost = new TpchMoney(supplyCost));
        values[4] = this.comment = checkNotNull(comment, "comment is null");
        entity = new EntityInstance(relationName, headers, types, values); 
    }

    public long getPartKey()  {   return partKey;   }

    public long getSupplierKey(){  return supplierKey;  }

    public long getAvailableQuantity(){  return availableQuantity;  }

    public double getSupplyCost(){  return supplyCost.getValue() / 100.0;  }

    public long getSupplyCostInCents(){  return supplyCost.getValue();  }

    public String getComment(){  return comment;  }

    public static void main(String argz[]){
    	PartSupplier p = new PartSupplier(1,1,1,798,1,"a");
    	System.out.println(p); 
    	System.out.println(p.toLine(null));
    	System.out.println(p.toJson(null));
    	System.out.println(p.toXML(null));
    	System.out.println(p.toCSV(",", null));
    }    
}
