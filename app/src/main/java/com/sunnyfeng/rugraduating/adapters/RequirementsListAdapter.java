package com.sunnyfeng.rugraduating.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sunnyfeng.rugraduating.MainActivity;
import com.sunnyfeng.rugraduating.R;
import com.sunnyfeng.rugraduating.Requirement;
import com.sunnyfeng.rugraduating.RequirementsActivity;

import java.util.ArrayList;

public class RequirementsListAdapter extends RecyclerView.Adapter<RequirementsListAdapter.MyViewHolder> {
    private ArrayList<Requirement> requirements;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private View itemView;
        private TextView titleView;
        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            context = itemView.getContext();
            titleView = itemView.findViewById(R.id.req_title);
            progressBar = itemView.findViewById(R.id.req_progressBar);
        }
    }

    public RequirementsListAdapter(ArrayList<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public RequirementsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.requirement_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Requirement curReq = requirements.get(position);
        holder.titleView.setText(curReq.title);
        holder.progressBar.setMax(curReq.totalRequired);
        holder.progressBar.setProgress(curReq.alreadyCompleted);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, RequirementsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MainActivity.REQUIREMENT_INTENT_KEY, curReq);
                intent.putExtras(bundle);
                holder.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }
}