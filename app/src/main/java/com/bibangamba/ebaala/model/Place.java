package com.bibangamba.ebaala.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bibangamba on 10/18/2017.
 */
public class Place {
    private String address;
    private String email;
    private Double latitude, longitude;
    private String name;
    private double rating;
    private String phone;
    private String associatedTags;
    private String services;
    private String image;
    private ArrayList<String> events, open, popular;
    private String beer, shots, food;

    public Place() {

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAssociatedTags() {
        return associatedTags;
    }

    public void setAssociatedTags(String associatedTags) {
        this.associatedTags = associatedTags;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public ArrayList<String> getOpen() {
        return open;
    }

    public void setOpen(ArrayList<String> open) {
        this.open = open;
    }

    public String getBeer() {
        return beer;
    }

    public void setBeer(String beer) {
        this.beer = beer;
    }

    public String getShots() {
        return shots;
    }

    public void setShots(String shots) {
        this.shots = shots;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public ArrayList<String> getPopular() {
        return popular;
    }

    public void setPopular(ArrayList<String> popular) {
        this.popular = popular;
    }

    @Override
    public String toString() {
        return "Place{" +
                "address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", phone='" + phone + '\'' +
                ", associatedTags='" + associatedTags + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("email", email);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("name", name);
        result.put("rating", rating);
        result.put("phone", phone);
        result.put("events", events);
        result.put("services", services);
        result.put("associatedTags", associatedTags);
        result.put("beer", beer);
        result.put("shots", shots);
        result.put("food", food);
        result.put("open", open);
        result.put("popular", open);
        result.put("image", image);
        return result;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
