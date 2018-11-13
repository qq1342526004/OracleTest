package com.hasee.onlinedb.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hasee.onlinedb.App;
import com.hasee.onlinedb.MainActivity;
import com.hasee.onlinedb.MyListener;
import com.hasee.onlinedb.R;
import com.hasee.onlinedb.adapter.AddMsgAdapter;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddDialog extends DialogFragment {

    private ListView addListView;
    private TextView addDialogSubmitButton,addDialogBackButton;
    private List<List<String>> stringList = new ArrayList<>();
    private AddMsgAdapter adapter;
    private MyListener listener;
    public static String[] strings;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dialog,container,false);
        addListView = (ListView) view.findViewById(R.id.add_listView);
        addDialogSubmitButton = (TextView) view.findViewById(R.id.addDialog_submit_tv);
        addDialogSubmitButton.setOnClickListener(onClickListener);
        addDialogBackButton = (TextView) view.findViewById(R.id.addDialog_back_tv);
        addDialogBackButton.setOnClickListener(onClickListener);
        init(savedInstanceState);
        adapter = new AddMsgAdapter(getContext(), stringList);
        addListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1,-2 );
    }

    private void init(Bundle savedInstanceState) {
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("add_item");
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONArray jsonArray = new JSONArray();
            switch (view.getId()) {
                case R.id.addDialog_submit_tv:
                    jsonArray.add("4");
                    StringBuffer sql = new StringBuffer();
                    sql.append("insert into ");
                    sql.append(MainActivity.currentTabelName);
                    sql.append(" values");
                    sql.append("(");
                    for (int i = 0; i < strings.length; i++) {
                        if("INT".equalsIgnoreCase(strings[i])||"DOUBLE".equalsIgnoreCase(strings[i])||"FLOAT".equalsIgnoreCase(strings[i])){
                            sql.append(strings[i]);
                        }else{
                            sql.append("'"+strings[i]+"'");
                        }
                        if(i < strings.length-1){
                            sql.append(",");
                        }
                    }
                    sql.append(")");
                    jsonArray.add(sql.toString());
                    jsonArray.add(JSONObject.fromObject(App.getInstance().getPreferences()));
                    listener.sendMessageToServer(jsonArray.toString());
                    break;
                case R.id.addDialog_back_tv:
                    jsonArray.add("100");
                    listener.sendMessage(jsonArray);
                    break;
            }
        }
    };
}
