package com.zmm.zhoumin20180730.model;

import com.zmm.zhoumin20180730.bean.ShoppingBean;
import com.zmm.zhoumin20180730.view.IShoppingListener;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2018/7/30.
 */

public interface IModel {
    void shop(String url, Map<String, String> map, IShoppingListener iShoppingListener);
}
