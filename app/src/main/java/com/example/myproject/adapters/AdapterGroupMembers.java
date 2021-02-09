package com.example.myproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.example.myproject.interfaces.ICallbackRemoveMember;
import com.example.myproject.models.GroupMember;

import java.util.ArrayList;

public class AdapterGroupMembers extends RecyclerView.Adapter<AdapterGroupMembers.MyViewHolder> {
    private ArrayList<GroupMember> userArrayList;
    private ICallbackRemoveMember iCallbackRemoveMember;

    public AdapterGroupMembers(ArrayList<GroupMember> userArrayList, ICallbackRemoveMember iCallbackRemoveMember) {
        this.userArrayList = userArrayList;
        this.iCallbackRemoveMember = iCallbackRemoveMember;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_group_members, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GroupMember current = userArrayList.get(position);

        holder.textViewUserName.setText(current.getuName());

    }

    @Override
    public int getItemCount() {
        return userArrayList == null ? 0 : userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewUserName, textViewBtnRemove, textViewBtnRemoveSuccess;
        Context context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.tv_userName);
            textViewBtnRemove = itemView.findViewById(R.id.tvBtnRemoveMembers);
            textViewBtnRemoveSuccess = itemView.findViewById(R.id.tvBtnRemoveMembersSuccess);
            context = itemView.getContext();

            textViewBtnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            GroupMember current = userArrayList.get(getAdapterPosition());

            switch (view.getId()) {
                case R.id.tvBtnRemoveMembers:
//                    removeMember();
                    iCallbackRemoveMember.removeMember(current.getuId(), current.getgId());
                    textViewBtnRemove.setVisibility(View.GONE);
                    textViewBtnRemoveSuccess.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private void removeMember() {
            userArrayList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), userArrayList.size());
        }
    }


}
