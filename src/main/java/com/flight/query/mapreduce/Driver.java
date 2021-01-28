package com.flight.query.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Driver {

    public static void main(String[] args) throws Exception {
        String rawInput = args[0];
        String startDate = args[1];
        String endDate = args[2];
        String lib = args[3];
        String numberOfGroup = args[4];


        Configuration conf1 = new Configuration();

        conf1.set("starttime", startDate);
        conf1.set("endtime", endDate);
        conf1.set("numberOfGroup", numberOfGroup);

        Job job1 = Job.getInstance(conf1);
        job1.setJobName("first");
        job1.setJarByClass(Driver.class);

        job1.setMapperClass(MRQuery.MRQueryMapper.class);
        job1.setReducerClass(MRQuery.MRQueryReducer.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);

        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);

        TextInputFormat.setInputPaths(job1, new Path(rawInput));
        TextOutputFormat.setOutputPath(job1, new Path(lib));

        job1.waitForCompletion(true);


//        Configuration conf2 = new Configuration();
//        conf2.set("starttime",startDate);
//        conf2.set("endtime",endDate);
//
//        Job job2 = Job.getInstance(conf2);
//        job2.setJobName("second");
//        job2.setJarByClass(Driver.class);
//
//        job2.setOutputKeyClass(Text.class);
//        job2.setOutputValueClass(Text.class);
//
//        job2.setMapperClass(Merge.MergeMapper.class);
//        job2.setReducerClass(Merge.MergeReducer.class);
//
//        job2.setInputFormatClass(TextInputFormat.class);
//        job2.setOutputFormatClass(TextOutputFormat.class);
//
//        TextInputFormat.setInputPaths(job2,args[3]);
//        TextOutputFormat.setOutputPath(job2,new Path(res));
//
//        job2.waitForCompletion(true);


    }
}
