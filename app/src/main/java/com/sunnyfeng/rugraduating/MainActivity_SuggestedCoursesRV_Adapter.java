package com.sunnyfeng.rugraduating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity_SuggestedCoursesRV_Adapter extends
        RecyclerView.Adapter<MainActivity_SuggestedCoursesRV_Adapter.MyViewHolder> {
    private ArrayList<Course> courses;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView titleView;
        private TextView codeView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.course_title);
            codeView = itemView.findViewById(R.id.course_code);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainActivity_SuggestedCoursesRV_Adapter(ArrayList<Course> courses) {
        this.courses = courses;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainActivity_SuggestedCoursesRV_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.course_list_item, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Course curCourse = courses.get(position);
        holder.titleView.setText(curCourse.title);
        holder.codeView.setText(curCourse.code);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return courses.size();
    }
}