package com.example.myproject.strategyDesignPattern;

import android.content.Context;
import android.content.Intent;

import com.example.myproject.NoticesActivity;
import com.example.myproject.strategyDesignPattern.IStrategy;

public class IntentNoticesClass implements IStrategy {
    Context context;

    public IntentNoticesClass(Context context) {
        this.context = context;
    }

    @Override
    public void mIntentData(String gId, String gName) {
        Intent intent = new Intent(context, NoticesActivity.class);
        intent.putExtra("gId", gId);
        intent.putExtra("gName", gName);

        context.startActivity(intent);
    }
}
