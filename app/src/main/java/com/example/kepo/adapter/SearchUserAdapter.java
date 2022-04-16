package com.example.kepo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.databinding.ResultSearchUserListLayoutBinding;
import com.example.kepo.interfaces.IClickableUser;
import com.example.kepo.model.User;

import java.util.ArrayList;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>{

    private ArrayList<User> searchUsers;
    private IClickableUser listener;

    public SearchUserAdapter(IClickableUser listener){
        searchUsers = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ResultSearchUserListLayoutBinding itemLayoutBinding = ResultSearchUserListLayoutBinding.inflate(layoutInflater,parent,false);
        return new SearchUserViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
            User users = searchUsers.get(position);
            holder.itemLayoutBinding.setUser(users);

            holder.itemLayoutBinding.rlResultUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(users);
                }
            });
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }

    public void updateData(ArrayList<User> newSearchUsers){
        searchUsers.clear();
        searchUsers.addAll(newSearchUsers);
        notifyDataSetChanged();
    }

    public void clearData(){
        searchUsers.clear();
        notifyDataSetChanged();
    }

    class SearchUserViewHolder extends RecyclerView.ViewHolder{

        private ResultSearchUserListLayoutBinding itemLayoutBinding;

        public SearchUserViewHolder(@NonNull ResultSearchUserListLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }
    }
}
