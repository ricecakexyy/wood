package com.xlyj.slqj.SLQJ.AI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.baidu.ai.edge.core.util.FileUtil;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.AI.xeye.XeyeActivity;
import com.xlyj.utils.StatusBarUtil;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private Button startUIActivityBtn;
    private String name = "";
    private String version = "";
    private String ak;
    private String sk;
    private String apiUrl;
    private String soc;
    private ArrayList<String> socList = new ArrayList<>();
    private int type;
    // 请替换为您的序列号
    private static final String SERIAL_NUM = "202C-621D-4189-C839"; //"XXXX-XXXX-XXXX-XXXX"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        final AlertDialog.Builder agreementDialog = new AlertDialog.Builder(this)
                .setTitle("允许“百度EasyDL”使用数据？")
                .setMessage("可能同时包含无线局域网和蜂窝移动数据")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = getSharedPreferences("demo_auth_info",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isAgree", true);
                        editor.commit();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startUICameraActivity();
                            }
                        }).start();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("不允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        initConfig();

        TextView modelNameText = findViewById(R.id.model_text);
        modelNameText.setText(name);

        startUIActivityBtn = findViewById(R.id.start_ui_activity);
        startUIActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("demo_auth_info", Context.MODE_PRIVATE);
                boolean hasAgree = sp.getBoolean("isAgree", false);
                boolean checkChip = checkChip();
                if (hasAgree) {
                    Log.i(this.getClass().getSimpleName(), "socList:" + socList.toString()
                            + ", Build.HARDWARE is :" + Build.HARDWARE + "soc:" + soc);
                    if (checkChip) {
                        startUICameraActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "socList:" + socList.toString()
                                        + ", Build.HARDWARE is :" + Build.HARDWARE+"wuwu"+"checkChip"+checkChip,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    agreementDialog.show();
                }
            }
        });
    }

    private boolean checkChip() {
        if (socList.contains("arm")) {
            soc = "arm";
            return true;
        }
        if (socList.contains("dsp") && Build.HARDWARE.equalsIgnoreCase("qcom")) {
            soc = "dsp";
            return true;
        }
        if (socList.contains("npu") && (Build.HARDWARE.contains("kirin970") || Build.HARDWARE.contains("kirin980"))) {
            if (Build.HARDWARE.contains("kirin970")) {
                soc = "npu150";
            }
            if (Build.HARDWARE.contains("kirin980")) {
                soc = "npu200";
            }
            return true;
        }
        if (socList.contains("npu-vinci") && (Build.HARDWARE.contains("kirin810")
                || Build.HARDWARE.contains("kirin820") || Build.HARDWARE.contains("kirin990"))) {
            soc = "npu-vinci";
            return true;
        }

        if (socList.contains("xeye")) {
            soc = "xeye";
            return true;
        }

        return false;
    }

    private void startUICameraActivity() {
        if (soc.equals("xeye")) {
            Intent xeyeIntent = new Intent((MainActivity.this), XeyeActivity.class);
            xeyeIntent.putExtra("model_type", type);
            xeyeIntent.putExtra("serial_num", SERIAL_NUM);
            startActivity(xeyeIntent);
            Log.i("tessssss","XeyeActivity");
        } else {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("model_type", type);
            intent.putExtra("serial_num", SERIAL_NUM);
            Log.i("tessssss","CameraActivity");

            if (apiUrl != "null") {
                intent.putExtra("apiUrl", apiUrl);
                intent.putExtra("ak", ak);
                intent.putExtra("sk", sk);
            }

            intent.putExtra("soc", soc);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * 读取json配置
     */
    private void initConfig() {
        try {
            String configJson = FileUtil.readAssetFileUtf8String(getAssets(), "demo/config.json");
            //Toast.makeText(this,"json is "+configJson,Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject(configJson);
            name = jsonObject.getString("model_name");
            version = jsonObject.getString("model_version");
            type = jsonObject.getInt("model_type");

            if (jsonObject.has("apiUrl")) {
                apiUrl = jsonObject.getString("apiUrl");
                ak = jsonObject.getString("ak");
                sk = jsonObject.getString("sk");
            }

            JSONArray jsonArray = jsonObject.getJSONArray("soc");
            for (int i = 0; i < jsonArray.length(); i++) {
                String s = jsonArray.getString(i);
                socList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
