package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;

public class Course implements Serializable, CourseItem {

    private String _id;
    private String name;
    private Integer credits;
    private String department;
    private String school;
    private String description;
    private String[] prereqs;
    private String[] coreqs;

    public Course(String _id, String name, Integer credits, String department, String school,
                  String description, String[] prereqs, String[] coreqs) {
                                    //TODO: Make these string arrays into course objects
        this._id = _id;
        this.name = name;
        this.credits = credits;
        this.department = department;
        this.school = school;
        this.description = description;
        this.prereqs = prereqs;
        this.coreqs = coreqs;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits.intValue();
    }

    public void setCredits(int credits) {
        this.credits = new Integer(credits);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(String[] prereqs) {
        this.prereqs = prereqs;
    }

    public String[] getCoreqs() {
        return coreqs;
    }

    public void setCoreqs(String[] coreqs) {
        this.coreqs = coreqs;
    }

    @Override
    public int getType() {
        return TYPE_COURSE;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSubtitle() {
        return _id;
    }

}
