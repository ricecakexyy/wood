package com.xlyj.slqj.SLQJ.Museum.Exhibition;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.slqj.R;

import java.util.List;

public class DetailedExhibitionAdapter extends RecyclerView.Adapter<DetailedExhibitionAdapter.ViewHolder> {
    private List<DetailedExhibition> mEhList;

    public DetailedExhibitionAdapter(List<DetailedExhibition> mEhList){
        this.mEhList=mEhList;
    }
    @Override
    public DetailedExhibitionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_exhibition_item, parent, false);
        DetailedExhibitionAdapter.ViewHolder holder = new DetailedExhibitionAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedExhibitionAdapter.ViewHolder holder, int position) {
        DetailedExhibition eh=mEhList.get(position);
        holder.ehImage.setImageResource(eh.getImageId());
        holder.ehName.setText(eh.getName());
        holder.lochall.setText(eh.getLoc_hall());
        holder.locf.setText(eh.getLoc_f());
        holder.looks.setText(eh.getLooks()+"");

    }

    @Override
    public int getItemCount() {
        return mEhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ehImage;
        TextView ehName,locf,lochall,looks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ehImage=itemView.findViewById(R.id.iv_de);
            ehName=itemView.findViewById(R.id.tv_de_name);
            lochall=itemView.findViewById(R.id.tv_de_lochall);
            locf=itemView.findViewById(R.id.tv_de_locf);
            looks=itemView.findViewById(R.id.tv_de_looks);



            ViewGroup.LayoutParams params = ehImage.getLayoutParams();
            int width = ((Activity) ehImage.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            //设置图片的相对于屏幕的宽高比
            params.width = width/2;
            params.height =  (int) (200 + Math.random() * 400) ;
            ehImage.setLayoutParams(params);


        }
    }

}
