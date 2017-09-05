package com.osama.project34.data;

import java.io.Serializable;

/**
 * Created by bullhead on 9/5/17.
 *
 */

public class Folder implements Serializable {
    private int id;
    private int title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
