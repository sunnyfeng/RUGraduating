package com.sunnyfeng.rugraduating.objects;

// Interface to display the types of course items
public interface CourseItem {
    int TYPE_COURSE = 101;
    int TYPE_EQUIV = 102;
    int TYPE_REGEX = 103;

    // Returns type of item
    int getType();

    // Returns the title that should be displayed to the user
    String getTitle();

    // Returns the subtitle that should be displayed under
    String getSubtitle();
}
