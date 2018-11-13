package com.hasee.onlinedb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hasee.onlinedb.adapter.SelectAdapter;
import com.hasee.onlinedb.fragment.SelectItemDialog;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity implements MyListener{
    private Toolbar toolbar;
    private ListView selectFragmentListview;
    private Button selectFragmentButtonAdd,selectFragmentButtonExecute;
    public static List<String[]> list;
    public static SelectAdapter adapter;
    private SelectItemDialog selectItemDialog;

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
        list = new ArrayList<>();//每次初始化
        adapter = new SelectAdapter(SelectActivity.this,list,this);
        selectFragmentListview.setAdapter(adapter);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.selectFragment_button_add:
                    list.add(new String[]{MainActivity.colNameString[0],"=","","AND"});
                    adapter.notifyDataSetChanged();
//                    selectFragmentListview.setSelection(list.size()-1);
                    break;
                case R.id.selectFragment_button_execute:
                    break;
            }
        }
    };

    private void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = selectFragmentListview.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = selectFragmentListview.getChildAt(itemIndex - visiblePosition);
            //调用adapter更新界面
            adapter.updateView(view, itemIndex);
        }
    }

    @Override
    public void sendMessageToServer(String message) {
        if(selectItemDialog != null){
            selectItemDialog.dismiss();
            this.updateView(Integer.parseInt(message));
        }
    }

    @Override
    public void sendMessage(JSONArray jsonArray) {
        Bundle bundle = new Bundle();
        bundle.putInt("positon",jsonArray.getInt(1));
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
