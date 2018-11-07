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

import com.hasee.oracletest.MyListener;
import com.hasee.oracletest.R;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class HandleDialog extends DialogFragment {
    private ListView handleListView;
    private ArrayAdapter<String> adapter;
    private List<String> lists = new ArrayList<>();
    private MyListener listener;
    private JSONArray handle_message_json = new JSONArray();
    private List<List<String>> stringList;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyListener)getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.handle_dialog,container,false);
        savedInstanceState = this.getArguments();
        stringList = (List<List<String>>) savedInstanceState.getSerializable("showmsg_item");
        handleListView = (ListView)view.findViewById(R.id.handle_listView);
        initList();
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,lists);
        handleListView.setAdapter(adapter);
        handleListView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private void initList() {
        lists.add("查看数据");
        lists.add("修改数据");
        lists.add("删除数据");
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0:
                    handle_message_json.add("0");
                    handle_message_json.add(stringList);
                    listener.sendMessage(handle_message_json);
                    break;
                case 1:
                    handle_message_json.add("1");
                    handle_message_json.add(stringList);
                    listener.sendMessage(handle_message_json);
                    break;
                case 2:
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add("5");//或者使用主码
//                    for (int j = 0; j < stringList.size(); j++) {
//                        jsonArray.add(stringList.get(i).get(0)+"="+"");
//                    }
                    listener.sendMessageToServer(jsonArray.toString());
                    break;
            }
        }
    };
}
