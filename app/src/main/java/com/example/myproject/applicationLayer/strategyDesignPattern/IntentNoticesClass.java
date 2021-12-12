package com.example.myproject.applicationLayer.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.presentationLayer.NoticesActivity;

public class IntentNoticesClass implements IStrategy {
    Context context;

    public IntentNoticesClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gDocId, String gName) {
        Intent intent = new Intent(context, NoticesActivity.class);
        intent.putExtra("gDocId", gDocId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
