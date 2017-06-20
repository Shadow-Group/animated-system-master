package com.osama.project34;

/**
 * Created by Shadow on 6/17/2017.
 */

public class Data {
    String Folder_Name;
    String Email;
    String Subject;
    String User_Name;
    String Message;
    public void setFolderName(String folder_Name){
        Folder_Name=folder_Name;
    }
    public String getFolder_Name(){
        return Folder_Name;
    }
    public void setEmail(String email){
        Email=email;
    }
    public String getEmail(){
        return Email;
    }
    public void setSubject(String subject){
        Subject=subject;
    }
    public String getSubject(){
        return Subject;
    }
    public void setUser_Name(String user_name){
        User_Name=user_name;
    }
    public String getUser_Name(){
        return User_Name;
    }
    public  void setMessage(String message){
        Message=message;
    }
    public  String getMessage(){
        return Message;
    }

}

