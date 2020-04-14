package com.sunnyfeng.rugraduating.objects;

import java.io.Serializable;

public class Regex implements Serializable, CourseItem {

    private String _id;
    private String[] courses;

    public Regex(String _id, String[] courses) {
        this._id = _id;
        this.courses = courses;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    @Override
    public int getType() {
        return TYPE_REGEX;
    }

    @Override
    public String getTitle() {
        return _id;
    }

    @Override
    public String getCode() {
        return courses.length + " courses";
    }
}
