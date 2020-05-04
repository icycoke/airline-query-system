package com.flight.query.sequential;

import java.io.*;
import java.util.*;

/**
 * A class represent a sequential query for airlines
 */
public class SequentialQuery {
    public static Set<Route> ttcQuery(Date startDate, Date endDate) {
        // Input path of the query
        String inputPath = "./input/input.txt";
        // The input file
        File inputFile = new File(inputPath);
        // A set contains all possibly valid airports
        Set<Airport> airportSet = new HashSet<>();
        // A set contains all valid routes
        Set<Route> routeSet = new HashSet<>();

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
                            airportSet.add(depAirport);
                            airportSet.add(destAirport);
                            routeSet.add(new Route(depAirport, destAirport, curDate, curDate));
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

        for (Airport depAirport : airportSet) {
            for (Airport destAirport : airportSet) {
                Route route = findRoute(depAirport, destAirport, routeSet, airportSet);
                if (route != null) {
                    routeSet.add(route);
                }
            }
        }

        return routeSet;
    }

    public static Route findRoute(Airport depAirport, Airport destAirport, Set<Route> routeSet, Set<Airport> airportSet) {
        Route curRoute = new Route(depAirport, destAirport, null, null);
        for (Route route : routeSet) {
            if (route.equals(curRoute)) {
                return route;
            }
        }
        for (Airport airport : airportSet) {
            //TODO
        }
    }
}
