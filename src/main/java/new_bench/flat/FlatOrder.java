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
package new_bench.flat;

import new_bench.snow.Customer;
import new_bench.snow.Nation;
import new_bench.snow.Order;
import new_bench.snow.Region;
import new_bench.types.MultiEntity;
import new_bench.types.EntityInstance;

public class FlatOrder extends MultiEntity{
//    private final long rowNumber;
    //private final Part part; private final Supplier supplier;  private final PartSupplier partSupplier;
    //private final Nation nation; private final Region region; 
    private final static String relationName ="FlatOrder"; 
    
    public static String custProjection[] = {"name","address","nationKey","phone","accountBalance","marketSegment"}; // "customerKey","name","address","nationKey","phone","accountBalance","marketSegment","comment"
    public static String c_nationProjection[] = {"nation_name"};
    public static String c_regionProjection[] = {"region_name"};
    
    public static String orderProjection[] = {"orderKey", "orderStatus", "totalPrice", "orderDate","orderPriority", "clerk", "shipPriority"}; // "orderKey","customerKey","orderStatus","totalPrice","orderDate","orderPriority","clerk","shipPriority" 
    // lineorder, order, star_customer
    // star_supplier, part, part_supplier
    // date 
    
    
    public FlatOrder(long rowNumber,
    		Order order, Customer cust, Nation c_nation, Region c_region)
    {
    	super(rowNumber, relationName, new EntityInstance[]{
    			order.getEntity(), cust.getEntity(), c_nation.getEntity(), c_region.getEntity()}); 
    	//this.part = part; this.supplier = supplier; this.partSupplier = partSupplier; this.nation=nation; this.region = region; 
    	
    	//partSupplier.setProjection(partSupplierProjection);
    	order.setProjection(orderProjection);
    	cust.setProjection(custProjection);
    	c_nation.setProjection(c_nationProjection);
    	c_region.setProjection(c_regionProjection);
    	// prefixes
    	cust.setPrefix("c_"); c_nation.setPrefix("c_"); c_region.setPrefix("c_");
    	order.setPrefix("o_"); 
    }
    
    public static void main(String argz[]){
    	// simple test
    	Customer c = new Customer(1,1, "a", "b", 1, "a", 1, "a"); //"n","s","s","n","s","m","s", "s"
    	Nation c_n = new Nation(1, 1, "FRA", 1, "");
    	Region c_r = new Region(1, 1, "ASIA");
    	Order o = new Order(1,1,1,'a',1,9001,"a","b",4,"t");  
    	// {"n","n","n","n","n","m","m", "m","s", "s", "d", "d", "d", "s", "s", "s"}; 
    	
    	FlatOrder x = new FlatOrder(1, o, c, c_n, c_r);
    	System.out.println(x); 
    	System.out.println(x.toJson(null));
    	System.out.println(x.toXML(null));
    	System.out.println(x.toLine(null));
    	System.out.println(x.toCSV(",", null));
    	
    }    
}
