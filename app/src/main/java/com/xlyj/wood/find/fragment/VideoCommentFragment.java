package com.xlyj.wood.find.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.domain.VideoComment;
import com.xlyj.wood.domain.VideoReplay;
import com.xlyj.wood.find.adapter.VideoCommentAdapter;
import com.xlyj.wood.ui.ProgressDialog;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/6
 * fun:
 */
public class VideoCommentFragment extends BaseFragment {
    private View view;
    private Video video;
    private VideoCommentAdapter adapter;
    private ExpandableListView expandableListView;
    TextView tvSendComment;

    EditText etComment;
    List<VideoComment> comments;
    List<List<VideoReplay>> allReplays;

    private final int GET_COMMENT_SUCCESS = 1;
    private final int GET_COMMENT_FAIL = 2;
    private final int REPLAYS_INITED = 3;
    int replaysNumber = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_COMMENT_SUCCESS:
                    initView();
                    break;
                case GET_COMMENT_FAIL:
                    break;
                case REPLAYS_INITED:
                    replaysNumber++;
                    if (replaysNumber == comments.size())
                        initExpandableListView(comments);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_fragment_video_comment, null, false);
        this.view = view;
        getDate();
        setDate();
        return view;
    }

    private void getDate() {
        Bundle bundle = this.getArguments();//得到从Activity传来的数据
        video = (Video) bundle.getSerializable("video");
    }

    private void setDate() {
        comments = new ArrayList<>();
        BmobQuery<VideoComment> query = new BmobQuery<VideoComment>();
        query.addWhereRelatedTo("comments", new BmobPointer(video));
        query.include("user");
        query.order("-date");
        query.findObjects(new FindListener<VideoComment>() {
            @Override
            public void done(List<VideoComment> object, BmobException e) {
                if (e == null) {
                    comments = object;
                    Log.i("ShowActivity", "得到评论" + object.size() + "个" + object.isEmpty());
                    handler.sendEmptyMessage(GET_COMMENT_SUCCESS);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                    handler.sendEmptyMessage(GET_COMMENT_SUCCESS);
                }
            }
        });
    }

    private void initView() {
        etComment = view.findViewById(R.id.detail_page_do_comment);
        expandableListView = view.findViewById(R.id.detail_page_lv_comment);
        tvSendComment = view.findViewById(R.id.tv_send_comment);
        tvSendComment.setOnClickListener(new VCOnClickListener());

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    tvSendComment.setTextColor(Color.parseColor("#9B6C48"));
                } else {
                    tvSendComment.setTextColor(Color.parseColor("#555555"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initReplays(comments);
    }

    private class VCOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_send_comment:
                    doComment();
                    break;
            }
        }
    }

    private void initReplays(List<VideoComment> comments) {
        allReplays = new ArrayList<>();
        for (VideoComment comment : comments) {
            allReplays.add(null);
        }
        for (int i = 0; i < comments.size(); i++) {
            VideoComment comment = comments.get(i);
            final List<VideoReplay>[] replays1 = new List[]{new ArrayList<>()};
            BmobQuery<VideoReplay> query = new BmobQuery<VideoReplay>();
            query.addWhereRelatedTo("replays", new BmobPointer(comment));
            query.include("user");
            int finalI = i;
            query.findObjects(new FindListener<VideoReplay>() {
                @Override
                public void done(List<VideoReplay> object, BmobException e) {
                    if (e == null) {
                        replays1[0] = object;
                        allReplays.set(finalI, replays1[0]);
                        Log.i("bmob回复", "查询个数：" + object.size());
                        handler.sendEmptyMessage(REPLAYS_INITED);
                    } else {
                        Log.i("bmob回复", "失败：" + e.getMessage() + "错误码" + e.getErrorCode());
                    }
                }
            });
        }
    }


    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(List<VideoComment> comments) {
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new VideoCommentAdapter(getContext(), comments, allReplays);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < comments.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                showReplyDialog(groupPosition);
                return true;
            }
        });
    }

    /**
     * by moos on 2018/04/20
     * func:进行评论
     */
    private void doComment() {
        String commentContent = etComment.getText().toString().trim();
        if(!BmobUser.isLogin()){
            Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(commentContent)) {
            etComment.clearFocus();
            ((InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            etComment.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            VideoComment comment = new VideoComment();
            comment.setDate(new BmobDate(new Date()));
            comment.setComment(commentContent);
            comment.setVideo(video);
            comment.setUser(BmobUser.getCurrentUser(User.class));
            comment.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        BmobRelation relation = new BmobRelation();
                        relation.add(comment);
                        video.setComments(relation);
                        video.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    if (comments == null || comments.isEmpty())
                                        setDate();
                                    else
                                        adapter.addTheCommentData(comment);
                                    Log.i("bmob", "多对多关联添加成功");
                                } else {
                                    Log.i("bmob", "失败：" + e.getMessage() + e.getErrorCode());
                                }
                            }

                        });
                        etComment.setText("");
                        Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "错误码" + e.getErrorCode());
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position) {
        etComment.setFocusable(true);
        etComment.setFocusableInTouchMode(true);
        etComment.requestFocus();
        etComment.getShowSoftInputOnFocus();
        User user = comments.get(position).getUser();
        String name = user.getNickname() == "" ? user.getUsername() : user.getNickname();
        etComment.setHint("回复 " + name + " 的评论:");
        tvSendComment.setText("进行回复");
        tvSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = etComment.getText().toString().trim();
                if (!TextUtils.isEmpty(replyContent)) {
                    etComment.clearFocus();
                    ((InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    etComment.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    VideoReplay replay = new VideoReplay();
                    replay.setReplay(replyContent);
                    replay.setComment(comments.get(position));
                    replay.setUser(BmobUser.getCurrentUser(User.class));
                    replay.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                BmobRelation relation = new BmobRelation();
                                relation.add(replay);
                                comments.get(position).setReplays(relation);
                                comments.get(position).update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Log.i("bmob", "多对多关联添加成功");
                                            adapter.addTheReplyData(replay, position);
                                            expandableListView.expandGroup(position);
                                            Toast.makeText(getContext(), "回复成功", Toast.LENGTH_SHORT).show();
                                            etComment.setText("");
                                        } else {
                                            Log.i("bmob", "多对多添加失败：" + e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                Log.i("bmob", "评论失败：" + e.getMessage());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
