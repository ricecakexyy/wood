package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.Exhibition;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.ExhitionAdapter;
import com.xlyj.slqj.SLQJ.Museum.ExhibitionHall.ExhibitionHall;
import com.xlyj.slqj.SLQJ.Museum.ExhibitionHall.ExhitionHallAdapter;
import com.xlyj.utils.StatusBarUtil;


import java.util.ArrayList;
import java.util.List;

public class MainMuseumActivity extends BaseActivity {
    List<ExhibitionHall> EHlist=new ArrayList<>();
    List<Exhibition> Elist=new ArrayList<>();
    RadioButton rb_vr;
    RadioGroup tabsRg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_museum);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        initEH();//初始化展厅数据
        initE();//初始化展品数据
        //initBanner();
        //展厅列表
        RecyclerView recyclerView=findViewById(R.id.rv_exhibitionhall);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        ExhitionHallAdapter adapter = new ExhitionHallAdapter(EHlist,this);
        recyclerView.setAdapter(adapter);
        //展品列表
        RecyclerView recyclerView1=findViewById(R.id.rv_exhibition);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);
        ExhitionAdapter adapter1 = new ExhitionAdapter(Elist,this);
        recyclerView1.setAdapter(adapter1);
        tabsRg=findViewById(R.id.tabs_rg);
        rb_vr=findViewById(R.id.vr_tab);
        initListner();

    }
    private void initEH(){
        ExhibitionHall eh1=new ExhibitionHall("历史厅",R.mipmap.museum_exhall1,"展厅位置：2F");
        EHlist.add(eh1);
        ExhibitionHall eh2=new ExhibitionHall("生活厅",R.mipmap.museum_exhall2,"展厅位置：2F");
        EHlist.add(eh2);
        ExhibitionHall eh3=new ExhibitionHall("大师厅",R.mipmap.museum_exhall3,"展厅位置：3F");
        EHlist.add(eh3);
        ExhibitionHall eh4=new ExhibitionHall("世界厅",R.mipmap.museum_exhall4,"展厅位置：3F");
        EHlist.add(eh4);
        ExhibitionHall eh5=new ExhibitionHall("竹艺厅",R.mipmap.museum_exhall5,"展厅位置：3F");
        EHlist.add(eh5);
    }
    /**private void initBanner(){
        mBanner = findViewById(R.id.bn_museum);
        //图片资源
        int[] imageResourceID = new int[]{R.mipmap.map_img1, R.mipmap.map_img2, R.mipmap.map_img3};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        String[] mtitle = new String[]{"图片1", "图片2", "图片3"};
        List<String> titleList = new ArrayList<>();

        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(MainMuseumActivity.this).load(path).into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            mBanner.setBannerAnimation(Transformer.Default);
            mBanner.setImages(imgeList);//设置图片资源
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner显示样式（带标题的样式）
            mBanner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            mBanner.setDelayTime(2000);//设置轮播时间3秒切换下一图
            mBanner.setOnBannerListener(this);//设置监听
            mBanner.start();//开始进行banner渲染
        }
    }**/


    private void initE(){
        Exhibition e1=new Exhibition("歌山画水",R.mipmap.museum_ex1);
        Elist.add(e1);
        Exhibition e2=new Exhibition("歌山画水",R.mipmap.museum_ex2);
        Elist.add(e2);
        Exhibition e3=new Exhibition("歌山画水",R.mipmap.museum_ex3);
        Elist.add(e3);
        Exhibition e4=new Exhibition("歌山画水",R.mipmap.museum_ex4);
        Elist.add(e4);
        Exhibition e5=new Exhibition("歌山画水",R.mipmap.museum_ex5);
        Elist.add(e5);
        Exhibition e6=new Exhibition("歌山画水",R.mipmap.museum_ex6);
        Elist.add(e6);
    }
    private void initListner() {
        tabsRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.e_tab){
                    startActivity(new Intent(MainMuseumActivity.this, AllExhibitionShowActivity.class));
                }
                if(i==R.id.map_tab){
                    startActivity(new Intent(MainMuseumActivity.this, MapActivity.class));
                }
                if(i==R.id.vr_tab){
                    startActivity(new Intent(MainMuseumActivity.this,VrHallActivity.class));
                }
                if(i==R.id.intro_tab){
                    startActivity(new Intent(MainMuseumActivity.this, GuideActivity.class));
                }


                /**switch (i) {
                    case R.id.e_tab:
                        startActivity(new Intent(MainMuseumActivity.this, AllExhibitionShowActivity.class));
                        break;
                    case R.id.map_tab:
                        startActivity(new Intent(MainMuseumActivity.this, MapActivity.class));
                        break;
                    case R.id.vr_tab:
                        startActivity(new Intent(MainMuseumActivity.this,VrHallActivity.class));
                        break;
                    case R.id.intro_tab:
                        startActivity(new Intent(MainMuseumActivity.this,GuideActivity.class));
                        break;
                    default:
                        break;
                }**/
            }
        });
    }

}