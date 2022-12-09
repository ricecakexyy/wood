package com.xlyj.wood.find.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.find.adapter.VideoAdapter;
import com.xlyj.wood.find.adapter.VideoDetailAdapter;
import com.xlyj.wood.utils.DensityUtil;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/6
 * fun:
 */
public class VideoBriefFragment extends BaseFragment {
    private View view;
    TextView descriptionView;
    View layoutView, expandView; //LinearLayout布局和ImageView

    RecyclerView recyclerView;
    VideoDetailAdapter adapter;

    private Video video;
    private TextView tvName;
    RoundedImageView ivHead;
    List<Video> videos;
    public VideoBriefFragment(List<Video> videos) {
        super();
        this.videos = videos;
    }

//    public VideoBriefFragment(Video video){
//        this.video = video;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_fragment_video_brief, null, false);
        this.view = view;
        Bundle bundle =this.getArguments();//得到从Activity传来的数据
        video = (Video) bundle.getSerializable("video");
        initView();
        initRecycler();
        return view;
    }

    private void initRecycler() {
        adapter = new VideoDetailAdapter(getContext(),videos);
        recyclerView = view.findViewById(R.id.recommend_video);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void initView() {
        tvName = view.findViewById(R.id.tv_name);
        tvName.setText(video.getAuthor().getNickname());
        ivHead = view.findViewById(R.id.iv_head);
        Glide.with(getContext())
                .load(video.getAuthor().getAvatar())
                .into(ivHead);
        layoutView = view.findViewById(R.id.description_layout);
        descriptionView = view.findViewById(R.id.description_view);
        expandView = view.findViewById(R.id.expand_view);

        //设置文本
//        descriptionView.setText("台湾人传统早餐！无敌海景馒头+猪排火腿花生蛋||太和豆浆||台湾新北市板桥");
        descriptionView.setText("["+video.getTitle()+"]"+video.getBrief());

        //descriptionView设置默认显示高度
        int maxDescripLine = 1;
        descriptionView.setHeight(descriptionView.getLineHeight() * maxDescripLine);
        //根据高度来判断是否需要再点击展开
        descriptionView.post(new Runnable() {

            @Override
            public void run() {
                expandView.setVisibility(descriptionView.getLineCount() > maxDescripLine ? View.VISIBLE : View.GONE);
            }
        });

        layoutView.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;//是否已展开的状态

            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                descriptionView.clearAnimation();//清楚动画效果
                final int deltaValue ;//默认高度，即前边由maxLine确定的高度
                final int startValue = descriptionView.getHeight();//起始高度
                int durationMillis = 350;//动画持续时间
                if (isExpand) {
                    /**
                    * 折叠动画
                    * 从实际高度缩回起始高度
                    */

                    deltaValue = descriptionView.getLineHeight() * descriptionView.getLineCount()  - startValue+ DensityUtil.dp2px(getContext(),5);
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandView.startAnimation(animation);
                } else {
                    /**
                    * 展开动画
                    * 从起始高度增长至实际高度
                    */
                    deltaValue = descriptionView.getLineHeight() * maxDescripLine - startValue;
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandView.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) { //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
                        descriptionView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }
                };
                animation.setDuration(durationMillis);
                descriptionView.startAnimation(animation);
            }
        });

    }
}
