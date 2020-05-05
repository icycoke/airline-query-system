package com.flight.query.sequential;

import java.util.Objects;

/**
 * An airport
 */
public class Airport extends Object {
    // The unique id of the airport
    private String id;

    public Airport(String id) {
        this.id = id;
    }

    /**
     * Get the id of the airport
     *
     * @return id of the airport
     */
    public String getId() {
        return id;
    }

    /**
     * If the airport equals another object
     *
     * @param o the object to compare
     * @return if the airport equals another object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null && !(o instanceof Airport))
            return false;
        Airport airport = (Airport) o;
        return getId().equals(airport.getId());
    }

    /**
     * Get the hash code of the airport
     *
     * @return the hash code generated from airport's id
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
