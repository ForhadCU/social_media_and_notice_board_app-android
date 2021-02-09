package com.example.myproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.example.myproject.models.Data_Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import com.example.allinoneproject.Interfaces.IMainActivityCallback;

public class MyAdapter_rvAllpost extends RecyclerView.Adapter<MyAdapter_rvAllpost.MyViewHolder> {
    private ArrayList<Data_Handler> dataHandlerList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public MyAdapter_rvAllpost(ArrayList<Data_Handler> dataHandlerList, Context context) {
        this.dataHandlerList = dataHandlerList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_allpost_custm_lay, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data_Handler current = dataHandlerList.get(position);
        holder.vTitle.setText(current.getPostTitle());
        holder.vDesc.setText(current.getDesc());
        Picasso.get().load(current.getImageUri()).into(holder.vImage);
        holder.vGroupName.setText(current.getgName());
    }

    @Override
    public int getItemCount() {
        return dataHandlerList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vCurrentUserName, vDesc, vTitle, vGroupName;
        ImageView vImage;
        ImageButton imageButtonLike, imageButtonComment, imageButtonDownload;
        Context currentContext;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.titleID);
            vDesc = itemView.findViewById(R.id.descID);
            vImage = itemView.findViewById(R.id.imgViewID);
            imageButtonLike = itemView.findViewById(R.id.imgBtnLike);
            imageButtonComment = itemView.findViewById(R.id.imgBtnComment);
            imageButtonDownload = itemView.findViewById(R.id.imgBtnDownload);
            vGroupName = itemView.findViewById(R.id.tv_rv_noticeList_groupName);
            currentContext = itemView.getContext();

            imageButtonLike.setOnClickListener(this);
            imageButtonComment.setOnClickListener(this);
            imageButtonDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
 /*           switch (v.getId()) {
                case R.id.imgBtn_deletePostID:
                        mDelete(getAdapterPosition());
                        DocumentReference documentReference = firestoreDB.collection("Post_Collection").document(currentItemDocId);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                    break;

                case R.id.imgBtn_editPostId:
                        String currentItemDesc = vDesc.getText().toString();
                        callback.iEditPostDesc(currentItemDesc, currentItemDocId);
                    break;
            }*/
            switch (v.getId()) {
                case R.id.imgBtnLike:
                    Toast.makeText(currentContext, "Like", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.imgBtnComment:
                    Toast.makeText(currentContext, "Comment", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.imgBtnDownload:
                    Toast.makeText(currentContext, "Download", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void mDelete(int adapterPosition) {
        dataHandlerList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
