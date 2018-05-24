package com.bibangamba.ebaala.model;

/**
 * Created by bibangamba on 9/27/2017.
 */

public class Event {
    private String name, time,venue, eventImage, day, happening;


    public Event() {

    }

    public Event(String name, String time, String venue, String eventImage) {
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.eventImage = eventImage;
    }

    public Event(String name, String time, String venue, String eventImage, String day, String happening) {
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.eventImage = eventImage;
        this.day = day;
        this.happening = happening;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHappening() {
        return happening;
    }

    public void setHappening(String happening) {
        this.happening = happening;
    }
}

