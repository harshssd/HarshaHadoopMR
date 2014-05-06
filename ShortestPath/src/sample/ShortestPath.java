package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ShortestPath extends Configured implements Tool{

	public static String outputFilePath = "/output";  
    public static String inputFilePath = "/inputlarger";  
	
	public static void main(String[] args) throws Exception {  
		System.exit(ToolRunner.run(new ShortestPath(), args));
    }
	
	  
    public int run(String[] args) throws Exception {  
    
    	//Key, Value is space separted
    	getConf().set("mapred.textoutputformat.separator", " ");  
  
    	
        String infile = inputFilePath;  
        //append output path with TimeStamp since multiple will be created
        String outputfile = outputFilePath + System.nanoTime();   
  
        boolean isdone = false;  
        boolean success = false;  
  
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();  
  
        while (!isdone) {  
  
            @SuppressWarnings("deprecation")
			Job job = new Job(getConf(), "Shortest Path");  
            job.setJarByClass(ShortestPath.class);  
            job.setOutputKeyClass(LongWritable.class);  
            job.setOutputValueClass(Text.class);  
            job.setMapperClass(ShortestPathMapper.class);  
            job.setReducerClass(ShortestPathReducer.class);  
            job.setInputFormatClass(TextInputFormat.class);  
            job.setOutputFormatClass(TextOutputFormat.class);  
  
            FileInputFormat.addInputPath(job, new Path(infile));  
            FileOutputFormat.setOutputPath(job, new Path(outputfile));  
  
            success = job.waitForCompletion(true);  
  
            if (!infile.equals(inputFilePath)) {  
                String indir = infile.replace("part-r-00000", "");  
                Path ddir = new Path(indir);  
                FileSystem dfs = FileSystem.get(getConf());  
                dfs.delete(ddir, true);  
            }  
  
            //Make the output of previous iteration as input
            infile = outputfile + "/part-r-00000";  
            //New output folder is created and appended with timestamp
            outputfile = outputFilePath + System.nanoTime();  
  
            isdone = true;//set the job to NOT run again!  
            Path ofile = new Path(infile);  
            FileSystem fs = FileSystem.get(new Configuration());  
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(ofile)));  
  
            HashMap<Integer, Integer> imap = new HashMap<Integer, Integer>();  
            String line = br.readLine();  
            while (line != null) {  
                //each line looks like 0 1 2:3:  
                //we need to verify node -> distance doesn't change  
                String[] sp = line.split(" ");  
                int node = Integer.parseInt(sp[0]);  
                int distance = Integer.parseInt(sp[1]);  
                imap.put(node, distance);  
                line = br.readLine();  
            }  
            if (map.isEmpty()) {    
                isdone = false;  
            } else {  
                for (Integer key : imap.keySet()) {  
                    int val = imap.get(key);  
                    if (map.get(key) != val) {  
                        //values aren't the same... we aren't at convergence yet  
                        isdone = false;  
                    }  
                }  
            }  
            if (!isdone) {  
                map.putAll(imap);//copy imap to map for the next iteration (if required)  
            }  
        }  
  
        return success ? 0 : 1;  
    }    
}