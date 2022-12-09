package com.xlyj.wood.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditItemActivity extends BaseActivity {

    @BindView(R.id.tv_item)
    TextView tvItem;
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.btn_save)
    Button btnSave;
    String item;

    private static final String UPDATE_USER_INFO = "android.intent.action.USER_INFO_UPDATE_SUCCESS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_edit_item);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        initView();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
            }
        });
    }

    private void doUpdate() {
        if(item.equals("修改昵称")){
          User user =  BmobUser.getCurrentUser(User.class);
          if(et.getText().toString().trim().equals("")){
              Toast.makeText(this,"请输入有效值", Toast.LENGTH_SHORT).show();
              return;
          }
          user.setNickname(et.getText().toString());
          user.update(new UpdateListener() {
              @Override
              public void done(BmobException e) {
                 if(e == null){
                     Toast.makeText(EditItemActivity.this,"修改成功", Toast.LENGTH_SHORT).show();
                     Intent broadcast = new Intent(UPDATE_USER_INFO);
                     LocalBroadcastManager.getInstance(EditItemActivity.this).sendBroadcast(broadcast);
                 }else {
                     Toast.makeText(EditItemActivity.this,"修改失败", Toast.LENGTH_SHORT).show();

                 }
              }
          });
        }
    }

    private void initView() {
        item = getIntent().getStringExtra("item");
        String old = getIntent().getStringExtra("old");
        tvItem.setText(item);
        et.setText(old);
    }
}
