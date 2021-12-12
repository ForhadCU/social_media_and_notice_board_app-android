package com.example.myproject.applicationLayer.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.presentationLayer.MembersActivity;

public class IntentGroupMembersClass implements IStrategy{
    Context context;

    public IntentGroupMembersClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gDocId, String gName) {
        Intent intent = new Intent(context, MembersActivity.class);
        intent.putExtra("gDocId", gDocId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
