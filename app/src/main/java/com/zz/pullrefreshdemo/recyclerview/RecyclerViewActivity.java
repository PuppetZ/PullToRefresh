package com.zz.pullrefreshdemo.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListView;

import com.zz.pullrefreshdemo.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {
    private PullToRefreshRV mRV;
    private List<String> mList = new ArrayList<>();
    private RecyclerViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initView();
    }

    private void initView() {
        mRV = (PullToRefreshRV) findViewById(R.id.recyclerView);
        for (int i = 0; i < 20; i++) {
            mList.add("这是第" + i + "数据");
        }
        mAdapter = new RecyclerViewAdapter(RecyclerViewActivity.this, mList);
        mRV.setLayoutManager(new LinearLayoutManager(RecyclerViewActivity.this));
        mRV.setAdapter(mAdapter);

        mRV.setOnRefreshCompleteListener(new PullToRefreshRV.OnRefreshCompleteListener() {
            @Override
            public void onRefreshComplete() {

            }
        });
    }
}
