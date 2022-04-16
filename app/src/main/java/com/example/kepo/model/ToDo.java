package com.example.kepo.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.kepo.BR;

public class ToDo extends BaseObservable {

    private String toDoID;
    private String toDoTitle;
    private String toDoDescription;
    private String toDoLastModified;
    private boolean isChecked;
    private String toDoUsername;
    private String ToDoUserID;

    @Bindable
    public String getToDoUserID() {
        return ToDoUserID;
    }

    public void setToDoUserID(String toDoUserID) {
        ToDoUserID = toDoUserID;
        notifyPropertyChanged(BR.toDoUserID);
    }

    @Bindable
    public String getToDoUsername() {
        return toDoUsername;
    }


    public void setToDoUsername(String toDoUsername) {
        this.toDoUsername = toDoUsername;
        notifyPropertyChanged(BR.toDoUsername);
    }

    @Bindable
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Bindable
    public String getToDoID() {
        return toDoID;
    }

    public void setToDoID(String toDoID) {
        this.toDoID = toDoID;
        notifyPropertyChanged(BR.toDoID);
    }

    @Bindable
    public String getToDoTitle() {
        return toDoTitle;
    }

    public void setToDoTitle(String toDoTitle) {
        this.toDoTitle = toDoTitle;
        notifyPropertyChanged(BR.toDoTitle);
    }

    @Bindable
    public String getToDoDescription() {
        return toDoDescription;
    }

    public void setToDoDescription(String toDoDescription) {
        this.toDoDescription = toDoDescription;
        notifyPropertyChanged(BR.toDoDescription);
    }

    @Bindable
    public String getToDoLastModified() {
        return toDoLastModified;
    }

    public void setToDoLastModified(String toDoLastModified) {
        this.toDoLastModified = toDoLastModified;
        notifyPropertyChanged(BR.toDoLastModified);
    }
}
