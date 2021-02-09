package com.example.myproject.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.MembersActivity;
import com.example.myproject.NoticesActivity;

public class IntentGroupMembersClass implements IStrategy{
    Context context;

    public IntentGroupMembersClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gId, String gName) {
        Intent intent = new Intent(context, MembersActivity.class);
        intent.putExtra("gId", gId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
