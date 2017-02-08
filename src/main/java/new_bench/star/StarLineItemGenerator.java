package new_bench.star;

//import static new_bench.util.GenerateUtils.calculateRowCount;
import java.util.Iterator;

import new_bench.snow.LineItem;
import new_bench.snow.LineItemGenerator;
import new_bench.snow.LineItemGenerator.LineItemGeneratorIterator;
import new_bench.snow.Order;
import new_bench.snow.OrderGenerator;
import new_bench.snow.OrderGenerator.OrderGeneratorIterator;
import new_bench.types.Entity;

import com.google.common.collect.AbstractIterator;

public class StarLineItemGenerator implements Iterable<Entity> {
	int scaleFactor; 
	int step = 1; int children = 1; // default values 

	public StarLineItemGenerator(int scaleFactor){this.scaleFactor = scaleFactor;}
    
    @Override
    public Iterator<Entity> iterator()
    {
        return new StarLineItemGeneratorIterator(scaleFactor, step, children); 
    }

    private static class StarLineItemGeneratorIterator
    extends AbstractIterator<Entity>
    {
    	int step = 1; int children = 1; 
    	LineItemGeneratorIterator liIt;
    	OrderGeneratorIterator oIt;
		
    	public StarLineItemGeneratorIterator(int scaleFactor, int part, int partCount){ 

    		LineItemGenerator liGen = new LineItemGenerator(scaleFactor, step, children);
    		OrderGenerator oGen = new OrderGenerator(scaleFactor, step, children);

    		
    		liIt = (LineItemGeneratorIterator) liGen.iterator();
    		oIt = (OrderGeneratorIterator) oGen.iterator();
    	}
    	
        @Override
        protected Entity computeNext()
        {
                	
            if (!liIt.hasNext()) return endOfData();

            LineItem li = liIt.next(); 
            Order o = oIt.makeOrder(li.getOrderKey());  
            return new StarLineItem(li, o.getCustomerKey());
        }
    }    
    	
	public static void main(String[] args) {
		StarLineItemGenerator gen = new StarLineItemGenerator(1); 
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
		//System.out.println("rowCount: "+ calculateRowCount(OrderGenerator.SCALE_BASE, 1, 1, 1));
	}

}
