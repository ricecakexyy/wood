package com.xlyj.wood.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xlyj.wood.R;


/**
 * @author cute xyy biu~biu~
 * created on 2020/7/18
 */
public class ProgressDialog extends Dialog {
    private String text;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    public ProgressDialog(@NonNull Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_dialog);
        TextView textView = findViewById(R.id.text);
        textView.setText(text);
        //设置宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * 0.6);//设置Dialog的宽度为屏幕的0.8倍
        //   p.dimAmount=0.0f;//设置背景不暗，0.0f完全不暗，1.0f全暗
        //    p.alpha=0.8f;//设置不透明度
        getWindow().setAttributes(p);
    }
}
