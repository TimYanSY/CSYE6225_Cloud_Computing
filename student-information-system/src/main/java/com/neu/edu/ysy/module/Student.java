package com.neu.edu.ysy.module;

public class Student {
    private String image;
    private String name;
    private static long idCnt = 1;
    final long id;
    Program program;

    public Student(String name, String image, Program program) {
        id = idCnt++;
        this.name = name;
        this.image = image;
        this.program = program;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Program getProgram() {
        return program;
    }
}
