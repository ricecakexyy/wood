package com.xlyj.slqj.SLQJ.Museum.Exhibition;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.ExhibitionShowActivity;

import java.util.List;

public class ExhitionAdapter extends RecyclerView.Adapter<ExhitionAdapter.ViewHolder> {
    private List<Exhibition> mEhList;
    private Context context;

    public ExhitionAdapter(List<Exhibition> mEhList,Context context){

        this.mEhList=mEhList;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exhibition_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Exhibition eh=mEhList.get(position);
    holder.ehImage.setImageResource(eh.getImageId());
        holder.ehImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ExhibitionShowActivity.class));
            }
        });
    //holder.ehName.setText(eh.getName());

    }

    @Override
    public int getItemCount() {
        return mEhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ehImage;
        //TextView ehName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ehImage=itemView.findViewById(R.id.iv_e);
            //ehName=itemView.findViewById(R.id.tv_e_name);
        }
    }
}
