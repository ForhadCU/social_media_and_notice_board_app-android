package com.example.myproject.facadeDesignPattern;

import com.example.myproject.CreateGroupActivity;
import com.example.myproject.models.Groups;

import java.util.ArrayList;

public class GroupMaintainer {
  public static String groupCreatedConfirmation(CommandType ft) {
	  CreateGroupActivity createGroupActivity = new CreateGroupActivity();
	  IDbHandler dbHandlerInterface;
	  switch(ft) {
	   case PUBLIC:
	   		ArrayList<Groups> publicGroupIng;
		   dbHandlerInterface = (IDbHandler) new PublicGroup();
		   publicGroupIng = createGroupActivity.getPublicGroupIngredients();
//		   String publicGroupIngStr = createGroupActivity.getPublicGroupIngredients();
		   dbHandlerInterface.storeData(publicGroupIng);
		   return dbHandlerInterface.storeConfirmationMethod();
	   case PRIVATE:
	   		ArrayList<Groups> privateGroupIng;
		   dbHandlerInterface = new PrivateGroup();
		   privateGroupIng = createGroupActivity.getPrivateGroupIngredients();
//		   String privateGroupIngStr = createGroupActivity.getPrivateGroupIngredients();
		   dbHandlerInterface.storeData(privateGroupIng);
		   return dbHandlerInterface.storeConfirmationMethod();
//		   return String.valueOf(privateGroupIng.size());
		default:
			return null;
	  }
  }
}
