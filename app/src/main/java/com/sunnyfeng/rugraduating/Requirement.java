package com.sunnyfeng.rugraduating;

import java.io.Serializable;

/**
 * Holds requirement information.
 */
public class Requirement implements Serializable {
    String title;
    boolean isCredits;
    int totalRequired;
    int alreadyCompleted;

    /**
     * @param title name of requirement
     * @param isCredits if the req is defined by total credits or total number of courses
     * @param totalRequired total number required to fulfill requirement
     * @param alreadyCompleted number of courses/credits already completed
     */
    public Requirement(String title, boolean isCredits, int totalRequired, int alreadyCompleted)
    {
        this.title = title;
        this.isCredits = isCredits;
        this.totalRequired = totalRequired;
        this.alreadyCompleted = alreadyCompleted;
    }

}
