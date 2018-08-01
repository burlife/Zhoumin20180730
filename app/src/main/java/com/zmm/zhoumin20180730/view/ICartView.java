package com.zmm.zhoumin20180730.view;


import com.zmm.zhoumin20180730.bean.CartBean;

import java.util.List;


public interface ICartView {
    void onSuccess(List<CartBean.DataBean> data);

    void onFailed(String err);
}
