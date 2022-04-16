package com.example.kepo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kepo.R;
import com.example.kepo.session.SharedPref;


public class FooterFragment extends Fragment {

    private TextView tvFooter;
    private SharedPref pref;

    public FooterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_footer, container, false);

        tvFooter = view.findViewById(R.id.tv_footer);

        pref = new SharedPref(getActivity());
        String footer = pref.loadFooter();

        if(footer.equals("HomeActivity")){
            tvFooter.setBackgroundColor(getResources().getColor(R.color.RoyalBlue));
            tvFooter.setTextColor(getResources().getColor(R.color.white));
        }

        return view;
    }
}