package com.zmm.zhoumin20180730.model;

import android.util.Log;

import com.google.gson.Gson;
import com.zmm.zhoumin20180730.bean.CartBean;
import com.zmm.zhoumin20180730.presenter.CartPresetnerImpl;
import com.zmm.zhoumin20180730.utils.HttpUtils;


public class CartModelImpl {
    CartPresetnerImpl cartPresetner;
    private static final String TAG = "CartModelImpl";

    public CartModelImpl(CartPresetnerImpl cartPresetner) {

        this.cartPresetner = cartPresetner;
    }

    public void showCart(String url) {
        HttpUtils httpUtils = HttpUtils.getHttpUtils();
        httpUtils.okGet(url);
        httpUtils.setOkLoadListener(new HttpUtils.OkLoadListener() {
            @Override
            public void okLoadSuccess(String success) {
                Gson gson = new Gson();
                CartBean cartBean = gson.fromJson(success, CartBean.class);
                String code = cartBean.getCode();
                if (code.equals("0")) {
                    cartPresetner.onFormSuccess(cartBean);
                } else {
                    cartPresetner.onFormFailed(cartBean.getMsg());
                }
            }

            @Override
            public void okLoadError(String error) {
                cartPresetner.onFormFailed(error);
            }
        });
    }

}
