package com.xlyj.wood.find.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.domain.VideoComment;
import com.xlyj.wood.domain.VideoDanmaku;
import com.xlyj.wood.find.OnDoubleClickListener;
import com.xlyj.wood.find.fragment.BaseFragment;
import com.xlyj.wood.find.fragment.VideoBriefFragment;
import com.xlyj.wood.find.fragment.VideoCommentFragment;
import com.xlyj.wood.utils.ConstantUtil;
import com.xlyj.wood.utils.StatusBarUtil;
import com.xlyj.wood.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class VideoDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.video_tab_layout)
    TabLayout videoTabLayout;
    @BindView(R.id.video_view_pager)
    ViewPager videoViewPager;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

    GestureDetector gestureDetector;

    Video video;
    @BindView(R.id.rl_video_view)
    RelativeLayout rlVideoView;
    @BindView(R.id.ll_seek_bar)
    LinearLayout llSeekBar;
    @BindView(R.id.video_seek_bar)
    SeekBar videoSeekBar;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.danmu)
    DanmakuView mDanmakuView;
    @BindView(R.id.et_send_dammu)
    EditText etSendDammu;
    private DanmakuContext mContext;
    private BaseDanmakuParser mParser;//解析器对象
    private int position;

    VideoView videoView;
    TimeUtils utils;
    List<Video> videos;
    private BottomSheetDialog dammuDialog;


    /**
     * 测试数据
     */
