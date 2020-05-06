package com.flight.query.sequential;

import com.flight.query.Airport;
import com.flight.query.Route;

import java.io.*;
import java.util.*;

/**
 * An optimized sequential query
 */
public class SequentialQuery {
    public static Set<Route> ttcQuery(Date startDate, Date endDate) {
        // Input path of the query
        String inputPath = "./input/inputV1.txt";
        // The input file
        File inputFile = new File(inputPath);

        // A map contains all <Date, Set<Route>> pairs
        Map<Date, Set<Route>> dateListMap = new HashMap<>();

        /*
        Initialize the date-list map and add all lists into setQueue
         */
        Calendar calendar1 = Calendar.getInstance();
        Queue<Set<Route>> setQueue = new LinkedList<>();
        for (calendar1.setTime(startDate); calendar1.getTime().compareTo(endDate) <= 0; calendar1.add(Calendar.DATE, 1)) {
            Set<Route> curSet = new HashSet<>();
            dateListMap.put(calendar1.getTime(), curSet);
            setQueue.add(curSet);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            // Current read line
            String curLine;

            while ((curLine = br.readLine()) != null) {
                // The array contains data of current line
                String[] strArr;

                strArr = curLine.split("\"");
                if (strArr.length == 14) {
                    // The array contains year, month, and day of current line
                    String[] dateArr = strArr[1].split("-");
                    if (dateArr.length == 3) {
                        // The date of the flight
                        Date curDate = new Date(Integer.valueOf(dateArr[0]) - 1900,
                                Integer.valueOf(dateArr[1]) - 1,
                                Integer.valueOf(dateArr[2]));
                        if (curDate.compareTo(startDate) >= 0 && curDate.compareTo(endDate) <= 0) {
                            Airport depAirport = new Airport(strArr[7]);
                            Airport destAirport = new Airport(strArr[11]);
                            dateListMap.get(curDate).add(new Route(depAirport, destAirport, curDate, curDate));
                        }
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The result of the query
        Set<Route> res = setQueue.remove();

        /*
        Merge all set of routes and add new valid routes to the result
         */
        while (!setQueue.isEmpty()) {
            Set<Route> setToMerge = setQueue.remove();
            Set<Route> setToAdd = new HashSet<>();
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
        return res;
    }
}
