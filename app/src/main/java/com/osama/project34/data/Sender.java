package com.osama.project34.data;

import java.io.Serializable;


public class Sender implements Serializable {
    private String mail;
    private String name;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
