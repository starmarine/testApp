package com.example.tmac.testapp.dto;

import com.example.tmac.testapp.utils.AbstractBaseObject;

public class BindingDto extends AbstractBaseObject{
    private String server;
    private String ticket;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
