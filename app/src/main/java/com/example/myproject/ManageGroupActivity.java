package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.adapters.Adapter_rvAllGroups;
import com.example.myproject.database.LocalDataHandler;
import com.example.myproject.interfaces.ICallbackRemoveGroups;
import com.example.myproject.models.Groups;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageGroupActivity extends AppCompatActivity implements View.OnClickListener, ICallbackRemoveGroups {
    private Toolbar toolbar;
    private Button buttonCreateGroup;
    private LocalDataHandler localDataHandler;
    private ArrayList<Groups> groupsArrayList;
    private RecyclerView recyclerView;
    private Adapter_rvAllGroups adapter_rvAllGroups;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uId = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceMyGroups = firebaseFirestore.collection("MY_GROUPS");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        buttonCreateGroup = findViewById(R.id.btn_createGroupIntent);
        recyclerView = findViewById(R.id.rv_groupList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        localDataHandler = new LocalDataHandler(this);
        groupsArrayList = new ArrayList<>();
        buttonCreateGroup.setOnClickListener(this);

        rvHandler();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showAllGroups();
    }

    private void showAllGroups() {
        getAllGroups();
    }

    private void rvHandler() {
        adapter_rvAllGroups = new Adapter_rvAllGroups(groupsArrayList, this);
        recyclerView.setAdapter(adapter_rvAllGroups);
//        adapter_rvAllGroups.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_createGroupIntent:
                startActivity(new Intent(ManageGroupActivity.this, CreateGroupActivity.class));
        }
    }

    private void getAllGroups() {
        mClear();
/*
        collectionReferenceMyGroups.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                    if (queryDocumentSnapshot != null && queryDocumentSnapshot.exists()) {
                        if (queryDocumentSnapshot.get("uId").equals(uId)) {
                            Groups groupsData = queryDocumentSnapshot.toObject(Groups.class);
                            groupsArrayList.add(groupsData);

                            adapter_rvAllGroups.notifyDataSetChanged();
                        }

                    } else
                        Log.d("Test", "onEvent: queryDocumentSnapshot is null");
                }
            }
        });
*/
        collectionReferenceMyGroups.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DocumentSnapshot documentSnapshot : task.getResult())
                    {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            if (documentSnapshot.get("uId").equals(uId)) {
                                Groups groupsData = documentSnapshot.toObject(Groups.class);
                                groupsArrayList.add(groupsData);

                                adapter_rvAllGroups.notifyDataSetChanged();
                            }

                        } else
                            Log.d("Test", "onEvent: queryDocumentSnapshot is null");
                    }
                    }
                }
        });
    }

    private void mClear() {
        if (groupsArrayList != null) {
            groupsArrayList.clear();
            if (adapter_rvAllGroups != null)
                adapter_rvAllGroups.notifyDataSetChanged();
        }
//        groupsArrayList = new ArrayList<>();
    }


    @Override
    public void removeGroup(final String gId) {
        collectionReferenceMyGroups.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (DocumentSnapshot documentSnapshot : task.getResult())
                    {
                        if (documentSnapshot.get("gId").toString().equals(gId))
                        {
                            collectionReferenceMyGroups.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ManageGroupActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
