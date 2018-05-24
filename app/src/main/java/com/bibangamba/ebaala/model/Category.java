package com.bibangamba.ebaala.model;

/**
 * Created by davy on 4/20/2018.
 */

public class Category {

    private String name, id;

    public Category() {

    }

    public Category(String name, String id) {
        this.name = name;
        this.id = id;
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
}