//    private int[] videos_url = {R.raw.video1, R.raw.video2, R.raw.video3, R.raw.video4, R.raw.video5};


    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    private static final int UPDATE_UI = 1;
    private int oldProgress = 0;

    private static final int PROGRESS = 1; //处理进度条
    private static final int GET_DANMUKU_SUCCESS = 2; //成功获得弹幕
    private static final int GET_DANMUKU_FAIL = 3; //获得弹幕失败
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS://处理进度条

                    //1.得到当前的视频播放进程
                    int currentPosition = videoView.getCurrentPosition();//0

                    //2.SeekBar.setProgress(当前进度);
                    videoSeekBar.setProgress(currentPosition);
                    tvCurrent.setText(utils.stringForTime(currentPosition));
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
                case GET_DANMUKU_SUCCESS:
                    for(int i = 0; i<danmakus.size() ; i++){
                        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                        if (danmaku == null || mDanmakuView == null) {
                            return;
                        }
                        Log.e( "handleMessage: ", ""+i);
                        VideoDanmaku myDanmaku = danmakus.get(i);
                        //弹幕显示的文字
                        danmaku.text = myDanmaku.getText();
                        //设置相应的边距，这个设置的是四周的边距
                        danmaku.padding = 5;
                        // 可能会被各种过滤器过滤并隐藏显示，若果是本机发送的弹幕，建议设置成1；
                        danmaku.priority = 0;
                        //是否是直播弹幕
                        danmaku.isLive = false;
                        long time = myDanmaku.getTime();
                        danmaku.setTime(time);
                        //设置文字大小
                        danmaku.textSize = myDanmaku.getTextSize();
                        //设置文字颜色
                        danmaku.textColor = myDanmaku.getTextColor();
                        // danmaku.underlineColor = Color.GREEN;
                        //添加这条弹幕，也就相当于发送
                        mDanmakuView.addDanmaku(danmaku);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_video_details);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        initDate();
        initDanmu();
        initView();
        setOnClickListener();
    }

    /**
     * 初始化弹幕
     */
    private void initDanmu() {
        //设置最大显示行数
        HashMap<Integer, Integer> maxLInesPair = new HashMap<>(16);
        maxLInesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 8);
        //设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>(16);
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        //创建弹幕上下文
        mContext = DanmakuContext.create();
        //设置一些相关的配置
        mContext.setDuplicateMergingEnabled(false)
                //是否重复合并
                .setScrollSpeedFactor(1.2f)
                //设置文字的比例
                .setScaleTextSize(1.2f)
                //设置显示最大行数
                .setMaximumLines(maxLInesPair)
                //设置防，null代表可以重叠
                .preventOverlapping(overlappingEnablePair);
        if (mDanmakuView != null) {
            //设置解析器
            BaseDanmakuParser defaultDanmakuParser = getDefaultDanmakuParser();
            getAllDanmaku();
            //相应的回掉
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                    //定时器更新的时候回掉
                }

                @Override
                public void drawingFinished() {
                    //弹幕绘制完成时回掉
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                    //弹幕展示的时候回掉
                }

                @Override
                public void prepared() {
                    //弹幕准备好的时候回掉，这里启动弹幕
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(defaultDanmakuParser, mContext);
            mDanmakuView.enableDanmakuDrawingCache(true);

        }

        etSendDammu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDammuDialog();
            }
        });
    }

    List<VideoDanmaku> danmakus;

    /**
     * 显示已有弹幕
     */
    private void getAllDanmaku() {

        BmobQuery<VideoDanmaku> query = new BmobQuery<VideoDanmaku>();
        query.addWhereRelatedTo("danmakus", new BmobPointer(video));
        query.include("user");
        query.findObjects(new FindListener<VideoDanmaku>() {
            @Override
            public void done(List<VideoDanmaku> object, BmobException e) {
                if (e == null) {
                    danmakus = object;
                    Log.e("VideoDetails", "得到弹幕" + object.size() + "个" + object.isEmpty());
                    handler.sendEmptyMessage(GET_DANMUKU_SUCCESS);
                } else {
                    Log.e("VideoDetails", "失败：" + e.getMessage());
                }
            }
        });
    }

    TextView tvSize, tvColor;
    /**
     * 弹出弹幕框
     */
    private void showDammuDialog() {
        dammuDialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.f_video_dammu_dialog, null);
        EditText etDanmu = commentView.findViewById(R.id.et_danmu);
        TextView tvDanmu = commentView.findViewById(R.id.tv_danmu);
        TextView tvText = commentView.findViewById(R.id.tv_text);
        LinearLayout llText = commentView.findViewById(R.id.ll_text_style);

        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llText.getVisibility() == View.VISIBLE) {
                    llText.setVisibility(View.GONE);
                } else {
                    llText.setVisibility(View.VISIBLE);
                }
            }
        });
        tvSize = commentView.findViewById(R.id.tv_text_size);
        if(textSize == 45f)
            tvSize.setText("大字号");
        else if(textSize == 25f)
            tvSize.setText("小字号");
        else
            tvSize.setText("正常");
        tvColor = commentView.findViewById(R.id.tv_text_color);
        tvColor.setTextColor(textColor);
        commentView.findViewById(R.id.iv_text_big).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_text_small).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_text_normal).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_white).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_red).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_green).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_deep_blue).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_yellow).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_pink).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_orange).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_blue).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_black).setOnClickListener(new DanmuClick());
        commentView.findViewById(R.id.iv_purple).setOnClickListener(new DanmuClick());

        dammuDialog.setContentView(commentView);
        etDanmu.setFocusable(true);
        etDanmu.setFocusableInTouchMode(true);
        etDanmu.requestFocus();
        etDanmu.getShowSoftInputOnFocus();

        tvDanmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDanmaku(false, etDanmu.getText().toString().trim(), textSize, textColor);
                etDanmu.clearFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                etDanmu.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                etDanmu.setText("");
                dammuDialog.dismiss();
                Toast.makeText(VideoDetailsActivity.this, "弹幕发送成功", Toast.LENGTH_SHORT).show();
            }
        });
        dammuDialog.show();
    }

    float textSize = 35f;
    int textColor = Color.BLACK;

    /**
     * 弹幕样式
     */
    public class DanmuClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_text_big:
                    textSize = 45f;
                    tvSize.setText("大字号");
                    break;
                case R.id.iv_text_small:
                    textSize = 25f;
                    tvSize.setText("小字号");
                    break;
                case R.id.iv_text_normal:
                    textSize = 35f;
                    tvSize.setText("正常");
                    break;
                case R.id.iv_white:
                    textColor = Color.WHITE;
                    tvColor.setTextColor(Color.WHITE);
                    break;
                case R.id.iv_red:
                    textColor = Color.RED;
                    tvColor.setTextColor(Color.RED);
                    break;
                case R.id.iv_green:
                    textColor = Color.GREEN;
                    tvColor.setTextColor(Color.GREEN);
                    break;
                case R.id.iv_deep_blue:
                    textColor = Color.BLUE;
                    tvColor.setTextColor(Color.BLUE);
                    break;
                case R.id.iv_black:
                    textColor = Color.BLACK;
                    tvColor.setTextColor(Color.BLACK);
                    break;
                case R.id.iv_yellow:
                    textColor = Color.YELLOW;
                    tvColor.setTextColor(Color.YELLOW);
                    break;
                case R.id.iv_purple:
                    textColor = Color.rgb(103, 58, 183);
                    tvColor.setTextColor( Color.rgb(103, 58, 183));
                    break;
                case R.id.iv_orange:
                    textColor = Color.rgb(255, 87, 34);
                    tvColor.setTextColor( Color.rgb(255, 87, 34));
                    break;
                case R.id.iv_pink:
                    textColor = Color.rgb(233, 30, 99);
                    tvColor.setTextColor( Color.rgb(233, 30, 99));
                    break;
                case R.id.iv_blue:
                    textColor = Color.rgb(0, 188, 212);
                    tvColor.setTextColor( Color.rgb(0, 188, 212));
                    break;
            }
        }
    }

    public static BaseDanmakuParser getDefaultDanmakuParser() {
        return new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
    }

    /**
     * 增加一条弹幕
     * @param islive
     * @param text
     * @param textSize
     * @param textColor
     */
    private void addDanmaku(boolean islive, String text, float textSize, int textColor) {
        //创建一个弹幕对象，这里后面的属性是设置滚动方向的！
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        //弹幕显示的文字
        danmaku.text = text;
        //设置相应的边距，这个设置的是四周的边距
        danmaku.padding = 5;
        // 可能会被各种过滤器过滤并隐藏显示，若果是本机发送的弹幕，建议设置成1；
        danmaku.priority = 0;
        //是否是直播弹幕
        danmaku.isLive = islive;
        long time = mDanmakuView.getCurrentTime();
        danmaku.setTime(time );
        //设置文字大小
        danmaku.textSize = textSize;
        //设置文字颜色
        danmaku.textColor = textColor;
        // danmaku.underlineColor = Color.GREEN;
        //添加这条弹幕，也就相当于发送
        mDanmakuView.addDanmaku(danmaku);
        VideoDanmaku mydanmaku = new VideoDanmaku();
        mydanmaku.setText(text);
        mydanmaku.setUser(BmobUser.getCurrentUser(User.class));
        mydanmaku.setTextColor(textColor);
        mydanmaku.setTextSize((int) textSize);
        mydanmaku.setTime( time);
        mydanmaku.setVideo(video);
        mydanmaku.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    BmobRelation relation = new BmobRelation();
                    relation.add(mydanmaku);
                    video.setDanmakus(relation);
                    video.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("VideoDetailsActivity", "添加弹幕成功");
                            } else {
                                Log.i("VideoDetailsActivity", "失败：" + e.getMessage() + e.getErrorCode());
                            }
                        }

                    });
                }else{

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }


    /**
     * 初始化数据
     */
    private void initDate() {
        video = (Video) getIntent().getSerializableExtra("video");
        videos = (List<Video>)getIntent().getSerializableExtra("allVideo");
        position = getIntent().getIntExtra("position", 0);
        utils = new TimeUtils();
    }

    private void initView() {
        VideoBriefFragment videoBriefFragment = new VideoBriefFragment(videos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("video", video);
        videoBriefFragment.setArguments(bundle);//数据传递到fragment中
        fragments.add(videoBriefFragment);
        VideoCommentFragment videoCommentFragment = new VideoCommentFragment();
        videoCommentFragment.setArguments(bundle);
        fragments.add(videoCommentFragment);

        videoTabLayout.setupWithViewPager(videoViewPager);
        videoTabLayout.setTabIndicatorFullWidth(false);

        videoViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));

        videoTabLayout.getTabAt(0).setText("简介");
        videoTabLayout.getTabAt(1).setText("评论");

        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("点击播放视频");
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);//设置收缩后标题的位置
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后标题的颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);//设置收缩后标题的颜色

        videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(ConstantUtil.vedio_net+video.getVideo_url()));
