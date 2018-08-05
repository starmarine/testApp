package com.example.tmac.testapp.utils.http;

/**
 * Created by tmac on 2018/8/5.
 */

public class HttpResponse {
    private int status = 200;
    private String body = null;

    public HttpResponse(int status,String body){
        this.status = status;
        this.body = body;
    }

    public int getStatus(){
        return status;
    }

    public String getBody(){
        return body;
    }


}
