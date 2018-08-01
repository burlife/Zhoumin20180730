package com.zmm.zhoumin20180730.presenter;


import com.zmm.zhoumin20180730.bean.CartBean;

public interface ICartPresenter {
    void onFormSuccess(CartBean cartBean);
    void onFormFailed(String error);
}
