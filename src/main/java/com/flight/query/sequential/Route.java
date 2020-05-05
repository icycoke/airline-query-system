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

    /**
     * If the route equals another route
     *
     * @param o the object to compare
     * @return if the route equals another route
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof Route))
            return false;
        Route route = (Route) o;
        return getDepAirport().equals(route.getDepAirport()) && getDestAirport().equals(route.getDestAirport());
    }

    /**
     * Get the hash code of the route
     *
     * @return the hash code generated from departure and destination airport
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDepAirport(), getDestAirport());
    }
}
