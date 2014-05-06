package sample;

import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

public class AssociativeArray extends MapWritable{

	@Override
	public String toString() {
		Set<Writable> keys = this.keySet();
		StringBuilder stripeToString = new StringBuilder();
        for (Writable key : keys) {
            IntWritable count = (IntWritable) this.get(key);
            stripeToString = stripeToString.append("\n" + key + "," + "\t"+count);            
        }		
        return stripeToString.toString();
	}
	
}
