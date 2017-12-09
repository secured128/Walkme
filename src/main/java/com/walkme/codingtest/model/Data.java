package com.walkme.codingtest.model;

import java.io.Serializable;


/*
Represents any type of serializable data which could be needed by the client
 */
public class Data implements Serializable {

    private Serializable data;

    public Data(Serializable data) {
        this.setData(data);
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }
}
