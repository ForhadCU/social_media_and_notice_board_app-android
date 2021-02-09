package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.adapters.AdapterGroupMembers;
import com.example.myproject.interfaces.ICallbackRemoveMember;
import com.example.myproject.models.GroupMember;
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

public class MembersActivity extends AppCompatActivity implements ICallbackRemoveMember {
    private Toolbar toolbar;
    private AdapterGroupMembers adapterGroupMembers;
    private RecyclerView recyclerViewGroupMembers;
    private ArrayList<GroupMember> groupMemberArrayList;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceGroupMembers = firebaseFirestore.collection("GROUP_MEMBERS");

    private String currentGId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        this.setTitle("Group Members");

        intentHandler();

        recyclerViewGroupMembers = findViewById(R.id.rv_groupMembers);
        recyclerViewGroupMembers.setHasFixedSize(true);
        recyclerViewGroupMembers.setLayoutManager(new LinearLayoutManager(this));
        groupMemberArrayList = new ArrayList<>();
        adapterGroupMembers = new AdapterGroupMembers(groupMemberArrayList, this);
        recyclerViewGroupMembers.setAdapter(adapterGroupMembers);
//        adapterGroupMembers.notifyDataSetChanged();
    }

    private void intentHandler() {
        Intent getData = getIntent();
        currentGId = getData.getStringExtra("gId");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShowAllMembers();
    }

    private void mShowAllMembers() {
        mClear();

/*
        collectionReferenceGroupMembers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : value)
                {
                    if (queryDocumentSnapshot.get("gId").toString().equals(currentGId)){
                        GroupMember groupMember = queryDocumentSnapshot.toObject(GroupMember.class);
                        groupMemberArrayList.add(groupMember);
                        adapterGroupMembers.notifyDataSetChanged();
                    }
                }
            }
        });
*/

        collectionReferenceGroupMembers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("gId").toString().equals(currentGId)) {
                            GroupMember groupMember = documentSnapshot.toObject(GroupMember.class);
                            groupMemberArrayList.add(groupMember);
                            adapterGroupMembers.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
    }

    private void mClear() {
        if (groupMemberArrayList != null) {
            groupMemberArrayList.clear();
            if (adapterGroupMembers != null)
                adapterGroupMembers.notifyDataSetChanged();
        }
    }

    @Override
    public void removeMember(final String uId, final String gId) {

        collectionReferenceGroupMembers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("uId").toString().equals(uId) && documentSnapshot.get("gId").toString().equals(gId)) {
                            collectionReferenceGroupMembers.document(documentSnapshot.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MembersActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}