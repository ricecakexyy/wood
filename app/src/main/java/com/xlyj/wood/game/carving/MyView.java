package com.xlyj.wood.game.carving;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyView extends View {
    private Drawable mDrawable;
    private Path mPath;
    private float mX = -1;
    private float mY = -1;
    private float mPastX;
    private float mPastY;

    List<Path> pathList;

    Paint paint;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyView(Context context) {
        super(context);
        init();
    }

    void init(){
        paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        pathList = new ArrayList<>();
    }

    public void setDrawable(Drawable pDrawable) {
        mDrawable = pDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
//        mDrawable.setBounds(0, 0, getWidth(), getHeight());
//        Rect lRect = mDrawable.getBounds();
//        mDrawable.draw(canvas);

//            Log.i("xiao", "lRect.left =  " + lRect.left + " top = " + lRect.top + " right = " + lRect.right + " bottom = " + lRect.bottom);
//            if(!mPath.isEmpty()){
//                Log.i("xiao", "!mPath.isEmpty()");
//
////                mDrawable.draw(canvas);
//                canvas.drawPath(mPath, paint);
//
//                canvas.clipPath(mPath);
//                //mDrawable.draw(canvas);
//            }


        if (!pathList.isEmpty()) {
            for (int i = 0; i < pathList.size(); i++) {
                Path path = pathList.get(i);
                canvas.drawPath(path, paint);

            }
        }


//            mDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.i("xiao", "onTouchEvent");
        int lAction = event.getAction();
        if (mX != -1) {
            mPastX = mX;
            mPastY = mY;
        }
        mX = event.getX();
        mY = event.getY();
        switch (lAction) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mPath.reset();
                mPath.moveTo(mX, mY);
                pathList.add(mPath);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(mPastX, mPastY, (mX + mPastX) / 2, (mY + mPastY) / 2);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);
                mX = -1;
                mY = -1;
                invalidate();
                break;
            default:

                break;
        }
        return true;
    }

    List<Path> getPath(){
        return pathList;
    }


}