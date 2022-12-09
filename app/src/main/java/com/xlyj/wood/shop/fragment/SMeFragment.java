package com.xlyj.wood.shop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/25
 * fun:
 */
public class SMeFragment extends Fragment {

    private View view;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.s_fragment_me, container, false);
        initView();
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        ImageView ivHead = view.findViewById(R.id.iv_head);
        TextView tvName = view.findViewById(R.id.tv_username);
        user = BmobUser.getCurrentUser(User.class);
        Glide.with(getContext())
                .load(user.getAvatar())
                .into(ivHead);
        tvName.setText(user.getNickname());
    }
}
