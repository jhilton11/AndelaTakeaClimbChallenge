package com.appify.andelatakeaclimbchallenge;

/**
 * Created by Yinka Ige on 02/04/2018.
 */

public class Medication {
    private String id;
    private String name;
    private int interval;

    public Medication() {

    }

    public Medication(String id, String name, int interval) {
        this.id = id;
        this.name = name;
        this.interval = interval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
