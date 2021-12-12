package com.example.myproject.applicationLayer.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.presentationLayer.CreateNoticeActivity;

public class IntentCreateNoticeClass implements IStrategy{
    Context context;

    public IntentCreateNoticeClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gDocId, String gName) {
        Intent intent = new Intent(context, CreateNoticeActivity.class);
        intent.putExtra("gDocId", gDocId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }

}
