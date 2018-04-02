package com.example.tmac.testapp.utils;

import com.alibaba.fastjson.JSON;

/**
 * Created by tmac on 2018/4/2.
 */

public class AbstractBaseObject {
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
