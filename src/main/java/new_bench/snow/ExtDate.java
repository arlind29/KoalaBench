package new_bench.snow;

import java.util.Calendar; 

import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.util.GenerateUtils;
import static java.util.Locale.ENGLISH;
import static new_bench.util.GenerateUtils.formatDate;

/*
 * This is a date in an extended format, more similar to decision support uses
 */
public class ExtDate extends AbstractEntity{
		
	/* as appearing in SSB
	 * DATE Table Layout (7 years of days)
	 * DATEKEY identifier, unique id -- e.g. 19980327 (what we use)
     * DATE fixed text, size 18: e.g. December 22, 1998 
     * DAYOFWEEK fixed text, size 8, Sunday..Saturday 
     * MONTH fixed text, size 9: January, ..., December 
     * YEAR unique value 1992-1998 D_YEARMONTHNUM numeric (YYYYMM) 
     * YEARMONTH fixed text, size 7: (e.g.: Mar1998 
     * DAYNUMINWEEK numeric 1-7 D_DAYNUMINMONTH numeric 1-31 
     * DAYNUMINYEAR numeric 1-366 D_MONTHNUMINYEAR numeric 1-12 
     * WEEKNUMINYEAR numeric 1-53 D_SELLINGSEASON text, size 12 (e.g.: Christmas) 
     * LASTDAYINWEEKFL 1 bit 
     * LASTDAYINMONTHFL 1 bit
	 * HOLIDAYFL 1 bit
	 * WEEKDAYFL 1 bit
	 * Primary Key: D_DATEKEY 
	*/
	
	Calendar calendar; 
	
    public static String headers[] = {"date","month","year","yearMonthNum",
    						"yearMonth","dayNumInWeek","dayNumInMonth", "dayNumInYear", 
    						"monthNumInYear", "weekNumInYear"}; 
    public static String types[] = {"s","s","s","s","s","s", "s", "s", "s", "s"}; 	

    
	public ExtDate(Calendar calendar){
		super(0);
    	this.relationName = "date";

		//calendar = (Calendar) GenerateUtils.MIN_DATE.clone();  //calendar.add(Calendar.DATE, dateIndex);
		
    	String[] values = new String[10];
		 
    	values[0] = String.format(ENGLISH, "%04d-%02d-%02d", 
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)); 
    	values[1] = "" + calendar.get(Calendar.MONTH);
    	values[2] = "" + calendar.get(Calendar.YEAR);
    	values[3] = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
    	values[4] = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
    	values[5] = "" + calendar.get(Calendar.DAY_OF_WEEK);
    	values[6] = "" + calendar.get(Calendar.DAY_OF_MONTH);
    	values[7] = "" + calendar.get(Calendar.DAY_OF_YEAR);
    	values[8] = "" + calendar.get(Calendar.MONTH);
    	values[9] = "" + calendar.get(Calendar.WEEK_OF_YEAR);
    	//values[8] = "" + calendar.get(Calendar.g);
    	entity = new EntityInstance(relationName, headers, types, values);
	}		
	
	public String toString(){ return String.format(ENGLISH, "%04d-%02d-%02d", 
			calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));  }

	public static void main(String argz[]){
		Calendar calendar = (Calendar) GenerateUtils.MIN_DATE.clone();  
		calendar.add(Calendar.DATE, 0);

		ExtDate dt = new ExtDate(calendar); 
		System.out.println(dt.toJson(null));

		calendar = (Calendar) GenerateUtils.MIN_DATE.clone();  
		calendar.add(Calendar.DATE, 2556);
		
		dt = new ExtDate(calendar); 
		System.out.println(dt.toJson(null));
	}
}
