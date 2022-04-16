package com.example.kepo.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPref {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
    }

    public void saveUser(User user){
        editor.putString("userID",user.getUserID());
        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putString("name",user.getName());
        editor.apply();
    }

    public User load()
    {
        User user = new User();
        user.setUserID(sharedPreferences.getString("userID",""));
        user.setUsername(sharedPreferences.getString("username",""));
        user.setPassword(sharedPreferences.getString("password",""));
        user.setName(sharedPreferences.getString("name",""));

        return user;
    }

    public void saveSearchUser(User user){
        editor.putString("user_id",user.getUserID());
        editor.putString("user_name",user.getUsername());
        editor.putString("pass_word",user.getPassword());
        editor.putString("naMe",user.getName());
        editor.apply();
    }

    public User loadSearchUser()
    {
        User user = new User();
        user.setUserID(sharedPreferences.getString("user_id",""));
        user.setUsername(sharedPreferences.getString("user_name",""));
        user.setPassword(sharedPreferences.getString("pass_word",""));
        user.setName(sharedPreferences.getString("naMe",""));
        return user;
    }

    public void saveToDo(ToDo toDo){
        editor.putString("toDoUserID",toDo.getToDoUserID());
        editor.putString("toDoUsername",toDo.getToDoUsername());
        editor.putString("toDoID",toDo.getToDoID());
        editor.putString("toDoTitle",toDo.getToDoTitle());
        editor.putString("toDoDescription",toDo.getToDoDescription());
        editor.putString("toDoLastModified",toDo.getToDoLastModified());
        editor.putBoolean("toDoIsChecked",toDo.isChecked());
        editor.apply();
    }

    public ToDo loadToDo(){
        ToDo toDo = new ToDo();
        toDo.setToDoUserID(sharedPreferences.getString("toDoUserID",""));
        toDo.setToDoUsername(sharedPreferences.getString("toDoUsername",""));
        toDo.setToDoID(sharedPreferences.getString("toDoID",""));
        toDo.setToDoTitle(sharedPreferences.getString("toDoTitle",""));
        toDo.setToDoDescription(sharedPreferences.getString("toDoDescription",""));
        toDo.setToDoLastModified(sharedPreferences.getString("toDoLastModified",""));
        toDo.setChecked(sharedPreferences.getBoolean("toDoIsChecked",false));

        return toDo;
    }

    public void saveActivity(String activity){
        editor.putString("activity",activity);
        editor.apply();
    }

    public String loadActivity(){
        return sharedPreferences.getString("activity","");
    }

    public void saveActivityTodo(String activity){
        editor.putString("activityTodo",activity);
        editor.apply();
    }

    public String loadActivityTodo(){
        return sharedPreferences.getString("activityTodo","");
    }

    public void saveMessage(String message){
        editor.putString("message",message);
        editor.apply();
    }

    public String loadMessage(){
        return sharedPreferences.getString("message","");
    }

    public void saveFooter(String footer){
        editor.putString("footer",footer);
        editor.apply();
    }

    public String loadFooter(){
        return sharedPreferences.getString("footer","");
    }


    public void saveListToDelete(ArrayList<ToDo> arrayList){
        Gson gson = new Gson();
        String s = gson.toJson(arrayList);
        editor.putString("list",s);
        editor.apply();
    }

    public ArrayList<ToDo> loadListToDelete(){
        String s = sharedPreferences.getString("list","");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ToDo>>(){}.getType();
        ArrayList<ToDo> list = gson.fromJson(s,type);

        return list;
    }

    public void saveSearchUserQuery(String s){
        editor.putString("searchUserQuery",s);
        editor.apply();
    }

    public String loadSearchUserQuery(){
        return sharedPreferences.getString("searchUserQuery","");
    }

    public void saveSearchTodo(String s){
        editor.putString("searchTodo",s);
        editor.apply();
    }

    public String loadSearchTodo(){
        return sharedPreferences.getString("searchTodo","");
    }

    public void saveCheckByUser(int checkByUser){
        editor.putInt("checkByUser",checkByUser);
        editor.apply();
    }

    public int loadCheckByUser(){
        return sharedPreferences.getInt("checkByUser",0);
    }

    public void saveCheckByTodo(int checkByTodo){
        editor.putInt("checkByTodo",checkByTodo);
        editor.apply();
    }

    public int loadCheckByTodo(){
        return sharedPreferences.getInt("checkByTodo",0);
    }


    public void saveLogOut(Boolean check){
        editor.putBoolean("alreadyLogout",check);
        editor.apply();
    }

    public boolean loadLogout(){
        return sharedPreferences.getBoolean("alreadyLogout",false);
    }



}
