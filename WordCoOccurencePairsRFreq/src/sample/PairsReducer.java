package sample;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PairsReducer extends Reducer<WordPair,IntWritable,WordPair,DoubleWritable> {
	private DoubleWritable totalCount = new DoubleWritable();
	private DoubleWritable relativeFreq = new DoubleWritable();
	private Text currentWord = new Text("NOT_SET"); 
	private Text specialWord = new Text("*");
	
	@Override
	protected void reduce(WordPair pair, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		
		if(pair.getNeighbor().equals(specialWord)){
			if(pair.getWord().equals(currentWord)){
				totalCount.set(totalCount.get() + getTotalCount(values));
			}else{
				currentWord.set(pair.getWord());
				totalCount.set(0);
				totalCount.set(getTotalCount(values));
			}
		}else{
			int count = getTotalCount(values);
			relativeFreq.set((double)count/totalCount.get());
			context.write(pair, relativeFreq);
		}
		
	}

	private int getTotalCount(Iterable<IntWritable> values) {
		int count = 0;
		for (IntWritable value : values) {
			count += value.get();
		}
		return count;
	}
}