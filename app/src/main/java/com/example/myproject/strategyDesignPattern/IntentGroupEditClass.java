package com.example.myproject.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.EditGroupActivity;
import com.example.myproject.NoticesActivity;

public class IntentGroupEditClass implements IStrategy{
    Context context;

    public IntentGroupEditClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gId, String gName) {
        Intent intent = new Intent(context, EditGroupActivity.class);
        intent.putExtra("gId", gId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
