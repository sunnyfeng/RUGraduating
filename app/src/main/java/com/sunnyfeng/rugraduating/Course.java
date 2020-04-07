package com.sunnyfeng.rugraduating;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    public String title;
    public String code;
    public int numCredits;
    public String description;
    private ArrayList<Course> prereqs;
    private ArrayList<Course> equivs;


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
        equivs = new ArrayList<>();
    }

    public void addPrereq(Course prereq) {
        prereqs.add(prereq);
    }

    public ArrayList<Course> getPrereqs() {
        return prereqs;
    }

    public void addEquiv(Course equiv) {
        equivs.add(equiv);
    }

    public ArrayList<Course> getEquivs() {
        return equivs;
    }

}
