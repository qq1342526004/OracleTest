package com.hasee.oracletest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.hasee.oracletest.MainActivity;
import com.hasee.oracletest.MyListener;
import com.hasee.oracletest.R;
import com.hasee.oracletest.adapter.AddMsgAdapter;


import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class AddDialog extends DialogFragment {

    private ListView addListView;
    private Button addDialogSubmitButton;
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
        addDialogSubmitButton = (Button) view.findViewById(R.id.addDialog_submit_button);
        addDialogSubmitButton.setOnClickListener(onClickListener);
        init(savedInstanceState);
        adapter = new AddMsgAdapter(getContext(), stringList);
        addListView.setAdapter(adapter);
        return view;
    }

    private void init(Bundle savedInstanceState) {
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("add_item");
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.addDialog_submit_button:
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add("4");
                    StringBuffer sql = new StringBuffer();
                    sql.append("insert into ");
                    sql.append(MainActivity.currentTabelName);
                    sql.append(" values");
                    sql.append("(");
                    for (int i = 0; i < strings.length; i++) {
                        if("INT".equals(strings[i])){
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
                    listener.sendMessageToServer(jsonArray.toString());
                    break;
            }
        }
    };
}
