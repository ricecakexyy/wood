package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.domain.VideoComment;
import com.xlyj.wood.domain.VideoReplay;
import com.xlyj.wood.utils.DensityUtil;
import com.xlyj.wood.utils.StringFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Moos
 * E-mail: moosphon@gmail.com
 * Date:  18/4/20.
 * Desc: 评论与回复列表的适配器
 */

public class VideoCommentAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CommentExpandAdapter";
    int size;
    boolean isLike = false;
    private List<VideoComment> commentBeanList;
    //    private List<ReplyDetailBean> replyBeanList;
    private Context context;
    private int pageIndex = 1;
    private List<List<VideoReplay>> replays;

    public VideoCommentAdapter(Context context, List<VideoComment> commentBeanList, List<List<VideoReplay>> replays) {
        this.context = context;
        this.commentBeanList = commentBeanList;
        this.replays = replays;
    }

    @Override
    public int getGroupCount() {
        return commentBeanList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return replays.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return commentBeanList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return commentBeanList.get(i).getReplays().getObjects().get(i1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpand, View convertView, ViewGroup viewGroup) {
        final GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.f_video_comment_item_layout, viewGroup, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        VideoComment comment = commentBeanList.get(groupPosition);
        User user = comment.getUser();
        Glide.with(context)
                .load(user.getAvatar())
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);

        if (user.getNickname() != null && user.getNickname() != "")
            groupHolder.tv_name.setText(user.getNickname());
        else
            groupHolder.tv_name.setText(user.getUsername());
        groupHolder.tv_time.setText(comment.getDate().getDate());

        /**
         * 文本展开
         */
        groupHolder.view_seemore_tvlinecount.setText(comment.getComment());
        if(groupHolder.view_seemore_tvlinecount.getLineCount()>6){
            groupHolder.view_seemore_tv_seemore.setVisibility(View.VISIBLE);
        }else{
            groupHolder.view_seemore_tv_seemore.setVisibility(View.GONE);
        }
        groupHolder.tv_content.setText(comment.getComment());
        groupHolder.view_seemore_tv_seemore.setText("查看更多");
        groupHolder.tv_content.setMaxLines(6);
        groupHolder.view_seemore_tv_seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupHolder.tv_content.getMaxLines() == 20){
                    //这是收起的关键代码，将最大行数设置为你想展示的最小行数即可
                    groupHolder.tv_content.setMaxLines(6);
                    groupHolder.view_seemore_tv_seemore.setText("查看更多");
                }else{
                    //这是查看更多的关键代码，将最大行数设置一个大数即可
                    groupHolder.tv_content.setMaxLines(20);
                    groupHolder.view_seemore_tv_seemore.setText( "收起");
                }
            }
        });


        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLike) {
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#000000"));
                } else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.f_video_comment_reply_item_layout, viewGroup, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        VideoReplay replay = replays.get(groupPosition).get(childPosition);
        User replyUser = replay.getUser();
        String name;
        name = replyUser.getNickname() + ":";
        StringFormatUtil spanStr = new StringFormatUtil(context,name+ replay.getReplay(),
                name,R.color.bk_blue).fillColor();
        childHolder.tv_content.setText(spanStr.getResult());
        if(childPosition == (replays.get(groupPosition).size()-1)){
            childHolder.tv_more.setVisibility(View.VISIBLE);
        }
//        childHolder.tv_content.setText(childHolder.tv_name.getText()+replay.getReplay());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * by moos on 2018/04/20
     * func:评论成功后插入一条数据
     *
     * @param commentDetailBean 新的评论数据
     */
    public void addTheCommentData(VideoComment commentDetailBean) {
        if (commentBeanList==null || commentBeanList.isEmpty()) {
            Log.i(TAG, "评论为空" + commentBeanList.size());
            commentBeanList = new ArrayList<>();
            commentBeanList.set(0,commentDetailBean);
        }else {
            Log.i(TAG, "添加评论");
        commentBeanList.add(commentDetailBean);
        }
        replays.add(new ArrayList<>());
        replays.set(commentBeanList.size() - 1, new ArrayList<>());
        notifyDataSetChanged();
    }

    /**
     * by moos on 2018/04/20
     * func:回复成功后插入一条数据
     *
     * @param replay 新的回复数据
     */
    public void addTheReplyData(VideoReplay replay, int groupPosition) {
        replays.get(groupPosition).add(replay);
        notifyDataSetChanged();
    }

    private class GroupHolder {
        private RoundedImageView logo;
        private TextView tv_name, tv_content, tv_time;
        private ImageView iv_like;
        private TextView view_seemore_tvlinecount, view_seemore_tv_seemore;

        public GroupHolder(View view) {
            logo = view.findViewById(R.id.comment_item_logo);
            tv_content = view.findViewById(R.id.comment_item_content);
            tv_name = view.findViewById(R.id.comment_item_userName);
            tv_time = view.findViewById(R.id.comment_item_time);
            iv_like = view.findViewById(R.id.comment_item_like);
            view_seemore_tv_seemore = view.findViewById(R.id.view_seemore_tv_seemore);
            view_seemore_tvlinecount = view.findViewById(R.id.view_seemore_tvlinecount);
        }
    }

    private class ChildHolder {
        private TextView  tv_content,tv_more;
        private List<VideoReplay> replays;

        public ChildHolder(View view) {
            tv_content = view.findViewById(R.id.reply_item_content);
            tv_more = view.findViewById(R.id.tv_more);
        }
    }
//
//    /**
//     * by moos on 2018/04/20
//     * func:添加和展示所有回复
//     * @param replyBeanList 所有回复数据
//     * @param groupPosition 当前的评论
//     */
//    private void addReplyList(List<ReplyDetailBean> replyBeanList, int groupPosition){
//        if(commentBeanList.get(groupPosition).getReplyList() != null ){
//            commentBeanList.get(groupPosition).getReplyList().clear();
//            commentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
//        }else {
//
//            commentBeanList.get(groupPosition).setReplyList(replyBeanList);
//        }
//
//        notifyDataSetChanged();
//    }

}
