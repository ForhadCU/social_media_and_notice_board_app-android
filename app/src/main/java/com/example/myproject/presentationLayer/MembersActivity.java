package com.example.myproject.presentationLayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.example.myproject.applicationLayer.adapters.AdapterGroupMembers;
import com.example.myproject.applicationLayer.interfaces.ICallbackRemoveMember;
import com.example.myproject.databaseLayer.models.Constants;
import com.example.myproject.databaseLayer.models.GroupMember;
import com.example.myproject.databaseLayer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class MembersActivity extends AppCompatActivity implements ICallbackRemoveMember {
    private Toolbar toolbar;
    private AdapterGroupMembers adapterGroupMembers;
    private RecyclerView recyclerViewGroupMembers;
    private ArrayList<GroupMember> groupMemberArrayList;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceGroupMembers = firebaseFirestore.collection("GROUP_MEMBERS");
    private CollectionReference collRefGroup = firebaseFirestore.collection(Constants.coll_groups);
    private CollectionReference collRefUsers = firebaseFirestore.collection(Constants.coll_users);

    private String gDocId;

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
        gDocId = getData.getStringExtra("gDocId");
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

        collRefGroup.document(gDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    ArrayList<String> gMemberList = new ArrayList<>();
                     gMemberList = (ArrayList<String>) Objects.requireNonNull(documentSnapshot).get("gMembers");
                     for (String s : gMemberList)
                     {
                         collRefUsers.document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                             @Override
                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                 if (task.isSuccessful())
                                 {
                                     GroupMember groupMember = Objects.requireNonNull(task.getResult()).toObject(GroupMember.class);
                                     groupMemberArrayList.add(groupMember);
                                     adapterGroupMembers.notifyDataSetChanged();
                                 }
                             }
                         });
                     }

                    Log.d("CRUD", "onComplete: gMemberList size: "+gMemberList.size());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("CRUD", "onFailure: failed to read group");
            }
        });

/*
        collectionReferenceGroupMembers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("gId").toString().equals(gDocId)) {
                            GroupMember groupMember = documentSnapshot.toObject(GroupMember.class);
                            groupMemberArrayList.add(groupMember);
                            adapterGroupMembers.notifyDataSetChanged();
                        }

                    }
                }
            }
        });
*/
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

        collRefGroup.document(gDocId).update(Constants.gMembers, FieldValue.arrayRemove(uId));

    /*addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        });*/
    }
}