package com.xlyj.wood.pet.Task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.R;
import com.xlyj.wood.home.activity.CultureActivity;
import com.xlyj.wood.pet.TestActivity;
import com.xlyj.wood.shop.ShopActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> mEhList;
    private Context context;
    int bgs[]={R.drawable.pet_task_bg1_shape,R.drawable.pet_task_bg2_shape,R.drawable.pet_task_bg3_shape};

    public TaskAdapter(List<Task> mEhList, Context context) {

        this.mEhList = mEhList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task eh = mEhList.get(position);
        holder.ehImage.setImageResource(eh.getImageId());
        holder.ehtitle.setText(eh.getTitle());
        holder.ehcontent.setText(eh.getContent());

        holder.btn_gototask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, CultureActivity.class));
            }
        });
        int bg=position%3;
        holder.rl.setBackgroundResource(bgs[bg]);

    }

    @Override
    public int getItemCount() {
        return mEhList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ehImage;
        TextView ehtitle, ehcontent;
        Button btn_gototask;
        RelativeLayout rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ehImage = itemView.findViewById(R.id.iv_pet_task_item_logo);
            ehtitle = itemView.findViewById(R.id.tv_pet_task_item_title);
            ehcontent = itemView.findViewById(R.id.tv_pet_task_item_content);
            btn_gototask=itemView.findViewById(R.id.btn_pet_task_item);
            rl=itemView.findViewById(R.id.rl_pet_task_item);
        }
    }
}
