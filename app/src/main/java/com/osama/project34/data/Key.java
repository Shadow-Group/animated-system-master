package com.osama.project34.data;

import java.io.Serializable;

/**
 * Created by bullhead on 9/5/17.
 */

public class Key implements Serializable {
    private String user;
    private String text;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
