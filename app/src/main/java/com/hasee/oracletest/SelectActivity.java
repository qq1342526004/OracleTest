package com.hasee.oracletest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hasee.oracletest.adapter.SelectAdapter;
import com.hasee.oracletest.fragment.SelectItemDialog;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView selectFragmentListview;
    private Button selectFragmentButtonAdd,selectFragmentButtonExecute;
    public static List<String[]> list;
    public static SelectAdapter adapter;
    private SelectItemDialog selectItemDialog;
    private MyListener listener = new MyListener() {
        @Override
        public void sendMessageToServer(String message) {

        }

        @Override
        public void sendMessage(JSONArray jsonArray) {
            Bundle bundle = new Bundle();
            selectItemDialog = new SelectItemDialog();
            switch (Integer.parseInt(jsonArray.getString(0))){
                case 0:
                    bundle.putString("title","选择列名");
                    selectItemDialog.setArguments(bundle);
                    break;
                case 1:
                    bundle.putString("title","选择运算符");
                    selectItemDialog.setArguments(bundle);
                    break;
                case 2:
                    bundle.putString("title","选择");
                    selectItemDialog.setArguments(bundle);
                    break;
            }
            selectItemDialog.show(getSupportFragmentManager(),"selectItem_dialog");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_dialog);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitle("查询过滤器");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        selectFragmentListview = (ListView)findViewById(R.id.selectFragment_listview);
        selectFragmentButtonAdd = (Button)findViewById(R.id.selectFragment_button_add);
        selectFragmentButtonAdd.setOnClickListener(onClickListener);
        selectFragmentButtonExecute = (Button)findViewById(R.id.selectFragment_button_execute);
        selectFragmentButtonExecute.setOnClickListener(onClickListener);
        list = new ArrayList<>();
        adapter = new SelectAdapter(SelectActivity.this,list,listener);
        selectFragmentListview.setAdapter(adapter);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.selectFragment_button_add:
                    list.add(new String[]{});
                    adapter.notifyDataSetChanged();
                    selectFragmentListview.setSelection(list.size()-1);
                    break;
                case R.id.selectFragment_button_execute:
                    break;
            }
        }
    };

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
