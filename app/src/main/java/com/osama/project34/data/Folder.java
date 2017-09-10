package com.osama.project34.data;

import java.io.Serializable;



public class Folder implements Serializable {
    private int id;
    private String title;

    public Folder(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Folder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
