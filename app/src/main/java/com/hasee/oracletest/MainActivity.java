package com.hasee.oracletest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hasee.oracletest.adapter.TabelAdapter;
import com.hasee.oracletest.fragment.HandleDialog;
import com.hasee.oracletest.fragment.ShowMsgFragment;
import com.hasee.oracletest.fragment.UpdateFragment;
import com.hasee.oracletest.util.SocketUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyListener {
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private Toolbar toolbar;//标题栏
    private ListView listView;//显示表格行的ListView
    private TextView emptyListViewTextView;
    private List<TabelAdapter.TabelRow> list = new ArrayList<>();//数据源
    private TabelAdapter adapter;//ListView适配器
    private DrawerLayout drawerLayout;
    private LinearLayout tabelNameLayout;
    //下部页面操作
    private LinearLayout bottomLayout;
    private TextView firstPage, previewPage, nextPage, lastPage, jumpPage;
    private EditText indexPage;
    public static int indexNumber = 0;//索引号
    public static final int MAX_ITEM_COUNT = 10;//一页最多多少数据
    private int pageNumber;//总页数
    TabelAdapter.TabelRow tabelNameRow;//表头
    JSONArray colAttributeJSONArray;
    //Util
    private SocketUtil socketUtil = null;
    private Myhandler myhandler = new Myhandler();
    //Fragment
    private HandleDialog handleDialog = new HandleDialog();
    private ShowMsgFragment showMsgFragment;
    private UpdateFragment updateFragment;
    //OtherActivity
    private TextView settingTv = null;
    private TextView aboutTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolBar_main1);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerListener(drawerListener);
        drawerLayout.setTag("main1_layout");
        emptyListViewTextView = (TextView)findViewById(R.id.empty_listView_tv);
        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(emptyListViewTextView);
        adapter = new TabelAdapter(MainActivity.this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        bottomLayout.setVisibility(View.INVISIBLE);
        settingTv = (TextView) findViewById(R.id.setting_tv);
        settingTv.setOnClickListener(onClickListener);
        aboutTv = (TextView) findViewById(R.id.about_tv);
        aboutTv.setOnClickListener(onClickListener);
        firstPage = (TextView) findViewById(R.id.first_page);
        firstPage.setOnClickListener(onClickListener);
        previewPage = (TextView) findViewById(R.id.preview_page);
        previewPage.setOnClickListener(onClickListener);
        nextPage = (TextView) findViewById(R.id.next_page);
        nextPage.setOnClickListener(onClickListener);
        lastPage = (TextView) findViewById(R.id.last_page);
        lastPage.setOnClickListener(onClickListener);
        indexPage = (EditText) findViewById(R.id.index_page);
        jumpPage = (TextView) findViewById(R.id.jump_page);
        jumpPage.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socketUtil!=null){
            socketUtil.closeAll();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.select_menu:
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(3);
                sendMessageToServer(jsonArray.toString());
                break;
            case R.id.filter_menu:
//                selectFragment = new SelectFragment();
//                selectFragment.show(getSupportFragmentManager(),"select_dialog");
                Intent intent = new Intent(MainActivity.this,SelectActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    /*
     * OnClickListener
     * */
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.setting_tv:
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.about_tv:
                    Intent intent1 = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.first_page://首页
                    indexNumber = 0;
                    indexPage.setHint(1 + "/" + pageNumber);
                    adapter.notifyDataSetChanged();
                    showToast("首页");
                    break;
                case R.id.preview_page://上一页
                    if (indexNumber > 0) {
                        indexNumber--;
                        adapter.notifyDataSetChanged();
                        indexPage.setHint((indexNumber + 1) + "/" + pageNumber);
                        showToast("上一页");
                    }
                    break;
                case R.id.next_page://下一页
                    if(indexNumber<pageNumber-1){
                        indexNumber++;
                        adapter.notifyDataSetChanged();
                        indexPage.setHint((indexNumber + 1) + "/" + pageNumber);
                        showToast("下一页");
                    }
                    break;
                case R.id.last_page://尾页
                    indexNumber = pageNumber-1;
                    indexPage.setHint(pageNumber + "/" + pageNumber);
                    adapter.notifyDataSetChanged();
                    showToast("尾页");
                    break;
                case R.id.jump_page://跳页
                    if (!"".equals(indexPage.getText().toString().trim())&&(!"0".equals(indexPage.getText().toString().trim()))) {
                        int number = Integer.parseInt(indexPage.getText().toString().trim());
                        if ((number <= pageNumber)) {
                            indexNumber = number - 1;
                            indexPage.setHint(number + "/" + pageNumber);
                            indexPage.setText("");
                            adapter.notifyDataSetChanged();
                            showToast("跳页");
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void sendMessageToServer(final String message) {
        if(handleDialog.isVisible()){
            handleDialog.dismiss();
        }
        showProgressDialog();
        socketUtil = new SocketUtil();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = null;
                socketUtil.connected();
                if (socketUtil.getFlag()) {//连接成功
                    String receive_meaasge = socketUtil.send_receive(message);
                    Log.d(TAG, "run: " + receive_meaasge);
                    jsonArray = JSONArray.fromObject(receive_meaasge);
                    socketUtil.closeAll();
                } else {//连接失败
                    jsonArray = new JSONArray();
                    jsonArray.add("0");
                }
                Message msg = myhandler.obtainMessage();
                msg.obj = jsonArray;
                myhandler.sendMessage(msg);
                Log.d(TAG, "run: myhandle");
            }
        }).start();
    }

    @Override
    public void sendMessage(JSONArray jsonArray) {
        handleDialog.dismiss();
        int flag = Integer.parseInt(jsonArray.getString(0));
        Bundle bundle = new Bundle();
        if(flag == 2){
//            bundle
        }else{
            bundle.putSerializable("msg_item", (Serializable) jsonArray.get(1));
        }
        switch (flag){
            case 0:
                showMsgFragment = new ShowMsgFragment();
                showMsgFragment.setArguments(bundle);
                showMsgFragment.show(getSupportFragmentManager(),"showMsg_dialog");
                break;
            case 1:
                updateFragment = new UpdateFragment();
                updateFragment.setArguments(bundle);
                updateFragment.show(getSupportFragmentManager(),"update_dialog");
                break;
//            case 2:
//                SelectItemDialog selectItemDialog = new SelectItemDialog();
//                selectItemDialog.show(getSupportFragmentManager(),"selectItem_dialog");
//                break;
        }
    }

    /*
     * 主页面listview事件处理
     * */
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            i = i+MAX_ITEM_COUNT*indexNumber;//ListView当前位置
            List<List<String>> stringList = new ArrayList<>();
            for (int j = 0; j < tabelNameRow.getSize(); j++) {
                List<String> strings = new ArrayList<>();//第j列
                strings.add(colAttributeJSONArray.getString(j));//列属性
                strings.add(tabelNameRow.getCellValue(j).getValue());//列名
                strings.add(list.get(i).getCellValue(j).getValue());//对应值
                stringList.add(strings);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("showmsg_item", (Serializable) stringList);
            handleDialog = new HandleDialog();
            handleDialog.setArguments(bundle);
            handleDialog.show(getSupportFragmentManager(), "handle_dialog");
        }
    };


    /*
     * 处理数据显示UI
     * */
    class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            JSONArray jsonArray = (JSONArray) msg.obj;
            switch (Integer.parseInt(jsonArray.getString(0))) {
                case 0://操作失败
                    break;
                case 1:
                    break;
                case 103://查询全部信息
                    selectShow(jsonArray.toString());
                    adapter.notifyDataSetChanged();
                    closeProgressDialog();
                    break;
            }
        }
    }

    private void selectShow(String receive_message) {
//        receive_message = "";
        JSONArray jsonArray = JSONArray.fromObject(receive_message);
        //初始化每一列对应的属性
        colAttributeJSONArray = jsonArray.getJSONArray(1);
        //初始化列名
        JSONArray colName = jsonArray.getJSONArray(2);//列名JSONArray
//        Log.d(TAG, "init: " + colName.toString());
        int colNameLength = colName.size();
        int width = this.getWindowManager().getDefaultDisplay().getWidth() / colName.size();
        //清空list中的数据
        list.clear();
        //定义标题
        TabelAdapter.TabelCell[] cellColName = new TabelAdapter.TabelCell[colNameLength];
        for (int i = 0; i < colNameLength; i++) {
            cellColName[i] = new TabelAdapter.TabelCell(colName.getString(i),
                    width + 8 * i, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        //定义表头数据
        tabelNameLayout = (LinearLayout)findViewById(R.id.tabelName_layout);
        tabelNameRow = new TabelAdapter.TabelRow(cellColName);
        //把表头加入tabelNameLayout
        for (int i = 0; i < tabelNameRow.getSize(); i++) {//逐个将单元格添加到行
            TabelAdapter.TabelCell cell = tabelNameRow.getCellValue(i);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cell.getWidth(),cell.getHeight());//按照单元格指定大小设置空间
            layoutParams.setMargins(0,0,1,1);//预留空隙制造边框
            //设置单元格中的数据以及排版
            TextView cellTextView = new TextView(MainActivity.this);
            cellTextView.setLines(1);
            cellTextView.setGravity(Gravity.CENTER);
            cellTextView.setText(cell.getValue());
            cellTextView.setTextSize(20);
            cellTextView.setTextColor(Color.WHITE);
//            cellTextView.setBackgroundColor(Color.parseColor("#00EE76"));
            cellTextView.setBackgroundResource(R.color.tabelRow_tabelName);
            tabelNameLayout.addView(cellTextView,layoutParams);
        }

        //定义行数据
        for (int i = 3; i < jsonArray.size(); i++) {//从3开始取对象
            JSONObject rowObject = jsonArray.getJSONObject(i);//行对象
            TabelAdapter.TabelCell[] cellRowObject = new TabelAdapter.TabelCell[colNameLength];//一行中单元格的个数;
            for (int j = 0; j < colNameLength; j++) {
                cellRowObject[j] =
                        new TabelAdapter.TabelCell(
                                rowObject.getString(colName.getString(j)), width + 8 * j, LinearLayout.LayoutParams.MATCH_PARENT);//每一个单元格
            }
            list.add(new TabelAdapter.TabelRow(cellRowObject));
        }
        bottomLayout.setVisibility(View.VISIBLE);
        pageNumber = (list.size() / MAX_ITEM_COUNT)+1;//界面中显示的页码:系统真实页码 = pageNumber:pageNumber-1
        indexPage.setHint(1 + "/" + pageNumber);
    }

    /*
     * 页面滑动效果
     * */
    public DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View view, float v) {
            if ("main1_layout".equals(drawerLayout.getTag())) {
                View content = drawerLayout.getChildAt(0);
                int offSet = (int) (view.getWidth() * v);
                content.setTranslationX(offSet);
//                content.setScaleX(1 - v * 0.5f);
//                content.setScaleY(1 - v * 0.5f);
            }
        }

        @Override
        public void onDrawerOpened(@NonNull View view) {

        }

        @Override
        public void onDrawerClosed(@NonNull View view) {

        }

        @Override
        public void onDrawerStateChanged(int i) {

        }
    };


    /*
     * 显示提示信息
     * */
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * 显示进度对话框
     * */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity.this);
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

}
