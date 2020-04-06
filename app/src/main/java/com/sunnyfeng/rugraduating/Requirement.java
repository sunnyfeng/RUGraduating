package com.sunnyfeng.rugraduating;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds requirement information.
 */
public class Requirement implements Serializable {
    String title;
    boolean isCredits;
    int totalRequired;
    int alreadyCompleted;
    ArrayList<Course> coursesTaken;

    /**
     * @param title name of requirement
     * @param isCredits if the req is defined by total credits or total number of courses
     * @param totalRequired total number required to fulfill requirement
     * @param alreadyCompleted number of courses/credits already completed
     */
    public Requirement(String title, boolean isCredits, int totalRequired, int alreadyCompleted) {
        this.title = title;
        this.isCredits = isCredits;
        this.totalRequired = totalRequired;
        this.alreadyCompleted = alreadyCompleted;

        this.coursesTaken = new ArrayList<>();
    }

    public void addCourseTaken(Course courseTaken) {
        coursesTaken.add(courseTaken);
    }

    public ArrayList<Course> getCoursesTaken() {
        return coursesTaken;
    }
}
