package com.hasee.oracletest;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.hasee.oracletest.R;
import com.hasee.oracletest.adapter.TabelAdapter;
import com.hasee.oracletest.fragmeent.HandleDialog;
import com.hasee.oracletest.util.SocketUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyListener{
    private static final String TAG = "MainActivity";
    private ProgressDialog progressDialog;
    private Toolbar toolbar;//标题栏
    private ListView listView;//显示表格行的ListView
    private List<TabelAdapter.TabelRow> list = new ArrayList<>();//数据源
    private TabelAdapter adapter;//ListView适配器
    //下部页面操作
    private LinearLayout bottomLayout;
    private TextView firstPage,previewPage,nextPage,lastPage,jumpPage;
    private EditText indexPage;
    public static int indexNumber = 0;//索引号
    public static final int MAX_ITEM_COUNT = 10;//一页最多多少数据
    private int pageNumber;//总页数
    //Util
    private SocketUtil socketUtil = null;
    private Myhandler myhandler = new Myhandler();
    //Fragment
    private HandleDialog handleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        listView = (ListView)findViewById(R.id.listView);
        adapter = new TabelAdapter(MainActivity.this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        bottomLayout = (LinearLayout)findViewById(R.id.bottom_layout);
        bottomLayout.setVisibility(View.INVISIBLE);
        firstPage = (TextView)findViewById(R.id.first_page);
        firstPage.setOnClickListener(onClickListener);
        previewPage = (TextView)findViewById(R.id.preview_page);
        previewPage.setOnClickListener(onClickListener);
        nextPage = (TextView)findViewById(R.id.next_page);
        nextPage.setOnClickListener(onClickListener);
        lastPage = (TextView)findViewById(R.id.last_page);
        lastPage.setOnClickListener(onClickListener);
        indexPage = (EditText) findViewById(R.id.index_page);
        jumpPage = (TextView)findViewById(R.id.jump_page);
        jumpPage.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.add(3);
//                sendMessageToServer(jsonArray.toString());
                selectShow("");
                adapter.notifyDataSetChanged();
                showToast("home");
                break;
            case R.id.:
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
            switch (view.getId()){
                case R.id.first_page://首页
                    indexNumber=0;
                    indexPage.setHint(1+"/"+pageNumber);
                    adapter.notifyDataSetChanged();
                    showToast("first");
                    break;
                case R.id.preview_page://上一页
                    if(indexNumber>0){
                        indexNumber--;
                        adapter.notifyDataSetChanged();
                        checkButton();
                        showToast("preView");
                    }
                    break;
                case R.id.next_page://下一页
                    indexNumber++;
                    adapter.notifyDataSetChanged();
                    checkButton();
                    showToast("next");
                    break;
                case R.id.last_page://尾页
                    indexNumber=pageNumber-1;
                    indexPage.setHint(pageNumber+"/"+pageNumber);
                    adapter.notifyDataSetChanged();
                    showToast("last");
                    break;
                case R.id.jump_page://跳页
                    if(!"".equals(indexPage.getText().toString().trim())){
                        int number = Integer.parseInt(indexPage.getText().toString().trim());
                        if((number<=pageNumber)){
                            indexNumber = number-1;
                            indexPage.setHint(number+"/"+pageNumber);
                            adapter.notifyDataSetChanged();
                            showToast("jump");
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void sendMessageToServer(final String message) {
//        if(handleDialog.isVisible()){
//            handleDialog.dismiss();
//        }
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
            }
        }).start();
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {

    }

    /*
     * 主页面listview事件处理
     * */
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle bundle = new Bundle();
//            bundle.putSerializable("jsonItem",list.get(i));
            handleDialog = new HandleDialog();
//            handleDialog.setArguments(bundle);
            handleDialog.show(getSupportFragmentManager(),"handle_dialog");
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
                    break;
            }
        }
    }


    public void checkButton(){
        if(indexNumber<=0){//索引值<=0
            previewPage.setEnabled(false);//上一页不可用
            indexPage.setHint(1+"/"+pageNumber);
        }else{
            nextPage.setEnabled(true);//否则下一页可用
            indexPage.setHint((indexNumber+1)+"/"+pageNumber);
        }

        if(list.size()-indexNumber*MAX_ITEM_COUNT<=MAX_ITEM_COUNT){//尾页了
            nextPage.setEnabled(false);//下一页不可用
        }else{//否则均可用
            previewPage.setEnabled(true);
            nextPage.setEnabled(true);
        }
    }


    private void selectShow(String receive_message) {
        receive_message = "[\"103\",[\"s_number\",\"s_name\",\"s_age\"],{\"s_number\":\"10\",\"s_name\":\"MYGkh\",\"s_age\":\"13\"},{\"s_number\":\"100\",\"s_name\":\"GsUxB\",\"s_age\":\"18\"},{\"s_number\":\"101\",\"s_name\":\"SwEID\",\"s_age\":\"34\"},{\"s_number\":\"102\",\"s_name\":\"uAtsJ\",\"s_age\":\"18\"},{\"s_number\":\"103\",\"s_name\":\"ugxUf\",\"s_age\":\"35\"},{\"s_number\":\"104\",\"s_name\":\"SPtAv\",\"s_age\":\"27\"},{\"s_number\":\"105\",\"s_name\":\"HzyVh\",\"s_age\":\"39\"},{\"s_number\":\"106\",\"s_name\":\"zAcLz\",\"s_age\":\"18\"},{\"s_number\":\"107\",\"s_name\":\"UHDSF\",\"s_age\":\"23\"},{\"s_number\":\"108\",\"s_name\":\"uGWPj\",\"s_age\":\"26\"},{\"s_number\":\"109\",\"s_name\":\"iipdw\",\"s_age\":\"38\"},{\"s_number\":\"11\",\"s_name\":\"eekOp\",\"s_age\":\"14\"},{\"s_number\":\"110\",\"s_name\":\"wswfm\",\"s_age\":\"36\"},{\"s_number\":\"111\",\"s_name\":\"IJxiz\",\"s_age\":\"38\"},{\"s_number\":\"112\",\"s_name\":\"qLIiP\",\"s_age\":\"25\"},{\"s_number\":\"113\",\"s_name\":\"ghssH\",\"s_age\":\"16\"},{\"s_number\":\"114\",\"s_name\":\"ifcXF\",\"s_age\":\"14\"},{\"s_number\":\"115\",\"s_name\":\"TasPV\",\"s_age\":\"13\"},{\"s_number\":\"116\",\"s_name\":\"XsUuo\",\"s_age\":\"16\"},{\"s_number\":\"117\",\"s_name\":\"mDfQO\",\"s_age\":\"22\"},{\"s_number\":\"118\",\"s_name\":\"QPBmE\",\"s_age\":\"18\"},{\"s_number\":\"119\",\"s_name\":\"EgSXg\",\"s_age\":\"34\"},{\"s_number\":\"12\",\"s_name\":\"RMiDv\",\"s_age\":\"20\"},{\"s_number\":\"120\",\"s_name\":\"HRmJL\",\"s_age\":\"25\"},{\"s_number\":\"121\",\"s_name\":\"xJevN\",\"s_age\":\"28\"},{\"s_number\":\"122\",\"s_name\":\"LPPFG\",\"s_age\":\"20\"},{\"s_number\":\"123\",\"s_name\":\"QdTjq\",\"s_age\":\"10\"},{\"s_number\":\"124\",\"s_name\":\"exWtb\",\"s_age\":\"11\"},{\"s_number\":\"125\",\"s_name\":\"eszuy\",\"s_age\":\"14\"},{\"s_number\":\"126\",\"s_name\":\"uAtqz\",\"s_age\":\"23\"},{\"s_number\":\"127\",\"s_name\":\"SVanp\",\"s_age\":\"32\"},{\"s_number\":\"128\",\"s_name\":\"OHXNZ\",\"s_age\":\"30\"},{\"s_number\":\"129\",\"s_name\":\"ymOlN\",\"s_age\":\"14\"},{\"s_number\":\"13\",\"s_name\":\"BEpPb\",\"s_age\":\"33\"},{\"s_number\":\"130\",\"s_name\":\"wKlbw\",\"s_age\":\"13\"},{\"s_number\":\"131\",\"s_name\":\"nYfEE\",\"s_age\":\"15\"},{\"s_number\":\"132\",\"s_name\":\"eYEbt\",\"s_age\":\"33\"},{\"s_number\":\"133\",\"s_name\":\"NydXB\",\"s_age\":\"34\"},{\"s_number\":\"134\",\"s_name\":\"xTYkg\",\"s_age\":\"10\"},{\"s_number\":\"135\",\"s_name\":\"NJhLg\",\"s_age\":\"23\"},{\"s_number\":\"136\",\"s_name\":\"VkmFp\",\"s_age\":\"30\"},{\"s_number\":\"137\",\"s_name\":\"zuBxJ\",\"s_age\":\"12\"},{\"s_number\":\"138\",\"s_name\":\"tERxJ\",\"s_age\":\"13\"},{\"s_number\":\"139\",\"s_name\":\"AlBbC\",\"s_age\":\"29\"},{\"s_number\":\"14\",\"s_name\":\"QMoje\",\"s_age\":\"35\"},{\"s_number\":\"140\",\"s_name\":\"BOobp\",\"s_age\":\"23\"},{\"s_number\":\"141\",\"s_name\":\"tzqhP\",\"s_age\":\"25\"},{\"s_number\":\"142\",\"s_name\":\"inRux\",\"s_age\":\"13\"},{\"s_number\":\"143\",\"s_name\":\"iBijv\",\"s_age\":\"26\"},{\"s_number\":\"144\",\"s_name\":\"ASPuG\",\"s_age\":\"36\"},{\"s_number\":\"145\",\"s_name\":\"DifbR\",\"s_age\":\"12\"},{\"s_number\":\"146\",\"s_name\":\"UlvwT\",\"s_age\":\"13\"},{\"s_number\":\"147\",\"s_name\":\"WpLKr\",\"s_age\":\"27\"},{\"s_number\":\"148\",\"s_name\":\"TFyza\",\"s_age\":\"29\"},{\"s_number\":\"149\",\"s_name\":\"hOAlC\",\"s_age\":\"11\"},{\"s_number\":\"15\",\"s_name\":\"dKrDP\",\"s_age\":\"18\"},{\"s_number\":\"150\",\"s_name\":\"EQrkb\",\"s_age\":\"24\"},{\"s_number\":\"151\",\"s_name\":\"vDGtd\",\"s_age\":\"14\"},{\"s_number\":\"152\",\"s_name\":\"CoMVq\",\"s_age\":\"34\"},{\"s_number\":\"153\",\"s_name\":\"irmhZ\",\"s_age\":\"26\"},{\"s_number\":\"154\",\"s_name\":\"PpfGQ\",\"s_age\":\"16\"},{\"s_number\":\"155\",\"s_name\":\"EsYOZ\",\"s_age\":\"27\"},{\"s_number\":\"156\",\"s_name\":\"btNLq\",\"s_age\":\"21\"},{\"s_number\":\"157\",\"s_name\":\"aTqZZ\",\"s_age\":\"39\"},{\"s_number\":\"158\",\"s_name\":\"TzQGH\",\"s_age\":\"21\"},{\"s_number\":\"159\",\"s_name\":\"bYMOL\",\"s_age\":\"17\"},{\"s_number\":\"16\",\"s_name\":\"eAsla\",\"s_age\":\"22\"},{\"s_number\":\"160\",\"s_name\":\"eLpqJ\",\"s_age\":\"24\"},{\"s_number\":\"161\",\"s_name\":\"qkcHd\",\"s_age\":\"22\"},{\"s_number\":\"162\",\"s_name\":\"Ukret\",\"s_age\":\"29\"},{\"s_number\":\"163\",\"s_name\":\"jTTNg\",\"s_age\":\"21\"},{\"s_number\":\"164\",\"s_name\":\"CGvlS\",\"s_age\":\"27\"},{\"s_number\":\"165\",\"s_name\":\"wruZL\",\"s_age\":\"29\"},{\"s_number\":\"166\",\"s_name\":\"hNszu\",\"s_age\":\"24\"},{\"s_number\":\"167\",\"s_name\":\"mOgod\",\"s_age\":\"24\"},{\"s_number\":\"168\",\"s_name\":\"lHEam\",\"s_age\":\"16\"},{\"s_number\":\"169\",\"s_name\":\"rckVV\",\"s_age\":\"34\"},{\"s_number\":\"17\",\"s_name\":\"caTtk\",\"s_age\":\"36\"},{\"s_number\":\"170\",\"s_name\":\"spyvK\",\"s_age\":\"18\"},{\"s_number\":\"171\",\"s_name\":\"mvqrM\",\"s_age\":\"31\"},{\"s_number\":\"172\",\"s_name\":\"psSnK\",\"s_age\":\"33\"},{\"s_number\":\"173\",\"s_name\":\"LqtXG\",\"s_age\":\"18\"},{\"s_number\":\"174\",\"s_name\":\"EcymM\",\"s_age\":\"10\"},{\"s_number\":\"175\",\"s_name\":\"VwydT\",\"s_age\":\"15\"},{\"s_number\":\"176\",\"s_name\":\"siOvL\",\"s_age\":\"19\"},{\"s_number\":\"177\",\"s_name\":\"BFAJS\",\"s_age\":\"18\"},{\"s_number\":\"178\",\"s_name\":\"PkErV\",\"s_age\":\"25\"},{\"s_number\":\"179\",\"s_name\":\"UXbos\",\"s_age\":\"38\"},{\"s_number\":\"18\",\"s_name\":\"TILEM\",\"s_age\":\"39\"},{\"s_number\":\"180\",\"s_name\":\"FkimN\",\"s_age\":\"10\"},{\"s_number\":\"181\",\"s_name\":\"NQOwR\",\"s_age\":\"35\"},{\"s_number\":\"182\",\"s_name\":\"PvIev\",\"s_age\":\"35\"},{\"s_number\":\"183\",\"s_name\":\"WhVjg\",\"s_age\":\"13\"},{\"s_number\":\"184\",\"s_name\":\"kIJyo\",\"s_age\":\"39\"},{\"s_number\":\"185\",\"s_name\":\"hMlXc\",\"s_age\":\"23\"},{\"s_number\":\"186\",\"s_name\":\"aKBzV\",\"s_age\":\"13\"},{\"s_number\":\"187\",\"s_name\":\"ROwRU\",\"s_age\":\"38\"},{\"s_number\":\"188\",\"s_name\":\"dAqex\",\"s_age\":\"10\"},{\"s_number\":\"189\",\"s_name\":\"LBBay\",\"s_age\":\"20\"},{\"s_number\":\"19\",\"s_name\":\"MJnkk\",\"s_age\":\"23\"},{\"s_number\":\"190\",\"s_name\":\"tNIco\",\"s_age\":\"18\"},{\"s_number\":\"191\",\"s_name\":\"AMhBk\",\"s_age\":\"23\"},{\"s_number\":\"192\",\"s_name\":\"EFpIw\",\"s_age\":\"15\"},{\"s_number\":\"193\",\"s_name\":\"FAKaU\",\"s_age\":\"24\"},{\"s_number\":\"194\",\"s_name\":\"JbchF\",\"s_age\":\"27\"},{\"s_number\":\"195\",\"s_name\":\"dDJLA\",\"s_age\":\"21\"},{\"s_number\":\"196\",\"s_name\":\"waMGY\",\"s_age\":\"37\"},{\"s_number\":\"197\",\"s_name\":\"LSeUm\",\"s_age\":\"23\"},{\"s_number\":\"198\",\"s_name\":\"FFnxB\",\"s_age\":\"17\"},{\"s_number\":\"199\",\"s_name\":\"JJtQW\",\"s_age\":\"17\"},{\"s_number\":\"2\",\"s_name\":\"xSWdE\",\"s_age\":\"30\"},{\"s_number\":\"20\",\"s_name\":\"EFpIx\",\"s_age\":\"17\"},{\"s_number\":\"200\",\"s_name\":\"yECYm\",\"s_age\":\"17\"},{\"s_number\":\"201\",\"s_name\":\"ARFBR\",\"s_age\":\"28\"},{\"s_number\":\"202\",\"s_name\":\"DapzB\",\"s_age\":\"14\"},{\"s_number\":\"203\",\"s_name\":\"mMWwv\",\"s_age\":\"32\"},{\"s_number\":\"204\",\"s_name\":\"DDhZA\",\"s_age\":\"27\"},{\"s_number\":\"205\",\"s_name\":\"wtFVN\",\"s_age\":\"11\"},{\"s_number\":\"206\",\"s_name\":\"YJAyT\",\"s_age\":\"38\"},{\"s_number\":\"207\",\"s_name\":\"fLnfN\",\"s_age\":\"23\"},{\"s_number\":\"208\",\"s_name\":\"dXANl\",\"s_age\":\"35\"},{\"s_number\":\"209\",\"s_name\":\"HGkhf\",\"s_age\":\"13\"},{\"s_number\":\"21\",\"s_name\":\"VNflQ\",\"s_age\":\"21\"},{\"s_number\":\"210\",\"s_name\":\"phNwS\",\"s_age\":\"39\"},{\"s_number\":\"211\",\"s_name\":\"oBOpj\",\"s_age\":\"10\"},{\"s_number\":\"212\",\"s_name\":\"yrosV\",\"s_age\":\"26\"},{\"s_number\":\"213\",\"s_name\":\"cEPjD\",\"s_age\":\"18\"},{\"s_number\":\"214\",\"s_name\":\"MQSRE\",\"s_age\":\"23\"},{\"s_number\":\"215\",\"s_name\":\"AinSy\",\"s_age\":\"34\"},{\"s_number\":\"216\",\"s_name\":\"LgvMx\",\"s_age\":\"39\"},{\"s_number\":\"217\",\"s_name\":\"IpzBm\",\"s_age\":\"26\"},{\"s_number\":\"218\",\"s_name\":\"fRSNn\",\"s_age\":\"10\"},{\"s_number\":\"219\",\"s_name\":\"qCPrp\",\"s_age\":\"22\"},{\"s_number\":\"22\",\"s_name\":\"AvBwB\",\"s_age\":\"21\"},{\"s_number\":\"220\",\"s_name\":\"pjaCI\",\"s_age\":\"29\"},{\"s_number\":\"221\",\"s_name\":\"qEZlf\",\"s_age\":\"37\"},{\"s_number\":\"222\",\"s_name\":\"ihmMb\",\"s_age\":\"36\"},{\"s_number\":\"223\",\"s_name\":\"oOedf\",\"s_age\":\"20\"},{\"s_number\":\"224\",\"s_name\":\"sPWmt\",\"s_age\":\"16\"},{\"s_number\":\"225\",\"s_name\":\"SKYKE\",\"s_age\":\"35\"},{\"s_number\":\"226\",\"s_name\":\"AZubX\",\"s_age\":\"30\"},{\"s_number\":\"227\",\"s_name\":\"BEtdn\",\"s_age\":\"11\"},{\"s_number\":\"228\",\"s_name\":\"zqeAq\",\"s_age\":\"10\"},{\"s_number\":\"229\",\"s_name\":\"jTNkK\",\"s_age\":\"37\"},{\"s_number\":\"23\",\"s_name\":\"pqMJn\",\"s_age\":\"17\"},{\"s_number\":\"230\",\"s_name\":\"xDxBp\",\"s_age\":\"36\"},{\"s_number\":\"231\",\"s_name\":\"CfVjk\",\"s_age\":\"23\"},{\"s_number\":\"232\",\"s_name\":\"Kgzge\",\"s_age\":\"13\"},{\"s_number\":\"233\",\"s_name\":\"tykFx\",\"s_age\":\"23\"},{\"s_number\":\"234\",\"s_name\":\"QOxVk\",\"s_age\":\"17\"},{\"s_number\":\"235\",\"s_name\":\"MTijx\",\"s_age\":\"29\"},{\"s_number\":\"236\",\"s_name\":\"YVGtc\",\"s_age\":\"11\"},{\"s_number\":\"237\",\"s_name\":\"gzfbP\",\"s_age\":\"37\"},{\"s_number\":\"238\",\"s_name\":\"gXrSo\",\"s_age\":\"35\"},{\"s_number\":\"239\",\"s_name\":\"qfDBW\",\"s_age\":\"13\"},{\"s_number\":\"24\",\"s_name\":\"AMhDu\",\"s_age\":\"16\"},{\"s_number\":\"240\",\"s_name\":\"NxXvj\",\"s_age\":\"30\"},{\"s_number\":\"241\",\"s_name\":\"Trcni\",\"s_age\":\"10\"},{\"s_number\":\"242\",\"s_name\":\"DRyRO\",\"s_age\":\"20\"},{\"s_number\":\"243\",\"s_name\":\"uXEfL\",\"s_age\":\"18\"},{\"s_number\":\"244\",\"s_name\":\"oAOpi\",\"s_age\":\"38\"},{\"s_number\":\"245\",\"s_name\":\"nxzdV\",\"s_age\":\"19\"},{\"s_number\":\"246\",\"s_name\":\"Pfegt\",\"s_age\":\"25\"},{\"s_number\":\"247\",\"s_name\":\"xLlXg\",\"s_age\":\"32\"},{\"s_number\":\"248\",\"s_name\":\"sDMaQ\",\"s_age\":\"11\"},{\"s_number\":\"249\",\"s_name\":\"ThglI\",\"s_age\":\"31\"},{\"s_number\":\"25\",\"s_name\":\"YkeOK\",\"s_age\":\"13\"},{\"s_number\":\"250\",\"s_name\":\"FSyNw\",\"s_age\":\"35\"},{\"s_number\":\"251\",\"s_name\":\"bABdN\",\"s_age\":\"27\"},{\"s_number\":\"252\",\"s_name\":\"LONwT\",\"s_age\":\"11\"},{\"s_number\":\"253\",\"s_name\":\"KrDSC\",\"s_age\":\"16\"},{\"s_number\":\"254\",\"s_name\":\"rfBqZ\",\"s_age\":\"39\"},{\"s_number\":\"255\",\"s_name\":\"VHyuE\",\"s_age\":\"32\"},{\"s_number\":\"256\",\"s_name\":\"cWBTN\",\"s_age\":\"16\"},{\"s_number\":\"257\",\"s_name\":\"NgtBy\",\"s_age\":\"33\"},{\"s_number\":\"258\",\"s_name\":\"DyEEh\",\"s_age\":\"38\"},{\"s_number\":\"259\",\"s_name\":\"nzJWI\",\"s_age\":\"23\"},{\"s_number\":\"26\",\"s_name\":\"zeXBQ\",\"s_age\":\"23\"},{\"s_number\":\"260\",\"s_name\":\"suSil\",\"s_age\":\"27\"},{\"s_number\":\"261\",\"s_name\":\"mzLft\",\"s_age\":\"28\"},{\"s_number\":\"262\",\"s_name\":\"SzSQD\",\"s_age\":\"19\"},{\"s_number\":\"263\",\"s_name\":\"XPgkH\",\"s_age\":\"28\"},{\"s_number\":\"264\",\"s_name\":\"jbFVP\",\"s_age\":\"17\"},{\"s_number\":\"265\",\"s_name\":\"UKRdP\",\"s_age\":\"35\"},{\"s_number\":\"266\",\"s_name\":\"PwOEG\",\"s_age\":\"20\"},{\"s_number\":\"267\",\"s_name\":\"SnJIo\",\"s_age\":\"20\"},{\"s_number\":\"268\",\"s_name\":\"aWHwn\",\"s_age\":\"39\"},{\"s_number\":\"269\",\"s_name\":\"iTREv\",\"s_age\":\"19\"},{\"s_number\":\"27\",\"s_name\":\"NBqZZ\",\"s_age\":\"38\"},{\"s_number\":\"270\",\"s_name\":\"uYHqI\",\"s_age\":\"22\"},{\"s_number\":\"271\",\"s_name\":\"ZMKqA\",\"s_age\":\"27\"},{\"s_number\":\"272\",\"s_name\":\"wuJmf\",\"s_age\":\"34\"},{\"s_number\":\"273\",\"s_name\":\"NuHcp\",\"s_age\":\"21\"},{\"s_number\":\"274\",\"s_name\":\"cdiFD\",\"s_age\":\"10\"},{\"s_number\":\"275\",\"s_name\":\"sLBBZ\",\"s_age\":\"22\"},{\"s_number\":\"276\",\"s_name\":\"gqotd\",\"s_age\":\"15\"},{\"s_number\":\"277\",\"s_name\":\"PtByP\",\"s_age\":\"27\"},{\"s_number\":\"278\",\"s_name\":\"DdEPi\",\"s_age\":\"23\"},{\"s_number\":\"279\",\"s_name\":\"KkWcy\",\"s_age\":\"15\"},{\"s_number\":\"28\",\"s_name\":\"TyMqs\",\"s_age\":\"33\"},{\"s_number\":\"280\",\"s_name\":\"CikAV\",\"s_age\":\"12\"},{\"s_number\":\"281\",\"s_name\":\"HZckV\",\"s_age\":\"37\"},{\"s_number\":\"282\",\"s_name\":\"VOhsu\",\"s_age\":\"37\"},{\"s_number\":\"283\",\"s_name\":\"vokgd\",\"s_age\":\"38\"},{\"s_number\":\"284\",\"s_name\":\"FfNzj\",\"s_age\":\"23\"},{\"s_number\":\"285\",\"s_name\":\"GQkDm\",\"s_age\":\"23\"},{\"s_number\":\"286\",\"s_name\":\"EFlqV\",\"s_age\":\"28\"},{\"s_number\":\"287\",\"s_name\":\"oCWcv\",\"s_age\":\"38\"},{\"s_number\":\"288\",\"s_name\":\"CQzZx\",\"s_age\":\"19\"},{\"s_number\":\"289\",\"s_name\":\"hRNpk\",\"s_age\":\"14\"},{\"s_number\":\"29\",\"s_name\":\"Vlrcm\",\"s_age\":\"10\"},{\"s_number\":\"290\",\"s_name\":\"fegsx\",\"s_age\":\"13\"},{\"s_number\":\"291\",\"s_name\":\"qiTVX\",\"s_age\":\"39\"},{\"s_number\":\"292\",\"s_name\":\"hKepp\",\"s_age\":\"25\"},{\"s_number\":\"293\",\"s_name\":\"OpgMp\",\"s_age\":\"19\"},{\"s_number\":\"294\",\"s_name\":\"LGWRu\",\"s_age\":\"22\"},{\"s_number\":\"295\",\"s_name\":\"SdOKh\",\"s_age\":\"28\"},{\"s_number\":\"296\",\"s_name\":\"HvhwN\",\"s_age\":\"24\"},{\"s_number\":\"297\",\"s_name\":\"iqfDC\",\"s_age\":\"11\"},{\"s_number\":\"298\",\"s_name\":\"GYYWN\",\"s_age\":\"10\"},{\"s_number\":\"299\",\"s_name\":\"Sjsle\",\"s_age\":\"31\"},{\"s_number\":\"3\",\"s_name\":\"Iotbb\",\"s_age\":\"10\"},{\"s_number\":\"30\",\"s_name\":\"wdbZQ\",\"s_age\":\"14\"},{\"s_number\":\"300\",\"s_name\":\"omvry\",\"s_age\":\"19\"},{\"s_number\":\"301\",\"s_name\":\"hRKce\",\"s_age\":\"18\"},{\"s_number\":\"31\",\"s_name\":\"rmgXt\",\"s_age\":\"10\"},{\"s_number\":\"32\",\"s_name\":\"XLNHW\",\"s_age\":\"32\"},{\"s_number\":\"33\",\"s_name\":\"YCSGC\",\"s_age\":\"36\"},{\"s_number\":\"34\",\"s_name\":\"NhyVi\",\"s_age\":\"11\"},{\"s_number\":\"35\",\"s_name\":\"QWlnI\",\"s_age\":\"24\"},{\"s_number\":\"36\",\"s_name\":\"zZADq\",\"s_age\":\"35\"},{\"s_number\":\"37\",\"s_name\":\"oSvAs\",\"s_age\":\"15\"},{\"s_number\":\"38\",\"s_name\":\"UTKVy\",\"s_age\":\"28\"},{\"s_number\":\"39\",\"s_name\":\"JEQsp\",\"s_age\":\"22\"},{\"s_number\":\"4\",\"s_name\":\"eruaQ\",\"s_age\":\"12\"},{\"s_number\":\"40\",\"s_name\":\"kMhAd\",\"s_age\":\"33\"},{\"s_number\":\"41\",\"s_name\":\"NuMAr\",\"s_age\":\"15\"},{\"s_number\":\"42\",\"s_name\":\"SNkLZ\",\"s_age\":\"34\"},{\"s_number\":\"43\",\"s_name\":\"eZLEN\",\"s_age\":\"12\"},{\"s_number\":\"44\",\"s_name\":\"iCnGs\",\"s_age\":\"38\"},{\"s_number\":\"45\",\"s_name\":\"GrNQP\",\"s_age\":\"27\"},{\"s_number\":\"46\",\"s_name\":\"xAmJI\",\"s_age\":\"17\"},{\"s_number\":\"47\",\"s_name\":\"pNUig\",\"s_age\":\"15\"},{\"s_number\":\"48\",\"s_name\":\"yRQAf\",\"s_age\":\"39\"},{\"s_number\":\"49\",\"s_name\":\"EZmjm\",\"s_age\":\"29\"},{\"s_number\":\"5\",\"s_name\":\"aORRI\",\"s_age\":\"35\"},{\"s_number\":\"50\",\"s_name\":\"zAdSb\",\"s_age\":\"28\"},{\"s_number\":\"51\",\"s_name\":\"ZgHRl\",\"s_age\":\"29\"},{\"s_number\":\"52\",\"s_name\":\"zEuoj\",\"s_age\":\"12\"},{\"s_number\":\"53\",\"s_name\":\"Uiisn\",\"s_age\":\"16\"},{\"s_number\":\"54\",\"s_name\":\"oOcTm\",\"s_age\":\"26\"},{\"s_number\":\"55\",\"s_name\":\"eOGOa\",\"s_age\":\"32\"},{\"s_number\":\"56\",\"s_name\":\"HagFI\",\"s_age\":\"24\"},{\"s_number\":\"57\",\"s_name\":\"xQJXN\",\"s_age\":\"38\"},{\"s_number\":\"58\",\"s_name\":\"xxTZt\",\"s_age\":\"37\"},{\"s_number\":\"59\",\"s_name\":\"tkREy\",\"s_age\":\"26\"},{\"s_number\":\"6\",\"s_name\":\"oQoWR\",\"s_age\":\"21\"},{\"s_number\":\"60\",\"s_name\":\"tkUUM\",\"s_age\":\"10\"},{\"s_number\":\"61\",\"s_name\":\"PbMFS\",\"s_age\":\"21\"},{\"s_number\":\"62\",\"s_name\":\"vWtdm\",\"s_age\":\"38\"},{\"s_number\":\"63\",\"s_name\":\"dxdaR\",\"s_age\":\"15\"},{\"s_number\":\"64\",\"s_name\":\"uuPRN\",\"s_age\":\"19\"},{\"s_number\":\"65\",\"s_name\":\"sNOEG\",\"s_age\":\"21\"},{\"s_number\":\"66\",\"s_name\":\"baVEj\",\"s_age\":\"14\"},{\"s_number\":\"67\",\"s_name\":\"hsoue\",\"s_age\":\"18\"},{\"s_number\":\"68\",\"s_name\":\"fMsES\",\"s_age\":\"26\"},{\"s_number\":\"69\",\"s_name\":\"ijxIY\",\"s_age\":\"36\"},{\"s_number\":\"7\",\"s_name\":\"xcTlA\",\"s_age\":\"35\"},{\"s_number\":\"70\",\"s_name\":\"BWeIb\",\"s_age\":\"15\"},{\"s_number\":\"71\",\"s_name\":\"OuIex\",\"s_age\":\"39\"},{\"s_number\":\"72\",\"s_name\":\"BNjHH\",\"s_age\":\"18\"},{\"s_number\":\"73\",\"s_name\":\"CREvp\",\"s_age\":\"17\"},{\"s_number\":\"74\",\"s_name\":\"wqsNO\",\"s_age\":\"28\"},{\"s_number\":\"75\",\"s_name\":\"MSdOJ\",\"s_age\":\"11\"},{\"s_number\":\"76\",\"s_name\":\"masNN\",\"s_age\":\"23\"},{\"s_number\":\"77\",\"s_name\":\"dUrYS\",\"s_age\":\"20\"},{\"s_number\":\"78\",\"s_name\":\"jVXcu\",\"s_age\":\"35\"},{\"s_number\":\"79\",\"s_name\":\"cJqyx\",\"s_age\":\"34\"},{\"s_number\":\"8\",\"s_name\":\"MiGFh\",\"s_age\":\"37\"},{\"s_number\":\"80\",\"s_name\":\"OtEOf\",\"s_age\":\"16\"},{\"s_number\":\"81\",\"s_name\":\"RzWjf\",\"s_age\":\"38\"},{\"s_number\":\"82\",\"s_name\":\"zGFhV\",\"s_age\":\"13\"},{\"s_number\":\"83\",\"s_name\":\"VknJH\",\"s_age\":\"15\"},{\"s_number\":\"84\",\"s_name\":\"VcBBa\",\"s_age\":\"23\"},{\"s_number\":\"85\",\"s_name\":\"lNjEu\",\"s_age\":\"15\"},{\"s_number\":\"86\",\"s_name\":\"MhBku\",\"s_age\":\"22\"},{\"s_number\":\"87\",\"s_name\":\"QUbwf\",\"s_age\":\"16\"},{\"s_number\":\"88\",\"s_name\":\"MelRA\",\"s_age\":\"11\"},{\"s_number\":\"89\",\"s_name\":\"MAqcp\",\"s_age\":\"20\"},{\"s_number\":\"9\",\"s_name\":\"hZABi\",\"s_age\":\"17\"},{\"s_number\":\"90\",\"s_name\":\"QdSeU\",\"s_age\":\"15\"},{\"s_number\":\"91\",\"s_name\":\"jtsHj\",\"s_age\":\"39\"},{\"s_number\":\"92\",\"s_name\":\"tTnKK\",\"s_age\":\"22\"},{\"s_number\":\"93\",\"s_name\":\"VrWHy\",\"s_age\":\"21\"},{\"s_number\":\"94\",\"s_name\":\"ynTEo\",\"s_age\":\"29\"},{\"s_number\":\"95\",\"s_name\":\"zxNwW\",\"s_age\":\"20\"},{\"s_number\":\"96\",\"s_name\":\"aWJEU\",\"s_age\":\"31\"},{\"s_number\":\"97\",\"s_name\":\"SgcWx\",\"s_age\":\"25\"},{\"s_number\":\"98\",\"s_name\":\"mIFZf\",\"s_age\":\"28\"},{\"s_number\":\"99\",\"s_name\":\"LOLol\",\"s_age\":\"17\"}]";
        JSONArray jsonArray = JSONArray.fromObject(receive_message);
        JSONArray colName = jsonArray.getJSONArray(1);//列名JSONArray
        Log.d(TAG, "init: "+colName.toString());
        int colNameLength = colName.size();
        int width = this.getWindowManager().getDefaultDisplay().getWidth()/colName.size();
        //清空list中的数据
        list.clear();
        //定义标题
        TabelAdapter.TabelCell[] cellColName = new TabelAdapter.TabelCell[colNameLength];
        for (int i = 0; i < colNameLength; i++) {
            cellColName[i] = new TabelAdapter.TabelCell(colName.getString(i),
                    width+8*i, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        //把标题加入List
        list.add(new TabelAdapter.TabelRow(cellColName));
        //定义行数据
        for (int i = 2; i < jsonArray.size(); i++) {//从2开始取对象
            JSONObject rowObject = jsonArray.getJSONObject(i);//行对象
            TabelAdapter.TabelCell[] cellRowObject = new TabelAdapter.TabelCell[colNameLength];//一行中单元格的个数;
            for (int j = 0; j < colNameLength; j++) {
                cellRowObject[j] =
                        new TabelAdapter.TabelCell(
                                rowObject.getString(colName.getString(j)),width+8*j,LinearLayout.LayoutParams.MATCH_PARENT);//每一个单元格
            }
            list.add(new TabelAdapter.TabelRow(cellRowObject));
        }
        bottomLayout.setVisibility(View.VISIBLE);
        pageNumber = (list.size()/MAX_ITEM_COUNT)+1;
        indexPage.setHint(1+"/"+pageNumber);
    }

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
