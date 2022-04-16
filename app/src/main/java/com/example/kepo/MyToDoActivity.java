package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.adapter.ToDoAdapter;
import com.example.kepo.databinding.ActivityMyToDoBinding;
import com.example.kepo.fragment.DeleteSelectedToDoFragment;
import com.example.kepo.fragment.ErrorLoginFragment;
import com.example.kepo.fragment.LogoutFragment;
import com.example.kepo.interfaces.IClickableTodo;
import com.example.kepo.interfaces.QuantityListener;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;

public class MyToDoActivity extends AppCompatActivity implements QuantityListener {

    private ActivityMyToDoBinding binding;
    private ToDoAdapter toDoAdapter;
    private static final String BASE_URL ="https://it-division-kepo.herokuapp.com/user/";
    private SharedPref pref;
    private int countDelete = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_to_do);

        pref = new SharedPref(this);
        User userData = pref.load();
        pref.saveActivityTodo("MyToDoActivity");

        IClickableTodo listener = new IClickableTodo() {
            @Override
            public void onItemClick(ToDo toDo) {
                pref.saveToDo(toDo);
                goToDetailToDoActivity();
            }
        };
        initAdapter(listener);
        loadData(userData.getUserID());

        binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyToDoActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.saveActivity("MyToDoActivity");
                Intent intent = new Intent(MyToDoActivity.this, InsertUpdateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void goToDetailToDoActivity(){
        Intent intent = new Intent(MyToDoActivity.this,DetailToDoActivity.class);
        startActivity(intent);
    }

    private void initAdapter(IClickableTodo listener){
        toDoAdapter = new ToDoAdapter(this,listener,this);
        binding.rvMyTodos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMyTodos.setAdapter(toDoAdapter);
    }

    private void loadData(String userID){
        binding.tvDisplayShowNoData.setVisibility(View.GONE);
        binding.pbSearchTodo.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + userID + "/todo",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.pbSearchTodo.setVisibility(View.GONE);
                        try {
//                            String status = response.getString("status");
//                            String message = response.getString("message");
//                            Log.d("<RESULT>", "onResponse: " + status);
//                            Log.d("<MESSAGE>", "onResponse: " + message);

                            ArrayList<ToDo> result = new ArrayList<>();
                            JSONObject data = response.getJSONObject("data");
                            JSONArray listTodo = data.getJSONArray("listTodo");

                            if(listTodo.length()!=0){
                                for (int i = 0; i< listTodo.length();i++) {

                                    JSONObject jsonObject = listTodo.getJSONObject(i);
                                    String todoID = jsonObject.getString("todo_id");
                                    String todoTitle = jsonObject.getString("title");
                                    String todoLastEdited = jsonObject.getString("last_edited");
                                    //String dtStart = "2019-08-15T09:27:37Z";
                                    SimpleDateFormat formatinDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    Date date = formatinDate.parse(todoLastEdited);
                                    formatinDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                    todoLastEdited = formatinDate.format(date);
                                    //textView.setText("ISO 1801 date/time: " + date);

                                    ToDo todo = new ToDo();
                                    todo.setToDoID(todoID);
                                    todo.setToDoTitle(todoTitle);
                                    todo.setToDoLastModified(todoLastEdited);
                                    result.add(todo);
                                    toDoAdapter.updateData(result);
                                }
                            }else{
                                binding.tvDisplayShowNoData.setVisibility(View.VISIBLE);
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

    @Override
    public void onQuantityChange(ArrayList<ToDo> arrayList) {
        countDelete = arrayList.size();

        if(countDelete> 0){
            Snackbar snackBar = Snackbar.make(binding.getRoot(), ""+ countDelete+" item(s)", Snackbar.LENGTH_LONG) .setAction("DELETE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new DeleteSelectedToDoFragment());
                    fragmentTransaction.commit();
                }
            });
            snackBar.setActionTextColor(Color.RED);
            snackBar.show();
        }
        pref.saveListToDelete(arrayList);

    }
}