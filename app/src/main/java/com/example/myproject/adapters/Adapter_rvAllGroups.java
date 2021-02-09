package com.example.myproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.strategyDesignPattern.GroupActivity;
import com.example.myproject.R;
import com.example.myproject.interfaces.ICallbackRemoveGroups;
import com.example.myproject.models.Groups;

import java.util.ArrayList;

public class Adapter_rvAllGroups extends RecyclerView.Adapter<Adapter_rvAllGroups.MyViewModel> {
    private ArrayList<Groups> groupsList;
    private ICallbackRemoveGroups iCallbackRemoveGroups;

    public Adapter_rvAllGroups(ArrayList<Groups> groupsList, ICallbackRemoveGroups iCallbackRemoveGroups) {
        this.groupsList = groupsList;
        this.iCallbackRemoveGroups = iCallbackRemoveGroups;
    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_group_list_layout, parent, false);
        MyViewModel myViewModel = new MyViewModel(view);
        return myViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {
        Groups current = groupsList.get(position);
        holder.textViewGroupName.setText(current.getgName());
        holder.textViewGroupID.setText(current.getgId());
    }

    @Override
    public int getItemCount() {
        return groupsList == null ? 0: groupsList.size();
    }

    public class MyViewModel extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewGroupName, textViewGroupID, textViewRemoveGroup;
        private Context contextThis;
        public MyViewModel(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.tv_rv_grpList_groupName);
            textViewGroupID = itemView.findViewById(R.id.tv_rv_grpList_groupId);
            textViewRemoveGroup = itemView.findViewById(R.id.tvBtnRemoveGroup);
            contextThis = itemView.getContext();

            itemView.setOnClickListener(this);
            textViewRemoveGroup.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Groups current = groupsList.get(getAdapterPosition());
            if (view.getId() == R.id.tvBtnRemoveGroup)
            {
                remove();
                iCallbackRemoveGroups.removeGroup(current.getgId());
            }
            else {
                String gId = textViewGroupID.getText().toString();
                String gName = textViewGroupName.getText().toString();

                Intent intentGroupActivity = new Intent(contextThis, GroupActivity.class);
                intentGroupActivity.putExtra("gId", gId);
                intentGroupActivity.putExtra("gName", gName);
                contextThis.startActivity(intentGroupActivity);
            }
        }

        private void remove() {
            groupsList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), groupsList.size());
        }
    }
}
