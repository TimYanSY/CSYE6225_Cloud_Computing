package com.neu.edu.ysy.module;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseName;
    private Board board;
    private Lecture lecture;
    private List<String> roster;

    public Course (String courseName) {
        this.courseName = courseName;
        board = new Board();
        lecture = new Lecture();
        roster = new ArrayList<String>();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public List<String> getRoster() {
        return roster;
    }

    public void setRoster(List<String> roster) {
        this.roster = roster;
    }
}
