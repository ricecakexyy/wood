package com.xlyj.wood.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xlyj.wood.R;
import com.xlyj.wood.utils.DensityUtil;


/**
 * Created by yuLook on 2016/4/7.
 * 动画效果是线一点点画出来的 而不是一条线一条线直接显示出来的
 */
public class BrokenLineView extends View {

    private String price1, price2, price3, price4, price5, price6, price7;
    private String maxPrice, minPrice;
    private int wide = 0, high = 0;

    private String target1, target2,target3,target4,target5,target6,target7;

    void setTarget(){
        target1 = "风景";
        target2 = "江南";
        target3 = "屏风";
        target4 = "黄小明";
        target5 = "浮雕";
        target6 = "杭州";
        target7 = "G20峰会";
    }

    public BrokenLineView(Context context) {
        super(context);
    }

    public BrokenLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrokenLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 七个价格坐标点
     */
    public void setSevenPrice(String price1, String price2, String price3, String price4, String price5, String price6, String price7) {
        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
        this.price4 = price4;
        this.price5 = price5;
        this.price6 = price6;
        this.price7 = price7;
        setTarget();
    }

    /**
     * Y轴最大值最小值
     */
    public void setMaxMinPrice(String maxPrice, String minPrice) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    /**
     * 获得宽高
     */
    public void setWideHigh(int wide, int high) {
        this.wide = wide;
        this.high = high;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStrokeWidth((float) 2.0);
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        p.setColor(getResources().getColor(R.color.white));

        Paint pt = new Paint();
        pt.setTextSize(DensityUtil.sp2px(getContext(),16));
        pt.setColor(getResources().getColor(R.color.white));

//        canvas.drawCircle(movePointX, movePointY, 20, pt);

        int px = DensityUtil.dp2px(getContext(), 10);

        if (wide > 0) {

            if (time <= 5 && time > 0) {

                double s_y = getRoundY(price1);
                double e_y = getRoundY(price2);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * time;
               draw(b, 1,p,pt,canvas,px);

            } else if (time > 5 && time <= 10) {

                double s_y = getRoundY(price2);
                double e_y = getRoundY(price3);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * (time - 5);

               draw(b,2,p,pt,canvas,px);

            } else if (time > 10 && time <= 15) {

                double s_y = getRoundY(price3);
                double e_y = getRoundY(price4);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * (time - 10);
                draw(b,3,p,pt,canvas,px);

            } else if (time > 15 && time <= 20) {

                double s_y = getRoundY(price4);
                double e_y = getRoundY(price5);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * (time - 15);
                draw(b,4,p,pt,canvas,px);

            } else if (time > 20 && time <= 25) {

                double s_y = getRoundY(price5);
                double e_y = getRoundY(price6);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * (time - 20);
                draw(b,5,p,pt,canvas,px);

            } else if (time > 25) {

                double s_y = getRoundY(price6);
                double e_y = getRoundY(price7);
                double a = (s_y - e_y) / 5;
                double b = s_y - a * (time - 25);
                draw(b,6,p,pt,canvas,px);

            }
        }
    }



