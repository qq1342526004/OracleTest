package com.hasee.oracletest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.hasee.oracletest.R;
import com.hasee.oracletest.adapter.UpdateMsgAdapter;

import java.util.ArrayList;
import java.util.List;

public class UpdateFragment extends DialogFragment {
    private ListView updateListView;
    private Button updateDialogSubmitButton;
    private List<List<String>> stringList = new ArrayList<>();
    private UpdateMsgAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_dialog,container,false);
        updateListView = (ListView)view.findViewById(R.id.update_listView);
        updateDialogSubmitButton = (Button)view.findViewById(R.id.updateDialog_submit_button);
        updateDialogSubmitButton.setOnClickListener(onClickListener);
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("msg_item");
        adapter = new UpdateMsgAdapter(getContext(),stringList);
        updateListView.setAdapter(adapter);
        return view;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
