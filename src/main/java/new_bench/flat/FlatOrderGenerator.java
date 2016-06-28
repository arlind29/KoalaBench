package new_bench.flat;

import static new_bench.util.GenerateUtils.calculateRowCount;

import java.util.Iterator;

import new_bench.snow.Customer;
import new_bench.snow.CustomerGenerator;
import new_bench.snow.CustomerGenerator.CustomerGeneratorIterator;
import new_bench.snow.Nation;
import new_bench.snow.NationGenerator;
import new_bench.snow.Order;
import new_bench.snow.OrderGenerator;
import new_bench.snow.OrderGenerator.OrderGeneratorIterator;
import new_bench.snow.NationGenerator.NationGeneratorIterator;
import new_bench.snow.Region;
import new_bench.snow.RegionGenerator;
import new_bench.snow.RegionGenerator.RegionGeneratorIterator;
import new_bench.types.Entity;

import com.google.common.collect.AbstractIterator;

public class FlatOrderGenerator implements Iterable<Entity> {
	int scaleFactor; 
	int step = 1; int children = 1; // default values 

	public FlatOrderGenerator(int scaleFactor){this.scaleFactor = scaleFactor;}

    
    @Override
    public Iterator<Entity> iterator()
    {

        return new PartSupplierStarGeneratorIterator(scaleFactor, step, children); 
    }

    private static class PartSupplierStarGeneratorIterator
    extends AbstractIterator<Entity>
    {
    	int step = 1; int children = 1; 
    	OrderGeneratorIterator oIt;
		//Iterator<PartSupplier> partSuppIt; 
		CustomerGeneratorIterator custIt;
		NationGeneratorIterator c_natIt; 
		RegionGeneratorIterator c_regIt; 
		
    	public PartSupplierStarGeneratorIterator(int scaleFactor, int part, int partCount){ 

    		OrderGenerator oGen = new OrderGenerator(scaleFactor, step, children);
    		CustomerGenerator cGen = new CustomerGenerator(scaleFactor, step, children);
    		NationGenerator c_natGen = new NationGenerator(); 
    		RegionGenerator c_regGen = new RegionGenerator();
    		
    		oIt = (OrderGeneratorIterator) oGen.iterator();
    		custIt = (CustomerGeneratorIterator) cGen.iterator(); 
    		c_natIt = (NationGeneratorIterator) c_natGen.iterator();
    		c_regIt = (RegionGeneratorIterator) c_regGen.iterator();

    		//rowCount = calculateRowCount(OrderGenerator.SCALE_BASE, scaleFactor, part, partCount);  
    	}
    	
        @Override
        protected Entity computeNext()
        {
                	
            if (!oIt.hasNext()) return endOfData(); 
            Order o = oIt.next();  
            Customer cust = custIt.makeCustomer(o.getCustomerKey()); 
            Nation c_nat = c_natIt.makeNation((int) cust.getNationKey());
            Region c_reg = c_regIt.makeRegion((int) c_nat.getRegionKey());
            return new FlatOrder(o.getRowNumber(), o, cust, c_nat, c_reg);
        }

    }    
    	
	public static void main(String[] args) {
		// simple test
		FlatOrderGenerator gen = new FlatOrderGenerator(1); 
		System.out.println("-----"); 
		//int count = 0;  
		//EntityPrinter p = new EntityPrinter();		
		for (Entity entity : gen) {
            //System.out.println(entity.toJson());
            if (entity.getRowNumber()%100000 ==0) System.out.println(entity.getRowNumber());
            //p.print(custGen, "star_partsupplier", "json");
            // count++; 
            //if (count>20) break; 
        }		        	

		System.out.println("rowCount: "+ calculateRowCount(OrderGenerator.SCALE_BASE, 1, 1, 1)); 
	}

}
