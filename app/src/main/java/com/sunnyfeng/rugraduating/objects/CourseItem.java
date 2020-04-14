package com.sunnyfeng.rugraduating.objects;

public interface CourseItem {
    int TYPE_COURSE = 101;
    int TYPE_EQUIV = 102;
    int TYPE_REGEX = 103;

    int getType();

    String getTitle();

    String getCode();
}
