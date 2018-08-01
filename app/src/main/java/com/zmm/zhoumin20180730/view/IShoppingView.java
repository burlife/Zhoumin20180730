package com.zmm.zhoumin20180730.view;

import com.zmm.zhoumin20180730.bean.ShoppingBean;

import java.util.List;

/**
 * Created by 1 on 2018/7/30.
 */

public interface IShoppingView {
    void ShowShoppingToViews(List<ShoppingBean.DataBean>data);

    String getName();

    int getpage();

    String getsort();
}
