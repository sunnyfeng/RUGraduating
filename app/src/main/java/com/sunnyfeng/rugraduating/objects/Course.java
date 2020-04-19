package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable, CourseItem {

    private String _id;
    private String name;
    private Integer credits;
    private String department;
    private String school;
    private String description;
    private ArrayList<String> prereqs;
    private ArrayList<String> coreqs;

    public Course(String _id, String name, Integer credits, String department, String school,
                  String description, ArrayList<String> prereqs, ArrayList<String> coreqs) {
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

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        if (!(_id.equals(other._id))) {
            return false;
        }
        return true;
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

    public ArrayList<String> getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(ArrayList<String> prereqs) {
        this.prereqs = prereqs;
    }

    public ArrayList<String> getCoreqs() {
        return coreqs;
    }

    public void setCoreqs(ArrayList<String> coreqs) {
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
