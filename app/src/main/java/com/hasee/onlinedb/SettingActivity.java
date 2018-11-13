package com.hasee.onlinedb;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hasee.onlinedb.util.SocketUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private EditText linkNameEt, linkAddressEt, linkPortEt, linkAccountEt, linkPassowrdEt,otherInfoEt;
    private Button submitButton;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject = new JSONObject();
    private JSONArray jsonArray;
    private SocketUtil socketUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        linkNameEt = (EditText) findViewById(R.id.linkName_et);
        linkAddressEt = (EditText) findViewById(R.id.linkAddress_et);
        linkPortEt = (EditText) findViewById(R.id.linkPort_et);
        linkAccountEt = (EditText) findViewById(R.id.linkAccount_et);
        linkPassowrdEt = (EditText) findViewById(R.id.linkPassword_et);
        otherInfoEt  = (EditText)findViewById(R.id.other_et);
        otherInfoEt.setText("characterEncoding=UTF-8" +
                "&connectTimeout=30000" +
                "&socketTimeout=30000" +
                "&autoReconnect=true" +
                "&tinyInt1isBit=false");
        submitButton = (Button) findViewById(R.id.setting_submit_button);
        submitButton.setOnClickListener(onClickListener);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_submit_button:
                    if (etIsEmpty()){
                        jsonObject.put("dbName",linkNameEt.getText().toString());
                        jsonObject.put("hostIp",linkAddressEt.getText().toString());
                        jsonObject.put("hostPort",linkPortEt.getText().toString());
                        jsonObject.put("linkAccount",linkAccountEt.getText().toString());
                        jsonObject.put("linkPassword",linkPassowrdEt.getText().toString());
                        jsonObject.put("otherInfo",otherInfoEt.getText().toString());
                        dbConnect(jsonObject);
                    }else{
                        showToast("输入框不能为空");
                    }
                    break;
            }
        }
    };

    /*
     * 判断编辑框中的数值是否为空
     * */
    public boolean etIsEmpty() {
        if ("".equals(linkNameEt.getText().toString().trim()) ||
                "".equals(linkAddressEt.getText().toString().trim()) ||
                "".equals(linkPortEt.getText().toString().trim()) ||
                "".equals(linkAccountEt.getText().toString().trim()) ||
                "".equals(linkPassowrdEt.getText().toString().trim())||
                "".equals(otherInfoEt.getText().toString().trim())) {
            return false;
        }
        return true;
    };


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            closeProgressDialog();
            JSONArray jsonArray = (JSONArray) msg.obj;
            switch (Integer.parseInt(jsonArray.getString(0))){
                case 0:
                    showToast("连接远程数据库失败");
                    break;
                case 1:
                    SharedPreferences.Editor editor = getSharedPreferences("databaseLinkInfo",MODE_PRIVATE).edit();
                    editor.putString("databaseLinkInfo",jsonObject.toString());
                    editor.apply();
                    finish();
                    break;
            }
        }
    };

    /*
    * 数据是否连接成功
    * */
    public void dbConnect(final JSONObject jsonObject){
        showProgressDialog();
        socketUtil = new SocketUtil();
        jsonArray = new JSONArray();
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketUtil.connected();
                if(socketUtil.getFlag()){
                    jsonArray.add("50");
                    jsonArray.add(jsonObject);
                    String receive_message = socketUtil.send_receive(jsonArray.toString());
                    jsonArray = JSONArray.fromObject(receive_message);
                    socketUtil.closeAll();
                }else{
                    jsonArray.add("0");
                }
                Message msg = handler.obtainMessage();
                msg.obj = jsonArray;
                handler.sendMessage(msg);
            }
        }).start();
    }


    /*
     * 显示提示信息
     * */
    public void showToast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * 显示进度对话框
     * */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SettingActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*
     * 关闭进度对话框
     * */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(socketUtil != null){
                socketUtil.closeAll();
            }
            return true;
        }
        return false;
    }
}
