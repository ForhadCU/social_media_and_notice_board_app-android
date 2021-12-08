package com.example.myproject.applicationLayer.facadeDesignPattern;

import com.example.myproject.databaseLayer.models.Groups;

import java.util.ArrayList;

public interface IDbHandler {
	 void storeData(ArrayList<Groups> groupIng);
	 String storeConfirmationMethod();
}
