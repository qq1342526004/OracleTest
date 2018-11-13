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
import com.hasee.onlinedb.adapter.UpdateMsgAdapter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateFragment extends DialogFragment {
    private static final String TAG = "UpdateFragment";
    private ListView updateListView;
    private TextView updateDialogSubmitButton,updateDialogBackButton;
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
        updateDialogSubmitButton = (TextView) view.findViewById(R.id.updateDialog_submit_tv);
        updateDialogSubmitButton.setOnClickListener(onClickListener);
        updateDialogBackButton = (TextView) view.findViewById(R.id.updateDialog_back_tv);
        updateDialogBackButton.setOnClickListener(onClickListener);
        savedInstanceState = this.getArguments();//一行中列名和对应数据
        stringList = (List<List<String>>) savedInstanceState.getSerializable("msg_item");
        adapter = new UpdateMsgAdapter(getContext(), stringList);
        updateListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(-1,-2 );
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONArray jsonArray = new JSONArray();
            switch (view.getId()) {
                case R.id.updateDialog_submit_tv:
                    if (hashMap.size() != 0) {
                        jsonArray.add("6");
                        StringBuffer sql = new StringBuffer();
                        sql.append("update ");
                        sql.append(MainActivity.currentTabelName);
                        sql.append(" set ");
                        int count = hashMap.size();
                        for (int i = 0; i < stringList.size(); i++) {
                            if (hashMap.containsKey(i)) {
                                if ("INT".equalsIgnoreCase(stringList.get(i).get(0))||"DOUBLE".equalsIgnoreCase(stringList.get(i).get(0))||"FLOAT".equalsIgnoreCase(stringList.get(i).get(0))) {
                                    sql.append(stringList.get(i).get(1) + "=" + hashMap.get(i));
                                } else {
                                    sql.append(stringList.get(i).get(1) + "=" + "'" + hashMap.get(i) + "'");
                                }
                                if (count > 1) {
                                    sql.append(",");
                                    count--;
                                }
                            }
                        }
                        sql.append(" where ");
                        for (int j = 0; j < stringList.size(); j++) {//列数
                            if("INT".equalsIgnoreCase(stringList.get(j).get(0))||"DOUBLE".equalsIgnoreCase(stringList.get(j).get(0))||"FLOAT".equalsIgnoreCase(stringList.get(j).get(0))){//列
                                sql.append(stringList.get(j).get(1)+"="+stringList.get(j).get(2));
                            }else{
                                sql.append(stringList.get(j).get(1)+"="+"'"+stringList.get(j).get(2)+"'");
                            }
                            if(j < (stringList.size()-1)){//j<列数-1
                                sql.append(" and ");
                            }
                        }
                        jsonArray.add(sql.toString());
                        jsonArray.add(JSONObject.fromObject(App.getInstance().getPreferences()));
                        listener.sendMessageToServer(jsonArray.toString());
                    }
                    break;
                case R.id.updateDialog_back_tv:
                    jsonArray.add("100");
                    listener.sendMessage(jsonArray);
                    break;
            }
        }
    };

}
