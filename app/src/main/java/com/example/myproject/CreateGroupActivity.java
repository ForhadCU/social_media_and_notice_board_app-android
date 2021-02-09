package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.myproject.database.LocalDataHandler;
import com.example.myproject.facadeDesignPattern.CommandType;
import com.example.myproject.facadeDesignPattern.GroupMaintainer;
import com.example.myproject.models.Groups;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private CollectionReference myGroupsRef = firebaseFirestore.collection("MY_GROUPS");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        testLd = new LocalDataHandler(this);


        editTextGroupName = findViewById(R.id.edtTxt_groupName);
        editTextGroupID = findViewById(R.id.edtTxt_groupID);
        editTextGroupDesc = findViewById(R.id.edtTxt_groupDesc);
        buttonCreateGroup = findViewById(R.id.btn_createGroup);
        spinnerGroupSec = findViewById(R.id.spinner_groupSec);


        spinnerHandler();

        buttonCreateGroup.setOnClickListener(this);
        spinnerGroupSec.setOnItemSelectedListener(this);


    }

    private void spinnerHandler() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groupSecure, android.R.layout.simple_spinner_dropdown_item);
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
                check();
                if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(groupDesc)) {

//                    long insertID = localDataHandler.mAddGroupDetails(groupId, groupName , groupDesc);
                    mAddGroupDetailsOnline(groupId, groupName, groupDesc, mAuth.getCurrentUser().getUid());

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

    public void mAddGroupDetailsOnline(String groupID, String groupName, String groupDesc, String uId) {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("gId", groupID);
        groupMap.put("gName", groupName);
        groupMap.put("gDesc", groupDesc);
        groupMap.put("uId", uId);

        myGroupsRef.add(groupMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
