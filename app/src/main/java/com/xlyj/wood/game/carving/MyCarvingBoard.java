package com.xlyj.wood.game.carving;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xlyj.wood.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/20
 * fun:
 */
public class MyCarvingBoard extends androidx.appcompat.widget.AppCompatImageView {
    private Drawable mDrawable;
    public MyCarvingBoard(Context context) {
        this(context,null);
    }

    public MyCarvingBoard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCarvingBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawable = this.getResources().getDrawable(R.drawable.game_carving_board);
    }


    List<Path> paths = new ArrayList<>();

    public void Clip(List<Path> path) {
        this.paths = path;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.setBounds(0, 0, getWidth(), getHeight());

        if (!paths.isEmpty()) {
            Paint paint  = new Paint();
            paint.setStrokeWidth(4);
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            for (int i = 0; i <paths.size(); i++) {
                Path path = paths.get(i);
                canvas.clipPath(path);
                canvas.drawPath(path,paint);
            }
        }
        mDrawable.draw(canvas);

    }
}
