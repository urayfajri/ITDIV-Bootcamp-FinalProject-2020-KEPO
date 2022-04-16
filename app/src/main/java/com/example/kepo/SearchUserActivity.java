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
import com.example.kepo.adapter.SearchUserAdapter;
import com.example.kepo.databinding.ActivitySearchUserBinding;
import com.example.kepo.interfaces.IClickableUser;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchUserActivity extends AppCompatActivity {

    private ActivitySearchUserBinding binding;
    private SearchUserAdapter searchUserAdapter;
    private static final String BASE_URL ="https://it-division-kepo.herokuapp.com/searchUser";
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_search_user);
        pref = new SharedPref(this);

        User userData = pref.load();

        if(!pref.loadSearchUserQuery().equals("")){
            binding.pbSearchUser.setVisibility(View.VISIBLE);
            binding.llResultSearchUser.setVisibility(View.VISIBLE);
            binding.tvDisplayResultSearchUser.setText(pref.loadSearchUserQuery());
            binding.etSearchUser.getText().clear();
            loadSearchUserData(userData.getUserID(),pref.loadSearchUserQuery());
        }

        IClickableUser listener = new IClickableUser() {
            @Override
            public void onItemClick(User user) {
                //Toast.makeText(SearchUserActivity.this, user.getUsername(), Toast.LENGTH_SHORT).show();
                pref.saveSearchUser(user);
                gotoDetailUserActivity();
            }
        };

        initAdapter(listener);

        binding.ivBackToPreviousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUserActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.ivSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rvSearchUsers.setVisibility(View.GONE);
                binding.tvDisplayShowNoData.setVisibility(View.GONE);
                binding.llResultSearchUser.setVisibility(View.GONE);
                searchUserAdapter.clearData();
                searchUsers(userData.getUserID());
            }
        });
    }

    private void gotoDetailUserActivity(){
        Intent intent = new Intent(SearchUserActivity.this,DetailUserActivity.class);
        startActivity(intent);
    }

    private void initAdapter(IClickableUser listener){
        searchUserAdapter = new SearchUserAdapter(listener);
        binding.rvSearchUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchUsers.setAdapter(searchUserAdapter);
    }

    private void searchUsers(String userID){
        String searchUsers = binding.etSearchUser.getText().toString();
        if(searchUsers.equals("")){
            Toast.makeText(this, "Text field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            binding.pbSearchUser.setVisibility(View.VISIBLE);
            binding.llResultSearchUser.setVisibility(View.VISIBLE);
            binding.tvDisplayResultSearchUser.setText(searchUsers);
            binding.etSearchUser.getText().clear();
            pref.saveSearchUserQuery(searchUsers);
            loadSearchUserData(userID,searchUsers);
        }
    }

    private void loadSearchUserData(String userID, String searchUsers){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL,
                getSearchBody(userID, searchUsers),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        binding.pbSearchUser.setVisibility(View.GONE);
                        int status = 0;
                        try {
                            status = response.getInt("status");
                            Log.d("<RESUTLT>", "onResponse: " + status);
                            if(status == 200){
                                binding.rvSearchUsers.setVisibility(View.VISIBLE);

                                ArrayList<User> result = new ArrayList<>();
                                JSONArray listTodo = response.getJSONArray("data");

                                if(listTodo.length()!=0){
                                    for (int i = 0; i< listTodo.length();i++){

                                        JSONObject jsonObject = listTodo.getJSONObject(i);
                                        String UserID = jsonObject.getString("user_id");
                                        String username = jsonObject.getString("username");
                                        String name = jsonObject.getString("name");

                                        User user = new User();
                                        user.setUserID(userID);
                                        user.setUsername(username);
                                        user.setName(name);

                                        result.add(user);
                                        searchUserAdapter.updateData(result);
                                    }
                                }else{
                                    binding.tvDisplayShowNoData.setVisibility(View.VISIBLE);
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        binding.pbSearchUser.setVisibility(View.GONE);
                        Toast.makeText(SearchUserActivity.this,"user_id parameter is required" , Toast.LENGTH_SHORT).show();

                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private JSONObject getSearchBody(String userID,String searchUsers){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",userID);
            jsonObject.put("searchQuery",searchUsers);
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }
}