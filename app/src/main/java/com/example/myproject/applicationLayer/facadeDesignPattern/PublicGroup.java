package com.example.myproject.applicationLayer.facadeDesignPattern;

import com.example.myproject.databaseLayer.models.Groups;

import java.util.ArrayList;

public class PublicGroup implements IDbHandler {
    private String preparedItem;
/*	public void storeData(ArrayList<String> ingredients) {
		// TODO Auto-generated method stub
		preparedItem = "Thin crust pasta with ingredients "+ingredients;

	}*/

	@Override
	public void storeData(ArrayList<Groups> groupIng) {

	}

	public String storeConfirmationMethod() {
		// TODO Auto-generated method stub
		return "This is from PrivateGroup";
	}

}
