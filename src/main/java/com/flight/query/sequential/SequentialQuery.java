package com.flight.query.sequential;

import java.io.*;
import java.util.*;

/**
 * A class represent a sequential query for airlines
 */
public class SequentialQuery {
    public static List<List<Airport>> ttcQuery(Date startDate, Date endDate) {
        // Input path of the query
        String inputPath = "./input/sqlinput.txt";
        // The input file
        File inputFile = new File(inputPath);
        // A set contains all possibly valid airports
        Set<Airport> airportSet = new HashSet<>();
        // A set contains all valid routes
        Set<Route> directRouteSet = new HashSet<>();
        // A matrix contains the routes between airports
        ArrayList<Route>[][] routeMatrix;

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
                            directRouteSet.add(new Route(depAirport, destAirport, curDate, curDate));
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


        // A map of <airport, index> pairs
        Map<Airport, Integer> airportIndexMap = new HashMap<>();
        // Generate the airport-index map
        int index = 0;
        for (Airport airport : airportSet) {
            airportIndexMap.put(airport, index);
            index++;
        }

        // Initialize the route matrix
        routeMatrix = new ArrayList[airportSet.size()][airportSet.size()];
        for (int i = 0; i < routeMatrix.length; i++) {
            for (int j = 0; j < routeMatrix[0].length; j++) {
                routeMatrix[i][j] = new ArrayList<>();
            }
        }

        // Add all direct route to the route matrix
        for (Route directRoute : directRouteSet) {
            int i = airportIndexMap.get(directRoute.getDepAirport());
            int j = airportIndexMap.get(directRoute.getDestAirport());
            routeMatrix[i][j].add(directRoute);
        }

        // Use Floyd-Warshall algorithm to generate all valid routes
        for (int k = 0; k < routeMatrix.length; k++) {
            for (int i = 0; i < routeMatrix.length; i++) {
                for (int j = 0; j < routeMatrix.length; j++) {
                    for (int firstIndex = 0; firstIndex < routeMatrix[i][k].size(); firstIndex++) {
                        for (int secondIndex = 0; secondIndex < routeMatrix[k][j].size(); secondIndex++) {
                            Route firstPart = routeMatrix[i][k].get(firstIndex);
                            Route secondPart = routeMatrix[k][j].get(secondIndex);
                            if (firstPart != null
                                    && secondPart != null
                                    && firstPart.getEndDate().compareTo(secondPart.getStartDate()) < 0) {
                                routeMatrix[i][j].add(new Route(firstPart.getDepAirport(),
                                        secondPart.getDestAirport(),
                                        firstPart.getStartDate(),
                                        secondPart.getEndDate()));
                            }
                        }
                    }
                }
            }
        }

        List<List<Airport>> resList = new ArrayList<>();
        for (int i = 0; i < routeMatrix.length; i++) {
            for (int j = 0; j < routeMatrix.length; j++) {
                if (!routeMatrix[i][j].isEmpty()) {
                    ArrayList<Airport> curPair = new ArrayList<>();
                    curPair.add(routeMatrix[i][j].get(0).getDepAirport());
                    curPair.add(routeMatrix[i][j].get(0).getDestAirport());
                    resList.add(curPair);
                }
            }
        }
        return resList;
    }
}
