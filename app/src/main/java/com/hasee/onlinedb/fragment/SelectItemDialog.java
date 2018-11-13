package com.hasee.onlinedb.fragment;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hasee.onlinedb.MainActivity;
import com.hasee.onlinedb.MyListener;
import com.hasee.onlinedb.R;
import com.hasee.onlinedb.SelectActivity;

public class SelectItemDialog extends DialogFragment {
    private static final String TAG = "SelectItemDialog";
    private String[] colNames = new String[]{};
    private String[] tv_conditions = new String[]{"=","!=","<>",">","<",">=","<=","!<","!>","BETWEEN","EXISTS","IN","NOT IN","LIKE","GLOB","NOT"};
    private String[] button_conditions = new String[]{"AND","OR"};

    private Context mContext;
    private TextView selectItemDialogTitle;
    private ListView selectItemDialogListview;
    private String[] strings ;
    private ArrayAdapter<String> adapter;
    private MyListener listener;
    private int currentItem;//目前选择的哪个方框
    private int currentPosition;

    private String colName;
    private String tv_condition;
    private String button_condition;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        listener = (MyListener)getActivity();
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
        currentPosition = savedInstanceState.getInt("position");
        selectItemDialogTitle.setText(title);
        if("选择列名".equals(title)){
            colNames = MainActivity.colNameString;
            strings = colNames;
            currentItem = 0;
        }else if("选择运算符".equals(title)){
            strings = tv_conditions;
            currentItem = 1;
        }else if("选择".equals(title)){
            strings = button_conditions;
            currentItem = 2;
        }
    }

    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (currentItem){
                case 0://当前是列名
                    colName = colNames[i];
                    SelectActivity.list.get(currentPosition)[0] = colName;
                    break;
                case 1://当前是条件
                    tv_condition = tv_conditions[i];
                    SelectActivity.list.get(currentPosition)[1] = tv_condition;
                    break;
                case 2://当前是button条件
                    button_condition = button_conditions[i];
                    SelectActivity.list.get(currentPosition)[3] = button_condition;
                    break;
            }
            Log.d(TAG, "onItemClick: "+currentPosition);
            listener.sendMessageToServer(String.valueOf(currentPosition));
        }
    };
}
