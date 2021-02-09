package com.example.myproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.example.myproject.interfaces.ICallbackAddMembers;
import com.example.myproject.models.User;

import java.util.ArrayList;

public class AdapterAllUsers extends RecyclerView.Adapter<AdapterAllUsers.MyViewHolder> {
    private ArrayList<User> userArrayList;
    private ICallbackAddMembers iCallbackAddMembers;

    public AdapterAllUsers(ArrayList<User> userArrayList, ICallbackAddMembers iCallbackAddMembers) {
        this.userArrayList = userArrayList;
        this.iCallbackAddMembers = iCallbackAddMembers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_all_users_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User current = userArrayList.get(position);

        holder.textViewUserName.setText(current.getUsername());
        holder.textViewUserEmail.setText(current.getEmail());


    }

    @Override
    public int getItemCount() {
        return userArrayList == null ? 0 : userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewUserName, textViewUserEmail;
        TextView textViewAddButton, textViewAddedButton ;
        Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserEmail = itemView.findViewById(R.id.tv_userEmail);
            textViewUserName = itemView.findViewById(R.id.tv_userName);
            textViewAddButton = itemView.findViewById(R.id.tvBtnInviteMembers);
            textViewAddedButton = itemView.findViewById(R.id.tvBtnInvitedSuccess);
            context = itemView.getContext();

            textViewAddButton.setOnClickListener(this);
            textViewAddedButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User current = userArrayList.get(getAdapterPosition());
            switch (view.getId())
            {
                case R.id.tvBtnInviteMembers:
                    textViewAddButton.setVisibility(View.GONE);
                    textViewAddedButton.setVisibility(View.VISIBLE);
                    iCallbackAddMembers.addMembers(current.getUserId(), current.getUsername(), true);
                    break;

                case R.id.tvBtnInvitedSuccess:
                    textViewAddButton.setVisibility(View.VISIBLE);
                    textViewAddedButton.setVisibility(View.GONE);
                    iCallbackAddMembers.addMembers(current.getUserId(), current.getUsername(), false);
                    break;
            }
        }
    }
}
