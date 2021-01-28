package com.flight.query.mapreduce;


import com.flight.query.Airport;
import com.flight.query.Route;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MRQuery {

    public static class MRQueryMapper extends Mapper<LongWritable, Text, Text, Text> {

        String startDateStr;
        String endDateStr;
        String numberOfGroup;
        int Num = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long left = 0;
        long right = 0;
        long pivot = 0;

        Date startDate = new Date();
        Date endDate = new Date();
        Date[] dateGroup;
        String[] keyset;

        Date prev = new Date();
        Date next = new Date();
//        Date pivotDate = new Date();
//        Date pivotNextDate = new Date();
//        String pivotDateStr = "";
//        String pivotNextDateStr = "";
//        String key1 = "";
//        String key2 = "";

        public void setup(Context context) {
            Configuration conf = context.getConfiguration();
            startDateStr = conf.get("starttime", "2016-09-10");
            endDateStr = conf.get("endtime", "2016-09-11");
            numberOfGroup = conf.get("numberOfGroup", "2");
            Num = Integer.parseInt(numberOfGroup);
            dateGroup = new Date[Num + 1];
            keyset = new String[Num];


            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);

                prev = startDate;

                left = startDate.getTime();
                right = endDate.getTime();
                pivot = right / Num - left / Num;
                dateGroup[0] = startDate;

                for (int i = 1; i <= Num; i++) {
                    next = new Date(prev.getTime() + pivot);
                    dateGroup[i] = next;
                    prev = next;
                }

                for (int i = 0; i < Num; i++) {
                    Date first = dateGroup[i];
                    Date second = dateGroup[i + 1];
                    String firstkey = sdf.format(first);
                    String secondkey = sdf.format(second);
                    String res = firstkey + " " + secondkey;
                    keyset[i] = res;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] data = value.toString().split("\"");
            String curStartDateStr = data[1];


            try {

                Date curStartDate = sdf.parse(curStartDateStr);
                if ((curStartDate.compareTo(startDate) < 0) || (curStartDate.compareTo(endDate)) > 0) {
                    return;
                }
                for (int i = 1; i < dateGroup.length; i++) {
                    if (curStartDate.compareTo(dateGroup[i]) <= 0) {
                        context.write(new Text(keyset[i - 1]), new Text(data[1] + " " + data[1] + " " + data[7] + " " + data[11]));
                        return;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    public static class MRQueryReducer extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) {


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String[] startEndDateStr = key.toString().split(" ");
                Date startDate = sdf.parse(startEndDateStr[0]);
                Date endDate = sdf.parse(startEndDateStr[1]);

                // A map contains all <Date, Set<Route>> pairs
                Map<Date, Set<Route>> dateListMap = new HashMap<>();

                /*
                 * Initialize the date-list map and add all lists into setQueue
                 */
                Calendar calendar1 = Calendar.getInstance();
                Queue<Set<Route>> setQueue = new LinkedList<>();
                for (calendar1.setTime(startDate); calendar1.getTime().compareTo(endDate) <= 0; calendar1.add(Calendar.DATE, 1)) {
                    Set<Route> curSet = new HashSet<>();
                    dateListMap.put(calendar1.getTime(), curSet);
                    setQueue.add(curSet);
                }

                for (Text value : values) {
                    String[] routeInfo = value.toString().split(" ");
                    if (routeInfo.length != 4) {
                        continue;
                    }
                    Date date = sdf.parse(routeInfo[0]);
                    Airport depAirport = new Airport(routeInfo[2]);
                    Airport destAirport = new Airport(routeInfo[3]);
                    dateListMap.get(date).add(new Route(depAirport, destAirport, date, date));
                }

                // The result of the query
                Set<Route> res = setQueue.remove();

                /*
                 * Merge all set of routes and add new valid routes to the result
                 */
                while (!setQueue.isEmpty()) {
                    Set<Route> setToMerge = setQueue.remove();
                    Set<Route> setToAdd = new HashSet<>();
                    for (Route firstRoute : res) {
                        for (Route secondRoute : setToMerge) {
                            if (firstRoute.getDestAirport().equals(secondRoute.getDepAirport())) {
                                setToAdd.add(new Route(firstRoute.getDepAirport(), secondRoute.getDestAirport(),
                                        firstRoute.getStartDate(), secondRoute.getEndDate()));
                            }
                        }
                    }
                    for (Route routeToAdd : setToAdd) {
                        res.add(routeToAdd);
                    }
                    for (Route routeToMerge : setToMerge) {
                        res.add(routeToMerge);
                    }
                }

                for (Route route : res) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(sdf.format(startDate))
                            .append(",")
                            .append(sdf.format(endDate))
                            .append(",")
                            .append(route.getDepAirport().toString())
                            .append(",")
                            .append(route.getDestAirport().toString());
                    context.write(new Text(key.toString()), new Text(sb.toString()));
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

}