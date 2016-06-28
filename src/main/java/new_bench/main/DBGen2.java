package new_bench.main;

import java.io.File;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import new_bench.flat.FlatLineItemGenerator;
import new_bench.flat.FlatOrderGenerator;
import new_bench.flat.Sparse1OrderGenerator;
import new_bench.snow.*;
import new_bench.star.StarCustomerGenerator;
import new_bench.star.StarLineItemGenerator;
import new_bench.star.StarSupplierGenerator;
import new_bench.util.EntityPrinter;

public class DBGen2{
	
	Vector<String> options = new Vector<String>();
	int scaleFactor = 0; 
	String format = "tab"; int noFormatOptions= 0;
	String model = "flat"; int noModelOptions= 0;
	int step = 1; int children = 1; // default values
	boolean printToHdfs = false; 
	
	EntityPrinter p = new EntityPrinter(); 
	String genDataDir="data_gen"; 
	static String sep = File.separator; 
	

	public DBGen2(String[] argz){
		String params = String.join(" ", argz); 
		
		Pattern paramPattern = Pattern.compile("\\s*\\w+\\s*=\\s*(\\w|\\d)+");
		Matcher m = paramPattern.matcher(params);

		while (m.find()) {
			String arg = m.group(); //System.out.println("Param: " + m.group() + "");
			int equalIndex = arg.indexOf('='); //	System.out.println("index: "+equalIndex);
			
			String argName = arg.substring(0, equalIndex).trim().toLowerCase();
			String argValue = arg.substring(equalIndex+1).trim();
			//System.out.println("    "+ argName + ": "+ argValue);
		
			if (argName.equals("sf")) 
				try{ scaleFactor = Integer.parseInt(argValue);}catch(Exception e){System.out.println("ERROR: wrong scale factor format"); e.printStackTrace();}
			// File location directory or hdfs directory
			if (argName.equals("destination")) {
				if (argValue.startsWith("hdfs://")){
					printToHdfs = true; 
				}
				this.genDataDir = argValue;  
				this.genDataDir = this.genDataDir.trim().replaceAll("\\"+File.separator+"*$", "");
			}
			
			if (argName.equals("format")) {format = argValue; noFormatOptions++; }
			if (argName.equals("model")) {model = argValue; noModelOptions++; }
		}
		 			
		if (noFormatOptions>1) {System.out.println("ERROR: Multiple format options");  return;}
		if (noModelOptions>1) {System.out.println("ERROR: Multiple model options");  return;}

	}
	
	public void generate(){
		System.out.println("Generating data with sf="+scaleFactor); 		
		System.out.println("Data generation started");
		System.out.println("Data model: "+model);
		System.out.println("Data format: "+format);
		System.out.println("Scale factor: "+scaleFactor);
		System.out.println("..."); 
		
		if (true) System.exit(-1); 

		
		switch (model){
			case "snow": generateSnow(); break;
			case "star": generateStar(); break;
			case "flat": generateFlat(); break;
			case "flat_order": generateFlatOrder(); break;
			case "sparse": generateSparse(); break;
		}
	}
	
	private void generateSnow(){
		
		CustomerGenerator custGen = new CustomerGenerator(scaleFactor, step, children);
		p.print(custGen, genDataDir+sep+"snow_customer", format); 

		SupplierGenerator suppGen = new SupplierGenerator(scaleFactor, step, children);
		p.print(suppGen, genDataDir+sep+"snow_supplier", format); 
		
		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
		p.print(partGen, genDataDir+sep+"snow_part", format); 

		NationGenerator nGen = new NationGenerator();
		p.print(nGen, genDataDir+sep+"snow_nation", format); 
		
		RegionGenerator rGen = new RegionGenerator();
		p.print(rGen, genDataDir+sep+"snow_region", format); 

		PartSupplierGenerator psGen = new PartSupplierGenerator(scaleFactor, step, children);
		p.print(psGen, genDataDir+sep+"snow_part_supplier", format); 
		
		OrderGenerator orderGen = new OrderGenerator(scaleFactor, step, children);
		p.print(orderGen, genDataDir+sep+"snow_order", format); 

		LineItemGenerator lineItemGen = new LineItemGenerator(scaleFactor, step, children);
		p.print(lineItemGen, genDataDir+sep+"snow_line_item", format);
		
		ExtDateGenerator dateGen = new ExtDateGenerator(); 
		p.print(dateGen, genDataDir+sep+"snow_date", format);
		
		System.out.println("\n...\nData generation ended");
	}
	
	private void generateStar(){
		StarCustomerGenerator custGen = new StarCustomerGenerator(scaleFactor);
		p.print(custGen, genDataDir+sep+"star_customer", format); 
		
		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
		p.print(partGen, genDataDir+sep+"star_part", format); 

		StarSupplierGenerator psGen = new StarSupplierGenerator(scaleFactor);
		p.print(psGen, genDataDir+sep+"star_supplier", format); 
		
		StarLineItemGenerator lineItemGen = new StarLineItemGenerator(scaleFactor);
		p.print(lineItemGen, genDataDir+sep+"star_line_item", format);
		
		ExtDateGenerator dateGen = new ExtDateGenerator(); 
		p.print(dateGen, genDataDir+sep+"snow_date", format);
		
		System.out.println("\n...\nData generation ended");
	}	
	
	private void generateFlat(){
		FlatLineItemGenerator lineItemGen = new FlatLineItemGenerator(scaleFactor);
		p.print(lineItemGen, genDataDir+sep+"flat_line_item", format);
		System.out.println("\n...\nData generation ended");
	}

	private void generateFlatOrder(){
		FlatOrderGenerator orderGen = new FlatOrderGenerator(scaleFactor);
		p.print(orderGen, genDataDir+sep+"flat_order", format);
		System.out.println("\n...\nData generation ended");
	}

	private void generateSparse(){
		if (format.equals("tab")||format.equals("csv")) {
			System.out.println("ERROR: Unsupported format for sparse order data generation, format: "+ format);  
			return;} 
		Sparse1OrderGenerator sparseOrderGen = new Sparse1OrderGenerator(scaleFactor); 
		p.print(sparseOrderGen, genDataDir+sep+"sparse_order", format);
		System.out.println("\n...\nData generation ended");
	}

	public static void main(String argz[]){	
		argz = new String[3]; argz[0]="sf=1 format=json model=flat"; 
		DBGen2 gen = new DBGen2(argz);
		gen.generate();
	}
}
