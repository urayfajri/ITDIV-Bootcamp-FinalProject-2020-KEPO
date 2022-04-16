package com.example.kepo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.ResultSearchTodoListLayoutBinding;
import com.example.kepo.databinding.UserTodoListLayoutBinding;
import com.example.kepo.interfaces.IClickableTodo;
import com.example.kepo.model.ToDo;

import java.util.ArrayList;

public class SearchToDoAdapter extends RecyclerView.Adapter<SearchToDoAdapter.SearchToDoViewHolder>{

    private ArrayList<ToDo> searchTodos;
    private IClickableTodo listener;

    public SearchToDoAdapter(IClickableTodo listener){

        this.searchTodos = new ArrayList<>();
        this.listener = listener;

    }

    @NonNull
    @Override
    public SearchToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ResultSearchTodoListLayoutBinding itemLayoutBinding = ResultSearchTodoListLayoutBinding.inflate(layoutInflater,parent,false);
        return new SearchToDoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchToDoViewHolder holder, int position) {
        ToDo searchTodo = searchTodos.get(position);
        holder.itemLayoutBinding.setTodo(searchTodo);

        holder.itemLayoutBinding.rlResultToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(searchTodo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchTodos.size();
    }

    public void updateData(ArrayList<ToDo> newSearchTodos){
        searchTodos.clear();
        searchTodos.addAll(newSearchTodos);
        notifyDataSetChanged();
    }

    public void clearData(){
        searchTodos.clear();
        notifyDataSetChanged();
    }


    class SearchToDoViewHolder extends RecyclerView.ViewHolder{

        private ResultSearchTodoListLayoutBinding itemLayoutBinding;

        public SearchToDoViewHolder(@NonNull ResultSearchTodoListLayoutBinding itemLayoutBinding){
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }
    }
}
