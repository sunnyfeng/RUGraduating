package com.sunnyfeng.rugraduating;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    String title;
    String code;
    int numCredits;
    String description;
    private ArrayList<Course> prereqs;


    /**
     * @param title name of course
     * @param code code of course
     * @param numCredits number of credits for course
     * @param description description of course
     */
    public Course(String title, String code, int numCredits, String description)
    {

        this.title = title;
        this.code = code;
        this.numCredits = numCredits;
        this.description = description;
        prereqs = new ArrayList<>();
    }

    public void addPrereq(Course prereq) {
        prereqs.add(prereq);
    }

    public ArrayList<Course> getPrereqs() {
        return prereqs;
    }

}
