package com.example.myproject.facadeDesignPattern;

import com.example.myproject.models.Groups;

import java.util.ArrayList;

public interface IDbHandler {
	 void storeData(ArrayList<Groups> groupIng);
	 String storeConfirmationMethod();
}
