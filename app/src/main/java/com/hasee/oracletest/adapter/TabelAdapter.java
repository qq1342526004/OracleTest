package com.hasee.oracletest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hasee.oracletest.MainActivity;

import java.util.List;

import static com.hasee.oracletest.MainActivity.MAX_ITEM_COUNT;
import static com.hasee.oracletest.MainActivity.indexNumber;

public class TabelAdapter extends BaseAdapter {
    private Context mContext;
    private List<TabelRow> tabelRows;

    public TabelAdapter(Context context,List<TabelRow> tabelRows){
        this.mContext = context;
        this.tabelRows = tabelRows;
    }

    @Override
    public int getCount() {

        int preview_count = MAX_ITEM_COUNT*MainActivity.indexNumber;//前几页总共的数据量
        if(tabelRows.size()-preview_count<MAX_ITEM_COUNT){//总数-之前数=剩余数<10
            return tabelRows.size()-preview_count;//显示剩余数
        }else{
            return MAX_ITEM_COUNT;//返回一页最大数
        }

    }

    @Override
    public Object getItem(int i) {
        return tabelRows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TabelRow tabelRow = tabelRows.get(i+indexNumber*MAX_ITEM_COUNT);
        TabelRowView tabelRowView = new TabelRowView(mContext,tabelRow);
        return tabelRowView;
    }



    /*
     * 实现表格行的样式
     * */
    class TabelRowView extends LinearLayout {

        public TabelRowView(Context context,TabelRow tabelRow) {
            super(context);
            this.setOrientation(LinearLayout.HORIZONTAL);//单元格的布局方向
            for (int i = 0; i < tabelRow.getSize(); i++) {//逐个将单元格添加到行
                TabelCell cell = tabelRow.getCellValue(i);
                LinearLayout.LayoutParams layoutParams = new LayoutParams(cell.width,cell.height);//按照单元格指定大小设置空间
                layoutParams.setMargins(0,0,1,1);//预留空隙制造边框
                //设置单元格中的数据以及排版
                TextView cellTextView = new TextView(context);
                cellTextView.setLines(1);
                cellTextView.setGravity(Gravity.CENTER);
                cellTextView.setText(cell.value);
//                cellTextView.setBackgroundColor(Color.BLACK);
                addView(cellTextView,layoutParams);
            }
//            this.setBackgroundColor(Color.WHITE);//背景白色，利用空隙来实现边框
        }
    }

    /*
     * 实现表格的行单元
     * */
    public static class TabelRow {
        private TabelCell[] cells;
        public TabelRow(TabelCell[] cells){
            this.cells = cells;
        }

        public int getSize(){//获取一行中单元格的个数
            return cells.length;
        }

        public TabelCell getCellValue(int index){//返回一行中某个具体单元格

            if(index>=cells.length){
                return null;
            }
            return cells[index];
        }

    }


    /*
     * 实现表格的格单元
     * */
    public static class TabelCell {
        private String value;//单元格中的值
        private int width;//单元格的宽度
        private int height;//单元格的高度

        public TabelCell(String value, int width, int height) {
            this.value = value;
            this.width = width;
            this.height = height;
        }

    }
}
