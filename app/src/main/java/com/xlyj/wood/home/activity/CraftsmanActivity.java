package com.xlyj.wood.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalong.library.listener.OnItemClickListener;
import com.dalong.library.view.LoopRotarySwitchView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun: 匠人
 */

public class CraftsmanActivity extends BaseActivity {

    //    @BindView(R.id.view_page)
//    ViewPager viewPage;
    int[] imageresid = {R.drawable.craftsman_p1, R.drawable.craftsman_p2, R.drawable.craftsman_p3,
            R.drawable.craftsman_p4};
    int[] bgsid = {R.drawable.craftsman_bg1, R.drawable.craftsman_bg2, R.drawable.craftsman_bg3,
            R.drawable.craftsman_bg4};
    String[] names = {"杜云松", "黄紫金", "陆光正", "徐经彬","黄小明"};

    String[] lifes = {"1887——1960年", "1894——1981年", "1945年", "1993——2017年",""};

    String[] location = {"湖溪镇后山店村人", "湖溪镇黄大户村人", "东阳县画水镇岭下村人", "东阳县大连镇文祥村人",""};
    String[] designations = {"木雕皇帝", "木雕宰相", "中国工艺美术大师", "浙江省工艺美术大师",""};

    String[] experience = {
            "",
            "",
            "        陆光正 ，一九四五年出生于浙江东阳。一九六零年师从东阳老艺人楼水明先生。一九六五年赴浙江美术学院深造。中国工艺美术大师，中国工艺美术学会木雕艺术专业委员会会长，中国工艺美术学会雕塑专业委员会副会长。\n" +
                    "        陆光正的作品不仅继承了东阳木雕的所有精华，将先人留下的技艺发挥得淋漓尽致，而且在此基础上有所创新，有所突破，解决了前人所不能解决的难题。他的作品手法细腻，造型生动，章法布置巧妙，匠心独运地将圆雕、高浮雕、浅浮雕结合在一起，作品中既有对生活对自然的悉心领悟与把握，又有难以掩藏的书卷之气，可谓是“画中有诗”。\n" +
                    "        一九七四年创作的木雕挂屏《松鹤同春》、《百鸟朝凤》陈列于北京人民大会堂浙江厅。一九七九年创作的木雕台屏《三英战吕布》被评为国家珍品，现陈列于中国工艺美术馆珍宝苑。一九八八年创作的大型木雕落地屏《锦绣中华》，是当今木雕作品中不可多得的精品，现收藏于台湾南园。一九九七年创作的落地屏风《航归》，作为浙江省人民政府赠送给香港特区政府的香港回归礼品。二零零三年为杭州雷峰塔创作的8幅大型木雕壁画《白蛇传的故事》，及《佛教故事》，采用了浮雕、半圆雕、圆雕和多层叠雕技法，成为东阳木雕的创新之作，获得党和国家领导人的赞誉以及社会各界的好评。\n",
            "",
            ""
    };

//    String[] achieves = {
//            "1921年在仁义厂同行比赛中夺魁封“皇”。", "1924年参加浙江实业厅三雕之考名列第二。", "东阳木雕主厂的主创人之一。",
//            "名列“全国工艺美术名艺人”史册第六位",
//            "1953年创作的“三英战吕布”，在中央美院评比中获第三名。", "1956年，黄氏被荣荐为浙江省政协委员。", "1957年被浙江省人民政府授予“东阳木雕名艺人”称号。",
//            "东阳木雕集团木雕厂的主创人之一",
//            "1982年出任东阳木雕总厂厂长", "1988年，被称为高级工程师。", "2005年大型木雕壁挂《春满西湖》装饰在北京人民大会堂浙江厅。",
//            "",
//            "1996年被轻工总会评为全国优秀工艺美术专业技术人员。", "1988年荣获“浙江省工艺美术大师”称号。", "1999年《江南农家》获中国工艺美术“世纪杯”金奖。",
//            ""
//    };
//    String[] values = {
//            "杜云松艺不保守，不断进取，把东阳木雕的传统雕法与国画艺术得心应手地融为一体，在“雕花体”的基础上推出“画工体”极具代表性。所以，又被同行誉为“东阳" +
//                    "木雕革新派的开山祖”。",
//            "黄紫金的作品和画稿，是近代东阳木雕艺人中最多的，设计创作的图稿占全厂产品的70%以上，给东阳木雕事业留下来一份珍贵的艺术遗产，被同行" +
//                    "誉为木雕宰相",
//            "陆光正的作品不仅继承了东阳木雕的所有精华，将先人留下的技艺发挥得淋漓尽致，而且在此基础上有所创新，有所突破。他的作品手法细腻，造型生动，章法" +
//                    "布置巧妙，匠心独运地将圆雕、高浮雕、浅浮雕结合在一起。",
//            "他独具慧眼，对家乡山清水秀、稻谷飘香的江南美景和梨、耙、耕情有独钟，创作出较多江南民俗风情的系列作品，匠心独具，返璞归真，观照历史，启示后代，" +
//                    "具有震撼人心的艺术魅力和回味无穷的意境内涵。"
//    };
    @BindView(R.id.tv_name1)
    TextView tvName1;
    @BindView(R.id.ll_p1)
    LinearLayout llP1;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.ll_p2)
    LinearLayout llP2;
    @BindView(R.id.tv_name3)
    TextView tvName3;
    @BindView(R.id.ll_p3)
    LinearLayout llP3;
    @BindView(R.id.tv_name4)
    TextView tvName4;
    @BindView(R.id.ll_p4)
    LinearLayout llP4;
    @BindView(R.id.tv_name5)
    TextView tvName5;
    @BindView(R.id.ll_p5)
    LinearLayout llP5;
    @BindView(R.id.mLoopRotarySwitchView)
    LoopRotarySwitchView mLoopRotarySwitchView;

    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_craftman_all);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        initView();
