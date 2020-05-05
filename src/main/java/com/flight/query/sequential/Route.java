package com.flight.query.sequential;

/**
 * A route
 */
public class Route {
    // The departure airport
    private Airport depAirport;
    // The destination airport
    private Airport destAirport;
    // The time cost of the route
    private int time;

    public Route(Airport depAirport, Airport destAirport, int time) {
        this.depAirport = depAirport;
        this.destAirport = destAirport;
        this.time = time;
    }

    /**
     * Get the departure airport
     * @return the departure airport
     */
    public Airport getDepAirport() {
        return depAirport;
    }

    /**
     * Get the destination airport
     * @return the destination airport
     */
    public Airport getDestAirport() {
        return destAirport;
    }

    /**
     * Get the time cost
     * @return the time cost
     */
    public int getTime() {
        return time;
    }
}
