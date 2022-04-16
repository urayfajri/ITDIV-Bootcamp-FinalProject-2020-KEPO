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
import com.example.kepo.databinding.ActivityInsertUpdateBinding;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONObject;

public class InsertUpdateActivity extends AppCompatActivity {

    private ActivityInsertUpdateBinding binding;
    private static final String BASE_URL= "https://it-division-kepo.herokuapp.com/user/";
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_insert_update);
        binding.setTodo(new ToDo());

        pref = new SharedPref(this);
        User userData =pref.load();
        ToDo toDoData = pref.loadToDo();


        String previousActivity = pref.loadActivity();

        if(previousActivity.equals("MyToDoActivity")){
            binding.tvCreateUodateToDo.setText("Create Todo");

            binding.fabCreateUpdateTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insert(userData.getUserID());
                }
            });

            binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoToDoActivity();
                }
            });
        }
        else if(previousActivity.equals("DetailToDoActivity")){

            binding.setTodo(toDoData);
            binding.tvCreateUodateToDo.setText("Update Todo");

            binding.fabCreateUpdateTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update(userData.getUserID(),toDoData.getToDoID());
                }
            });

            binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoToDetailToDoActivity();
                }
            });
        }

    }

    private void update(String userID,String todoID){
        ToDo todo = binding.getTodo();

        try {
            if(todo.getToDoTitle().equals("") || todo.getToDoDescription().equals("")){
                binding.tvMessageErrorInsertUpdate.setText("Text field cannot be empty");
                binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            }
            else if(todo.getToDoDescription().length() > 100){
                binding.tvMessageErrorInsertUpdate.setText("Your description exceeded the maximum words");
                binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            }
            else{
                binding.pbLoadingApi.setVisibility(View.VISIBLE);
                binding.tvMessageErrorInsertUpdate.setVisibility(View.GONE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.PUT,
                        BASE_URL + userID + "/todo/" + todoID,
                        getUpdateTodo(todo),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                binding.pbLoadingApi.setVisibility(View.GONE);
                                try {
                                    String message = response.getString("message");
                                    //Log.d("<RESUTLT>", "onResponse: " + message);

                                    if(message.equals("Update todo success")){
                                        Toast.makeText(InsertUpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                                        gotoToDetailToDoActivity();
                                    }
                                }catch (Exception e){
                                    binding.pbLoadingApi.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                binding.pbLoadingApi.setVisibility(View.GONE);
                                error.printStackTrace();
                            }
                        }

                );
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(jsonObjectRequest);
            }

        }catch (Exception e){
            binding.tvMessageErrorInsertUpdate.setText("Text field cannot be empty");
            binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private void insert(String userID){
        ToDo todo = binding.getTodo();

        try {
            if(todo.getToDoTitle().equals("") || todo.getToDoDescription().equals("")){
                binding.tvMessageErrorInsertUpdate.setText("Text field cannot be empty");
                binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            }
            else if(todo.getToDoDescription().length() > 100){
                binding.tvMessageErrorInsertUpdate.setText("Your description exceeded the maximum words");
                binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            }
            else{
                binding.tvMessageErrorInsertUpdate.setVisibility(View.GONE);
                binding.pbLoadingApi.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        BASE_URL + userID + "/todo",
                        getInsertTodo(todo),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                binding.pbLoadingApi.setVisibility(View.GONE);
                                try {
                                    String message = response.getString("message");
                                    Log.d("<RESUTLT>", "onResponse: " + message);

                                    if(message.equals("Todo created successfully")){
                                        Toast.makeText(InsertUpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                                        gotoToDoActivity();
                                    }
                                }catch (Exception e){
                                    binding.pbLoadingApi.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                binding.pbLoadingApi.setVisibility(View.GONE);
                                error.printStackTrace();
                            }
                        }

                );
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(jsonObjectRequest);
            }

        }catch (Exception e){
            binding.tvMessageErrorInsertUpdate.setText("Text field cannot be empty");
            binding.tvMessageErrorInsertUpdate.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private JSONObject getInsertTodo(ToDo toDo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",toDo.getToDoTitle());
            jsonObject.put("description",toDo.getToDoDescription());
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }

    private JSONObject getUpdateTodo(ToDo toDo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",toDo.getToDoTitle());
            jsonObject.put("description",toDo.getToDoDescription());
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }

    private void gotoToDoActivity(){
        Intent intent = new Intent(InsertUpdateActivity.this, MyToDoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void gotoToDetailToDoActivity(){
        Intent intent = new Intent(InsertUpdateActivity.this, DetailToDoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}