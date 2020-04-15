package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Equivalency implements Serializable, CourseItem {

    private String _id;
    private ArrayList<Course> courses;

    public Equivalency(String _id, ArrayList<Course> courses) {
        this._id = _id;
        this.courses = courses;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @Override
    public int getType() {
        return TYPE_EQUIV;
    }

    @Override
    public String getTitle() {
        return _id;
    }

    @Override
    public String getSubtitle() {
        return courses.size() + " courses";
    }
}
