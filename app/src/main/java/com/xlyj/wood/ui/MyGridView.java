package com.xlyj.wood.ui;

import android.widget.GridView;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/27
 * fun:解决listview和gridview嵌套使用的冲突
 */
public class MyGridView extends GridView {
    public MyGridView(android.content.Context context,
                      android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
