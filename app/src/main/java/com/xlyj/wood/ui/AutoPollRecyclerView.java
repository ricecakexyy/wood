package com.xlyj.wood.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/26
 */
public class AutoPollRecyclerView extends RecyclerView {
    private static final long TIME_AUTO_POLL = 16;
    private static final long TIME_AUTO_POLL_1 = 4000;
    AutoPollTask autoPollTask;
    AutoPollTask1 autoPollTask1;
    private int index=0;
    private boolean running; //标示是否正在自动轮询
    private boolean canRun;//标示是否可以自动轮询,可在不需要的是否置false
    private final int mTouchSlop;
    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
// autoPollTask = new AutoPollTask(this);
        autoPollTask1 = new AutoPollTask1(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    /**
     * 持续滑动（走马灯）
     */
    static class AutoPollTask implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;
        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }
        @Override
        public void run() {
            Log.e("AutoPollRecyclerView", System.currentTimeMillis()+"");
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running &&recyclerView.canRun) {
                recyclerView.scrollBy(2, 2);
                recyclerView.postDelayed(recyclerView.autoPollTask,recyclerView.TIME_AUTO_POLL);
            }
        }
    }

    /***
     * 一次只能滑一个item（轮播图）
     */
    static class AutoPollTask1 implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;
        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask1(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }
        @Override
        public void run() {
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running &&recyclerView.canRun) {
                recyclerView .smoothScrollToPosition(++recyclerView.index);
                recyclerView.postDelayed(recyclerView.autoPollTask1, TIME_AUTO_POLL_1);
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running)
            stop();
        canRun = true;
        running = true;
// postDelayed(autoPollTask,TIME_AUTO_POLL);
        postDelayed(autoPollTask1,TIME_AUTO_POLL_1);
    }
    public void stop(){
        running = false;
// removeCallbacks(autoPollTask);
        removeCallbacks(autoPollTask1);
    }
    //取消RecyclerView的惯性，使每次手动只能滑一个
    int lastY=0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getRawY();
                if (running)
                    stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                int nowY = (int) ev.getRawY();
                if (nowY-lastY>mTouchSlop) {//向下滑动
                    smoothScrollToPosition(index==0?0:--index);
                    if (canRun)
                        start();
                    return true;
                }else if(lastY-nowY>mTouchSlop){//向上滑动
                    smoothScrollToPosition(++index);
                    if (canRun)
                        start();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