//        videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + videos_url[position % videos_url.length]));
        videoView.start();

        setNoMove();

    }


    /**
     * 设置不可滑动
     */
    public void setNoMove() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        //设置不能滑动
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbarLayout.getLayoutParams();
    }

    /**
     * 设置可滑动
     */
    public void setMove() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
//可以滑动，实现折叠悬浮效果
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbarLayout.getLayoutParams();
    }

    boolean seekBarVis = true;

    /**
     * 点击事件
     */
    private void setOnClickListener() {
        ivBack.setOnClickListener(new VideoDetailsClick());
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = videoView.getDuration();
                videoSeekBar.setMax(duration);
                handler.sendEmptyMessage(PROGRESS);
                tvDuration.setText("/" + utils.stringForTime(duration));
                videoView.start();
                ivPause.setImageResource(R.drawable.f_video_pause_small_icon);
            }
        });
        rlVideoView.setOnClickListener(new VideoDetailsClick());
        videoView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.ClickCallBack() {
            @Override
            public void onClick() {
                if (seekBarVis) {
                    seekBarVis = false;
                    llSeekBar.setVisibility(View.GONE);
                } else {
                    seekBarVis = true;
                    llSeekBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDoubleClick() {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivPause.setImageResource(R.drawable.f_video_start_small_icon);
                    mDanmakuView.pause();
                } else {
                    videoView.start();
                    ivPause.setImageResource(R.drawable.f_video_pause_small_icon);
                    mDanmakuView.resume();
                }
            }
        }, new OnDoubleClickListener.MoveCallBack() {
            @Override
            public void onMove(float x, float y) {
                int seekToProgress = (int) (videoView.getCurrentPosition() + x * 10);
                if (seekToProgress < 0)
                    seekToProgress = 0;
                else if (seekToProgress > videoView.getDuration())
                    seekToProgress = videoView.getDuration();
                videoView.seekTo(seekToProgress);
                videoSeekBar.setProgress(seekToProgress);
                mDanmakuView.seekTo((long) videoView.getCurrentPosition());
            }
        }));

        videoSeekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }

    /**
     * 进度条
     */
    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * 手指滑动时，引起seekBar进度变化
         *
         * @param seekBar
         * @param progress
         * @param fromUser 用户引起，为true
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoView.seekTo(progress);
                mDanmakuView.seekTo((long) progress);
            } else if (progress == videoSeekBar.getMax()) {
                //TODO 播放结束
            }
        }

        /**
         * 手指触碰时回调此方法
         *
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
// TODO           handler.removeMessages(HIDE_MEDIA_CONTROLLER);
        }

        /**
         * 手指离开时回调此方法
         *
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
//  TODO          handler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER, 4000);
        }
    }


    public class VideoDetailsClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> list;

        public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progress = videoView.getCurrentPosition();
        isPlay = videoView.isPlaying();
        handler.removeCallbacksAndMessages(null);
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    private int progress;
    private boolean isPlay;

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(progress);
        videoSeekBar.setProgress(progress);
        handler.sendEmptyMessage(PROGRESS);
        if (isPlay) {
            videoView.start();
            ivPause.setImageResource(R.drawable.f_video_pause_small_icon);
        } else {
            videoView.pause();
            ivPause.setImageResource(R.drawable.f_video_start_small_icon);
        }
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }
}
