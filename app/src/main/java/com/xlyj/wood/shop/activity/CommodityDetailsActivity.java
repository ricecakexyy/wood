package com.xlyj.wood.shop.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Commodity;
import com.xlyj.wood.domain.ShopCart;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.ui.AniManager;
import com.xlyj.wood.ui.BadgeView;
import com.xlyj.wood.utils.DensityUtil;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CommodityDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.tv_pic_current_position)
    TextView tvPicCurrentPosition;
    @BindView(R.id.tv_total_pic)
    TextView tvTotalPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.btn_add_to_cart)
    Button btnAddToCart;

    private BadgeView buyNumView;//购物车上的数量标签
    private AniManager mAniManager;
    private ImageView buyImg;// 界面上跑的小图片
    private int num = 0;

    CommodityDeatilsAdapter adapter;
    /**
     * viewpager的图片view
     */
    private List<View> picList;

    private int totalHeight = 0;  //定义总高度
    int cnt = 0;

    private static final int INITRECYCLER = 1;
    private static final int GETSHOPCART = 2;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITRECYCLER:
                    int height = (int) (((double) windowWidth) / adapter.getWidth() * adapter.getHeight());
                    totalHeight += height;
                    cnt++;
                    if (cnt == adapter.getCount()) {
                        setAdapter();
                    }
                    break;
                case GETSHOPCART:
                    //得到购物车数据后才能进行添加购物车的操作
                    //加入购物车的初始化
                    InitCart();
                    break;
            }
        }
    };

    private static final String ADD_TO_CART = "android.intent.action.ADD_TO_CART_SUCCESS";


    //    /**
