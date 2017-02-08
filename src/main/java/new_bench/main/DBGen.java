package new_bench.main;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import new_bench.flat.FlatLineItemGenerator;
import new_bench.flat.FlatOrderGenerator;
import new_bench.flat.Sparse1OrderGenerator;
import new_bench.snow.*;
import new_bench.star.StarCustomerGenerator;
import new_bench.star.StarLineItemGenerator;
import new_bench.star.StarSupplierGenerator;
import new_bench.util.EntityPrinter;
import new_bench.util.SchemaFilters;

public class DBGen{
	
	Vector<String> options = new Vector<String>();
	double scaleFactor = 0; 
	String format = "tab"; int noFormatOptions= 0;
	String model = "snow"; int noModelOptions= 0;
	int step = 1; int children = 1; // default values 
	boolean printToHdfs = false; 
	
	EntityPrinter p; 
	String genDataDir="data_gen"; 
	static String sep = File.separator; 
	 

	public DBGen(String[] argz){
		SchemaFilters filters = new SchemaFilters();
		for (String arg: argz) { 
			options.add(arg.trim().toLowerCase());
			// scale factor
			if (arg.startsWith("sf")) 
				try{ scaleFactor = Double.parseDouble(arg.substring(2));}catch(Exception e){System.out.println("ERROR: wrong scale factor format"); e.printStackTrace();}
			// File location directory or hdfs directory
			if (arg.startsWith(">")) {
				if (arg.startsWith(">hdfs://")){
					printToHdfs = true; 
				}
				this.genDataDir = arg.substring(1);  
				this.genDataDir = this.genDataDir.trim().replaceAll("\\"+File.separator+"*$", "");
			}			
			
			if (arg.startsWith("filter-file=")) {
				filters = SchemaFilters.readFromFile(arg.substring(12));
			}
		}
		 			
		// FORMAT
		if (options.contains("json")) {format = "json"; noFormatOptions++; }
		if (options.contains("xml")) {format = "xml"; noFormatOptions++; }
		if (options.contains("csv")) {format = "csv"; noFormatOptions++; }
		if (options.contains("tab")) {format = "tab"; noFormatOptions++; }
		if (options.contains("elastic_search_json")) {format = "elastic_search_json"; noFormatOptions++; }
		// check on format options
		if (noFormatOptions>1) {System.out.println("ERROR: Multiple format options");  return;}
		// MODEL 
		if (options.contains("snow")) {model = "snow"; noModelOptions++; }
		if (options.contains("star")) {model = "star"; noModelOptions++; }
		if (options.contains("flat")) {model = "flat"; noModelOptions++; }
		if (options.contains("flat_order")) {model = "flat_order"; noModelOptions++; }
		if (options.contains("sparse")) {model = "sparse"; noModelOptions++; }
		// check on model options
		if (noModelOptions>1) {System.out.println("ERROR: Multiple model options");  return;}
		
		 p = new EntityPrinter(filters);
		
	}
	
	public void generate(){
		
		System.out.println("Generating data with sf="+scaleFactor); 		
		System.out.println("Data generation started");
		System.out.println("Data model: "+model);
		System.out.println("Data format: "+format);
		System.out.println("Scale factor: "+scaleFactor);
		System.out.println("..."); 
		
		this.scaleFactor = 0.001; 
		
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
		StarCustomerGenerator custGen = new StarCustomerGenerator((int) scaleFactor);
		p.print(custGen, genDataDir+sep+"star_customer", format); 
		
		PartGenerator partGen = new PartGenerator(scaleFactor, step, children);
		p.print(partGen, genDataDir+sep+"star_part", format); 

		StarSupplierGenerator psGen = new StarSupplierGenerator( (int) scaleFactor);
		p.print(psGen, genDataDir+sep+"star_supplier", format); 
		
		StarLineItemGenerator lineItemGen = new StarLineItemGenerator((int) scaleFactor);
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
		FlatOrderGenerator orderGen = new FlatOrderGenerator((int) scaleFactor);
		p.print(orderGen, genDataDir+sep+"flat_order", format);
		System.out.println("\n...\nData generation ended");
	}

	private void generateSparse(){
		if (format.equals("tab")||format.equals("csv")) {
			System.out.println("ERROR: Unsupported format for sparse order data generation, format: "+ format);  
			return;} 
		Sparse1OrderGenerator sparseOrderGen = new Sparse1OrderGenerator((int) scaleFactor); 
		p.print(sparseOrderGen, genDataDir+sep+"sparse_order", format);
		System.out.println("\n...\nData generation ended");
	}

	public static void main(String argz[]){	
		DBGen gen = new DBGen(argz);
		gen.generate();
	}
}
