package com.iblogstreet.basecontructor.activity;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iblogstreet.basecontructor.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ListViewActivity extends Activity implements AbsListView.OnScrollListener {
    Button button_roll;
    ListView lv_1, lv_2, lv_3;
    TextView tv_result;
    //3 个初始随机数，作为 ListView 初始时开始条目的位置
    private int random1;
    private int random2;
    private int random3;
    private int currentState1 = -1;
    private int currentState2 = -1;
    private int currentState3 = -1;
    //是否中奖
    private boolean isBingo = false;

    private int rad1;
    private int rad2;
    private int rad3;

    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        iniView();
        iniValue();
        iniListener();
    }

    private void iniView() {
        button_roll = (Button) findViewById(R.id.button_roll);
        lv_1 = (ListView) findViewById(R.id.lv_1);
        lv_2 = (ListView) findViewById(R.id.lv_2);
        lv_3 = (ListView) findViewById(R.id.lv_3);
        tv_result = (TextView) findViewById(R.id.tv_result);
        myAdapter = new MyAdapter();
    }

    private void iniValue() {
        lv_1.setAdapter(myAdapter);
        lv_2.setAdapter(myAdapter);
        lv_3.setAdapter(myAdapter);

        random1 = new Random().nextInt(Integer.MAX_VALUE / 2);
        random2 = new Random().nextInt(Integer.MAX_VALUE / 2);
        random3 = new Random().nextInt(Integer.MAX_VALUE / 2);

        lv_1.setSelection(random1);
        lv_2.setSelection(random2);
        lv_3.setSelection(random3);
    }

    private void iniListener() {
        lv_1.setOnScrollListener(this);
        lv_2.setOnScrollListener(this);
        lv_3.setOnScrollListener(this);
        button_roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                *  生成新的随机数，该随机数介于死一个随机数±50 的范围
                *  这样滚动不会太久
                * */
                rad1 = random1 + new Random().nextInt(100) - 50;
                rad2 = random2 + new Random().nextInt(100) - 50;
                rad3 = random3 + new Random().nextInt(100) - 50;
                if((rad1%9==rad2%9)&&(rad2%9==rad3%9)){
                    isBingo=true;
                }
                else
                    isBingo=false;
                lv_1.smoothScrollToPosition(rad1);
                lv_2.smoothScrollToPosition(rad2);
                lv_3.smoothScrollToPosition(rad3);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch(view.getId()){
            case R.id.lv_1:
                currentState1=scrollState;
                break;
            case R.id.lv_2:
                currentState2=scrollState;
                break;
            case R.id.lv_3:
                currentState3=scrollState;
                break;
        }
        if(currentState1==0&&currentState2==0&&currentState3==0){
            lv_1.setSelection(rad1);
            lv_2.setSelection(rad2);
            lv_3.setSelection(rad3);
        }
        if(isBingo){
            tv_result.setText("Congratulations,Bud,you got a award!");
        }
        else {
            tv_result.setText("Sorry,Bud,you lost!");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("ListViewActivity","firstVisibleItem:"+firstVisibleItem+"visibleItemCount"+visibleItemCount
                +"totalItemCount"+totalItemCount
        );
    }


    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                convertView = new TextView(ListViewActivity.this);
            }
            tv = (TextView) convertView;
            switch (position % 9) {
                case 1:
                    tv.setText("1");
                    tv.setTextColor(Color.BLUE);
                    break;
                case 2:
                    tv.setText("2");
                    tv.setTextColor(Color.YELLOW);
                    break;
                case 3:
                    tv.setText("3");
                    tv.setTextColor(Color.GREEN);
                    break;
                case 4:
                    tv.setText("4");
                    tv.setTextColor(Color.RED);
                    break;
                case 5:
                    tv.setText("5");
                    tv.setTextColor(Color.CYAN);
                    break;
                case 6:
                    tv.setText("6");
                    tv.setTextColor(Color.LTGRAY);
                    break;
                case 7:
                    tv.setText("7");
                    tv.setTextColor(Color.MAGENTA);
                    break;
                case 8:
                    tv.setText("8");
                    tv.setTextColor(Color.BLACK);
                    break;
                case 0:
                    tv.setText("9");
                    tv.setTextColor(Color.LTGRAY);
                    break;
                default:
                    tv.setText("9");
                    tv.setTextColor(Color.LTGRAY);
                    break;
            }
            return tv;
        }
    }
}
