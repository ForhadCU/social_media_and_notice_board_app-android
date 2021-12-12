package com.example.myproject.applicationLayer.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.presentationLayer.AllUsersActivity;

public class IntentAddMembersClass implements IStrategy{
    Context context;

    public IntentAddMembersClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gDocId, String gName) {
        Intent intent = new Intent(context, AllUsersActivity.class);
        intent.putExtra("gDocId", gDocId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
