package com.flight.query.sequential;

import java.util.Date;
import java.util.Objects;

/**
 * A route
 */
public class Route {
    // The departure airport
    private Airport depAirport;
    // The destination airport
    private Airport destAirport;
    // The start date of the route
    private Date startDate;
    // The end date of the route
    private Date endDate;

    public Route(Airport depAirport, Airport destAirport, Date startDate, Date endDate) {
        this.depAirport = depAirport;
        this.destAirport = destAirport;
        this.startDate = startDate;
        this.endDate = endDate;
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
     * Get the start date
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Get the end date
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }
}
