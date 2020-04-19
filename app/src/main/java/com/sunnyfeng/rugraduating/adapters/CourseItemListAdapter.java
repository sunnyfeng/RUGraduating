package com.sunnyfeng.rugraduating.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.CourseInfoActivity;
import com.sunnyfeng.rugraduating.EquivInfoActivity;
import com.sunnyfeng.rugraduating.MajorActivity;
import com.sunnyfeng.rugraduating.ProfileActivity;
import com.sunnyfeng.rugraduating.R;
import com.sunnyfeng.rugraduating.RegexInfoActivity;
import com.sunnyfeng.rugraduating.objects.Course;
import com.sunnyfeng.rugraduating.objects.CourseItem;
import com.sunnyfeng.rugraduating.objects.Equivalency;
import com.sunnyfeng.rugraduating.objects.Regex;

import java.util.ArrayList;

public class CourseItemListAdapter extends
        RecyclerView.Adapter<CourseItemListAdapter.MyViewHolder> {
    private ArrayList<CourseItem> courses;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView titleView;
        private TextView codeView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.itemView = itemView;
            titleView = itemView.findViewById(R.id.course_item_title);
            codeView = itemView.findViewById(R.id.course_item_code);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseItemListAdapter(ArrayList<CourseItem> courses) {
        this.courses = courses;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
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
        final CourseItem courseItem = courses.get(position);
        holder.titleView.setText(courseItem.getTitle());
        holder.codeView.setText(courseItem.getSubtitle());

        // Go to more details
        holder.itemView.setOnClickListener(view -> {
            switch (courseItem.getType()){
                case CourseItem.TYPE_COURSE:
                    Intent intentCourse = new Intent(holder.context, CourseInfoActivity.class);
                    intentCourse.putExtra(MajorActivity.COURSE_INTENT_KEY, (Course) courseItem);
                    holder.context.startActivity(intentCourse);
                    break;
                case CourseItem.TYPE_EQUIV:
                    Intent intentEquiv = new Intent(holder.context, EquivInfoActivity.class);
                    intentEquiv.putExtra(MajorActivity.EQUIV_INTENT_KEY, (Equivalency) courseItem);
                    holder.context.startActivity(intentEquiv);
                    break;
                case CourseItem.TYPE_REGEX:
                    Intent intentRegex = new Intent(holder.context, RegexInfoActivity.class);
                    intentRegex.putExtra(MajorActivity.REGEX_INTENT_KEY, (Regex) courseItem);
                    holder.context.startActivity(intentRegex);
                    break;
            }
        });

        // Remove on long click
        holder.itemView.setOnLongClickListener(view -> {
            // only allow remove in ProfileActivity
            if(view.getContext() instanceof ProfileActivity) {
                courses.remove(position);
                // TODO: remove class from student's planned or taken course
                this.notifyDataSetChanged();
            }
            return true;
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return courses.size();
    }
}