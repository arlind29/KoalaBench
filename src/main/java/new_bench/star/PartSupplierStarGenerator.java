package new_bench.star;

import java.util.Iterator;

import new_bench.snow.Nation;
import new_bench.snow.NationGenerator;
import new_bench.snow.Part;
import new_bench.snow.PartGenerator;
import new_bench.snow.PartSupplier;
import new_bench.snow.PartSupplierGenerator;
import new_bench.snow.Region;
import new_bench.snow.RegionGenerator;
import new_bench.snow.Supplier;
import new_bench.snow.SupplierGenerator;
import new_bench.snow.NationGenerator.NationGeneratorIterator;
import new_bench.snow.PartGenerator.PartGeneratorIterator;
import new_bench.snow.RegionGenerator.RegionGeneratorIterator;
import new_bench.snow.SupplierGenerator.SupplierGeneratorIterator;
import new_bench.types.Entity;

import com.google.common.collect.AbstractIterator;

public class PartSupplierStarGenerator implements Iterable<Entity> {
	int scaleFactor; 
	int step = 1; int children = 1; // default values 

	public PartSupplierStarGenerator(int scaleFactor){this.scaleFactor = scaleFactor;}
    
    @Override
    public Iterator<Entity> iterator()
    {

        return new PartSupplierStarGeneratorIterator(scaleFactor); 
    }

    private static class PartSupplierStarGeneratorIterator
    extends AbstractIterator<Entity>
    {
    	int step = 1; int children = 1; 
		SupplierGeneratorIterator suppIt; 
		PartGeneratorIterator partIt; 
		Iterator<PartSupplier> partSuppIt; 
		NationGeneratorIterator natIt; 
		RegionGeneratorIterator regIt; 

		long index = 0; long rowCount = 0 ; 

    	public PartSupplierStarGeneratorIterator(int scaleFactor){ 

    		SupplierGenerator suppGen = new SupplierGenerator(scaleFactor, step, children);	
    		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
    		PartSupplierGenerator partSuppGen = new PartSupplierGenerator(scaleFactor, step, children);
    		NationGenerator natGen = new NationGenerator(); 
    		RegionGenerator regGen = new RegionGenerator();
    		
    		suppIt = (SupplierGeneratorIterator) suppGen.iterator();
    		partIt = (PartGeneratorIterator) partGen.iterator();
    		partSuppIt = partSuppGen.iterator();
    		natIt = (NationGeneratorIterator) natGen.iterator();
    		regIt = (RegionGeneratorIterator) regGen.iterator();

    		rowCount = PartGenerator.SCALE_BASE * scaleFactor; 
    	}
    	
        @Override
        protected Entity computeNext()
        {
                	
            if (index >= rowCount) { return endOfData();  }
			PartSupplier partSupp = partSuppIt.next();
			Part part = partIt.makePart(partSupp.getPartKey());  
			Supplier supp = suppIt.makeSupplier(partSupp.getSupplierKey());
			Nation n = natIt.makeNation((int)supp.getNationKey());
			Region r = regIt.makeRegion((int)n.getRegionKey());
            index++;
            return new PartSupplierStar(index, part, supp, partSupp, n, r);
        }

    }    
    
	public void testGeneration(){
		SupplierGenerator suppGen = new SupplierGenerator(scaleFactor, step, children);	
		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
		PartSupplierGenerator partSuppGen = new PartSupplierGenerator(scaleFactor, step, children);
		NationGenerator natGen = new NationGenerator(); 
		RegionGenerator regGen = new RegionGenerator();
		SupplierGeneratorIterator suppIt = (SupplierGeneratorIterator) suppGen.iterator();
		PartGeneratorIterator partIt = (PartGeneratorIterator) partGen.iterator();
		Iterator<PartSupplier> partSuppIt = partSuppGen.iterator();
		NationGeneratorIterator natIt = (NationGeneratorIterator) natGen.iterator();
		RegionGeneratorIterator regIt = (RegionGeneratorIterator) regGen.iterator();
		
		for (int i=0; i<PartGenerator.SCALE_BASE * scaleFactor; i++){
			try{
				PartSupplier partSupp = partSuppIt.next();
				Part part = partIt.makePart(partSupp.getPartKey());  
				Supplier supp = suppIt.makeSupplier(partSupp.getSupplierKey());
				Nation n = natIt.makeNation((int)supp.getNationKey());
				Region r = regIt.makeRegion((int) n.getRegionKey());
				System.out.println(supp.toJson(null));
				System.out.println(part.toJson(null));
				System.out.println(partSupp.toJson(null));
				System.out.println(n.toJson(null));
				System.out.println(r.toJson(null));
				PartSupplierStar pss = new PartSupplierStar(i, part, supp, partSupp, n, r); 
				System.out.println("* "+ pss.toJson(null));
				
			}catch(Exception e){System.out.println("Warning: "+ e.getLocalizedMessage()); e.printStackTrace();}
			//Part part = partIt.next(); 
			
			if (i > 3) return;
			System.out.println(); 
		}
	}
	
	public static void main(String[] args) {
		PartSupplierStarGenerator psgen = new PartSupplierStarGenerator(1); 
		psgen.testGeneration();
		System.out.println("-----"); 
		/*EntityPrinter p = new EntityPrinter();		
		PartSupplierStarGenerator gen = new PartSupplierStarGenerator(1);
		for (TpchEntity entity : gen) {
            System.out.println(entity.toJson()); 
        }*/		        	

		//p.print(custGen, "data_gen/star_partsupplier", "json"); 

	}

}
