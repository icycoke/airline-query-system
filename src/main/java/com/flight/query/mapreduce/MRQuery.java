package com.flight.query.mapreduce;

import com.flight.query.Airport;
import com.flight.query.Route;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MRQuery {
    private static Set<Route> routeSet = new HashSet<>();

    private static class QueryMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strArr = value.toString().split("\"");
            context.write(new Text(strArr[0] + " " + strArr[0]), new Text(strArr[7] + " " + strArr[11]));
        }
    }

    private static class QueryReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) {
            String[] airportsStrArr = values.toString().split(" ");
            String[] dateStrArr = key.toString().split(" ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Route route = new Route(new Airport(airportsStrArr[0]),
                        new Airport(airportsStrArr[1]),
                        sdf.parse(dateStrArr[0]),
                        sdf.parse(dateStrArr[1]));
                routeSet.add(route);
                context.write(new Text(route.getDepAirport().getId()), new Text(route.getDestAirport().getId()));
                for (Route existedRoute : routeSet) {
                    if (existedRoute.getDestAirport().equals(route.getDepAirport())
                            && existedRoute.getEndDate().compareTo(route.getStartDate()) < 0) {
                        Route routeToAdd = new Route(existedRoute.getDepAirport(),
                                route.getDestAirport(),
                                existedRoute.getStartDate(),
                                route.getEndDate());
                        routeSet.add(routeToAdd);
                        context.write(new Text(routeToAdd.getDepAirport().getId()), new Text(routeToAdd.getDestAirport().getId()));
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

    public static void main(String[] args) {
        // TODO
    }
}
