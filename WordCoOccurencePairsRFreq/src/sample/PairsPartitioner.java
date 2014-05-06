package sample;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class PairsPartitioner extends Partitioner<WordPair, IntWritable>{

	@Override
	public int getPartition(WordPair pair, IntWritable count, int num) {
		return pair.getWord().hashCode() % num;
	}

}
