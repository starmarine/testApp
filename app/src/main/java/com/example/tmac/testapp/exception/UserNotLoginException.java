package com.example.tmac.testapp.exception;

/**
 * Created by tmac on 2018/4/2.
 */

public class UserNotLoginException extends RuntimeException{
    public UserNotLoginException(String message, Throwable ex){
        super(message,ex);
    }

    public UserNotLoginException(String message){
        super(message);
    }
}
