package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarHeightView;
import com.xlyj.utils.StatusBarUtil;

import java.io.File;


public class MapActivity extends BaseActivity {
    private MapView mapView;
    private AMap map;
    LinearLayout iv_navi;
    private UiSettings mUiSettings;//定义一个UiSettings对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initmap();

        iv_navi = findViewById(R.id.map_gotonavi);
        iv_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调起百度地图客户端

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);

                //将功能Scheme以URI的方式传入data
                Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=&lat=29.29714201659764&lon=120.18971421257021&dev=1&style=2");
                intent.setData(uri);

                if (isInstallByread("com.autonavi.minimap")) {
                    //启动该页面即可
                    startActivity(intent);
                    Log.e("GasStation", "高德地图客户端已经安装");
                } else {
                    Log.e("GasStation", "没有安装高德地图客户端");
                }
            }
        });
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public void initmap() {
        map = mapView.getMap();
        mUiSettings = map.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setCompassEnabled(true);
        //map.setMapType(AMap.MAP_TYPE_SATELLITE);

        LatLng latLng = new LatLng(29.29714201659764, 120.18971421257021);//坐标对象
        map.clear();
        MarkerOptions options = new MarkerOptions();
        //options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
               // .decodeResource(getResources(), R.drawable.maplogo)));
        options.title("中国木雕博物馆").snippet("");
        options.position(latLng);//定位图标要显示的位置
        options.draggable(true);//设置Marker可拖动

        map.addMarker(options);//给地图添加标记
        CameraUpdate update =CameraUpdateFactory.changeLatLng(latLng);//更新定位范围

        map.moveCamera(update);//显示
        map.moveCamera(CameraUpdateFactory.zoomTo(17));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}