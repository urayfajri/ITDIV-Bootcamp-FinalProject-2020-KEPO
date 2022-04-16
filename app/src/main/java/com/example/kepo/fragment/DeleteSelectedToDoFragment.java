package com.example.kepo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kepo.MyToDoActivity;
import com.example.kepo.ProfileActivity;
import com.example.kepo.R;
import com.example.kepo.model.ToDo;
import com.example.kepo.model.User;
import com.example.kepo.session.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DeleteSelectedToDoFragment extends Fragment {


    private TextView tvYesDeleteTodo,tvNoDeleteTodo;
    private static final String BASE_URL = "https://it-division-kepo.herokuapp.com/user/";
    private SharedPref pref;

    public DeleteSelectedToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_selected_to_do, container, false);

        tvNoDeleteTodo= view.findViewById(R.id.tv_no_delete_todo);
        tvYesDeleteTodo = view.findViewById(R.id.tv_yes_delete_todo);

        pref = new SharedPref(getActivity());
        User user = pref.load();
        ArrayList<ToDo> arrayList = pref.loadListToDelete();

        tvNoDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyToDoActivity();
            }
        });

        tvYesDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemTodos(user.getUserID(),arrayList);
            }
        });
        return view;
    }

    private void goToMyToDoActivity(){
        Intent intent = new Intent(getActivity(), MyToDoActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void deleteItemTodos(String userID,ArrayList<ToDo> arrayList){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + userID + "/deleteTodo",
                getRequestBody(arrayList),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            if(message.equals("Delete todo success")){
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                goToMyToDoActivity();
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
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);
    }

    private JSONObject getRequestBody(ArrayList<ToDo> arrayList){
        try {

            JSONArray jsonArray = new JSONArray();

            for (int i=0; i< arrayList.size();i++){
                jsonArray.put((String) arrayList.get(i).getToDoID());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todos",jsonArray);

            return jsonObject;
        }catch (Exception e){
            return null;
        }
    }
}