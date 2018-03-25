package com.neu.edu.ysy.module;

public class Registration {
    private long id;
    private String courseName;

    public Registration(long id, String courseName) {
        this.id = id;
        this.courseName = courseName;
    }

    public long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }
}
