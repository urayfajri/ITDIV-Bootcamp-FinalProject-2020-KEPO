package com.example.kepo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kepo.ProfileActivity;
import com.example.kepo.R;
import com.example.kepo.SplashActivity;
import com.example.kepo.session.SharedPref;


public class LogoutFragment extends Fragment {


    TextView tvYesLogout,tvNoLogout;
    SharedPref pref;
    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        tvNoLogout = view.findViewById(R.id.tv_no_logout);
        tvYesLogout = view.findViewById(R.id.tv_yes_logout);
        pref = new SharedPref(getActivity());

        tvNoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        tvYesLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;


    }
}