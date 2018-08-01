package com.zmm.zhoumin20180730.presenter;

import android.util.Log;


import com.zmm.zhoumin20180730.bean.CartBean;
import com.zmm.zhoumin20180730.model.CartModelImpl;
import com.zmm.zhoumin20180730.view.ICartView;

import java.util.List;



public class CartPresetnerImpl implements ICartPresenter {
    String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";
    ICartView iCartView;
    private final CartModelImpl cartModel;
    private static final String TAG = "CartPresetnerImpl";
    public CartPresetnerImpl(ICartView iCartView){
        this.iCartView = iCartView;
        cartModel = new CartModelImpl(this);

        cartModel.showCart(url);
    }

    public void getPrice(List<CartBean.DataBean> data){
    }

    @Override
    public void onFormSuccess(CartBean cartBean) {
        List<CartBean.DataBean> data = cartBean.getData();
        iCartView.onSuccess(data);
    }

    @Override
    public void onFormFailed(String error) {
        iCartView.onFailed(error);
    }
}
