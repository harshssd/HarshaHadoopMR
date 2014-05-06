package sample;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SortMapper extends Mapper<Object, Text, IntWritable, Text>{
	
	public static IntWritable count = new IntWritable(0);
	public static int intCount = 0;
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		System.out.println(value);
		count.set(intCount);
		context.write(count, value);
		intCount++;
	}
}