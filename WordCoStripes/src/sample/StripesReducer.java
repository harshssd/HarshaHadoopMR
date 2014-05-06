package sample;

import java.io.IOException;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class StripesReducer  extends Reducer<Text, MapWritable, Text, IntWritable> {
    
	private MapWritable incrementingMap = new MapWritable();

    @Override
    protected void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
        incrementingMap.clear();
        for (MapWritable value : values) {
            addAll(value);
        }
        sendToOutput(key, context);
    }
    
    private void sendToOutput(Text key,
			Context context) throws IOException, InterruptedException {
    	Set<Writable> neighbors = incrementingMap.keySet();
    	Text pair;
    	IntWritable count;
    	for(Writable neighbor: neighbors){
    		if(incrementingMap.containsKey(neighbor)){
    			count = (IntWritable) incrementingMap.get(neighbor); 
    			pair = new Text(key.toString() + ", " + neighbor.toString());
    			context.write(pair, count);
    		}
    	}
    	
	}

    private void addAll(MapWritable mapWritable) {
        Set<Writable> keys = mapWritable.keySet();
        for (Writable key : keys) {
            IntWritable fromCount = (IntWritable) mapWritable.get(key);
            if (incrementingMap.containsKey(key)) {
                IntWritable count = (IntWritable) incrementingMap.get(key);
                count.set(count.get() + fromCount.get());
            } else {
                incrementingMap.put(key, fromCount);
            }
        }
    }
	
}
