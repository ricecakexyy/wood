package com.xlyj.wood.find;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class OnDoubleClickListener implements View.OnTouchListener {
    // 第一次点击的时间 long型
    private long firstClick = 0;
    // 最后一次点击的时间
    private long lastClick = 0;
    //两次点击间隔
    private int midMillis = 500;

    public OnDoubleClickListener(ClickCallBack callback,MoveCallBack moveCallBack){
        super();
        this.callback = callback;
        this.moveCallBack = moveCallBack;
    }

    private final int MSG_ONE_CLICK = 0x000012;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ONE_CLICK:
                    if (callback != null) {
                        callback.onClick();
                    }
                    Log.d("K_K", "one click");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private ClickCallBack callback;


    public interface ClickCallBack {
        void onClick();

        void onDoubleClick();
    }

    private MoveCallBack moveCallBack;

    public interface MoveCallBack {
        void onMove(float x, float y);
    }

    private float startX;
    private float startY;
    private float rawX;
    private float rawY;
    private boolean isMove;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
            isMove = false;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //5像素用来区别是不是点击的时候响应的move
            if (event.getX() - startX > 5 || event.getX() - startX < -5 || event.getY() - startY > 5 || event.getY() - startY < -5) {
                rawX = event.getRawX();
                rawY = event.getRawY();
                if (moveCallBack != null) {
                    moveCallBack.onMove(rawX - startX, rawY - startY);
                    Log.d("K_K", "move : " + " x:" + (rawX - startX) + " y:" + (rawY - startY));
                    isMove = true;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isMove) {
                mHandler.sendEmptyMessageDelayed(MSG_ONE_CLICK, midMillis);
                firstClick = lastClick;
                lastClick = System.currentTimeMillis();
                if (lastClick - firstClick < midMillis) {
                    //如果是双击，将之前发送的单击消息取消掉
                    mHandler.removeMessages(MSG_ONE_CLICK);
                    if (callback != null) {
                        callback.onDoubleClick();
                    }
                    Log.d("K_K", "double click");
                }
            }
        }

        return true;
    }

    public void setCallback(ClickCallBack callback) {
        this.callback = callback;
    }

    public void setMoveCallBack(MoveCallBack moveCallBack) {
        this.moveCallBack = moveCallBack;
    }
}