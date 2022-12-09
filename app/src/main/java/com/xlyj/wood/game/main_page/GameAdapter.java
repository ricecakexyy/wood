package com.xlyj.wood.game.main_page;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.R;
import com.xlyj.wood.game.carving.CarvingActivity;
import com.xlyj.wood.game.jigsaw_puzzle.activity.JigsawHomeActivity;
import com.xlyj.wood.pet.PetShowActivity;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/14
 * fun:
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.viewHolder> {
    Context myContext;
    int[] headSid={R.drawable.game_head1, R.drawable.game_head2,R.drawable.game_head3};
    String[] game= {
            "木雕拼图","木宝宝","木雕模拟"
    } ;
    int[] picSid={
      R.drawable.game1,R.drawable.game2,R.drawable.game3
    };
    String[] des = {
            "一起来完成拼图游戏吧",
            "领养属于你的木宝宝",
            "成为雕刻大师"
    };
    public GameAdapter(Context context){
        this.myContext = context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContext).inflate(R.layout.game_adapter, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tvGame.setText(game[position]);
        holder.tvDes.setText(des[position]);
        holder.ivPicture.setImageResource(picSid[position]);
        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(position == 0){
                    intent.setClass(myContext, JigsawHomeActivity.class);
                }else if(position == 1){
                    intent.setClass(myContext, PetShowActivity.class);
                }else if(position == 2){
                    intent.setClass(myContext, CarvingActivity.class);
                }
                myContext.startActivity(intent);
            }
        });
        holder.ivHead.setImageResource(headSid[position]);
    }

    @Override
    public int getItemCount() {
        return picSid.length;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView ivPicture, ivHead;
        TextView tvGame, tvDes;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ivHead = itemView.findViewById(R.id.iv_head);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvDes = itemView.findViewById(R.id.tv_des);
            tvGame = itemView.findViewById(R.id.tv_game);
        }
    }
}
