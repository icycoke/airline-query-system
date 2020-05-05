package com.flight.query.sequential;

import java.io.*;
import java.util.*;

/**
 * A class represent a sequential query for airlines
 */
public class SequentialQuery {
    public static List<List<Airport>> ttcQuery(int timeLimit) {
        // Input path of the query
        String inputPath = "./input/input.txt";
        // The input file
        File inputFile = new File(inputPath);
        // A set contains all possibly valid airports
        Set<Airport> airportSet = new HashSet<>();
        // A set contains all valid routes
        Set<Route> directRouteSet = new HashSet<>();
        // A matrix contains the time cost of route between airports
        int[][] timeMatrix;

        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            // Current read line
            String curLine;

            while ((curLine = br.readLine()) != null) {
                // The array contains data of current line
                String[] strArr;

                strArr = curLine.split("\"");
                if (strArr.length == 13) {
                    int airTime = Integer.parseInt(strArr[12]);
                    if (airTime <= timeLimit) {
                        Airport depAirport = new Airport(strArr[8]);
                        Airport destAirport = new Airport(strArr[10]);
                        airportSet.add(depAirport);
                        airportSet.add(destAirport);
                        directRouteSet.add(new Route(depAirport, destAirport, airTime));
                    }
                }
            }
            br.close();
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

        // A map of <index, airport> pairs
        Map<Integer, Airport> indexAirportMap = new HashMap<>();
        // Generate the index-airport map
        for (Airport airport : airportSet) {
            indexAirportMap.put(airportIndexMap.get(airport), airport);
        }

        // Initialize the route matrix
        timeMatrix = new int[airportSet.size()][airportSet.size()];
        for (int i = 0; i < timeMatrix.length; i++) {
            for (int j = 0; j < timeMatrix[0].length; j++) {
                if (i == j) {
                    timeMatrix[i][j] = 0;
                } else {
                    timeMatrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        // Add all direct route to the route matrix
        for (Route directRoute : directRouteSet) {
            int i = airportIndexMap.get(directRoute.getDepAirport());
            int j = airportIndexMap.get(directRoute.getDestAirport());
            timeMatrix[i][j] = Math.min(timeMatrix[i][j], directRoute.getTime());
        }

        // Use Floyd-Warshall algorithm to generate all time cost for routes
        for (int k = 0; k < timeMatrix.length; k++) {
            for (int i = 0; i < timeMatrix.length; i++) {
                for (int j = 0; j < timeMatrix.length; j++) {
                    timeMatrix[i][j] = Math.min(timeMatrix[i][k] + timeMatrix[k][j], timeMatrix[i][j]);
                }
            }
        }

        List<List<Airport>> resList = new ArrayList<>();
        for (int i = 0; i < timeMatrix.length; i++) {
            for (int j = 0; j < timeMatrix.length; j++) {
                if (timeMatrix[i][j] <= timeLimit) {
                    ArrayList<Airport> curPair = new ArrayList<>();
                    curPair.add(indexAirportMap.get(i));
                    curPair.add(indexAirportMap.get(j));
                    resList.add(curPair);
                }
            }
        }
        return resList;
    }
}
