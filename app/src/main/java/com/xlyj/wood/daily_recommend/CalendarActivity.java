package com.xlyj.wood.daily_recommend;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.ui.ProgressDialog;
import com.xlyj.wood.utils.FileUtils;
import com.xlyj.wood.utils.ImgUtils;
import com.xlyj.wood.utils.PlatformUtil;
import com.xlyj.wood.utils.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CalendarActivity extends BaseActivity {
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.iv_picture)
    RoundedImageView ivPicture;
    @BindView(R.id.tv_d1)
    TextView tvD1;
    @BindView(R.id.tv_d2)
    TextView tvD2;
    @BindView(R.id.calendar)
    LinearLayout calendar;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.iv_qq)
    ImageView ivQQ;
    ProgressDialog mLDialog;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_activity_calendar);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
    }

    private void initView() {

        final LinearLayout rootView = findViewById(R.id.ll_share);
        for (int i = 0; i < rootView.getChildCount(); i++) {
            final View view = rootView.getChildAt(i);
            view.setVisibility(View.INVISIBLE);
            //???????????????????????????(??????????????????????????????)
            Observable.timer(i * 100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            view.setVisibility(View.VISIBLE);
//                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//                            alphaAnimation.setDuration(1500);
//                            view.startAnimation(alphaAnimation);
                        }
                    });
        }

        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvD1.setTypeface(font);
        tvD2.setTypeface(font);
        ivDownload.setOnClickListener(new OnClick());
        ivQQ.setOnClickListener(new OnClick());
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_download:
                    requestPermissions();
                    break;
                case R.id.iv_qq:
                    if(bitmap==null) {
                        bitmap = createViewBitmap(calendar);
                    }
                    shareImageToQQ(bitmap);
                    break;
            }
        }
    }

//?????????????????????view????????? ??????bitmap??????

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            //??????sd????????????
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(this, mPermissionList)) {
                //???????????????
                saveImage();
            } else {
                //????????????,??????????????????????????????????????????
                EasyPermissions.requestPermissions(
                        this,  //?????????
                        "????????????????????????sd????????????", //????????????
                        REQUEST_CODE_SAVE_IMG, //?????????
                        mPermissionList //????????????
                );
            }
        } else {
            saveImage();
        }
    }



    private void saveImage() {
        Log.i("testss", "?????????");
        bitmap = createViewBitmap(calendar);
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(this, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ???????????????QQ??????
     *
     * @param bitmap
     */
    public void shareImageToQQ(Bitmap bitmap) {
        if (PlatformUtil.isInstallApp(this, PlatformUtil.PACKAGE_MOBILE_QQ)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        this.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // ???????????????????????????????????????????????????????????????
                ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");

                shareIntent.setComponent(componentName);
                // mContext.startActivity(shareIntent);
                this.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (Exception e) {
//            ContextUtil.getInstance().showToastMsg("???????????????**??????");
            }
        }
    }

}
