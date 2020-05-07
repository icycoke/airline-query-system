package com.flight.query.mapreduce;

import com.flight.query.Airport;
import com.flight.query.Route;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Partitioner;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MRQuery {
    private static Set<Route> routeSet = new HashSet<>();

    private static class QueryMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strArr = value.toString().split("\"");
            context.write(new Text(strArr[1] + " " + strArr[1]), new Text(strArr[7] + " " + strArr[11]));
        }
    }

    private static class QueryReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) {
            try {
                String[] dateStrArr = key.toString().split(" ");
                String[] d = dateStrArr[1].split("-");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String[] airportsStrArr;
                Queue<Route> routeQueue = new LinkedList<>();
                for (Text value : values) {
                    airportsStrArr = value.toString().split(" ");
                    Airport airport1 = new Airport(airportsStrArr[0]);
                    Airport airport2 = new Airport(airportsStrArr[1]);
                    Date p0 = sdf.parse(dateStrArr[0]);
                    Date p1 = sdf.parse(dateStrArr[1]);

                    Route route = new Route(airport1, airport2, p0, p1);
                    if (!routeSet.contains(route)) {
                        context.write(new Text(route.getDepAirport().getId()), new Text(route.getDestAirport().getId()));
                        routeQueue.add(route);
                    }
                    for (Route existedRoute : routeSet) {
                        if (existedRoute.getDestAirport().equals(route.getDepAirport())
                                && existedRoute.getEndDate().compareTo(route.getStartDate()) < 0) {
                            Route routeToAdd = new Route(existedRoute.getDepAirport(),
                                    route.getDestAirport(),
                                    existedRoute.getStartDate(),
                                    route.getEndDate());
                            if (!routeSet.contains(routeToAdd)) {
                                routeQueue.add(routeToAdd);
                                context.write(new Text(routeToAdd.getDepAirport().getId()), new Text(routeToAdd.getDestAirport().getId()));
                            }
                        }
                    }
                    while (!routeQueue.isEmpty()) {
                        routeSet.add(routeQueue.remove());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MyPartitioner extends Partitioner<Text, Text> {

        @Override
        public int getPartition(Text key, Text value, int numPartitions) {
            String[] dateStrArr = key.toString().split(" ");
            String[] d = dateStrArr[1].split("-");
            int day = Integer.parseInt(d[1]);
            int result = day / 7;
            return result;
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String[] path = {
                "hdfs://master:9000/user/hduser/sort/input/",
                "hdfs://master:9000/user/hduser/sort/output"
        };

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(MRQuery.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setMapperClass(QueryMapper.class);
        job.setReducerClass(QueryReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setNumReduceTasks(2);

        job.setPartitionerClass(MyPartitioner.class);

        FileInputFormat.setInputPaths(job, new Path(path[0] + args[0]));

        FileSystem fs = FileSystem.get(conf);

        Path p = new Path(path[1]);
        if (fs.exists(p)) {
            fs.delete(p, true);
        }
        FileOutputFormat.setOutputPath(job, p);
        long startTime = System.currentTimeMillis();
        boolean a = job.waitForCompletion(true);
        long endTime = System.currentTimeMillis();
        System.out.print("Time cost: ");
        long elaps = endTime - startTime;
        String elap = String.valueOf(elaps);
        System.out.print(elap);
        System.out.println("ms");


        if (a) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

}