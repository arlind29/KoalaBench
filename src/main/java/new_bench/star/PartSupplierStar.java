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
package new_bench.star;

import new_bench.snow.Nation;
import new_bench.snow.Part;
import new_bench.snow.PartSupplier;
import new_bench.snow.Region;
import new_bench.snow.Supplier;
import new_bench.types.MultiEntity;
import new_bench.types.EntityInstance;

public class PartSupplierStar extends MultiEntity{
//    private final long rowNumber;
    //private final Part part; private final Supplier supplier;  private final PartSupplier partSupplier;
    //private final Nation nation; private final Region region; 
    private final static String relationName ="PartSupplierStar"; 
    
    public static String partProjection[] = {"partKey", "name", "manufacturer", "brand", "type", "size", "container", "retailPrice"};
    public static String supplierProjection[] = {"supplierKey","name","address","phone","accountBalance"};
    public static String nationProjection[] = {"nation_name"};
    public static String regionProjection[] = {"region_name"};
    public static String partSupplierProjection[] = {"availableQuantity","supplyCost"};
    
    
    public PartSupplierStar(long rowNumber, Part part, Supplier supplier, PartSupplier partSupplier, Nation nation, Region region)
    {
    	super(rowNumber, relationName, new EntityInstance[]{part.getEntity(), supplier.getEntity(), partSupplier.getEntity(), nation.getEntity(), region.getEntity()}); 
    	//this.part = part; this.supplier = supplier; this.partSupplier = partSupplier; this.nation=nation; this.region = region; 
    	part.setProjection(partProjection); 
    	supplier.setProjection(supplierProjection); 
    	partSupplier.setProjection(partSupplierProjection);         
    	nation.setProjection(nationProjection);
    	region.setProjection(regionProjection);
    	part.setPrefix("p_");
    	supplier.setPrefix("s_"); nation.setPrefix("s_"); region.setPrefix("s_");
    	
    }
    
    public static void main(String argz[]){
    	Part p = new Part(1,1,"a","a","a","a",1,"a",1,"a");
    	PartSupplier ps = new PartSupplier(1,1,1,798,1,"a");
    	Supplier s = new Supplier(1, 1,"a", "bb", 6, "xp", 3, "aaa");
    	Nation n = new Nation(1, 1, "FRA", 1, "");
    	Region r = new Region(1, 1, "FRA");
    	PartSupplierStar x = new PartSupplierStar(1, p, s, ps, n, r);
    	System.out.println(x); 
    	System.out.println(x.toLine(null));
    	System.out.println(x.toJson(null));
    	System.out.println(x.toXML(null));
    	System.out.println(x.toCSV(",", null));
    }    
}
