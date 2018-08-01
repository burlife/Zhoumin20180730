package com.zmm.zhoumin20180730;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmm.zhoumin20180730.adapter.CartAdapter;
import com.zmm.zhoumin20180730.bean.CartBean;
import com.zmm.zhoumin20180730.presenter.CartPresetnerImpl;
import com.zmm.zhoumin20180730.view.ICartView;

import java.util.List;

/**
 * Created by 1 on 2018/7/30.
 */

public class CartActivity extends AppCompatActivity implements ICartView{
    private static final String TAG = "CartActivity===";
    private ImageView left_jiantou;
    private ExpandableListView expandable;
    private CheckBox cart_frag_checkbox;
    private TextView cart_frag_price;
    private TextView cart_frag_xiadan;
    private TextView cart_frag_sum;
    private RecyclerView cart_frag_recycler;
    private TextView cart_frag_xiaoji;
    private CartPresetnerImpl cartPresetner;
    private List<CartBean.DataBean> list;
    private CartAdapter adapter;
    private int counts;
    private boolean frag_checked;
    private boolean check_flag;
    private boolean childflag;
    boolean flag = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        initViews();
    }

    private void initViews() {
        left_jiantou = findViewById(R.id.c_fan);
        expandable = findViewById(R.id.exlistview);
        cart_frag_checkbox = findViewById(R.id.cart_frag_checkbox);
        cart_frag_price = findViewById(R.id.cart_frag_price);
        cart_frag_xiadan =findViewById(R.id.cart_frag_xiadan);
        cart_frag_sum =findViewById(R.id.cart_frag_sum);
        cart_frag_recycler = findViewById(R.id.cart_recycler);
        cart_frag_xiaoji = findViewById(R.id.cart_xiaoji);
        //条目点击
      expandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
          @Override
          public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
              return true;
          }
      });
        //全选
        cart_frag_checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "getCount: 全选框onClick: " + check_flag);

                frag_checked = cart_frag_checkbox.isChecked();
                for (int i = 0; i < list.size(); i++) {

                    List<CartBean.ChildDataBean> childList = CartActivity.this.list.get(i).getList();
                    counts += childList.size();
                    for (int j = 0; j < childList.size(); j++) {

                        boolean childFlag = childList.get(j).isChildChecked();
                        if (frag_checked == false) {
                            flag = false;
                        }
                        childList.get(j).setChildChecked(frag_checked);

                        adapter.formCartJisuan();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        //返回的点击事件
        left_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
        cartPresetner = new CartPresetnerImpl(this);

    }

    @Override
    public void onSuccess(List<CartBean.DataBean> data) {
        Log.d(TAG, "onSuccess: " + data);
        this.list = data;
        adapter = new CartAdapter(CartActivity.this, data, this);
        expandable.setAdapter(adapter);
        //展开子条目
        int count = expandable.getCount();
        for (int i = 0; i < count; i++) {
            expandable.expandGroup(i);
        }
       adapter.setOnGetCount(new CartAdapter.OnGetCount() {

            @Override
            public void getCount(int count, boolean checked, boolean childChecked) {
                //count = counts;
                check_flag = checked;
                childflag = childChecked;
                counts = count;
                if (counts == 0) {
                    count = 0;
                } else {
                    counts = count;
                }
               if (checked) {
                    cart_frag_sum.setText("已选（" + count + "）");
                } else {
                    cart_frag_sum.setText("已选（0）");
                }

            }
        });

        adapter.setFromJiSuan(new CartAdapter.FromJiSuanData() {
            @Override
            public void formJisuan(double sum) {
                cart_frag_xiaoji.setText("小计：￥" + sum);
                cart_frag_price.setText("￥" + sum);
            }
        });

        adapter.setChildIsFlag(new CartAdapter.ChildIsFlag() {
            @Override
            public void isFlag(boolean flag) {
                if (flag) {
                    cart_frag_sum.setText("已选（" + counts + "）");
                } else {
                    counts = 0;
                    cart_frag_sum.setText("已选（" + counts + "）");
                }
            }
        });

    }

    @Override
    public void onFailed(String err) {

    }
}
