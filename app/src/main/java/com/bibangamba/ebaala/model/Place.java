package com.bibangamba.ebaala.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bibangamba on 10/18/2017.
 */
public class Place {
    private String address;
    private String email;
    private double latitude;
    private double longitude;
    private String name;
    private double rating;
    private String phone;
    private String associatedTags;
    private String image;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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
        result.put("associatedTags", associatedTags);
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
