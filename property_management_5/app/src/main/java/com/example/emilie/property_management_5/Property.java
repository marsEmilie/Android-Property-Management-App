package com.example.emilie.property_management_5;

/**
 * Created by Emily on 3/20/2018.
 */

public class Property {

    private String propertyID;
    private String propertyName;
    private String propertyLocation;
    private String propertyType;
    private String propertyRental;


    public Property(){

    }

    public Property(String propertyID, String propertyName, String propertyLocation, String propertyRental,String propertyType) {
        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.propertyLocation = propertyLocation;
        this.propertyRental = propertyRental;
        this.propertyType = propertyType;

    }

    public String getPropertyID() {
        return propertyID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public String getPropertyRental() {
        return propertyRental;
    }
}

