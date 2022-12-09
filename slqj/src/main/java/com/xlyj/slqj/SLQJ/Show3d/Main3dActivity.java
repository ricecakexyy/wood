package com.xlyj.slqj.SLQJ.Show3d;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;

public class Main3dActivity extends BaseActivity {
    private ImageView iv_vr1,iv_vr2,iv_vr3,iv_vr4;
    String filename;
    private ViewPager viewPager;
    private LinearLayout layoutPoint;
    private TextView tv_modelname;
    private int[] imgs = {R.drawable.model0,R.drawable.model1,R.drawable.model2,R.drawable.model3};
    private String []names={"忆江南","百鸟朝凤","年年有余","钱江潮"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3d);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        viewPager =  findViewById(R.id.view_pager);
        layoutPoint =  findViewById(R.id.layout_point);
        tv_modelname=findViewById(R.id.tv_modelname);
        initView();
    }
    private void initView(){
        //ViewPager相关
        ViewPagerAdater myAdater = new ViewPagerAdater(Main3dActivity.this,imgs,names);
        viewPager.setAdapter(myAdater);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //在滑动完成后向布局中添加小圆点
                setLayoutPoint(position);
                tv_modelname.setText(names[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setLayoutPoint(0);
    }

    /**
     * 设置小圆点布局
     * @param position
     */
    private void setLayoutPoint(int position){
        layoutPoint.removeAllViews();
        for (int i = 0; i <4 ; i++) {
            ImageView imageView = new ImageView(Main3dActivity.this);
            //设置ImageView
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(20, 20);
            LinearLayout.LayoutParams params2= new LinearLayout.LayoutParams(30, 30);
            params1.setMargins(10,0,10,0);
            params2.setMargins(10,0,10,0);
            //设置小圆点样式
            if (position==i){
                imageView.setLayoutParams(params2);
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.show3d_pointbig));
            }else {
                imageView.setLayoutParams(params1);
                imageView.setImageDrawable(getResources().getDrawable(R.mipmap.show3d_point));
            }
            layoutPoint.addView(imageView);
        }
    }

}