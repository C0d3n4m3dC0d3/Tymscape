package com.asas.tymscape;
//DataBase Model for - USER

public class DBHelper {
    private String ID;
    private String uname;
    private String email;
    private String password;

    //Default constructor
    public DBHelper() {
    }

    //Constructor to set the values
    public DBHelper(String ID, String uname, String email, String password) {
        this.ID = ID;
        this.uname = uname;
        this.email = email;
        this.password = password;
    }
    //Setter methods


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Getter methods
    public String getId() {return ID; }

    public String getUname() {return uname; }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}