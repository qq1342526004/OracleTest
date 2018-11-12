package com.hasee.oracletest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hasee.oracletest.MainActivity;
import com.hasee.oracletest.MyListener;
import com.hasee.oracletest.R;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectTabelNameDialog extends DialogFragment {

    private ListView selectTabelNameListView;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    private Context mContext;
    private MyListener listener;
    private JSONArray jsonArray;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        listener = (MyListener)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_tabelname_dialog,container,false);
        init(savedInstanceState);
        selectTabelNameListView = (ListView)view.findViewById(R.id.select_tabelName_listView);
        adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,list);
        selectTabelNameListView.setAdapter(adapter);
        selectTabelNameListView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private void init(Bundle savedInstanceState) {
        savedInstanceState = this.getArguments();
        jsonArray = new JSONArray();
        jsonArray = (JSONArray) savedInstanceState.getSerializable("currentTabelName");
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.getString(i));
        }
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MainActivity.currentTabelName = list.get(i);
            jsonArray = new JSONArray();
            jsonArray.add("3");
            StringBuffer sql = new StringBuffer();
            sql.append("select * from ");
            sql.append(MainActivity.currentTabelName);
            jsonArray.add(sql.toString());
            jsonArray.add(list.get(i));
//            MainActivity.indexNumber = 0;
            listener.sendMessageToServer(jsonArray.toString());
        }
    };
}
