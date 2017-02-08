package new_bench.flat;

import static new_bench.util.GenerateUtils.calculateRowCount;

import java.util.Iterator;
import java.util.Vector;

import new_bench.snow.Customer;
import new_bench.snow.CustomerGenerator;
import new_bench.snow.CustomerGenerator.CustomerGeneratorIterator;
import new_bench.snow.LineItem;
import new_bench.snow.LineItemGenerator;
import new_bench.snow.LineItemGenerator.LineItemGeneratorIterator;
import new_bench.snow.Nation;
import new_bench.snow.NationGenerator;
import new_bench.snow.Order;
import new_bench.snow.OrderGenerator;
import new_bench.snow.OrderGenerator.OrderGeneratorIterator;
import new_bench.snow.Part;
import new_bench.snow.PartGenerator;
import new_bench.snow.Region;
import new_bench.snow.RegionGenerator;
import new_bench.snow.Supplier;
import new_bench.snow.SupplierGenerator;
import new_bench.snow.NationGenerator.NationGeneratorIterator;
import new_bench.snow.PartGenerator.PartGeneratorIterator;
import new_bench.snow.RegionGenerator.RegionGeneratorIterator;
import new_bench.snow.SupplierGenerator.SupplierGeneratorIterator;
import new_bench.types.ArrayEntityInstance;
import new_bench.types.Entity;
import new_bench.types.KeyValue;

import com.google.common.collect.AbstractIterator;

public class Sparse1OrderGenerator implements Iterable<Entity> {
	int scaleFactor; 
	int step = 1; int children = 1; // default values 

	public Sparse1OrderGenerator(int scaleFactor){this.scaleFactor = scaleFactor;}
    
    @Override
    public Iterator<Entity> iterator()
    {
        return new Sparse1OrderGeneratorIterator(scaleFactor, step, children); 
    }

    public static class Sparse1OrderGeneratorIterator
    extends AbstractIterator<Entity>
    {
    	int step = 1; int children = 1; 
    	LineItemGeneratorIterator liIt;
    	OrderGeneratorIterator oIt;
		PartGeneratorIterator partIt; 
		//Iterator<PartSupplier> partSuppIt; 
		NationGeneratorIterator c_natIt; 
		RegionGeneratorIterator c_regIt; 
		CustomerGeneratorIterator custIt;
		
    	public Sparse1OrderGeneratorIterator(int scaleFactor, int part, int partCount){ 

    		LineItemGenerator liGen = new LineItemGenerator(scaleFactor, step, children);
    		OrderGenerator oGen = new OrderGenerator(scaleFactor, step, children);
    		CustomerGenerator cGen = new CustomerGenerator(scaleFactor, step, children);
    		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
    		NationGenerator c_natGen = new NationGenerator(); 
    		RegionGenerator c_regGen = new RegionGenerator();

    		
    		liIt = (LineItemGeneratorIterator) liGen.iterator();
    		oIt = (OrderGeneratorIterator) oGen.iterator();
    		
    		partIt = (PartGeneratorIterator) partGen.iterator();

    		custIt = (CustomerGeneratorIterator) cGen.iterator(); 
    		c_natIt = (NationGeneratorIterator) c_natGen.iterator();
    		c_regIt = (RegionGeneratorIterator) c_regGen.iterator();
    	}
    	
        @Override
        protected Entity computeNext()
        {
                	
            if (!liIt.hasNext()) return endOfData();

            LineItem li = liIt.next(); 
            Order o = oIt.makeOrder(li.getOrderKey());
            Customer cust = custIt.makeCustomer(o.getCustomerKey()); 
            Nation c_nat = c_natIt.makeNation((int) cust.getNationKey());
            Region c_reg = c_regIt.makeRegion((int) c_nat.getRegionKey());
            
            Vector<KeyValue> keyValues = new Vector<KeyValue>();  
            
        	Part part = partIt.makePart(li.getPartKey());
        	keyValues.add(new KeyValue("part_"+part.getPartKey() + "", "" + li.getQuantity(), "n"));
             
            while (! liIt.willSwitchToNewOrder()){
                li = liIt.next(); 
            	part = partIt.makePart(li.getPartKey());
            	keyValues.add(new KeyValue("part_"+part.getPartKey() + "", "" + li.getQuantity(), "n"));
            }
            // copy to array
            KeyValue[] kv = new KeyValue[keyValues.size()]; 
            for (int i=0; i<keyValues.size(); i++){kv[i] = keyValues.elementAt(i);}
            
        	ArrayEntityInstance quantitiesPerPart 
        		= new ArrayEntityInstance("quantity_per_item", kv); 

            return new FlatOrderWithPartNames(o.getRowNumber(), o, cust, c_nat, c_reg, quantitiesPerPart);
        }
    }    
    	
	public static void main(String[] args) {
		Sparse1OrderGenerator gen = new Sparse1OrderGenerator(1); 
		System.out.println("-----"); 
		int count = 0; 
		//EntityPrinter p = new EntityPrinter();		
		for (Entity entity : gen) {
            System.out.println(entity.toJson(null));
			//long rnum = entity.getRowNumber();
            //if (rnum%100000 == 0) System.out.println(rnum);
            //p.print(custGen, "star_partsupplier", "json");
            count++; 
            if (count>2) break; 
        }		        	
		System.out.println("rowCount: "+ calculateRowCount(OrderGenerator.SCALE_BASE, 1, 1, 1));
	}

}
