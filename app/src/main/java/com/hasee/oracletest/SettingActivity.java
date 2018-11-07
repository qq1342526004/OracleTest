package com.hasee.oracletest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hasee.oracletest.util.SocketUtil;


public class SettingActivity extends AppCompatActivity {
    EditText ip_et,port_et;
    Button submit;
    Toolbar toolbar = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);
        ip_et = (EditText)findViewById(R.id.ip_et);
        ip_et.setHint("当前IP:"+SocketUtil.IP);
        port_et = (EditText)findViewById(R.id.port_et);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = ip_et.getText().toString();
//                int port = 1234;
                if("".equals(ip)){
                    Toast.makeText(SettingActivity.this, "IP和端口不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    SocketUtil.IP = ip;
//                    SocketUtil.PORT = port;
                    finish();
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
