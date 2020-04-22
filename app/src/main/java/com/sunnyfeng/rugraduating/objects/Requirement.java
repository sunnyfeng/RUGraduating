package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds requirement information.
 */
public class Requirement implements Serializable {
    public String name;
    public ArrayList<String> courses;
    public Integer numTakenCourses;
    public Integer numTotalCourses;
    public ArrayList<String> untakenCourses;
    // ArrayList<CourseItem> coursesTaken; // not in constructor because optional, may be empty

    /**
     * @param name name of requirement
     * @param numTakenCourses total number required to fulfill requirement
     * @param numTotalCourses number of courses/credits already completed
     */
    public Requirement(String name,  int numTakenCourses, int numTotalCourses) {
        this.name = name;
        this.numTakenCourses = numTakenCourses;
        this.numTotalCourses = numTotalCourses;

        // TODO: alreadyCompleted should equal the length of coursesTaken array
        this.courses = new ArrayList<>();
        this.untakenCourses = new ArrayList<>();
    }

    public void setCoursesTaken(ArrayList<String> courses) {
        this.courses = courses;
    }

    public void setUntakenCourses(ArrayList<String> untakenCourses) {
        this.untakenCourses = untakenCourses;
    }

    public ArrayList<String> getCoursesTaken() {
        return courses;
    }

    public ArrayList<String> getUntakenCourses() {
        return untakenCourses;
    }
}
