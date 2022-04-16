package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

import java.util.Observable;

public class User extends BaseObservable {

    private String userID;
    private String username;
    private String password;
    private String name;
    private int countToDo;

    @Bindable
    public int getCountToDo() {
        return countToDo;
    }

    public void setCountToDo(int countToDo) {
        this.countToDo = countToDo;
        notifyPropertyChanged(BR.countToDo);
    }

    @Bindable
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
        notifyPropertyChanged(BR.userID);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
