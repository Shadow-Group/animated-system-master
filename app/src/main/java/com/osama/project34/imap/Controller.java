package com.osama.project34.imap;

/**
 * Created by home on 3/24/17.
 *
 */

public class Controller {
    private static Controller instance;

    public static Controller getInstance() {
        if(instance==null){
            instance=new Controller();
        }
        return instance;
    }

}
