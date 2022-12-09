package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Commodity;
import com.xlyj.wood.domain.ShopCart;
import com.xlyj.wood.shop.fragment.SCartFragment;
import com.xlyj.wood.ui.AmountView;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/27
 * fun:
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    List<ShopCart> myShopCart;
    CheckBox cbAllCheck;
    TextView tvTotalPrice;

    public CartAdapter(Context context, List<ShopCart> myShopCart, CheckBox cbAllCheck, TextView tvTotalPrice) {
        this.mContext = context;
        this.myShopCart = myShopCart;
        this.cbAllCheck = cbAllCheck;
        this.tvTotalPrice = tvTotalPrice;
        cbAllCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbAllCheck.isChecked()){
                    checkAll(true);
                }else{
                    checkAll(false);
                }
                notifyDataSetChanged();
            }
        });
    }

    void checkAll(boolean checkAll){
        double totalPrice = 0;
        for (int i = 0 ; i < myShopCart.size(); i++) {
            ShopCart shopCart = myShopCart.get(i);
            shopCart.setCheck(checkAll);
            if(checkAll){
                totalPrice += shopCart.getNumber() * shopCart.getCommodity().getPrice();
            }
        }
        if(checkAll){
            tvTotalPrice.setText(""+totalPrice);
        }else{
            tvTotalPrice.setText("0.00");
        }
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.s_cart_commodity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopCart shopCart = myShopCart.get(position);
        Commodity commodity = shopCart.getCommodity();
        Glide.with(mContext)
                .load(commodity.getPictures()[0])
                .into(holder.ivPicture);
        holder.tvName.setText(commodity.getName());
        holder.tvDetails.setText(commodity.getName());
        holder.tvPrice.setText("￥"+Double.toString(commodity.getPrice()));
        holder.amountView.setGoods_storage(50);
        holder.amountView.setAmount(shopCart.getNumber());
        holder.checkBox.setChecked(shopCart.isCheck());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shopCart.setCheck(isChecked);
                getTotalPrice();
            }
        });
        holder.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
//                Toast.makeText(mContext, "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                shopCart.setNumber(amount);
                getTotalPrice();
            }
        });


    }

    /**
     * 获得总价
     */
    private void getTotalPrice() {
        boolean isAllCheck = true;
        double totalPrice = 0;
        for (int i= 0; i<myShopCart.size(); i++) {
            ShopCart shopCart = myShopCart.get(i);
            if(shopCart.isCheck()) {
                totalPrice += shopCart.getNumber() * shopCart.getCommodity().getPrice();
            }else{
                isAllCheck = false;
            }
        }
        tvTotalPrice.setText(""+totalPrice);
        if(isAllCheck){
            cbAllCheck.setChecked(true);
        }else {
            cbAllCheck.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return myShopCart.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        AmountView amountView;
        ImageView ivPicture;
        TextView tvName;
        TextView tvDetails;
        TextView tvPrice;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountView = itemView.findViewById(R.id.amount_view);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvDetails = itemView.findViewById(R.id.tv_details);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
