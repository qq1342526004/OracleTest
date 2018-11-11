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
import android.widget.TextView;

import com.hasee.oracletest.R;

public class SelectItemDialog extends DialogFragment {

    private String[] colNames = new String[]{};
    private String[] tv_conditions = new String[]{"=","!=","<>",">","<",">=","<=","!<","!>","BETWEEN","EXISTS","IN","NOT IN","LIKE","GLOB","NOT"};
    private String[] button_conditions = new String[]{"AND","OR"};

    private Context mContext;
    private TextView selectItemDialogTitle;
    private ListView selectItemDialogListview;
    private String[] strings ;
    private ArrayAdapter<String> adapter;

    private String colName;
    private String tv_condition;
    private String button_condition;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_item_dialog,container,false);
        selectItemDialogListview = (ListView)view.findViewById(R.id.select_item_dialog_listview);
        selectItemDialogTitle = (TextView)view.findViewById(R.id.select_item_dialog_title);
        init(savedInstanceState);
        adapter = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_1,strings);
        selectItemDialogListview.setAdapter(adapter);
        selectItemDialogListview.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private void init(Bundle savedInstanceState) {
        savedInstanceState = this.getArguments();
        String title = savedInstanceState.getString("title");
        selectItemDialogTitle.setText(title);
        if("选择列名".equals(title)){
            strings = colNames;
        }else if("选择运算符".equals(title)){
            strings = tv_conditions;
        }else if("选择".equals(title)){
            strings = button_conditions;
        }
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };
}
