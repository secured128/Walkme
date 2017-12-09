package com.walkme.codingtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

/*
Represents a single campaign with adjustments for required JSON representation for returned data
 */
@JsonPropertyOrder({"id", "data"})
public class Campaign {

    private int id;
    private String name;
    private Serializable data;
    private Cap cap;

    public Campaign(int id, String name, Serializable data, Cap cap) {
        this.setId(id);
        this.setName(name);
        this.setData(data);
        this.setCap(cap);
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Cap getCap() {
        return cap;
    }

    public void setCap(Cap cap) {
        this.cap = cap;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Campaign))
            return false;
        return getId() == ((Campaign) obj).getId();
    }
}
