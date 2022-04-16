package com.example.kepo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.UserTodoListLayoutBinding;
import com.example.kepo.interfaces.IClickableTodo;
import com.example.kepo.interfaces.QuantityListener;
import com.example.kepo.model.ToDo;
import com.example.kepo.session.SharedPref;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>{

    private ArrayList<ToDo> todos;
    private IClickableTodo listener;
    private Context context;
    QuantityListener quantityListener;

    private ArrayList<ToDo> arrayList_0 = new ArrayList<>();

    public ToDoAdapter(Context context,IClickableTodo listener, QuantityListener quantityListener)
    {
        this.context = context;
        this.todos = new ArrayList<>();
        this.listener = listener;
        this.quantityListener = quantityListener;

    }
    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        UserTodoListLayoutBinding itemLayoutBinding = UserTodoListLayoutBinding.inflate(layoutInflater,parent,false);
        return new ToDoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {

        ToDo toDo = todos.get(position);
        holder.itemLayoutBinding.setTodo(toDo);

        holder.itemLayoutBinding.rlMyToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(toDo);
            }

        });

        holder.itemLayoutBinding.cbTodoUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (toDo.isChecked()){
                    arrayList_0.add(todos.get(position));
                }
                else{
                    arrayList_0.remove(todos.get(position));
                }
                quantityListener.onQuantityChange(arrayList_0);

            }


        });
    }

    public void clearData(){
        todos.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void updateData(ArrayList<ToDo> newTodos){
        todos.clear();
        todos.addAll(newTodos);
        notifyDataSetChanged();
    }


    class ToDoViewHolder extends RecyclerView.ViewHolder{

        private UserTodoListLayoutBinding itemLayoutBinding;

        public ToDoViewHolder(@NonNull UserTodoListLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;

        }

    }
}
