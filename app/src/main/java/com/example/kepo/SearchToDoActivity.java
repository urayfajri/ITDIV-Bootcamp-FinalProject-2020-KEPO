package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.kepo.adapter.SearchToDoAdapter;
import com.example.kepo.databinding.ActivitySearchToDoBinding;
import com.example.kepo.interfaces.IClickableTodo;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchToDoActivity extends AppCompatActivity {

    private ActivitySearchToDoBinding binding;
    private SearchToDoAdapter searchToDoAdapter;
    private static final String BASE_URL ="https://it-division-kepo.herokuapp.com/searchTodos";
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_to_do);
        pref = new SharedPref(this);
        pref.saveActivityTodo("SearchToDoActivity");

        IClickableTodo listener = new IClickableTodo() {
            @Override
            public void onItemClick(ToDo toDo) {
                //Toast.makeText(SearchToDoActivity.this, toDo.getToDoUserID(), Toast.LENGTH_SHORT).show();
                pref.saveToDo(toDo);
                goToDetailToDoActivity();
            }
        };

        initAdapter(listener);

        binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchToDoActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        if(!pref.loadSearchTodo().equals("") && (pref.loadCheckByUser()!= 0 || pref.loadCheckByTodo()!=0 )){
            binding.pbSearchTodo.setVisibility(View.VISIBLE);
            binding.llResultSearchTodo.setVisibility(View.VISIBLE);
            binding.tvDisplayResultSearchToDo.setText(pref.loadSearchTodo());
            loadSearchToDoData(pref.loadSearchTodo(),pref.loadCheckByUser(),pref.loadCheckByTodo());
        }

        binding.ivSearchToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rvSearchTodos.setVisibility(View.GONE);
                binding.tvDisplayShowNoData.setVisibility(View.GONE);
                searchToDoAdapter.clearData();
                searchTodos();
            }
        });

    }

    private void goToDetailToDoActivity(){
        Intent intent = new Intent(SearchToDoActivity.this,DetailToDoActivity.class);
        startActivity(intent);
    }

    private void initAdapter(IClickableTodo listener){
        searchToDoAdapter = new SearchToDoAdapter(listener);
        binding.rvSearchTodos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchTodos.setAdapter(searchToDoAdapter);
    }

    private void searchTodos(){
        int checkByUser=0,checkByTodo = 0;
        String searchTodos = binding.etSearchTodo.getText().toString();

        if(searchTodos.equals("")){
            Toast.makeText(this, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            binding.pbSearchTodo.setVisibility(View.VISIBLE);
            if(binding.cbSearchTodoByUser.isChecked() || binding.cbSearchTodoByTodo.isChecked()){
                binding.llResultSearchTodo.setVisibility(View.VISIBLE);
                binding.tvDisplayResultSearchToDo.setText(searchTodos);
                binding.etSearchTodo.getText().clear();
            }
            if (binding.cbSearchTodoByTodo.isChecked()){
                checkByTodo = 1;
            }
            if(binding.cbSearchTodoByUser.isChecked()){
                checkByUser = 1;
            }
            pref.saveCheckByTodo(checkByTodo);
            pref.saveCheckByUser(checkByUser);
            pref.saveSearchTodo(searchTodos);

            loadSearchToDoData(searchTodos,checkByUser,checkByTodo);

        }
    }

    private void loadSearchToDoData(String searchTodo, int byUser, int byTodo ){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                getSearchBody(searchTodo,byUser,byTodo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.pbSearchTodo.setVisibility(View.GONE);
                        int status=0;
                        String message="";
                        try {
                              status = response.getInt("status");
                              if(status == 200){
                                  binding.rvSearchTodos.setVisibility(View.VISIBLE);
                                  message = response.getString("message");
                                  //Toast.makeText(SearchToDoActivity.this, "berhasil", Toast.LENGTH_SHORT).show();

                                    ArrayList<ToDo> result = new ArrayList<>();
                                    JSONArray listTodo = response.getJSONArray("data");

                                    if(listTodo.length()!=0){
                                        for (int i = 0; i< listTodo.length();i++){

                                            JSONObject jsonObject = listTodo.getJSONObject(i);
                                            String todoUserID = jsonObject.getString("user_id");
                                            String todoUsername = jsonObject.getString("username");
                                            String todoID = jsonObject.getString("todo_id");
                                            String todoTitle = jsonObject.getString("title");
                                            String todoDescription = jsonObject.getString("description");
                                            String todoLastEdited = jsonObject.getString("last_edited");
                                            //String dtStart = "2019-08-15T09:27:37Z";
                                            SimpleDateFormat formatinDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                            Date date = formatinDate.parse(todoLastEdited);
                                            formatinDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            todoLastEdited = formatinDate.format(date);
                                            //textView.setText("ISO 1801 date/time: " + date);

                                            ToDo todo = new ToDo();
                                            todo.setToDoUserID(todoUserID);
                                            todo.setToDoUsername(todoUsername);
                                            todo.setToDoID(todoID);
                                            todo.setToDoTitle(todoTitle);
                                            todo.setToDoDescription(todoDescription);
                                            todo.setToDoLastModified(todoLastEdited);
                                            result.add(todo);
                                            searchToDoAdapter.updateData(result);
                                        }
                                    }else{
                                        binding.tvDisplayShowNoData.setVisibility(View.VISIBLE);
                                    }
                              }

                        }catch (Exception e){
                            e.printStackTrace();
                            binding.pbSearchTodo.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        binding.pbSearchTodo.setVisibility(View.GONE);
                        Toast.makeText(SearchToDoActivity.this,"You must choose either to search by user, todo, or both" , Toast.LENGTH_SHORT).show();

                    }
                }

        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private JSONObject getSearchBody(String searchTodo,int checkByUser,int checkByTodo){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("searchQuery",searchTodo);
            jsonObject.put("filterUser",checkByUser);
            jsonObject.put("filterTodo",checkByTodo);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }

}