package com.neu.edu.ysy.module;


import java.util.LinkedHashSet;
import java.util.Set;

public enum Program {
    CSYE, INFO;

    private Set<Student> students;
    private Set<Course> courses;

    Program() {
        courses = new LinkedHashSet<Course>();
        students = new LinkedHashSet<Student>();
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Set<Course> getCourses() {
        return courses;

    }

    public boolean addCourse(Course course) {
        return this.courses.add(course);
    }

    public boolean addStudent(Student student) {
        return this.students.add(student);
    }
}
