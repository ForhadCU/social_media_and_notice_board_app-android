package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.adapters.MyAdapter_rvAllpost;
import com.example.myproject.models.Data_Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NoticesActivity extends AppCompatActivity {
    private String gId, gName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistener;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("Notifly_Post_Collection");
    private Data_Handler data_handler;
    private ArrayList<Data_Handler> dataHandlerList;
    private MyAdapter_rvAllpost myAdapter_rvAllpost;
    private RecyclerView recyclerView;
    ;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        Intent intentFromManageGroupActivity = getIntent();
        gId = intentFromManageGroupActivity.getStringExtra("gId");
        gName = intentFromManageGroupActivity.getStringExtra("gName");
        this.setTitle("Notices");

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.rv_allPostID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShowAllPosts();
    }

    private void mShowAllPosts() {
        mClearAll();
/*        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                String data = "";
                dataHandlerList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {

                    Data_Handler list = documentSnapshot.toObject(Data_Handler.class);
                    dataHandlerList.add(list);

                }
                myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList, getApplicationContext());
                recyclerView.setAdapter(myAdapter_rvAllpost);
//                showpostTextView.setText(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.toString());
            }
        }); *///it's also work

        //very Real Time
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                dataHandlerList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.get("currentUid").equals(mAuth.getCurrentUser().getUid()) && documentSnapshot.get("gId").equals(gId)) {
                        Data_Handler data_handler = documentSnapshot.toObject(Data_Handler.class);
                        dataHandlerList.add(data_handler);
                        myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList, getApplicationContext());
                        recyclerView.setAdapter(myAdapter_rvAllpost);
                        myAdapter_rvAllpost.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void mClearAll() {
        if (dataHandlerList != null) {
            dataHandlerList.clear();
            if (myAdapter_rvAllpost != null)
                myAdapter_rvAllpost.notifyDataSetChanged();
        }
        dataHandlerList = new ArrayList<>();
    }
}

