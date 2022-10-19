package com.example.myproject.presentationLayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myproject.R;
import com.example.myproject.applicationLayer.adapters.AdapterAllUsers;
import com.example.myproject.applicationLayer.interfaces.ICallbackAddMembers;
import com.example.myproject.databaseLayer.models.Constants;
import com.example.myproject.databaseLayer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllUsersActivity extends AppCompatActivity implements ICallbackAddMembers {
    private static final String TAG = "CRUD";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReferenceAllUsers = firebaseFirestore.collection(Constants.coll_users);
    private final CollectionReference collRefGroup = firebaseFirestore.collection(Constants.coll_groups);
    private final CollectionReference collectionReferenceGroupMembers = firebaseFirestore.collection("GROUP_MEMBERS");
    private AdapterAllUsers adapterAllUsers;
    private ArrayList<User> userArrayList;
    private ArrayList<User> userArrayList_temp;
    private String gDocId, gName, adminUid;

    private Toolbar toolbar;
    private RecyclerView recyclerViewAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        this.setTitle("All Users");
        Intent intentGet = getIntent();
        gDocId = intentGet.getStringExtra("gDocId");
        gName = intentGet.getStringExtra("gName");
        adminUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        recyclerViewAllUsers = findViewById(R.id.rv_allUserID);
        recyclerViewAllUsers.setHasFixedSize(true);
        recyclerViewAllUsers.setLayoutManager(new LinearLayoutManager(this));
        userArrayList = new ArrayList<>();
        rvHandler();


    }

    private void rvHandler() {

        adapterAllUsers = new AdapterAllUsers(userArrayList, this, gDocId);
        recyclerViewAllUsers.setAdapter(adapterAllUsers);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        mShowAllUsers();
        mShowAllUsers();
    }

    private void mShowAllUsers() {

        mClear();
        collectionReferenceAllUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot documentSnapshot : value)
                {
                    if (documentSnapshot != null && documentSnapshot.exists() && !documentSnapshot.getId().toString().equals(adminUid)) {

                        User user = documentSnapshot.toObject(User.class);
                        userArrayList.add(user);
//                        adapterAllUsers.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void mClear() {
        if (userArrayList != null) {
            userArrayList.clear();
            if (adapterAllUsers != null){}
//                adapterAllUsers.notifyDataSetChanged();
        }
    }


    @Override
    public void addMembers(final String uId, final String uName, boolean add) {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("uId", uId);
        tempMap.put("uName", uName);
        tempMap.put("gId", gDocId);
        tempMap.put("gName", gName);
//        tempMap.put()


        if (add)
        {
            collRefGroup.document(gDocId).update(Constants.gMembers, FieldValue.arrayUnion(uId)).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            collectionReferenceAllUsers.document(uId).update(Constants.joined_groups, FieldValue.arrayUnion(gDocId));
                            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+ e.toString());
                }
            });
            /*collectionReferenceGroupMembers.add(tempMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AddMembersActivity.this, "Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMembersActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });*/

        } else {
            collRefGroup.document(gDocId).update(Constants.gMembers, FieldValue.arrayRemove(uId)).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            collectionReferenceAllUsers.document(uId).update(Constants.joined_groups, FieldValue.arrayRemove(gDocId));
                            Toast.makeText(getApplicationContext(), "Undo", Toast.LENGTH_SHORT).show();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+ e.toString());
                }
            });

/*
            collectionReferenceGroupMembers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if (documentSnapshot.get("uId").toString().equals(uId) && documentSnapshot.get("gId").equals(gDocId))
                                {
                                    collectionReferenceGroupMembers.document(documentSnapshot.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddMembersActivity.this, "Undo", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    }
                }
            });
*/

        }
    }
}