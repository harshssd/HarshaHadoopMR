package sample;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PairsMapper extends Mapper<LongWritable, Text, WordPair, IntWritable>{

	private WordPair wordPair = new WordPair();
	private IntWritable ONE = new IntWritable(1);
	private IntWritable totalCount = new IntWritable();

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split("\\s+");
		
		if (tokens.length > 1) {
			for (int i = 0; i < tokens.length-1; i++) {
				tokens[i] = tokens[i].replaceAll("\\W+","");
				if(tokens[i].equals("")){
					continue;
				}
				wordPair.setWord(tokens[i]);
				for (int j = i+1; j < tokens.length; j++) {
					wordPair.setNeighbor(tokens[j].replaceAll("\\W",""));
					context.write(wordPair, ONE);
				}
				wordPair.setNeighbor("*");
				totalCount.set(tokens.length-i-1);
				context.write(wordPair, totalCount);
			}
		}
	}
}