//     * 商品详情图片
//     */
//    private int[] picSids = {
//            R.drawable.goods1_01, R.drawable.goods1_02, R.drawable.goods1_03, R.drawable.goods1_04, R.drawable.goods1_05,
//    };
//
//    /**
//     * 商品详细设计图片
//     */
//    private int[] detailsPicSids = {
//            R.drawable.goods1_01, R.drawable.goods1_02, R.drawable.goods1_03, R.drawable.goods1_04
//    };

    int windowWidth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_commodity_details);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
        getDate();

        //图片的viewpager
        initPicViewPager();

        initView();

        //详细设计的listview
        initCommodityDetails();

    }

    /**
     * 设置按钮的点击事件
     */
    private void setOnClick() {
        btnAddToCart.setOnClickListener(new MyClick());
    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add_to_cart:
                    startAnim(v);
                    break;
            }
        }
    }



    //启动加入购物车的动画
    public void startAnim(View v) {
        int[] end_location = new int[2];
        int[] start_location = new int[2];
        v.getLocationInWindow(start_location);// 获取购买按钮的在屏幕的X、Y坐标（动画开始的坐标）
        ivCart.getLocationInWindow(end_location);// 这是用来存储动画结束位置，也就是购物车图标的X、Y坐标
        buyImg = new ImageView(this);// buyImg是动画的图片
        buyImg.setImageResource(R.mipmap.add_to_cart);// 设置buyImg的图片

        //        mAniManager.setTime(5500);//自定义时间
        mAniManager.setAnim(this, buyImg, start_location, end_location);// 开始执行动画

        mAniManager.setOnAnimListener(new AniManager.AnimListener() {
            @Override
            public void setAnimBegin(AniManager a) {
                //动画开始时的监听
            }

            @Override
            public void setAnimEnd(AniManager a) {
                //动画结束后的监听
                num += 1;
                buyNumView.setText(num + "");
                buyNumView.show();
                AddToCart();
            }
        });
    }

    /**
     * 加入购物车具体操作
     */
    private void AddToCart() {
        for(int i = 0; i<myShopCart.size(); i++){
            if(myShopCart.get(i).getCommodity().getObjectId().equals(myCommodity.getObjectId())){
                //有这个商品，直接增加个数
                ShopCart shopCart = new ShopCart();
                shopCart.setObjectId(myShopCart.get(i).getObjectId());
                shopCart.setNumber(myShopCart.get(i).getNumber()+1);
                shopCart.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("CommodityDetails", "修改商品个数成功");
                            Intent broadcast = new Intent(ADD_TO_CART);
                            LocalBroadcastManager.getInstance(CommodityDetailsActivity.this).sendBroadcast(broadcast);
                        } else {
                            Log.i("CommodityDetails", "失败：" + e.getMessage() + e.getErrorCode());
                        }
                    }
                });
                return;
            }
        }
        //之前没有这个商品，增加relation
        ShopCart shopCart = new ShopCart();
        shopCart.setCommodity(myCommodity);
        shopCart.setNumber(num);
        shopCart.setUser(user);
        myShopCart.add(shopCart);
        shopCart.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    BmobRelation relation = new BmobRelation();
                    relation.add(shopCart);
                    user.setShopCart(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("CommodityDetails", "添加购物车成功");
                                Intent broadcast = new Intent(ADD_TO_CART);
                                LocalBroadcastManager.getInstance(CommodityDetailsActivity.this).sendBroadcast(broadcast);
                            } else {
                                Log.i("CommodityDetails", "失败：" + e.getMessage() + e.getErrorCode());
                            }
                        }
                    });
                }else{

                }
            }
        });

    }

    /**
     * 初始化加入购物车
     */
    private void InitCart() {
        mAniManager = new AniManager();//动画实例
        buyNumView = new BadgeView(this, ivCart);//把这个数字标签放在购物车图标上
        buyNumView.setBadgePosition(BadgeView.POSITION_CENTER);//放在图标中心
        buyNumView.setTextColor(Color.WHITE);//数字颜色
        buyNumView.setBadgeBackgroundColor(Color.RED);//背景颜色
        buyNumView.setWidth(DensityUtil.dp2px(this,(float) 13.5));
        buyNumView.setHeight(DensityUtil.dp2px(this,(float) 13.5));
        buyNumView.setTextSize(7);//数字大小

        btnAddToCart.setOnClickListener(new MyClick());

    }

    private void initView() {
        tvName.setText(myCommodity.getName());
        tvPrice.setText("￥" + Double.toString(myCommodity.getPrice()));
    }

    Commodity myCommodity;
    List<ShopCart> myShopCart;
    /**
     * 获取数据
     */
    private void getDate() {
        myCommodity = (Commodity) getIntent().getSerializableExtra("commodity");

        BmobQuery<ShopCart> query = new BmobQuery<ShopCart>();
        user = BmobUser.getCurrentUser(User.class);
        query.addWhereRelatedTo("shopCart", new BmobPointer(user));
        query.findObjects(new FindListener<ShopCart>() {

            @Override
            public void done(List<ShopCart> object, BmobException e) {
                if(e==null){
                    myShopCart = object;
                    handler.sendEmptyMessage(GETSHOPCART);
                    Log.i("查询购物车","查询个数："+object.size());
                }else{
                    Log.i("查询购物车","失败："+e.getMessage());
                }
            }

        });
    }

    /**
     * 初始化商品信息的图片列表
     */

    private void initCommodityDetails() {
//        adapter = new CommodityDeatilsAdapter(this, detailsPicSids);
        adapter = new CommodityDeatilsAdapter(this, myCommodity.getDetails());
        //解决NestedScrollView和listview的滑动冲突
        for (int i = 0; i < adapter.getCount(); i++) {
            View listitem = adapter.getView(i, null, listView);
        }
    }

    /**
     * 设置适配器
     */
    void setAdapter() {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //listview最终高度为item的高度+分隔线的高度，这是重新设置listview的属性
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //将重新设置的params再应用到listview中
        listView.setLayoutParams(params);
        listView.setAdapter(adapter);
    }


    /**
     * 详细图片的adapter
     */
    class CommodityDeatilsAdapter extends BaseAdapter {
        private Context mContext;
        //        int[] detailsPicSids;
        String[] detailsPicUris;

        public CommodityDeatilsAdapter(Context mContext, int[] detailsPicSids) {
            this.mContext = mContext;
//            this.detailsPicSids = detailsPicSids;
        }

        public CommodityDeatilsAdapter(CommodityDetailsActivity mContext, String[] details) {
            this.mContext = mContext;
            this.detailsPicUris = details;
        }

        @Override
        public int getCount() {
            return detailsPicUris.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        int height = 0;
        int width = 0;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.shop_commodity_deatils_pic_item, null);
                viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(mContext)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .override(Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Log.e("onGlideException==>", e.toString());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            width = resource.getWidth();//获取图片高度
                            height = resource.getHeight();//获取图片宽度
                            handler.sendEmptyMessage(INITRECYCLER);
                            return false;
                        }
                    })
                    .load(detailsPicUris[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivPicture);

//            viewHolder.ivPicture.setImageResource(detailsPicSids[position]);
            return convertView;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        class ViewHolder {
            private ImageView ivPicture;
        }
    }

    /**
     * 初始化左右滑动的图片的布局
     */
    private void initPicViewPager() {
//        tvTotalPic.setText("" + picSids.length);
//        picList = new ArrayList<>();
//        for (int i = 0; i < picSids.length; i++) {
//            View view = View.inflate(this, R.layout.s_commodity_vp_pic_layout, null);
//            ImageView iv = view.findViewById(R.id.iv_picture);
//            iv.setImageResource(picSids[i]);
//            picList.add(view);
//        }
        String[] pictures = myCommodity.getPictures();
        tvTotalPic.setText("" + pictures.length);
        picList = new ArrayList<>();
        for (int i = 0; i < pictures.length; i++) {
            View view = View.inflate(this, R.layout.s_commodity_vp_pic_layout, null);
            ImageView iv = view.findViewById(R.id.iv_picture);
//            iv.setImageURI(Uri.parse(pictures[i]));
            Glide.with(this)
                    .load(pictures[i])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            picList.add(view);
        }
        viewPage.setAdapter(new PicAdapter());
        viewPage.addOnPageChangeListener(new PicOnPageChangeListener());
        viewPage.setCurrentItem(0); // 设置当前的默认索引
    }

    /**
     * ViewPage的适配器
     */
    class PicAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return picList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(picList.get(position % picList.size()));
            return picList.get(position % picList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(picList.get(position % picList.size()));
        }
    }

    /**
     * 图片切换，重新设置当前页面位置
     */
    public class PicOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            tvPicCurrentPosition.setText("" + (position + 1));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
