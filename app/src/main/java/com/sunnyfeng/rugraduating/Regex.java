package com.sunnyfeng.rugraduating;

public class Regex {

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
}
