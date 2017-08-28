package com.zz.pullrefreshdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshListView.OnMyRefreshListener {
    private PullToRefreshListView list_view;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        for (int i = 0; i < 10; i++) {
            mList.add("第" + i + "数据");
        }
        list_view = (PullToRefreshListView) findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        list_view.setAdapter(mAdapter);
        list_view.setOnMyRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this,"下拉刷新",Toast.LENGTH_SHORT).show();
    }
}
