package com.xlyj.wood.find.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Article;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.utils.FileUtils;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import jp.wasabeef.richeditor.RichEditor;

public class RichEditActivity extends BaseActivity {

    private static final int REQUEST_PICK_IMAGE = 1;
    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.action_bold)
    ImageView actionBold;
    @BindView(R.id.action_insert_image)
    ImageView actionInsertImage;
    @BindView(R.id.action_undo)
    ImageView actionUndo;
    @BindView(R.id.action_redo)
    ImageView actionRedo;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_rich_edit);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        setClick();
    }

    /**
     * 点击事件
     */
    private void setClick() {
        actionInsertImage.setOnClickListener(new RichEditClick());
        ivSetting.setOnClickListener(new RichEditClick());
    }

    String[] mPermissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    /**
     * 具体点击事件的实现
     */
    public class RichEditClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.action_insert_image:
                    //选择图片
                    mEditor.focusEditor();
                    ActivityCompat.requestPermissions(RichEditActivity.this, mPermissionList, 100);
                    break;
                case R.id.iv_setting:
                   TextView tv = findViewById(R.id.tv);
                   tv.setVisibility(View.VISIBLE);
                   tv.setText(mEditor.getHtml());
                    Article article = new Article();
                    article.setHtml(mEditor.getHtml());
                    article.setUser(BmobUser.getCurrentUser(User.class));
                    article.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {

                        }
                    });
                   break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    getImage();
                } else {
                    Toast.makeText(this, "请设置必要权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void getImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        String realPathFromUri = FileUtils.getRealPathFromUri(this, data.getData());
//                        mEditor.insertImage("https://unsplash.it/2000/2000?random&58",
//                                "huangxiaoguo\" style=\"max-width:100%");
//                        mEditor.insertImage(realPathFromUri, realPathFromUri + "\" style=\"max-width:100%");
                        mEditor.insertImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597766143857&di=16797fd097bd6b7f81799b5b550df520&imgtype=0&src=http%3A%2F%2Fa1.att.hudong.com%2F05%2F00%2F01300000194285122188000535877.jpg","twitter"+ "\" style=\"max-width:100%");
//                        mEditor.insertImage(realPathFromUri, realPathFromUri + "\" style=\"max-width:100%;max-height:100%");
                    } else {
                        Toast.makeText(this, "图片损坏，请重新选择", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
