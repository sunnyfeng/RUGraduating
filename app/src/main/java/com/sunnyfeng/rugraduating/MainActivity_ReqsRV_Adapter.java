package com.sunnyfeng.rugraduating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity_ReqsRV_Adapter extends RecyclerView.Adapter<MainActivity_ReqsRV_Adapter.MyViewHolder> {
    private ArrayList<Requirement> requirements;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.req_title);
            progressBar = itemView.findViewById(R.id.req_progressBar);
        }
    }

    public MainActivity_ReqsRV_Adapter(ArrayList<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public MainActivity_ReqsRV_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.requirement_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Requirement curReq = requirements.get(position);
        holder.titleView.setText(curReq.title);
        holder.progressBar.setMax(curReq.totalRequired);
        holder.progressBar.setProgress(curReq.alreadyCompleted);
    }

    @Override
    public int getItemCount() {
        return requirements.size();
    }
}