package sample;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ShortestPathMapper extends Mapper<LongWritable, Text, LongWritable, Text> {  
	  
    public void map(LongWritable key, Text value, Context context)  
            throws IOException, InterruptedException {  

    	Text word = new Text();  
        String line = value.toString();//1 0 2:3:  
        String[] input = line.split(" ");//input[0] = 1, input[0] = 0, input[0] = 2:3:   
        int adjacentDist = Integer.parseInt(input[1]) + 1;  
        String[] adjacentNodes = input[2].split(":");//splits the list of adjacent nodes  
        for (String adj : adjacentNodes) {  
        	//Set the distance of adjacent nodes
            word.set("VALUE " + adjacentDist);  
            //Emit adjacent nodes and its distances
            context.write(new LongWritable(Integer.parseInt(adj)), word);  
            word.clear();  
        }  
        
        //Set the distance of current node
        word.set("VALUE " + input[1]);  
        //Emit current node and its distance from source
        context.write(new LongWritable(Integer.parseInt(input[0])), word);  
        word.clear();  

        //Set the list of adjacent nodes
        word.set("NODES " + input[2]);  
        //Emit current node and its adjacent nodes
        context.write(new LongWritable(Integer.parseInt(input[0])), word);  
        word.clear();  

    }  
}  
