package com.example.emilie.property_management_5;

/**
 * Created by Emily on 3/28/2018.
 */

public class Tenant {
    private String id, name, gender, contactNumber, email, paymentDate;

    public Tenant(){

    }

    public Tenant(String id, String name, String gender, String contactNumber, String email, String paymentDate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
        this.paymentDate = paymentDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
