# Airline Query System
A simulation of part of an airline query system
The project contains implementation of three different algorithms including the Floyd-Warshall-like algorithm, the optimized sequential algorithm, and the MapReduce algorithm

## Floyd-Warshall-like algorithm
The Floyd-Warshall-like algorithm is implemented in com.flight.query.sequential.FloydWarshellQuery and run by com.flight.query.sequential.FWMain

## Optimized sequential algorithm
The optimized sequential algorithm is implemented in com.flight.query.sequential.SequentialQuery and run by com.flight.query.sequential.SequentialMain

## MapReduce algorithm
The MapReduce algorithm is implemented in com.flight.query.mapreduce.MRQuery

## Other notes
1. com.flight.query.Route is a required class representing a route
2. com.flight.query.Airport is a required class representing an airport
3. The input dataset is input/inputV1.txt. The dataset was downloaded from https://www.transtats.bts.gov/Fields.asp?Table_ID=236
4. The implementation of MapReduce algorithm requires JDK 1.8 and Hadoop 2.7.7
5. The T-represent-time-span is an implementation of algorithms that assume the specified time range T is a time span, which is easier to implement but now abandoned
