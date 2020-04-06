package com.sunnyfeng.rugraduating;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity_SuggestedCoursesRV_Adapter extends
        RecyclerView.Adapter<MainActivity_SuggestedCoursesRV_Adapter.MyViewHolder> {
    private ArrayList<Course> courses;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView titleView;
        private TextView codeView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.itemView = itemView;
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Course curCourse = courses.get(position);
        holder.titleView.setText(curCourse.title);
        holder.codeView.setText(curCourse.code);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, CourseInfoActivity.class);
                intent.putExtra(MainActivity.COURSE_INTENT_KEY, curCourse);
                holder.context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return courses.size();
    }
}