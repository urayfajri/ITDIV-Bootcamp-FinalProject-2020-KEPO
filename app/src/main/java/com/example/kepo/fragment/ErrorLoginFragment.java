package com.example.kepo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kepo.LoginActivity;
import com.example.kepo.R;
import com.example.kepo.session.SharedPref;

public class ErrorLoginFragment extends Fragment {

    private TextView tvErrorLoginMessage,tvOk;
    private SharedPref pref;

    public ErrorLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_error_login, container, false);

        tvErrorLoginMessage = view.findViewById(R.id.tv_message_error_login);
        tvOk = view.findViewById(R.id.tv_ok_error_login);

        pref = new SharedPref(getActivity());
        String getErrorMessage = pref.loadMessage();
        tvErrorLoginMessage.setText(getErrorMessage);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), LoginActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               getActivity().finish();
            }
        });

        return view;
    }
}