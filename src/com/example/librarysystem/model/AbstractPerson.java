package com.example.librarysystem.model;

public abstract class AbstractPerson {
    private String id;
    private String name;

    public AbstractPerson(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
