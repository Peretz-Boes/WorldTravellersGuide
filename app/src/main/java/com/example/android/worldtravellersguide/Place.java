package com.example.android.worldtravellersguide;

/**
 * Created by Peretz on 2017-04-19.
 */

public class Place {
    private String hotel;
    private String restaurant;
    private String attractions;
    private String garages;
    private String doctorOffices;
    private String dentistOffices;
    private String orthodontistOffices;
    private String opticianOffices;
    private String hospitals;

    public Place(String attractions, String dentistOffices, String doctorOffices, String garages, String hospitals, String hotel, String opticianOffices, String orthodontistOffices, String restaurant) {
        this.attractions = attractions;
        this.dentistOffices = dentistOffices;
        this.doctorOffices = doctorOffices;
        this.garages = garages;
        this.hospitals = hospitals;
        this.hotel = hotel;
        this.opticianOffices = opticianOffices;
        this.orthodontistOffices = orthodontistOffices;
        this.restaurant = restaurant;
    }

    public String getAttractions() {
        return attractions;
    }

    public void setAttractions(String attractions) {
        this.attractions = attractions;
    }

    public String getDentistOffices() {
        return dentistOffices;
    }

    public void setDentistOffices(String dentistOffices) {
        this.dentistOffices = dentistOffices;
    }

    public String getDoctorOffices() {
        return doctorOffices;
    }

    public void setDoctorOffices(String doctorOffices) {
        this.doctorOffices = doctorOffices;
    }

    public String getGarages() {
        return garages;
    }

    public void setGarages(String garages) {
        this.garages = garages;
    }

    public String getHospitals() {
        return hospitals;
    }

    public void setHospitals(String hospitals) {
        this.hospitals = hospitals;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getOpticianOffices() {
        return opticianOffices;
    }

    public void setOpticianOffices(String opticianOffices) {
        this.opticianOffices = opticianOffices;
    }

    public String getOrthodontistOffices() {
        return orthodontistOffices;
    }

    public void setOrthodontistOffices(String orthodontistOffices) {
        this.orthodontistOffices = orthodontistOffices;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}
