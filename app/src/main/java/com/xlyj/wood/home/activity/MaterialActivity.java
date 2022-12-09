package com.xlyj.wood.home.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrusoft.squint.DiagonalView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaterialActivity extends BaseActivity {

    @BindView(R.id.view_page)
    ViewPager viewPage;
    int[] imageresid = {R.drawable.material_p1, R.drawable.material_p2, R.drawable.material_p3,
            R.drawable.material_p4, R.drawable.material_p5,R.drawable.material_p6,R.drawable.material_p7,
            R.drawable.material_p8, R.drawable.material_p9,R.drawable.material_p10,R.drawable.material_p11,R.drawable.material_p12};
    String[] materials = {"白杨树","椴树","樟树","银杏"};

    String[] des={
            "        白杨木，也叫毛白杨，落叶乔木，别称大叶杨、响叶杨。生长快，寿命长，树干高可达40米，胸径1以上。木材年轮明显，木质淡黄褐色" +
                    "或黄褐色，纹理直，结构细致，易干燥，加工性能良好，可作建筑、家具等用，也是东阳木雕理想的浮雕之材。",
            "        落叶乔木，别名青科柳、家鹤儿、金铜力树。主要分布于吉林、黑龙江、江西、湖南及贵州等省。高可达15米，木材质地纯白、轻柔、细致、纹理" +
                    "清淡，有丝绢光泽，易加工，不易开裂，但干燥时稍有翘曲及不耐腐等缺陷，是东阳木雕的常用之材，是20世纪60年代以来用得最多" +
                    "的材料，也是最理想的浮雕之材。",
            "        属樟科，常绿乔木，别名香樟、小叶樟，为亚热带常绿阔叶林的代表树种。分布于北纬10°至30°之间。香樟木材纹理交错，结构紧密；易于切削，切面光滑" +
                    "有光泽，干燥后不易变形，有特殊的香气，耐腐防虫，为家具、工艺品的上等用材，是东阳木雕最早使用、用量最多的传统木材之一。",
            "        银杏树也称公孙树、白果树，果实银杏也叫白果。落叶乔木，雌雄异株，产于我国大江南北各地，材质纹理致密，色白黄，结实而软，心材淡黄色，径切面上呈现" +
                    "银色光泽，近视有黄杨木外感，不易变形，适合于制作高档工艺品，是东阳木雕的理想浮雕之材。"
    };


    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_material);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        initViewPager();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvTitle.setTypeface(font);
    }

    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < materials.length; i++) {
            View view = View.inflate(this, R.layout.h_culture_material, null);
            TextView tvTitle  = view.findViewById(R.id.tv_material);
            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STXINGKA.TTF");
            tvTitle.setTypeface(font);
            tvTitle.setText(materials[i]);

            font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
            TextView tvDes = view.findViewById(R.id.tv_des);
            tvDes.setText(des[i]);
            tvDes.setTypeface(font);

            RoundedImageView ivP1 = view.findViewById(R.id.iv_p1);
            ivP1.setImageResource(imageresid[i*3]);

            RoundedImageView ivP2 = view.findViewById(R.id.iv_p2);
            ivP2.setImageResource(imageresid[i*3+1]);

            RoundedImageView ivP3 = view.findViewById(R.id.iv_p3);
            ivP3.setImageResource(imageresid[i*3+2]);

            viewList.add(view);
        }
        viewPage.setAdapter(new MaterialAdapter());
        viewPage.setCurrentItem(0); // 设置当前的默认索引
    }

    /**
     * ViewPage的适配器
     */
    class MaterialAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position % viewList.size()));
            return viewList.get(position % viewList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position % viewList.size()));
        }
    }
}
