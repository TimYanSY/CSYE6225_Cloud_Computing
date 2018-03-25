package com.neu.edu.ysy.service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.neu.edu.ysy.module.OperationMessage;
import com.neu.edu.ysy.module.Program;
import com.neu.edu.ysy.module.Student;

import java.util.*;

@Path("/student")
public class StudentService {

    static class StudentMap {
        static Map<Long, Student>  studentMap = initStudentMap();

        private static Map<Long, Student> initStudentMap() {
            Map<Long, Student>  map = new HashMap<Long, Student>();
            Student tom = new Student("Tom", "http://wallpaper-gallery.net/images/child-images/child-images-1.jpg", Program.CSYE);
            Student marry = new Student("Marry", "http://wallpaper-gallery.net/images/child-images/child-images-11.jpg", Program.INFO);
            map.put(tom.getId(), tom);
            map.put(marry.getId(), marry);
            return map;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public String getStudent(@PathParam("id") long id) {
        Gson gson = new Gson();
        return gson.toJson(StudentMap.studentMap.get(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/allCourses/{id}")
    public String getSelectedCourses(@PathParam("id") long id) {
        Gson gson = new Gson();
        return gson.toJson(RegisterService.studentRegisteredCourse.get(id));
    }

    // name, image, program
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String addStudent(String json) {
        Gson gson = new Gson();
        Student studentInfo = gson.fromJson(json, Student.class);
        if (StudentMap.studentMap.containsKey(studentInfo.getId())) {
            return gson.toJson(new OperationMessage(false, "Student already exists"));
        }

        if (studentInfo.getName() == null) {
            return gson.toJson(new OperationMessage(false, "please give a valid name"));
        }

        if (studentInfo.getProgram() == null) {
            return gson.toJson(new OperationMessage(false, "please give a valid program"));
        }

        Student newStudent = new Student(studentInfo.getName(), studentInfo.getImage(),studentInfo.getProgram());
        StudentMap.studentMap.put(newStudent.getId(), newStudent);
        return gson.toJson(new OperationMessage(true, "new Student created with id " + newStudent.getId()));
    }

    // id
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteStudent(String json) {
        Gson gson = new Gson();
        Student studentInfo = gson.fromJson(json, Student.class);
        long id = studentInfo.getId();

        if (!StudentMap.studentMap.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "No such student"));
        }

        Student student = StudentMap.studentMap.get(id);
        StudentMap.studentMap.remove(id);
        RegisterService.studentRegisteredCourse.remove(id);

        Iterator<Map.Entry<String, LinkedHashSet<Student>>> courseIterator = RegisterService.courseContainsStudent.entrySet().iterator();
        while (courseIterator.hasNext()) {
            Map.Entry<String, LinkedHashSet<Student>> curr = courseIterator.next();
            curr.getValue().remove(student);
        }
        return gson.toJson(new OperationMessage(true, "delete the selected student"));
    }

    //id, others(optional)
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public String updateStudent(String json) {
        Gson gson = new Gson();
        Student studentInfo = gson.fromJson(json, Student.class);
        long id = studentInfo.getId();

        if (!StudentMap.studentMap.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "No such student"));
        }

        Student formerStudent = StudentMap.studentMap.get(id);

        if (studentInfo.getName() != null) {
            formerStudent.setName(studentInfo.getName());
        }

        if (studentInfo.getImage() != null) {
            formerStudent.setImage(studentInfo.getImage());
        }

        if (studentInfo.getProgram() != null) {
            formerStudent.setProgram(studentInfo.getProgram());
        }

        return gson.toJson(StudentMap.studentMap.get(id));
    }
}
