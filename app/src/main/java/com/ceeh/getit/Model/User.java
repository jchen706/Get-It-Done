package com.ceeh.getit.Model;

import java.util.Calendar;
import java.util.Date;

public class User {
    private String firstName;
    private String password;
    private Date day;
    private int id;

    public User(int id, String firstName, String password) {
        this.id = id;
        this.firstName = firstName;
        this.password = password;
        day = Calendar.getInstance().getTime();
    }


    public User(int id, String firstName) {
        this(id, firstName, "111111");
    }

    public User(String firstName) {
        this( 1, firstName, "111111");
    }

    public User() {
        this("Bob");
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getDay() {
        return day;
    }

    public int getId() {
        return id;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return firstName;
    }
}
