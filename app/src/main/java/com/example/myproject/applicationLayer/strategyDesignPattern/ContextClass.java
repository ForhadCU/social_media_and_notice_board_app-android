package com.example.myproject.applicationLayer.strategyDesignPattern;

public class ContextClass {
    private IStrategy iStrategy;

    public ContextClass(IStrategy iStrategy) {
        this.iStrategy = iStrategy;
    }

    public void mExecuteStrategy(String gId, String gName)
    {
        iStrategy.mIntentData(gId, gName);
    }
}
