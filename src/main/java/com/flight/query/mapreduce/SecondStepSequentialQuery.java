package com.flight.query.mapreduce;

import com.flight.query.Airport;
import com.flight.query.Route;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SecondStepSequentialQuery {

    public static Set<Route> SQuery() {


        String inputPath = "/input/input.txt";

        File inputFile = new File(inputPath);

        HashMap<String, Set<Route>> map = new HashMap<>();

        Queue<Set<Route>> setQueue = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Set<Route> res = null;

        HashSet set1 = new HashSet();
        HashSet set2 = new HashSet();
        HashSet set3 = new HashSet();
        HashSet set4 = new HashSet();
        HashSet set5 = new HashSet();
        HashSet set6 = new HashSet();
        HashSet set7 = new HashSet();

        map.put("2016-01-01 2016-02-11", set1);
        map.put("2016-02-11 2016-03-24", set2);
        map.put("2016-03-24 2016-05-05", set3);
        map.put("2016-05-05 2016-06-16", set4);
        map.put("2016-06-16 2016-07-28", set5);
        map.put("2016-07-28 2016-09-08", set6);
        map.put("2016-10-20 2016-12-01", set7);
        setQueue.add(set1);
        setQueue.add(set2);
        setQueue.add(set3);
        setQueue.add(set4);
        setQueue.add(set5);
        setQueue.add(set6);
        setQueue.add(set7);

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] strArr;
                System.out.println(i++);
                strArr = line.split("\t");
                if (strArr.length != 2) {
                    continue;
                }
                String key = strArr[0];
                String[] value = strArr[1].split(",");

//                if (!map.containsKey(key)) {
//                    HashSet<Route> curSet = new HashSet<>();
//                    map.put(key,curSet);
//                    setQueue.add(curSet);
//                }

                map.get(key).add(new Route(new Airport(value[2]), new Airport(value[3]), sdf.parse(value[0]), sdf.parse(value[1])));

            }

            br.close();

            res = setQueue.remove();

            while (!setQueue.isEmpty()) {
                Set<Route> setToMerge = setQueue.remove();
                Set<Route> setToAdd = new HashSet<Route>();
                for (Route firstRoute : res) {
                    for (Route secondRoute : setToMerge) {
                        if (firstRoute.getDestAirport().equals(secondRoute.getDepAirport())) {
                            setToAdd.add(new Route(firstRoute.getDepAirport(),
                                    secondRoute.getDestAirport(),
                                    firstRoute.getStartDate(),
                                    secondRoute.getEndDate()));
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


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println("Second step: SequentialQuery");


        long startTime = System.currentTimeMillis();
        Set<Route> res = SecondStepSequentialQuery.SQuery();

        long endTime = System.currentTimeMillis();

        for (Route route : res) {
            System.out.println(route.getDepAirport().getId() + " " + route.getDestAirport().getId());
        }

        System.out.println("Number of pairs: " + res.size());
        System.out.print("Time cost: ");
        System.out.print(endTime - startTime);
        System.out.println("ms");


    }
}

