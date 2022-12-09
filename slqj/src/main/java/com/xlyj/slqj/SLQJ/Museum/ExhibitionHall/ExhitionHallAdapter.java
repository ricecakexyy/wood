package com.xlyj.slqj.SLQJ.Museum.ExhibitionHall;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.ExhibitionHallShowActivity;

import java.util.List;

public class ExhitionHallAdapter extends RecyclerView.Adapter<ExhitionHallAdapter.ViewHolder> {
    private List<ExhibitionHall> mEhList;
    private Context context;

    public ExhitionHallAdapter(List<ExhibitionHall> mEhList, Context context){
        this.mEhList=mEhList;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibitionhall_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ExhibitionHall eh=mEhList.get(position);
    holder.ehImage.setImageResource(eh.getImageId());
    holder.ehName.setText(eh.getName());
    holder.ehLoc.setText(eh.getLocation());
        holder.ehImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ExhibitionHallShowActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ehImage;
        TextView ehName;
        TextView ehLoc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ehImage=itemView.findViewById(R.id.iv_eh);
            ehName=itemView.findViewById(R.id.tv_eh_name);
            ehLoc=itemView.findViewById(R.id.tv_eh_loc);
        }
    }
}
