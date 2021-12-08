package com.example.myproject.applicationLayer.facadeDesignPattern;

import androidx.annotation.NonNull;

import com.example.myproject.databaseLayer.database.LocalDataHandler;
import com.example.myproject.databaseLayer.models.Groups;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrivateGroup implements IDbHandler {
    long insertID;
    String groupId;
    String groupName;
    String groupDesc;
    ArrayList<Groups> groupIng;
    LocalDataHandler localDataHandler;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference myGroupsRef = firebaseFirestore.collection("MY_GROUPS");


    @Override
    public void storeData(ArrayList<Groups> groupIng) {
        this.groupIng = groupIng;
        if (groupIng.size()>0){
            for (int i = 0; i < groupIng.size(); i++) {
                Groups current = groupIng.get(i);
                insertID = localDataHandler.mAddGroupDetails(current.getgId(), current.getgName(), current.getgDesc());
            }
        }
    }

    public String storeConfirmationMethod() {
        // TODO Auto-generated method stub
        if (insertID > 0)
            return "Save";
        else
            return "Failed";

    }

    public void mAddGroupDetailsOnline(String groupID, String groupName, String groupDesc, String uId)
    {
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
