package com.zmm.zhoumin20180730;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private TextView t_suosuo;
private EditText edname;
private AutoFlowLayout auto_flow;
private TextView auto_tv;
private View view;
private String name;
private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        list=new ArrayList<>();
        auto_flow=findViewById(R.id.auto_flow);
        auto_tv=findViewById(R.id.auto_tv);
        t_suosuo=findViewById(R.id.t_sousuo);
        edname=findViewById(R.id.ed_name);

        t_suosuo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.t_fan:
                finish();
                break;
            case R.id.t_sousuo:
                 name=edname.getText().toString();
                 list.add(name);
                 play();
                break;
        }
    }

    private void play() {
        auto_flow.setAdapter(new FlowAdapter(list) {
            @Override
            public View getView(int i) {
                if (list!=null){
                     view=View.inflate(MainActivity.this,R.layout.auto_layout,null);
                    final  TextView auto_tv=view.findViewById(R.id.auto_tv);
                    auto_tv.setText(list.get(i));
                    list.clear();
                }
                auto_flow.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(int i, View view) {
                        Intent intent=new Intent(MainActivity.this,ShoppingActivity.class);
                        String name=edname.getText().toString();
                        intent.putExtra("name",name);
                        startActivity(intent);
                    }
                });
                return view;
            }
        });
    }
}
