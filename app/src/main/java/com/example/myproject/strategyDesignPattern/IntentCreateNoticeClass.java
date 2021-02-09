package com.example.myproject.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.CreateGroupActivity;
import com.example.myproject.NoticesActivity;
import com.example.myproject.PostActivity;

public class IntentCreateNoticeClass implements IStrategy{
    Context context;

    public IntentCreateNoticeClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gId, String gName) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("gId", gId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }

}
