package sample;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TokenizerMapper extends Mapper<Object, Text, IntWritable, Text>{

//	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	//	StringTokenizer itr = new StringTokenizer(value.toString());
		IntWritable count = new IntWritable(1);
		String[] str = value.toString().split("\\s");
		if(str[0]!=null){
			word.set(str[0]);
		}
		if(str[1]!=null){
			count.set(Integer.parseInt(str[1]));
		}
		
		context.write(count, word);
		//General WordCount
/*		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
		/*
		//#TagCounts
		String nextWord;
		while (itr.hasMoreTokens()) {
			nextWord = itr.nextToken();
			if(nextWord.matches("#[\\S]+")){
				word.set(nextWord);
				context.write(word, one);
			}
		}
		
		//@TagCounts
		while (itr.hasMoreTokens()) {
			nextWord = itr.nextToken();
			if(nextWord.matches("@[\\S]+")){
				word.set(nextWord);
				context.write(word, one);
			}
		}	
		*/
	}
}