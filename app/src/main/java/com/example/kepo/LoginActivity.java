package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.kepo.databinding.ActivityLoginBinding;
import com.example.kepo.fragment.ErrorLoginFragment;
import com.example.kepo.fragment.FooterFragment;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String BASE_URL= "https://it-division-kepo.herokuapp.com/login";
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        binding.setUser(new User());
        pref = new SharedPref(this);
        pref.saveFooter("LoginActivity");

        User userData = pref.load();
        if(!userData.getUserID().equals("") && !userData.getUsername().equals("") && !userData.getPassword().equals("") && !userData.getName().equals("")){
            goToHomeActivity();
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FooterFragment());
        fragmentTransaction.commit();


    }

    private void login(){
        User user = binding.getUser();
        if(user.getUsername()== null || user.getPassword()==null ||user.getUsername().equals("") || user.getPassword().equals("") ){
            String message = "Please input username and password";
            pref.saveMessage(message);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ErrorLoginFragment());
            fragmentTransaction.commit();

        }
        else{
            //CONNECT TO SERVER
            binding.pbLogin.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    getLoginBody(user),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            binding.pbLogin.setVisibility(View.GONE);
                            try {
                                //String status = response.getString("status");
                                String message = response.getString("message");
                                //Log.d("<RESUTLT>", "onResponse: " + message);

                                if(message.equals("Username or password is incorrect")){
                                    //meghapus username dan password kembali
                                    pref.saveMessage(message);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, new ErrorLoginFragment());
                                    fragmentTransaction.commit();
                                    user.setUsername("");
                                    user.setPassword("");
                                }
                                else if(message.equals("Login success")){
                                    try {
                                        JSONObject data = response.getJSONObject("data");
                                        String userID = data.getString("user_id");
                                        String username = data.getString("username");
                                        String name = data.getString("name");


                                        user.setUserID(userID);
                                        user.setUsername(username);
                                        user.setName(name);

                                        pref.saveUser(user);
                                        goToHomeActivity();

                                    }catch (Exception e){
                                        e.printStackTrace();
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
                            binding.pbLogin.setVisibility(View.GONE);
                            error.printStackTrace();
                        }
                    }

            );
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(jsonObjectRequest);
        }
    }

    private JSONObject getLoginBody(User user){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",user.getUsername());
            jsonObject.put("password",user.getPassword());
            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}