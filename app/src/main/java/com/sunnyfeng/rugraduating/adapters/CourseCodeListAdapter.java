package com.sunnyfeng.rugraduating.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.R;

public class CourseCodeListAdapter extends
        RecyclerView.Adapter<CourseCodeListAdapter.MyViewHolder> {
    private String[] courseCodes;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView displayView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.itemView = itemView;
            displayView = itemView.findViewById(R.id.course_code_display);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseCodeListAdapter(String[] courseCodes) {
        this.courseCodes = courseCodes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseCodeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.course_code_list_item, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String courseCode = courseCodes[position];
        holder.displayView.setText(courseCode);
        //TODO: maybe then search for this object and then pass it to course info
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(holder.context, CourseInfoActivity.class);
//            intent.putExtra(MainActivity.COURSE_INTENT_KEY, curCourse);
//            holder.context.startActivity(intent);
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return courseCodes.length;
    }
}