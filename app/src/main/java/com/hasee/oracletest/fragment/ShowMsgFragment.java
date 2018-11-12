package com.hasee.oracletest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hasee.oracletest.R;
import com.hasee.oracletest.adapter.ShowMsgAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowMsgFragment extends DialogFragment {
    private ListView showMsgListView;
    private List<List<String>> stringList = new ArrayList<>();
    private ShowMsgAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.showmsg_dialog,container,false);
        showMsgListView = (ListView)view.findViewById(R.id.showMsg_listView);
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("msg_item");
        adapter = new ShowMsgAdapter(getContext(),stringList);
        showMsgListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1,-2 );
    }
}
