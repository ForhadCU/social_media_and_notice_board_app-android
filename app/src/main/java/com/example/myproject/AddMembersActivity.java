package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.adapters.AdapterAllUsers;
import com.example.myproject.interfaces.ICallbackAddMembers;
import com.example.myproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMembersActivity extends AppCompatActivity implements ICallbackAddMembers {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReferenceAllUsers = firebaseFirestore.collection("Users");
    private final CollectionReference collectionReferenceGroupMembers = firebaseFirestore.collection("GROUP_MEMBERS");
    private AdapterAllUsers adapterAllUsers;
    private ArrayList<User> userArrayList;
    private String gId, gName;

    private Toolbar toolbar;
    private RecyclerView recyclerViewAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_members);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        this.setTitle("Add member");
        Intent intentGet = getIntent();
        gId = intentGet.getStringExtra("gId");
        gName = intentGet.getStringExtra("gName");

        recyclerViewAllUsers = findViewById(R.id.rv_allUserID);
        recyclerViewAllUsers.setHasFixedSize(true);
        recyclerViewAllUsers.setLayoutManager(new LinearLayoutManager(this));
        userArrayList = new ArrayList<>();

        rvHandler();
    }

    private void rvHandler() {
        adapterAllUsers = new AdapterAllUsers(userArrayList, this);
        recyclerViewAllUsers.setAdapter(adapterAllUsers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mShowAllUsers();
    }

    private void mShowAllUsers() {
        collectionReferenceAllUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot documentSnapshot : value)
                {
                    if (documentSnapshot != null && documentSnapshot.exists())
                    {
                        User user = documentSnapshot.toObject(User.class);
                        userArrayList.add(user);
                        adapterAllUsers.notifyDataSetChanged();
                    } else
                        Toast.makeText(AddMembersActivity.this, "Empty", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void addMembers(final String uId, final String uName, boolean add) {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("uId", uId);
        tempMap.put("uName", uName);
        tempMap.put("gId", gId);
        tempMap.put("gName", gName);
//        tempMap.put()

        if (add)
        {
            collectionReferenceGroupMembers.add(tempMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(AddMembersActivity.this, "Added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMembersActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else
        {
            collectionReferenceGroupMembers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if (documentSnapshot.get("uId").toString().equals(uId) && documentSnapshot.get("gId").equals(gId))
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
        }
    }
}