    private void draw(double b, int cnt, Paint p, Paint pt, Canvas canvas, int px) {
        if(cnt == 1){
            canvas.drawLine(4,(float) getRoundY(price1), wide/30*time,(float)  b, p);
            canvas.drawCircle(8,(float) getRoundY(price1)+8, 8,p);
            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
        }else if(cnt == 2){
            canvas.drawLine(4, (float) getRoundY(price1), wide / 6, (float) getRoundY(price2), p);
            canvas.drawLine(wide / 6, (float) getRoundY(price2), wide / 6 + (wide / 6) * (time - 5) / 5, (float) b, p);
            canvas.drawCircle(8, (float) getRoundY(price1)+8, 8, p);
            canvas.drawCircle(wide / 6, (float) getRoundY(price2), 8, p);
            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
            canvas.drawText(target2,wide / 6 - px-pt.measureText(target2), (float) getRoundY(price2),pt);
        }else if(cnt == 3){
            canvas.drawLine(4, (float) getRoundY(price1), wide / 6, (float) getRoundY(price2), p);
            canvas.drawLine(wide / 6, (float) getRoundY(price2), wide / 3, (float) getRoundY(price3), p);
            canvas.drawLine(wide / 3, (float) getRoundY(price3), wide / 3 + (wide / 6) * (time - 10) / 5, (float) b, p);

            canvas.drawCircle(8, (float) getRoundY(price1)+8, 8, p);
            canvas.drawCircle(wide / 6, (float) getRoundY(price2), 8, p);
            canvas.drawCircle(wide / 3, (float) getRoundY(price3),8,p);

            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
            canvas.drawText(target2,wide / 6 - px-pt.measureText(target2), (float) getRoundY(price2),pt);
            canvas.drawText(target3,wide / 3 +  px, (float) getRoundY(price3),pt);
        }else if(cnt == 4){
            canvas.drawLine(4, (float) getRoundY(price1), wide / 6, (float) getRoundY(price2), p);
            canvas.drawLine(wide / 6, (float) getRoundY(price2), wide / 3, (float) getRoundY(price3), p);
            canvas.drawLine(wide / 3, (float) getRoundY(price3), wide / 2, (float) getRoundY(price4), p);
            canvas.drawLine(wide / 2, (float) getRoundY(price4), wide / 2 + (wide / 6) * (time - 15) / 5, (float) b, p);

            canvas.drawCircle(8, (float) getRoundY(price1)+8, 8, p);
            canvas.drawCircle(wide / 6, (float) getRoundY(price2), 8, p);
            canvas.drawCircle(wide / 3, (float) getRoundY(price3),8,p);
            canvas.drawCircle(wide / 2, (float) getRoundY(price4),8,p);

            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
            canvas.drawText(target2,wide / 6 - px-pt.measureText(target2), (float) getRoundY(price2),pt);
            canvas.drawText(target3,wide / 3 +  px, (float) getRoundY(price3),pt);
            canvas.drawText(target4,wide / 2 - px-pt.measureText(target4), (float) getRoundY(price4),pt);
        }else if(cnt == 5){
            canvas.drawLine(4, (float) getRoundY(price1), wide / 6, (float) getRoundY(price2), p);
            canvas.drawLine(wide / 6, (float) getRoundY(price2), wide / 3, (float) getRoundY(price3), p);
            canvas.drawLine(wide / 3, (float) getRoundY(price3), wide / 2, (float) getRoundY(price4), p);
            canvas.drawLine(wide / 2, (float) getRoundY(price4), wide / 6 * 4, (float) getRoundY(price5), p);
            canvas.drawLine(wide / 6 * 4, (float) getRoundY(price5), wide / 6 * 4 + (wide / 6) * (time - 20) / 5, (float) b, p);

            canvas.drawCircle(8, (float) getRoundY(price1)+8, 8, p);
            canvas.drawCircle(wide / 6, (float) getRoundY(price2), 8, p);
            canvas.drawCircle(wide / 3, (float) getRoundY(price3),8,p);
            canvas.drawCircle(wide / 2, (float) getRoundY(price4),8,p);
            canvas.drawCircle(wide / 6 * 4, (float) getRoundY(price5),8,p);

            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
            canvas.drawText(target2,wide / 6 - px-pt.measureText(target2), (float) getRoundY(price2),pt);
            canvas.drawText(target3,wide / 3 +  px, (float) getRoundY(price3),pt);
            canvas.drawText(target4,wide / 2 - px-pt.measureText(target4), (float) getRoundY(price4),pt);
            canvas.drawText(target5,wide / 6 * 4 +  px, (float) getRoundY(price5),pt);
        }else if(cnt == 6) {

            canvas.drawLine(4, (float) getRoundY(price1), wide / 6, (float) getRoundY(price2), p);
            canvas.drawLine(wide / 6, (float) getRoundY(price2), wide / 3, (float) getRoundY(price3), p);
            canvas.drawLine(wide / 3, (float) getRoundY(price3), wide / 2, (float) getRoundY(price4), p);
            canvas.drawLine(wide / 2, (float) getRoundY(price4), wide / 6 * 4, (float) getRoundY(price5), p);
            canvas.drawLine(wide / 6 * 4, (float) getRoundY(price5), wide / 6 * 5, (float) getRoundY(price6), p);
            canvas.drawLine(wide / 6 * 5, (float) getRoundY(price6), wide / 6 * 5 + (wide / 6) * (time - 25) / 5, (float) b, p);

            canvas.drawCircle(8, (float) getRoundY(price1)+8, 8, p);
            canvas.drawCircle(wide / 6, (float) getRoundY(price2), 8, p);
            canvas.drawCircle(wide / 3, (float) getRoundY(price3),8,p);
            canvas.drawCircle(wide / 2, (float) getRoundY(price4),8,p);
            canvas.drawCircle(wide / 6 * 4, (float) getRoundY(price5),8,p);
            canvas.drawCircle(wide / 6 * 5, (float) getRoundY(price6),8,p);
            canvas.drawCircle(wide-8 , (float) getRoundY(price7)-8,8,p);

            canvas.drawText(target1,px, (float) getRoundY(price1),pt);
            canvas.drawText(target2,wide / 6 - px-pt.measureText(target2), (float) getRoundY(price2),pt);
            canvas.drawText(target3,wide / 3 +  px, (float) getRoundY(price3),pt);
            canvas.drawText(target4,wide / 2 - px-pt.measureText(target4), (float) getRoundY(price4),pt);
            canvas.drawText(target5,wide / 6 * 4 +  px, (float) getRoundY(price5),pt);
            canvas.drawText(target6,wide / 6 * 5 - px-pt.measureText(target6), (float) getRoundY(price6),pt);
            canvas.drawText(target7,wide- px-pt.measureText(target7), (float)getRoundY(price7),pt);
        }
    }

    //获得Y轴坐标点
    private double getRoundY(String str) {
        double d_max = Double.parseDouble(maxPrice);
        double d_min = Double.parseDouble(minPrice);

        //return ((double) high)-((Double.parseDouble(str)-d_min)/(d_max-d_min))*high;
        return ((Double.parseDouble(str) - d_min) / (d_max - d_min)) * high;
    }



    //动画异步任务
    private int time = 0;

    public class task extends AsyncTask {

        private Handler handler;

        public task(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            for (int i = 1; i < 31; i++) {
                try {
                    time = i;
                    handler.sendEmptyMessage(1);
                    Thread.sleep((long) (600 / 30));
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
            handler.sendEmptyMessage(2);
            return null;
        }
    }

    //接受消息刷新界面
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    invalidate();
                    break;
                case 2:
                    if (a != null) {
                        a.cancel(true);
                        a = null;
                    }
                    break;
            }
        }
    };

    private AsyncTask a;

    //开始动画
    public void start() {
        a = new task(handler);
        a.execute();
    }

    //手动结束动画
    public void stop() {
        if (a != null) {
            a.cancel(true);
            a = null;
        }
        time = 0;
        invalidate();
    }


}