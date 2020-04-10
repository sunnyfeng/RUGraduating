package com.sunnyfeng.rugraduating.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.R;

public class StringArrayAdapter extends
        RecyclerView.Adapter<StringArrayAdapter.MyViewHolder> {
    private String[] strings;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView displayView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.itemView = itemView;
            displayView = itemView.findViewById(R.id.string_display);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StringArrayAdapter(String[] strings) {
        this.strings = strings;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StringArrayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.string_display_item, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String string = strings[position];
        holder.displayView.setText(string);
        //TODO: maybe then search for this object and then pass it to course info ? what do we do on click
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(holder.context, CourseInfoActivity.class);
//            intent.putExtra(MainActivity.COURSE_INTENT_KEY, curCourse);
//            holder.context.startActivity(intent);
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return strings.length;
    }
}