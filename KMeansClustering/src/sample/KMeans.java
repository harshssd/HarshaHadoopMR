package sample;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KMeans {

	private static final transient Logger LOG = LoggerFactory.getLogger(KMeans.class);
	public static Configuration conf = new Configuration();
	public static String inputPath = "/inputKMeans";
	public static String outputPath = "/outpuKMeans";
	public static String centroidFile = "/centroid";

	public static void main(String[] args) throws Exception {		

		LOG.info("HDFS Root Path: {}", conf.get("fs.defaultFS"));
		LOG.info("MR Framework: {}", conf.get("mapreduce.framework.name"));

		System.out.println(centroidFile);

		Path centroidPath = new Path(centroidFile);       

		String initial_centroid = " ";
		boolean success = false;
		while (!success)
		{
			initial_centroid = readCentroidFile(centroidPath, conf);
			deleteFolder(conf,outputPath);

			Job job = Job.getInstance(conf);

			job.setJarByClass(KMeans.class);
			job.setMapperClass(KMeansMapper.class);
			job.setCombinerClass(KMeansReducer.class);
			job.setReducerClass(KMeansReducer.class);
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.addInputPath(job, new Path(inputPath));
			FileOutputFormat.setOutputPath(job, new Path(outputPath));
			job.waitForCompletion(true);

			success = checkIfItsComplete(initial_centroid, conf, centroidPath);
		}

	}

	public static void writeToCentroidFile(String s, Configuration conf, Path centroidPath) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream out = fs.create(centroidPath);
		out.writeBytes(s);
		out.close();
	}

	@SuppressWarnings("deprecation")
	public static String readCentroidFile(Path centroidPath,
			Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);  
		String str ="";
		if(fs.exists(centroidPath)) {
			FSDataInputStream in = fs.open (centroidPath);
			String next = in.readLine();
			str = next;
			while(in!=null)
			{
				next = in.readLine();

				if (next == null)
					break;
				str = next;
			}
			in.close();
		}
		return str;
	}

	private static boolean checkIfItsComplete(String initialCentroid, Configuration conf, Path centroidPath) throws IOException {
		System.out.println("Initial Centroid Value "+initialCentroid);
		String currentCentroid = readCentroidFile(centroidPath, conf);
		System.out.println("Current Centroid Value "+currentCentroid);
		if(initialCentroid.equals(currentCentroid)){
			return true;
		}
		return false;
	}

	/**
	 * Delete a folder on the HDFS. This is an example of how to interact
	 * with the HDFS using the Java API. You can also interact with it
	 * on the command line, using: hdfs dfs -rm -r /path/to/delete
	 * 
	 * @param conf a Hadoop Configuration object
	 * @param folderPath folder to delete
	 * @throws IOException
	 */
	private static void deleteFolder(Configuration conf, String folderPath ) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(folderPath);
		if(fs.exists(path)) {
			fs.delete(path,true);
		}
	}
}