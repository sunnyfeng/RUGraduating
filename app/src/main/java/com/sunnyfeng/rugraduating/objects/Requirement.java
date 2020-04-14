package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds requirement information.
 */
public class Requirement implements Serializable {
    public String title;
    public boolean isCredits;
    public int totalRequired;
    public int alreadyCompleted;
    ArrayList<CourseItem> coursesTaken;

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

    public void addCourseTaken(CourseItem courseTaken) {
        coursesTaken.add(courseTaken);
    }

    public ArrayList<CourseItem> getCoursesTaken() {
        return coursesTaken;
    }
}
