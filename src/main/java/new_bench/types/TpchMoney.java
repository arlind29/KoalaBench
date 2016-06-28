package new_bench.types;

import static new_bench.util.GenerateUtils.formatMoney;

public class TpchMoney {

	long price;  
	
	public TpchMoney(long price){this.price =price; }
	
	public String toString(){ return formatMoney(price);  }
	
	public long getValue(){return price; } 
	
	public static void main(String[] args) {

	}

}
