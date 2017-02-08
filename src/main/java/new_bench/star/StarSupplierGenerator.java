package new_bench.star;

import java.util.Iterator;

import new_bench.snow.Supplier;
import new_bench.snow.SupplierGenerator;
import new_bench.snow.SupplierGenerator.SupplierGeneratorIterator;
import new_bench.snow.Nation;
import new_bench.snow.NationGenerator;
import new_bench.snow.Region;
import new_bench.snow.RegionGenerator;
import new_bench.snow.NationGenerator.NationGeneratorIterator;
import new_bench.snow.RegionGenerator.RegionGeneratorIterator;
import new_bench.types.Entity;

import com.google.common.collect.AbstractIterator;

public class StarSupplierGenerator implements Iterable<Entity> {
	int scaleFactor; 
	int step = 1; int children = 1; // default values 

	public StarSupplierGenerator(int scaleFactor){this.scaleFactor = scaleFactor;}
    
    @Override
    public Iterator<Entity> iterator()
    {
        return new StarSupplierGeneratorIterator(scaleFactor, step, children); 
    }

    private static class StarSupplierGeneratorIterator
    extends AbstractIterator<Entity>
    {
    	int step = 1; int children = 1; 
    	SupplierGeneratorIterator sIt;
    	NationGeneratorIterator nIt;
    	RegionGeneratorIterator rIt;
		
    	public StarSupplierGeneratorIterator(int scaleFactor, int part, int partCount){ 

    		SupplierGenerator sGen = new SupplierGenerator(scaleFactor, step, children);
    		NationGenerator nGen = new NationGenerator();
    		RegionGenerator rGen = new RegionGenerator();
    		    		
    		sIt = (SupplierGeneratorIterator) sGen.iterator();
    		nIt = (NationGeneratorIterator) nGen.iterator();
    		rIt = (RegionGeneratorIterator) rGen.iterator();
    	}
    	
        @Override
        protected Entity computeNext()
        {
                	
            if (!sIt.hasNext()) return endOfData();

            Supplier supp = sIt.next(); 
            Nation nat = nIt.makeNation((int) supp.getNationKey());
            Region reg = rIt.makeRegion((int) nat.getRegionKey());
            return new StarSupplier(supp, nat.getName(), reg.getName());
        }
    }    
    	
	public static void main(String[] args) {
		StarSupplierGenerator gen = new StarSupplierGenerator(1); 
		System.out.println("-----"); 
		int count = 0; 
		//EntityPrinter p = new EntityPrinter();		
		for (Entity entity : gen) {
            System.out.println(entity.toJson(null));
			//long rnum = entity.getRowNumber();
            //if (rnum%100000 ==0) System.out.println(rnum);
            //p.print(custGen, "star_partsupplier", "json");
            count++; 
            if (count>200) break; 
        }		        	
	}

}
