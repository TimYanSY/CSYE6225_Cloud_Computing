package com.neu.edu.ysy.service;

import com.google.gson.Gson;
import com.neu.edu.ysy.module.Course;
import com.neu.edu.ysy.module.OperationMessage;
import com.neu.edu.ysy.module.Registration;
import com.neu.edu.ysy.module.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Path("/register")
public class RegisterService {
    static Map<Long, LinkedHashSet<Course>> studentRegisteredCourse = new HashMap<Long, LinkedHashSet<Course>>();
    static Map<String, LinkedHashSet<Student>> courseContainsStudent = new HashMap<String, LinkedHashSet<Student>>();

    //GET:
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/{courseName}")
    public String getRegistrationInfo(@PathParam("id") long id, @PathParam("courseName") String courseName) {
        Gson gson = new Gson();
        if (!StudentService.StudentMap.studentMap.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "please give a valid student name"));
        }
        if (!CourseService.CourseMap.courseMap.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false, "please give a valid course name"));
        }
        if (studentRegisteredCourse.containsKey(id) && studentRegisteredCourse.get(id).contains(CourseService.CourseMap.courseMap.get(courseName)) && courseContainsStudent.containsKey(courseName) && courseContainsStudent.get(courseName).contains(StudentService.StudentMap.studentMap.get(id))) {
            return gson.toJson(new Registration(id, courseName));
        } else {
            return gson.toJson(new OperationMessage(false, "student has not registered this course"));
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String registerCourse(String json) {
        Gson gson = new Gson();
        Registration registration = gson.fromJson(json, Registration.class);
        long id = registration.getId();
        String courseName = registration.getCourseName();

        if (!StudentService.StudentMap.studentMap.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "No such student"));
        }
        Student student = StudentService.StudentMap.studentMap.get(id);

        if (!CourseService.CourseMap.courseMap.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false,"No such course"));
        }
        Course course = CourseService.CourseMap.courseMap.get(courseName);

        if((studentRegisteredCourse.containsKey(id) && studentRegisteredCourse.get(id).contains(course)) || (courseContainsStudent.containsKey(courseName) && courseContainsStudent.get(courseName).contains(student))) {
            return gson.toJson(new OperationMessage(false, "student has registered this course"));
        }

        if (!studentRegisteredCourse.containsKey(id)) {
            studentRegisteredCourse.put(id, new LinkedHashSet<Course>());
        }


        if (!courseContainsStudent.containsKey(courseName)) {
            courseContainsStudent.put(courseName, new LinkedHashSet<Student>());
        }

        studentRegisteredCourse.get(id).add(course);
        courseContainsStudent.get(courseName).add(student);
        course.getRoster().add(student.getName());

        return gson.toJson(new OperationMessage(true,"register successfully"));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteRegistration(String json) {
        Gson gson = new Gson();
        Registration registration = gson.fromJson(json, Registration.class);
        long id = registration.getId();
        String courseName = registration.getCourseName();

        if (!StudentService.StudentMap.studentMap.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "No such student"));
        }
        Student student = StudentService.StudentMap.studentMap.get(id);

        if (!CourseService.CourseMap.courseMap.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false,"No such course"));
        }
        Course course = CourseService.CourseMap.courseMap.get(courseName);

        if (!studentRegisteredCourse.containsKey(id)) {
            return gson.toJson(new OperationMessage(false, "student has not registered any course"));
        }

        if (!courseContainsStudent.containsKey(courseName)) {
            return gson.toJson(new OperationMessage(false, "course does not have any student"));
        }

        if (!studentRegisteredCourse.get(id).contains(course) || !courseContainsStudent.get(courseName).contains(student)) {
            return gson.toJson(new OperationMessage(false,"Student did not register the course"));
        }

        studentRegisteredCourse.get(id).remove(course);
        courseContainsStudent.get(courseName).remove(student);
        course.getRoster().remove(student.getName());

        return gson.toJson(new OperationMessage(true, "successfully removed student from registration"));
    }
}
