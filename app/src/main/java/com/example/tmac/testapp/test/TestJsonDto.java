package com.example.tmac.testapp.test;

import com.example.tmac.testapp.utils.AbstractBaseObject;

/**
 * Created by tmac on 2018/4/2.
 */

public class TestJsonDto extends AbstractBaseObject{
    private String subLemmaId;
    private String id;

    public String getSubLemmaId() {
        return subLemmaId;
    }

    public void setSubLemmaId(String subLemmaId) {
        this.subLemmaId = subLemmaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
