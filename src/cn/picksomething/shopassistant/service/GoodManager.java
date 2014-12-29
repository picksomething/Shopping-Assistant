package cn.picksomething.shopassistant.service;

import java.util.List;

import android.content.Context;

import cn.picksomething.shopassistant.model.Good;
import cn.picksomething.shopassistant.provider.GoodHelper;

public class GoodManager {

    private GoodHelper goodHelper;

    public GoodManager(Context context) {
        goodHelper = new GoodHelper(context);
    }

    public void addCollection(Good good) {
        goodHelper.addCollection(good);
    }

    public List<Good> getCollection() {
        return goodHelper.getCollection();
    }
}
