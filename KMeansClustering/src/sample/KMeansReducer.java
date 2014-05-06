package sample;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KMeansReducer 
extends Reducer<IntWritable,Text,IntWritable,Text> {
	
	public int iteration = 0;
	
	public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
     
	//input will be the centroid as key and the user specification as value
	//key = 50; value = uowusers 17 low	
	
	//Average the number of followers for each centroid and emit new centroid
		int totalFollowers = 0;
		int count = 0;
		String[] line;
		
		for(Text value: values){
			line = value.toString().split(" ");
			totalFollowers += Integer.parseInt(line[1]);
			++count;
		}
		System.out.print("TotalFollowers " +totalFollowers);
		int newCentroid = totalFollowers/count;
		
		for(Text value: values){
			context.write(new IntWritable(newCentroid), value);
		}
		
		String centroid = KMeans.readCentroidFile(new Path(KMeans.centroidFile), KMeans.conf);
		String[] centroids = centroid.split(" ");
		
		if(iteration == 0){
			String centroidFileContent = newCentroid+" "+centroids[1]+" "+centroids[2];
			KMeans.writeToCentroidFile(centroidFileContent, KMeans.conf, new Path(KMeans.centroidFile)); 
		}else if(iteration == 1){
			String centroidFileContent = centroids[0]+" "+newCentroid+" "+centroids[2];
			KMeans.writeToCentroidFile(centroidFileContent, KMeans.conf, new Path(KMeans.centroidFile));
		}else{
			String centroidFileContent = centroids[0]+" "+centroids[1]+" "+newCentroid;
			KMeans.writeToCentroidFile(centroidFileContent, KMeans.conf, new Path(KMeans.centroidFile));
		}

		++iteration;
	}
	
}
