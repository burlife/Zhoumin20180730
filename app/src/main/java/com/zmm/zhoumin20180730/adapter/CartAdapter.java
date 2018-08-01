package com.zmm.zhoumin20180730.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zmm.zhoumin20180730.R;
import com.zmm.zhoumin20180730.bean.CartBean;
import com.zmm.zhoumin20180730.view.ICartView;
import com.zmm.zhoumin20180730.view.SumLayout;

import java.util.List;


public class CartAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "CartAdapter";
    private int count = 0;
    List<CartBean.DataBean> list;
    Context context;
    OnGetCount onGetCount;
    FromJiSuanData fromJiSuanData;
    ICartView iCartView;
    private boolean checked;
    ChildIsFlag childIsFlag;

    public CartAdapter(Context context, List<CartBean.DataBean> data, ICartView iCartView) {
        Log.d(TAG, "CartAdapter: " + data);
        this.context = context;
        this.list = data;
        this.iCartView = iCartView;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getList().size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MyGroupView myGroupView = null;
        //复用convertView优化条目
        if (convertView == null) {
            myGroupView = new MyGroupView();
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_group_item, parent, false);
            myGroupView.textView = convertView.findViewById(R.id.cart_group_item_title);
            convertView.setTag(myGroupView);
        } else {
            myGroupView = (MyGroupView) convertView.getTag();
        }
        //给父条目赋值
        myGroupView.textView.setText(list.get(groupPosition).getSellerName());
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyChildView myChildView = null;
        //复用convertView优化条目
        if (convertView == null) {
            myChildView = new MyChildView();
            //加载子选项条目视图
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_child_item, parent, false);
            myChildView.checkBox = convertView.findViewById(R.id.cart_child_item_cb);
            myChildView.imageView = convertView.findViewById(R.id.cart_child_item_simple);
            myChildView.title = convertView.findViewById(R.id.cart_child_item_title);
            myChildView.price = convertView.findViewById(R.id.cart_child_item_price);
            myChildView.bargainPrice = convertView.findViewById(R.id.cart_child_item_bargainPrice);
            myChildView.sumLayout = convertView.findViewById(R.id.cart_childe_item_sumlayout);
            convertView.setTag(myChildView);
        } else {
            myChildView = (MyChildView) convertView.getTag();
        }
        //得到子选项条目数据
        List<CartBean.ChildDataBean> childList = this.list.get(groupPosition).getList();
        CartBean.ChildDataBean childDataBean = childList.get(childPosition);

        //给子条目赋值
        myChildView.checkBox.setChecked(childDataBean.isChildChecked());
        String urls= childDataBean.getImages().split("\\|")[0];
        //使用Glide加载图片
        myChildView.imageView.setImageURI(urls);

        //赋值
        myChildView.title.setText(childDataBean.getTitle());
        myChildView.price.setText("￥" + childDataBean.getPrice());
        myChildView.bargainPrice.setText("￥"+childDataBean.getBargainPrice());
        myChildView.bargainPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//设置删除线
        myChildView.sumLayout.setCount(childDataBean.getNum() + "");

        final MyChildView finalMyChildView = myChildView;

        myChildView.sumLayout.setOnDownSumLayoutListener(new SumLayout.OnDownSumLayouListener() {
            @Override
            public void onDownSumLayout() {
                String count = finalMyChildView.sumLayout.getCount();
                //
                int i = Integer.parseInt(count);
                list.get(groupPosition).getList().get(childPosition).setNum(i);
                formCartJisuan();
                notifyDataSetChanged();

            }
        });
        checked = myChildView.checkBox.isChecked();
        childIsFlag.isFlag(checked);
        final MyChildView finalMyChildView1 = myChildView;
        myChildView.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = finalMyChildView1.checkBox.isChecked();

                Log.d(TAG, "onClick: " + checked);
                list.get(groupPosition).getList().get(childPosition).setChildChecked(checked);

                if (checked) {
                    count++;

                } else {//否则减一
                    if (count > 0) {
                        count--;

                    }
                }


                formCartJisuan();
                boolean childChecked = list.get(groupPosition).getList().get(childPosition).isChildChecked();
                onGetCount.getCount(count,checked,childChecked);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnGetCount {
        void getCount(int count, boolean b, boolean checked);
    }

    public void setOnGetCount(OnGetCount onGetCount) {
        this.onGetCount = onGetCount;
    }

    public double formCartJisuan() {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            //得到子条目数据集合
            List<CartBean.ChildDataBean> childList = list.get(i).getList();
            //遍历子条目数据集合
            for (int j = 0; j < childList.size(); j++) {
                //如果，子条目复选框为选中状态时，则累加总价
                if (childList.get(j).isChildChecked()) {
                    //得到总价sum  总价=数量*单价
                    sum += childList.get(j).getNum() * childList.get(j).getPrice();
                }
            }
        }

        fromJiSuanData.formJisuan(sum);
        return sum;
    }

    public interface FromJiSuanData {
        void formJisuan(double sum);
    }

    public void setFromJiSuan(FromJiSuanData fromJiSuanData) {
        this.fromJiSuanData = fromJiSuanData;
    }

    public interface ChildIsFlag{
        void isFlag(boolean flag);
    }

    public void setChildIsFlag(ChildIsFlag childIsFlag){
        this.childIsFlag = childIsFlag;
    }

    class MyGroupView {
        //
        TextView textView;
    }

    //子条目ViewHolder
    class MyChildView {

        CheckBox checkBox;

        SimpleDraweeView imageView;

        TextView title;

        TextView price;

        TextView bargainPrice;

        SumLayout sumLayout;
    }
}
