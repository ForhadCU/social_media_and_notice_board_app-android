package com.example.myproject.presentationLayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myproject.R;
import com.example.myproject.applicationLayer.facadeDesignPattern.CommandType;
import com.example.myproject.applicationLayer.facadeDesignPattern.GroupMaintainer;
import com.example.myproject.applicationLayer.services.CRUD_firebase;
import com.example.myproject.databaseLayer.database.LocalDataHandler;
import com.example.myproject.databaseLayer.models.Constants;
import com.example.myproject.databaseLayer.models.Groups;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private EditText editTextGroupName, editTextGroupID, editTextGroupDesc;
    private Button buttonCreateGroup;
    private LocalDataHandler testLd;
    ManageGroupActivity manageGroupActivity = new ManageGroupActivity();
    private Spinner spinnerGroupSec;

    private String groupSec;
    private String groupId, groupName, groupDesc;
    private ArrayList<String> ingredientsList = new ArrayList<>();
    private ArrayList<Groups> groupsArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference myGroupsRef = firebaseFirestore.collection(Constants.coll_groups);
    private CollectionReference collRefUsers = firebaseFirestore.collection(Constants.coll_users);
    private String gDocId;
    private String adminUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        this.setTitle("Create Group");

        mSetToolbar();
        testLd = new LocalDataHandler(this);
        mBind();
        mInit();
        spinnerHandler();
        mSetOnclick();
    }

    private void mInit() {
        adminUid = mAuth.getCurrentUser().getUid();
    }

    private void mSetOnclick() {
        buttonCreateGroup.setOnClickListener(this);
        spinnerGroupSec.setOnItemSelectedListener(this);
    }

    private void mBind() {
        editTextGroupName = findViewById(R.id.edtTxt_groupName);
        editTextGroupID = findViewById(R.id.edtTxt_groupID);
        editTextGroupDesc = findViewById(R.id.edtTxt_groupDesc);
        buttonCreateGroup = findViewById(R.id.btn_createGroup);
        spinnerGroupSec = findViewById(R.id.spinner_groupSec);
    }

    private void mSetToolbar() {
        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void spinnerHandler() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.groupSecure,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupSec.setAdapter(adapter);
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
            case R.id.btn_createGroup:
                groupId = editTextGroupID.getText().toString();
                groupName = editTextGroupName.getText().toString();
                groupDesc = editTextGroupDesc.getText().toString();
                if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(groupDesc)) {

//                    long insertID = localDataHandler.mAddGroupDetails(groupId, groupName , groupDesc);
                    mAddGroupDetailsOnline(groupId, groupName, groupDesc);

                    if (groupSec.equals("private")) {
                        String confirmation = GroupMaintainer.groupCreatedConfirmation(CommandType.PRIVATE);
                        assert confirmation != null;
                        if (confirmation.equals("Save")) {
                            Intent intent = new Intent(CreateGroupActivity.this, ManageGroupActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            this.finish();
                        } else
                            Toast.makeText(this, confirmation, Toast.LENGTH_SHORT).show();
                    } else if (groupSec.equals("public")) {
                        String confirmation = GroupMaintainer.groupCreatedConfirmation(CommandType.PUBLIC);
                        assert confirmation != null;
                        if (confirmation.equals("Save")) {
                            Intent intent = new Intent(CreateGroupActivity.this, ManageGroupActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            this.finish();
                        } else
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    go();
                } else {
                    Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
                }
                check();


        }
    }

    private void check() {
        testLd.mAddGroupDetails2(groupId, groupName, groupDesc);
    }

    private void go() {
        Intent intent = new Intent(CreateGroupActivity.this, ManageGroupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        groupSec = (String) adapterView.getItemAtPosition(i);
//        Toast.makeText(manageGroupActivity, groupSec, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public ArrayList<Groups> getPublicGroupIngredients() {
        groupsArrayList = new ArrayList<>();
        groupsArrayList = testLd.showAllGroups2();

        return groupsArrayList;
    }

    public ArrayList<Groups> getPrivateGroupIngredients() {
        groupsArrayList = new ArrayList<>();
        groupsArrayList = testLd.showAllGroups2();

        return groupsArrayList;
    }

    public void mAddGroupDetailsOnline(String groupID, String groupName, String groupDesc) {
        ArrayList<String> gMembers = new ArrayList<>();
        ArrayList<String> gNotices = new ArrayList<>();


        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("gId", groupID);
        groupMap.put("gName", groupName);
        groupMap.put("gDesc", groupDesc);
        groupMap.put("adminUid", adminUid);
        groupMap.put("gMembers", gMembers);
        groupMap.put("gNotices", gNotices);

        myGroupsRef.add(groupMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                //save gDocId into this admin's User-data.
//                gDocId = documentReference.getId();
                mUpdateUser(documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void mUpdateUser(final String gDocId) {
        collRefUsers.document(adminUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    CRUD_firebase.mUpdateArray(
                            collRefUsers,
                            documentSnapshot.getId(),
                            Constants.my_groups,
                            gDocId);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.toString());

            }
        });

/*
        collRefUsers.document(adminUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        if (documentSnapshot.get(Constants.userId).toString().equals(adminUid)) {

                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
*/

    }
}
