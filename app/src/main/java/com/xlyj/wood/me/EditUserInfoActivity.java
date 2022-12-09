package com.xlyj.wood.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.utils.FileUtils;
import com.xlyj.wood.utils.StatusBarUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditUserInfoActivity extends BaseActivity {
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_edit_user_info);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        Glide.with(this)
                .load(BmobUser.getCurrentUser(User.class).getAvatar())
                .into(ivHead);
        setOnClick();
    }

    private void setOnClick() {
        EditOnClickListener clickListener = new EditOnClickListener();
        rlHead.setOnClickListener(clickListener);
        rlNickname.setOnClickListener(clickListener);
        rlPassword.setOnClickListener(clickListener);
    }

    private class EditOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_head:
                    //设置头像
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 2);
                    break;
                case R.id.rl_nickname:
                    edit("修改昵称", BmobUser.getCurrentUser(User.class).getNickname());
                    //修改昵称
                    break;
                case R.id.rl_password:
                    //修改密码
                    edit("修改密码", "");
                    break;
            }
        }
    }

    private void edit(String item, String old) {
        Intent intent = new Intent(this, EditItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("item", item);
        bundle.putString("old", old);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                String picPath = FileUtils.getRealPathFromUri(this, uri);
//                updateImg(picPath);
//                uploadFile(picPath, "xyy.jpg");
            }
        }
    }

    private void updateImg(String picPath) {
//        BmobFile bmobFile = new BmobFile("x.jpg",null,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594961164192&di=137394c1d2061bd74d0bf22ef09156b0&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201606%2F12%2F20160612213112_wWZEA.thumb.700_0.jpeg");
//        BmobFile bmobFile = new BmobFile(new File(picPath));
//        bmobFile.uploadblock(new UploadFileListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    Log.e("done: ", bmobFile.getFileUrl());//--返回的上传文件的完整地址
//                    Log.i("done: ", "上传文件成功:" + bmobFile.getFileUrl());
//                    User user = BmobUser.getCurrentUser(User.class);
//                    user.setAvatar(bmobFile);
//                    user.update(new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            if (e == null) {
//                                Log.i("done: ", "头像上传成功:" + bmobFile.getFileUrl());
//                            } else {
//                                Log.i("done: ", "头像上传失败：" + e.getMessage() + e.getErrorCode());
//                            }
//                        }
//                    });
//                } else {
//                    Log.i("done: ", "上传文件失败：" + e.getMessage() + e.getErrorCode());
//                }
////
//            }
//
////            @Override
////            public void onProgress(Integer value) {
////            }
//        });
    }

    public boolean uploadFile(String path, String filename) {
        OkHttpClient okhttp = new OkHttpClient();
        File file = new File(path);
        Boolean a = path.isEmpty();
        Boolean b = file.exists();
        if (path.isEmpty() || !file.exists())
            return false;
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, RequestBody.create(MediaType.parse("multipart/form-data"), new File(path)))
                .build();
        FutureTask<Boolean> task = new FutureTask<Boolean>(() -> {
            try {
                Response response = okhttp.newCall(new Request.Builder().post(body).url("http://www.lvyibin.top:8081/qiniu/upload").build()).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject o = jsonObject.optJSONObject("data");
                String s = o.optString("url");
                Log.i("code", s.toString());
                return false;
            } catch (IOException e) {
                return false;
            }
        });
        try {
            new Thread(task).start();
            Log.e("task.get()", task.get().toString());
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
        return true;
    }
}
