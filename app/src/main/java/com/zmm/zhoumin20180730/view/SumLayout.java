package com.zmm.zhoumin20180730.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmm.zhoumin20180730.R;


public class SumLayout extends LinearLayout implements View.OnClickListener {
    private TextView sub;
    private TextView count;
    private TextView add;
    private OnDownSumLayouListener onDownSumLayoutListener;
    public SumLayout(Context context) {
        this(context, null);
    }

    public SumLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SumLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载视图
        View view = View.inflate(context, R.layout.sumlayout, this);
        //获取控件
        sub = view.findViewById(R.id.jian);
        count = view.findViewById(R.id.count);
        add = view.findViewById(R.id.add);
        sub.setOnClickListener(this);
        add.setOnClickListener(this);
    }
    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jian://数量减减
                //得到数量
                String ss = count.getText().toString();
                int i = Integer.parseInt(ss);
                i--;
                if (i >= 0) {
                    setCount(i + "");
                    onDownSumLayoutListener.onDownSumLayout();
                }
                break;
            case R.id.add://数量加加
                String s = count.getText().toString();
                int i1 = Integer.parseInt(s);

                i1++;
                setCount(i1 + "");
                onDownSumLayoutListener.onDownSumLayout();
                break;
        }
    }
    public void setCount(String counts) {
        count.setText(counts);
    }
    //获取数量方法
    public String getCount() {
        return count.getText().toString();
    }
    public interface OnDownSumLayouListener {
        void onDownSumLayout();
    }
    public void setOnDownSumLayoutListener(OnDownSumLayouListener onDownSumLayoutListener) {
        this.onDownSumLayoutListener = onDownSumLayoutListener;
    }

}
