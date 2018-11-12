package com.hasee.oracletest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hasee.oracletest.MyListener;
import com.hasee.oracletest.R;
import com.hasee.oracletest.adapter.UpdateMsgAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateFragment extends DialogFragment {
    private static final String TAG = "UpdateFragment";

    private ListView updateListView;
    private Button updateDialogSubmitButton;
    private List<List<String>> stringList = new ArrayList<>();
    private UpdateMsgAdapter adapter;
    private MyListener listener;
    public static HashMap hashMap = new HashMap();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MyListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_dialog, container, false);
        updateListView = (ListView) view.findViewById(R.id.update_listView);
        updateDialogSubmitButton = (Button) view.findViewById(R.id.updateDialog_submit_button);
        updateDialogSubmitButton.setOnClickListener(onClickListener);
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("msg_item");
        adapter = new UpdateMsgAdapter(getContext(), stringList);
        updateListView.setAdapter(adapter);
        return view;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.updateDialog_submit_button:
                    if (hashMap.size() != 0) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add("6");
                        StringBuffer sql = new StringBuffer();
                        sql.append("update ");
                        sql.append("student");
                        sql.append(" set ");
                        for (int i = 0; i < stringList.size(); i++) {
                            if (hashMap.containsKey(i)) {
                                if ("INT".equals(stringList.get(i).get(0))) {
                                    sql.append(stringList.get(i).get(1) + "=" + hashMap.get(i));
                                } else {
                                    sql.append(stringList.get(i).get(1) + "=" + "'" + hashMap.get(i) + "'");
                                }
                                if (i < hashMap.size() - 1) {
                                    sql.append(",");
                                }
                            }
                        }
                        sql.append(" where ");
                        for (int j = 0; j < stringList.size(); j++) {
                            if ("INT".equals(stringList.get(j).get(0))) {//列
                                sql.append(stringList.get(j).get(1) + "=" + stringList.get(j).get(2));
                            } else {
                                sql.append(stringList.get(j).get(1) + "=" + "'" + stringList.get(j).get(2) + "'");
                            }
                            if (j != (stringList.get(j).size() - 1)) {
                                sql.append(",");
                            }
                        }
                        jsonArray.add(sql.toString());
                        Log.d(TAG, "onClick: " + jsonArray.toString());
                    }
                    break;
            }
        }
    };

}
