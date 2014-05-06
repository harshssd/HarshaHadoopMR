package sample;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PairsMapper extends Mapper<LongWritable, Text, WordPair, IntWritable>{

	private WordPair wordPair = new WordPair();
	private IntWritable ONE = new IntWritable(1);

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//		int neighbors = context.getConfiguration().getInt("neighbors", 2);
		String[] tokens = value.toString().split("\\s+");
		
		if (tokens.length > 1) {
			for (int i = 0; i < tokens.length-1; i++) {
				wordPair.setWord(tokens[i]);
				for (int j = i+1; j < tokens.length; j++) {
					wordPair.setNeighbor(tokens[j]);
					context.write(wordPair, ONE);
				}

			}
		}
	}

}
