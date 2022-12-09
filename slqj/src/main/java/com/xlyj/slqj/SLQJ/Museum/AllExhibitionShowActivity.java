package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.DetailedExhibition;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.DetailedExhibitionAdapter;
import com.xlyj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AllExhibitionShowActivity extends BaseActivity {
RecyclerView recyclerView;
DetailedExhibitionAdapter adapter;
int imageid[]={R.mipmap.slqj_ex1,R.mipmap.slqj_ex2,R.mipmap.slqj_ex3,R.mipmap.slqj_ex4,R.mipmap.slqj_ex5,R.mipmap.slqj_ex6,
        R.mipmap.slqj_ex7,R.mipmap.slqj_ex8,R.mipmap.slqj_ex9,R.mipmap.slqj_ex10,R.mipmap.slqj_ex11,R.mipmap.slqj_ex12,R.mipmap.slqj_ex13,
        R.mipmap.slqj_ex14,R.mipmap.slqj_ex15,R.mipmap.slqj_ex16,R.mipmap.slqj_ex17,R.mipmap.slqj_ex18};
String exname[]={"锦绣东阳","百工东阳","人文东阳","中国梦","锦绣东阳","九思堂屏风","渔翁","九龙壁","藻井","傩文化",
"锦绣江南","十分祥和","曹操赠袍","三英战吕布","树皮雕·喜满堂","贴金千工床","云龙花瓶","五代木雕罗汉像"};
    String exloc[]={"生活厅","大师厅","生活厅","大师厅","大师厅","历史厅","生活厅","历史厅","历史厅","世界厅",
            "大师厅","大师厅","历史厅","历史厅","世界厅","生活厅","生活厅","历史厅"};
    String exlocf[]={"1F","2F","1F","2F","2F","1F","1F","1F","1F","2F",
            "2F","2F","1F","1F","世界厅","1F","1F","1F"};
Random random=new Random();
    List<DetailedExhibition> Elist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_exhibition_show);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        initE();
        recyclerView =  findViewById(R.id.rl_all_ex);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter = new DetailedExhibitionAdapter(Elist));
    }
    private void initE(){
        for(int i=0;i<18;i++){
            DetailedExhibition e=new DetailedExhibition(exname[i],exlocf[i],exloc[i],imageid[i],random.nextInt(500));
            Elist.add(e);
        }
        //String name, String loc_f, String loc_hall, int imageId, int looks

    }
}