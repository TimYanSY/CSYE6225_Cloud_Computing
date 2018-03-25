package com.neu.edu.ysy.service;

import com.google.gson.Gson;
import com.neu.edu.ysy.module.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

@Path("/course")
public class CourseService {

    static class CourseMap {
        static Map<String, Course> courseMap = initCourseMap();

        private static Map<String, Course> initCourseMap() {
            Board oodBoard = new Board();
            oodBoard.addAnnouncement("first ood announcement");
            oodBoard.addImage("first image in ood board");
            Lecture oodLecture = new Lecture();
            oodLecture.addMaterial("first ood material");
            oodLecture.addNote("first ood note");
            Course ood = new Course("INFO5100");
            ood.setBoard(oodBoard);
            ood.setLecture(oodLecture);

            Board cloudBoard = new Board();
            cloudBoard.addAnnouncement("first cloud announcement");
            cloudBoard.addImage("first image in cloud board");
            Lecture cloudLecture = new Lecture();
            cloudLecture.addMaterial("first cloud material");
            cloudLecture.addNote("first cloud note");
            Course cloud = new Course("CSYE6225");
            cloud.setBoard(cloudBoard);
            cloud.setLecture(cloudLecture);

            Map<String, Course> map = new HashMap<String, Course>();
            map.put(ood.getCourseName(), ood);
            map.put(cloud.getCourseName(), cloud);
            return map;
        }
    }

    // look up course information
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{courseName}")
    public String getCourse(@PathParam("courseName") String courseName) {
        Gson gson = new Gson();
        return gson.toJson(CourseMap.courseMap.get(courseName));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allStudents/{courseName}")
    public String getAllStudents(@PathParam("courseName") String courseName) {
        Gson gson = new Gson();
        return gson.toJson(RegisterService.courseContainsStudent.get(courseName));
    }

    // add a new course
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addCourse(String json) {
        Gson gson = new Gson();
        Course courseInfo = gson.fromJson(json, Course.class);
        if (courseInfo.getCourseName() == null) {
            return gson.toJson(new OperationMessage(false,"Please give a course name"));
        }
        if (CourseMap.courseMap.containsKey(courseInfo.getCourseName())) {
            return gson.toJson(new OperationMessage(false, "Course already exists"));
        }
        CourseMap.courseMap.put(courseInfo.getCourseName(), courseInfo);
        return gson.toJson(new OperationMessage(true,"successfully added the course"));
    }

    // add a lecture
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lecture/{courseName}")
    public String addLecture(@PathParam("courseName") String courseName, String json) {
        Gson gson = new Gson();
        if (!CourseMap.courseMap.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false, "No such course"));
        }
        Course course = CourseMap.courseMap.get(courseName);
        OneLecture oneLecture = gson.fromJson(json, OneLecture.class);
        if (oneLecture== null || (oneLecture.getMaterials() == null && oneLecture.getNotes() == null)) {
            return gson.toJson(new OperationMessage(false, "Pleas give a valid lecture"));
        }
        course.getLecture().addMaterial(oneLecture.getMaterials());
        course.getLecture().addNote(oneLecture.getNotes());
        return gson.toJson(new OperationMessage(true, "Add lecture successfully"));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/board/{courseName}")
    public String addBoard(@PathParam("courseName") String courseName, String json) {
        Gson gson = new Gson();
        if (!CourseMap.courseMap.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false, "No such course"));
        }
        Course course = CourseMap.courseMap.get(courseName);
        OneBoardMessage oneBoardMessage = gson.fromJson(json, OneBoardMessage.class);
        if (oneBoardMessage == null || (oneBoardMessage.getAnnoucement() == null && oneBoardMessage.getImage() == null)) {
            return gson.toJson(new OperationMessage(false, "Please give a valid BoardMessage"));
        }
        course.getBoard().addImage(oneBoardMessage.getImage());
        course.getBoard().addAnnouncement(oneBoardMessage.getAnnoucement());
        return gson.toJson(new OperationMessage(true, "Add a board message successfully"));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteCourse(String json) {
        Gson gson = new Gson();
        Course courseInfo = gson.fromJson(json, Course.class);

        if (courseInfo == null || CourseMap.courseMap.get(courseInfo.getCourseName()) == null) {
            return gson.toJson(new OperationMessage(false, "please give a valid course"));
        }

        Course course = CourseMap.courseMap.get(courseInfo.getCourseName());
        CourseMap.courseMap.remove(courseInfo.getCourseName());
        RegisterService.courseContainsStudent.remove(courseInfo.getCourseName());

        Iterator<Map.Entry<Long, LinkedHashSet<Course>>> studentIterator = RegisterService.studentRegisteredCourse.entrySet().iterator();

        while (studentIterator.hasNext()) {
            Map.Entry<Long, LinkedHashSet<Course>> curr = studentIterator.next();
            curr.getValue().remove(course);
        }
        return gson.toJson(new OperationMessage(true, "Delete a course successfully"));
    }
}