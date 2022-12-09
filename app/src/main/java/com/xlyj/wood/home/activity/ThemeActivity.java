package com.xlyj.wood.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.intrusoft.squint.DiagonalView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.ThemeCategoryBean;
import com.xlyj.wood.home.adapter.ThemeLineAdapter;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun: 题材
 */
public class ThemeActivity extends BaseActivity {

//    @BindView(R.id.view_page)
//    ViewPager viewPage;

//
//    int[] imageresid = {R.drawable.theme_p1, R.drawable.theme_p2, R.drawable.theme_p3,
//            R.drawable.theme_p4, R.drawable.theme_p5,R.drawable.theme_p6,R.drawable.theme_p7};
//    String[] themes = {"灵芝","并蒂莲","牡丹","神话人物","龙","文人学士","鹿"};
//
//    String[] des={
//      "常雕灵芝纹边饰、口衔灵芝的","雕于牛腿以及大型壁挂等"," ",
//            "原有情人终成眷属","喻夫妻忠贞","恩爱到白头",
//            "以花名闻天下","是娇美的富贵花","花中之王",
//            "木雕艺人雕刻大量神话人物","希望能够人们带来好运","逢凶化吉",
//            "中华民族的图腾","最富有传奇色彩的神物","许多民族最为崇拜的吉祥物",
//            "雕于厅堂案桌、格扇之锁腰板","反映房主的儒家心态","反映商家富豪等附儒心",
//            "谐音为禄意为俸禄","财也福也","民间视为鹿为长寿的象征"
//    };

    String[] category = {"吉祥动物","寄情花木","民俗风情","风流人物","书法文字","铭史励志","诗画山水"};
    int[] pic_srcs = {R.drawable.h_theme1, R.drawable.h_theme2,R.drawable.h_theme3,
            R.drawable.h_theme4,R.drawable.h_theme5,R.drawable.h_theme6,R.drawable.h_theme7 };

    @BindView(R.id.theme_line)
     ListView mThemeLine ;

    private ThemeLineAdapter mAdapter ;

    List<ThemeCategoryBean> infoList = new ArrayList<ThemeCategoryBean>();

    private final int CATEGORY_ANIMAL = 1; //动物
    private final int CATEGORY_PLANT = 2; //植物
    private final int CATEGORY_FOLK = 3; //民俗
    private final int CATEGORY_PEOPLE = 4; //人物
    private final int CATEGORY_CALLIGRAPHY = 5; //书法
    private final int CATEGORY_STORY = 6; //铭史
    private final int CATEGORY_SCENERY = 7; //山水


//    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_theme);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        initListView();
//        initViewPager();
    }

    /**
     * 初始化listView
     */
    private void initListView() {
        mThemeLine.setOnItemClickListener(new ThemeClick());
        // 设置DIvider高度为0
        mThemeLine.setDividerHeight(0);

//        // 填充数据
        for (int i = 0; i < category.length ; i++) {
            ThemeCategoryBean infoBean = new ThemeCategoryBean();
            infoBean.setCategory(category[i]);
            infoBean.setPic_src(pic_srcs[i]);
            // 将实体添加到列表中
            infoList.add(infoBean);
        }

        // 设置适配器
        mAdapter = new ThemeLineAdapter(getApplicationContext(), infoList);
        mThemeLine.setAdapter(mAdapter);
    }

    /**
     * 初始化布局
     */
    private void initView() {
            TextView tvTitle = findViewById(R.id.tv_title);
            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
            tvTitle.setTypeface(font);
    }

    /**
     * 点击事件
     */
    public class ThemeClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int[] imageresid;
            String[] themes;
            String[] des;
            Intent intent;
            Bundle bundle;
            switch (position+1){

                case CATEGORY_ANIMAL:

                    break;
                case CATEGORY_PLANT:
                    imageresid = new int[]{R.drawable.theme_p1, R.drawable.theme_p2, R.drawable.theme_p3};
                    themes = new String[]{"灵芝", "并蒂莲", "牡丹"};
                    des= new String[]{
                             "常雕灵芝纹边饰、口衔灵芝的", "雕于牛腿以及大型壁挂等", " ",
                             "原有情人终成眷属", "喻夫妻忠贞", "恩爱到白头",
                             "以花名闻天下", "是娇美的富贵花", "花中之王"};
                    intent = new Intent(ThemeActivity.this, ThemeDetailsActivity.class);
                    bundle = new Bundle();
                    bundle.putStringArray("themes",themes);
                    bundle.putStringArray("des",des);
                    bundle.putIntArray("imageresid",imageresid);
                    bundle.putString("title",category[position]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case CATEGORY_FOLK:
                    break;
                case CATEGORY_PEOPLE:
                    break;
                case CATEGORY_CALLIGRAPHY:
                    break;
                case CATEGORY_STORY:
                    break;
                case CATEGORY_SCENERY:
                    imageresid = new int[]{R.drawable.h_theme_scenery1};
                    themes = new String[]{"江南美景"};
                    des= new String[]{
                            "烟雨江南,风景旧曾谙", "浙江山水如画", "匠人们用木雕体现其诗情画意",};
                    intent = new Intent(ThemeActivity.this, ThemeDetailsActivity.class);
                    bundle = new Bundle();
                    bundle.putStringArray("themes",themes);
                    bundle.putStringArray("des",des);
                    bundle.putIntArray("imageresid",imageresid);
                    bundle.putString("title",category[position]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;

            }
//            Toast.makeText(getApplicationContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
        }
    }

//    /**
//     * 初始化viewpage
//     */
//    private void initViewPager() {
//        viewList = new ArrayList<>();
//        for (int i = 0; i < imageresid.length; i++) {
//            View view = View.inflate(this, R.layout.h_culture_theme, null);
//            TextView tvTitle  = view.findViewById(R.id.tv_theme);
//            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
//            tvTitle.setTypeface(font);
//            tvTitle.setText(themes[i]);
////            tvTitle.setLetterSpacing(0.6f);
//
//            TextView tvD1  = view.findViewById(R.id.tv_d1);
//            tvD1.setTypeface(font);
//            tvD1.setText(des[i*3]);
//            tvD1.setLetterSpacing(0.6f);
//
//            TextView tvD2  = view.findViewById(R.id.tv_d2);
//            tvD2.setTypeface(font);
//            tvD2.setText(des[i*3+1]);
//            tvD2.setLetterSpacing(0.6f);
//
//            TextView tvD3  = view.findViewById(R.id.tv_d3);
//            tvD3.setTypeface(font);
//            tvD3.setText(des[i*3+2]);
//            tvD3.setLetterSpacing(0.6f);
//
//
//            DiagonalView iv = view.findViewById(R.id.iv_picture);
//            iv.setImageResource(imageresid[i]);
//            viewList.add(view);
//        }
//        viewPage.setAdapter(new ThemeAdapter());
//        viewPage.setCurrentItem(0); // 设置当前的默认索引
//    }

//    /**
//     * ViewPage的适配器
//     */
//    class ThemeAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return viewList.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//            return view == object;
//        }
//
//        @NonNull
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            container.addView(viewList.get(position % viewList.size()));
//            return viewList.get(position % viewList.size());
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(viewList.get(position % viewList.size()));
//        }
//    }
}
