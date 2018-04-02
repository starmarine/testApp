package com.example.tmac.testapp.exception;

/**
 * Created by tmac on 2018/4/2.
 */

public class ApplicationException extends RuntimeException{
    public ApplicationException(String message,Throwable ex){
        super(message,ex);
    }
}
