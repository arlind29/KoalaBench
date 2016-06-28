package new_bench.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
//import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import new_bench.types.Entity;

public class EntityPrinter {

	
	
	
	
	public void print(Iterable<? extends Entity> entities, String filename, String format){
		if (filename.startsWith("hdfs://")) printToHdfs(entities, filename, format); 
        BufferedWriter bw ;
        
        String ext = format; if (format.equals("elastic_search_json")) ext = "json";  
		try{
			bw = new BufferedWriter(new FileWriter(filename+"."+ext)); 
    		for (Entity entity : entities) {
                bw.write(printByFormat(entity, format)); 
                bw.write('\n');
            }		        	
    		bw.close(); 
        }catch(Exception e){e.printStackTrace();} 
	}
	
	private void printToHdfs(Iterable<? extends Entity> entities, String hdfsUrl, String format){
		if (!hdfsUrl.startsWith("hdfs:")) {
			System.out.println("ERROR: HDFS URL is incorrect: "+hdfsUrl);
			System.exit(-1);
		}
		try{
            Path pt=new Path(hdfsUrl);
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.create(pt,true)));
                                       // TO append data to a file, use fs.append(Path f)
    		for (Entity entity : entities) {
                br.write(printByFormat(entity, format)); 
                br.write('\n');
    		}
            br.close();
		}catch(Exception e){
            System.out.println("File not found");
		}			
	}

	
	
	
	public String printByFormat(Entity entity, String format){
		if (format=="json") return entity.toJson(); 
		if (format=="csv") return entity.toCSV(",");
		if (format=="xml") return entity.toXML();
		if (format == "elastic_search_json") return entity.toElasticSearchJson(); 
		return entity.toLine();
	}
	
}
