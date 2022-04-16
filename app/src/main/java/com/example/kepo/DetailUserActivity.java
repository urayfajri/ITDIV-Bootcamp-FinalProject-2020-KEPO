package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.adapter.SearchToDoAdapter;
import com.example.kepo.databinding.ActivityDetailUserBinding;
import com.example.kepo.interfaces.IClickableTodo;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailUserActivity extends AppCompatActivity {

    private ActivityDetailUserBinding binding;
    private SearchToDoAdapter searchToDoAdapter;
    private static final String BASE_URL ="https://it-division-kepo.herokuapp.com/searchTodos";
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_user);
        pref = new SharedPref(this);
        pref.saveActivityTodo("DetailUserActivity");
        User userData =pref.loadSearchUser();
        binding.setUser(userData);

        IClickableTodo listener = new IClickableTodo() {
            @Override
            public void onItemClick(ToDo toDo) {
                pref.saveToDo(toDo);
                goToDetailToDoActivity();
            }
        };

        initAdapter(listener);

        binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailUserActivity.this, SearchUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        binding.rvUserTodos.setVisibility(View.GONE);
        binding.tvDisplayShowNoData.setVisibility(View.GONE);
        String searchTodos = userData.getUsername();
        searchTodos(searchTodos);

    }

    private void searchTodos(String searchTodos){
        binding.pbUserTodo.setVisibility(View.VISIBLE);
        int checkByUser=1,checkByTodo = 0;
        loadSearchToDoData(searchTodos,checkByUser,checkByTodo);
    }

    private void loadSearchToDoData(String searchTodo, int byUser, int byTodo){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                getSearchBody(searchTodo,byUser,byTodo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.pbUserTodo.setVisibility(View.GONE);
                        int status=0;
                        String message="";
                        try {
                            status = response.getInt("status");
                            if(status == 200){
                                binding.rvUserTodos.setVisibility(View.VISIBLE);
                                message = response.getString("message");

                                ArrayList<ToDo> result = new ArrayList<>();
                                JSONArray listTodo = response.getJSONArray("data");

                                binding.tvCountTodos.setText(""+ listTodo.length());
                                if(listTodo.length()!=0){
                                    for (int i = 0; i< listTodo.length();i++){

                                        JSONObject jsonObject = listTodo.getJSONObject(i);
                                        String todoUserID = jsonObject.getString("user_id");
                                        String todoUsername = jsonObject.getString("username");
                                        String todoID = jsonObject.getString("todo_id");
                                        String todoTitle = jsonObject.getString("title");
                                        String todoDescription = jsonObject.getString("description");
                                        String todoLastEdited = jsonObject.getString("last_edited");

                                        SimpleDateFormat formatinDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        Date date = formatinDate.parse(todoLastEdited);
                                        formatinDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                        todoLastEdited = formatinDate.format(date);


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
                            binding.pbUserTodo.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        binding.pbUserTodo.setVisibility(View.GONE);
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

    private void goToDetailToDoActivity(){
        Intent intent = new Intent(DetailUserActivity.this,DetailToDoActivity.class);
        startActivity(intent);
    }

    private void initAdapter(IClickableTodo listener){
        searchToDoAdapter = new SearchToDoAdapter(listener);
        binding.rvUserTodos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUserTodos.setAdapter(searchToDoAdapter);
    }
}