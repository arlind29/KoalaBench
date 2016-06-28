package new_bench.types;

import static new_bench.util.GenerateUtils.formatDate;

public class TpchDate {

	int date;  
    //public static final int MIN_GENERATE_DATE = 92001;

	public TpchDate(int date){this.date =date; 
	   //if (date<MIN_GENERATE_DATE) {  System.out.println("ERROR: date value should be more than "+MIN_GENERATE_DATE);  System.exit(-1);	   }
	}
	
	public String toString(){ return formatDate(date);  }
	
	public int getValue(){return date; } 
	
	public static void main(String[] args) {

	}

}
