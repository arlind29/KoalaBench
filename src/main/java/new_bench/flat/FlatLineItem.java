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
import new_bench.snow.LineItem;
import new_bench.snow.Nation;
import new_bench.snow.Order;
import new_bench.snow.Part;
import new_bench.snow.Region;
import new_bench.snow.Supplier;
import new_bench.types.MultiEntity;
import new_bench.types.EntityInstance;

public class FlatLineItem extends MultiEntity{
//    private final long rowNumber;
    //private final Part part; private final Supplier supplier;  private final PartSupplier partSupplier;
    //private final Nation nation; private final Region region; 
    private final static String relationName ="FlatLineItem"; 
    
    public static String partProjection[] = {"partKey", "name", "manufacturer", "brand", "type", "size", "container", "retailPrice"};
    public static String supplierProjection[] = {"supplierKey","name","address","phone"};
    public static String s_nationProjection[] = {"nation_name"};
    public static String s_regionProjection[] = {"region_name"};
    
    public static String custProjection[] = {"customerKey", "name","address","phone","marketSegment"}; // "customerKey","name","address","nationKey","phone","accountBalance","marketSegment","comment" 
    public static String c_nationProjection[] = {"nation_name"};
    public static String c_regionProjection[] = {"region_name"};
    
    public static String orderProjection[] = {"orderKey", "orderStatus","orderDate","orderPriority", "shipPriority"}; // "orderKey","customerKey","orderStatus","totalPrice","orderDate","orderPriority","clerk","shipPriority" 
    public static String lineItemProjection[] = {"lineNumber","quantity","extendedPrice","discount","tax","returnFlag","status","shipDate","commitDate","receiptDate","shipInstructions","shipMode"};
    
    // lineorder, order, star_customer
    // star_supplier, part, part_supplier
    // date 
    
    
    public FlatLineItem(long rowNumber,
    		LineItem lineItem, Order order,
    		Customer cust, Nation c_nation, Region c_region,
    		Part part, Supplier supplier, Nation s_nation, Region s_region 
    		)
    {
    	super(rowNumber, relationName, new EntityInstance[]{
    			lineItem.getEntity(), order.getEntity(),
    			cust.getEntity(), c_nation.getEntity(), c_region.getEntity(),
    			part.getEntity(), supplier.getEntity(), s_nation.getEntity(), s_region.getEntity()	
    			}); 
    	//this.part = part; this.supplier = supplier; this.partSupplier = partSupplier; this.nation=nation; this.region = region; 
    	part.setProjection(partProjection); 
    	supplier.setProjection(supplierProjection); 
    	s_nation.setProjection(s_nationProjection);
    	s_region.setProjection(s_regionProjection);
    	
    	//partSupplier.setProjection(partSupplierProjection);
    	order.setProjection(orderProjection);
    	lineItem.setProjection(lineItemProjection);
    	cust.setProjection(custProjection);
    	c_nation.setProjection(c_nationProjection);
    	c_region.setProjection(c_regionProjection);
    	// prefixes
    	cust.setPrefix("c_"); c_nation.setPrefix("c_"); c_region.setPrefix("c_");
    	supplier.setPrefix("s_"); s_nation.setPrefix("s_"); s_region.setPrefix("s_");
    	order.setPrefix("o_"); part.setPrefix("p_");
    }
    
    public static void main(String argz[]){
    	Part p = new Part(1,1,"a","a","a","a",1,"a",1,"x");
    	//PartSupplier ps = new PartSupplier(1,1,1,798,1,"a");
    	Supplier s = new Supplier(1, 1,"a", "bb", 6, "xp", 3, "aaa");
    	Nation s_n = new Nation(1, 1, "FRA", 1, "");
    	Region s_r = new Region(1, 1, "ASIA");
    	Customer c = new Customer(1,101, "a", "b", 1, "a", 1, "a"); //"n","s","s","n","s","m","s", "s"
    	Nation c_n = new Nation(1, 1, "FRA", 1, "");
    	Region c_r = new Region(1, 1, "ASIA");
    	Order o = new Order(1,1,1,'a',1,9001,"a","b",4,"t");  
    	LineItem li = new LineItem(1, 1, 1, 1, 1, 1, 1, 1, 1, "", "", 9001, 9001, 9001, "", "", ""); 
    	// {"n","n","n","n","n","m","m", "m","s", "s", "d", "d", "d", "s", "s", "s"}; 
    	
    	FlatLineItem x = new FlatLineItem(1, li, o, c, c_n, c_r, p, s, s_n, s_r);
    	System.out.println(x); 
    	
    	System.out.println();
    	for (String att: x.headers())
    		System.out.print(att + ", "); 
    	System.out.println();
    	
    }    
}
