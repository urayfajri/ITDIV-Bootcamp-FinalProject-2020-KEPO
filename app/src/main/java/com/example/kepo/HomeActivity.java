package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kepo.databinding.ActivityHomeBinding;
import com.example.kepo.fragment.FooterFragment;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        pref = new SharedPref(this);
        pref.saveFooter("HomeActivity");
        User userData = pref.load();


        if(userData.getUsername().equals("") && userData.getPassword().equals("")){
            Intent intent = new Intent(HomeActivity.this,SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else{

            binding.setUser(userData);
            binding.rlMyTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,MyToDoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            binding.rlSearchTodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pref.saveSearchTodo("");
                    pref.saveCheckByUser(0);
                    pref.saveCheckByTodo(0);
                    Intent intent = new Intent(HomeActivity.this,SearchToDoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            binding.rlSearchUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pref.saveSearchUserQuery("");
                    Intent intent = new Intent(HomeActivity.this,SearchUserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            binding.rlProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new FooterFragment());
            fragmentTransaction.commit();
        }
    }
}