package com.zmm.zhoumin20180730.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zmm.zhoumin20180730.bean.ShoppingBean;
import com.zmm.zhoumin20180730.http.HttpConfig;
import com.zmm.zhoumin20180730.model.IModel;
import com.zmm.zhoumin20180730.view.IShoppingListener;
import com.zmm.zhoumin20180730.view.IShoppingView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PresenterImpel implements IPresenter {
    private static final String TAG = "PresenterImpel----";
private static  String url01="http://www.zhaoapi.cn/product/searchProducts";
    @Override
    public void getShop(IModel iModel, final IShoppingView iShoppingView) {
        Map<String,String> map = new HashMap<String,String> ();

        map.put ("keywords",iShoppingView.getName ());

        map.put ("page",iShoppingView.getpage ()+"");

        map.put ("sort",iShoppingView.getsort ());

        iModel.shop (HttpConfig.url, map, new IShoppingListener() {
            @Override
            public void onSuccess(String json) {

                Gson gson = new Gson ();

                ShoppingBean shoppingBean = gson.fromJson (json, ShoppingBean.class);

                List<ShoppingBean.DataBean> list = shoppingBean.getData ();

                iShoppingView.ShowShoppingToViews (list);
            }

            @Override
            public void onError(String error) {
                Log.d (TAG, "onError: 失败-------");
            }
        });
    }
}