//        initView();
//        initViewPager();
    }

    private void initView() {
        tvName1.setText(names[0]);
        tvName2.setText(names[1]);
        tvName3.setText(names[2]);
        tvName4.setText(names[3]);
        tvName5.setText(names[4]);

        mLoopRotarySwitchView
                .setR(500)//设置R的大小
                .setAutoRotation(true)//是否自动切换
                .setAutoScrollDirection(LoopRotarySwitchView.AutoScrollDirection.left)//切换方向
                .setAutoRotationTime(2000);//自动切换的时间  单位毫秒

        mLoopRotarySwitchView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int item, View view) {
                Intent intent = new Intent(CraftsmanActivity.this, CraftsmanDetailsActivity.class);
                Bundle bundle = new Bundle();
                BundleDate(bundle, item);
                intent.putExtras(bundle);
//                switch (view.getId()){
//                    case R.id.ll_p3: //陆光正
//                        bundle.putString("name",names[2]);
//                        bundle.putInt("imageresid",imageresid[2]);
//                        bundle.putString("life",lifes[2]);
//                        bundle.putString("location",location[2]);
//                        bundle.putString("designation",designations[2]);
//                        bundle.putStringArray("achieves",new String[]{achieves[2*3],achieves[2*3+1],achieves[2*3+2],achieves[2*3+3]});
//                        bundle.putString("value",values[2]);
//                        intent.putExtras(bundle);
//                        break;
//                    case R.id.ll_p1:
//                        BundleDate(bundle, item);
//                        intent.putExtras(bundle);
//                        break;
//                    case R.id.ll_p2:
//                        BundleDate(bundle, item);
//                        intent.putExtras(bundle);
//                        break;
//                    case R.id.ll_p4:
//                        BundleDate(bundle, item);
//                        intent.putExtras(bundle);
//                        break;
//                }
                startActivity(intent);

            }
        });


    }

    void BundleDate(Bundle bundle, int item){
        bundle.putString("name",names[item]);
        bundle.putInt("imageresid",imageresid[item]);
        bundle.putString("life",lifes[item]);
        bundle.putString("location",location[item]);
        bundle.putString("designation",designations[item]);
        bundle.putString("experience",experience[item]);
//        bundle.putStringArray("achieves",new String[]{achieves[item*4],achieves[item*4+1],achieves[item*4+2],achieves[item*4+3]});
//        bundle.putString("value",values[item]);
    }

    public class CraftsmanClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
//
//    private void initView() {
//        TextView tvTitle = findViewById(R.id.tv_title);
//        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
//        tvTitle.setTypeface(font);
//    }
//
//    private void initViewPager() {
//        viewList = new ArrayList<>();
//        for (int i = 0; i < imageresid.length; i++) {
//            View view = View.inflate(this, R.layout.h_culture_craftsman, null);
//            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
//            TextView tvName = view.findViewById(R.id.tv_name);
//            tvName.setText(names[i]);
//
//            TextView tvLife = view.findViewById(R.id.tv_life);
//            tvLife.setText(lifes[i]);
//
//            TextView tvLocation = view.findViewById(R.id.tv_location);
//            tvLocation.setText(location[i]);
//
//            TextView tvDesignation = view.findViewById(R.id.tv_designation);
//            tvDesignation.setText(designations[i]);
//
//            TextView tvA1 = view.findViewById(R.id.tv_a1);
//            tvA1.setTypeface(font);
//            tvA1.setText(achieves[i*4]);
//
//            TextView tvA2 = view.findViewById(R.id.tv_a2);
//            tvA2.setTypeface(font);
//            tvA2.setText(achieves[i*4+1]);
//
//            TextView tvA3 = view.findViewById(R.id.tv_a3);
//            tvA3.setTypeface(font);
//            tvA3.setText(achieves[i*4+2]);
//
//            TextView tvA4 = view.findViewById(R.id.tv_a4);
//            if(achieves[i*4+3].equals("")) {
//                tvA4.setVisibility(View.GONE);
//            }
//            else {
//                tvA4.setTypeface(font);
//                tvA4.setText(achieves[i * 4 + 3]);
//            }
//
//            TextView tvValue = view.findViewById(R.id.tv_value);
//            tvValue.setText(values[i]);
//            tvValue.setTypeface(font);
//
//            RoundedImageView iv = view.findViewById(R.id.iv_head);
//            iv.setImageResource(imageresid[i]);
//
//            iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CraftsmanActivity.this, CraftsmanWorksActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            RelativeLayout ivBg = view.findViewById(R.id.iv_bg);
//            ivBg.setBackgroundResource(bgsid[i]);
//
//            viewList.add(view);
//        }
//        viewPage.setAdapter(new CraftsmanAdapter());
//        viewPage.setCurrentItem(0); // 设置当前的默认索引
//    }
//
//    /**
//     * ViewPage的适配器
//     */
//    class CraftsmanAdapter extends PagerAdapter {
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
