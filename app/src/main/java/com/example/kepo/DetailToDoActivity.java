package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.databinding.ActivityDetailToDoBinding;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailToDoActivity extends AppCompatActivity {

    private ActivityDetailToDoBinding binding;
    private static final String BASE_URL ="https://it-division-kepo.herokuapp.com/user/";
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_to_do);

        pref = new SharedPref(this);
        User userData =pref.load();
        ToDo toDoData = pref.loadToDo();


        if(pref.loadActivityTodo().equals("SearchToDoActivity")){
            if(userData.getUserID().equals(toDoData.getToDoUserID())){
                loadDetailToDo(userData.getUserID(),toDoData.getToDoID());
                binding.fabDetailTodo.setVisibility(View.VISIBLE);
                binding.fabDetailTodo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pref.saveActivity("DetailToDoActivity");
                        Intent intent = new Intent(DetailToDoActivity.this, InsertUpdateActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{

                binding.setTodo(toDoData);
                binding.fabDetailTodo.setVisibility(View.GONE);
            }

            binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailToDoActivity.this, SearchToDoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

        }else if(pref.loadActivityTodo().equals("MyToDoActivity")){
            loadDetailToDo(userData.getUserID(),toDoData.getToDoID());
            binding.fabDetailTodo.setVisibility(View.VISIBLE);
            binding.fabDetailTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pref.saveActivity("DetailToDoActivity");
                    Intent intent = new Intent(DetailToDoActivity.this, InsertUpdateActivity.class);
                    startActivity(intent);
                }
            });

            binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailToDoActivity.this, MyToDoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }else if(pref.loadActivityTodo().equals("DetailUserActivity")){
            binding.setTodo(toDoData);
            binding.fabDetailTodo.setVisibility(View.GONE);
            binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailToDoActivity.this, DetailUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });

        }

    }

    private void loadDetailToDo(String userID,String todoID){
        binding.pbSearchTodo.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + userID + "/todo/" + todoID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.pbSearchTodo.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
//                            Log.d("<RESULT>", "onResponse: " + status);
//                            Log.d("<MESSAGE>", "onResponse: " + message);
                            if(message.equals("")){
                                try {
                                    JSONObject data = response.getJSONObject("data");
                                    String todoID = data.getString("todo_id");
                                    String todoTitle = data.getString("title");
                                    String todoDescription = data.getString("description");
                                    String todoLastEdited = data.getString("last_edited");
                                    //String dtStart = "2019-08-15T09:27:37Z";
                                    SimpleDateFormat formatinDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    Date date = formatinDate.parse(todoLastEdited);
                                    formatinDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                    todoLastEdited = formatinDate.format(date);

                                    ToDo todo = new ToDo();
                                    todo.setToDoID(todoID);
                                    todo.setToDoUserID(pref.loadToDo().getToDoUserID());
                                    todo.setToDoUsername(pref.loadToDo().getToDoUsername());
                                    todo.setToDoTitle(todoTitle);
                                    todo.setToDoDescription(todoDescription);
                                    todo.setToDoLastModified(todoLastEdited);
                                    pref.saveToDo(todo);
                                    binding.setTodo(todo);


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                        }catch (Exception e){
                            binding.pbSearchTodo.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.pbSearchTodo.setVisibility(View.GONE);
                        error.printStackTrace();

                    }
                }

        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}