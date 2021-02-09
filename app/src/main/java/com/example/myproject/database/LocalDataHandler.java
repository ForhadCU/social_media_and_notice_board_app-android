package com.example.myproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myproject.models.Groups;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalDataHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotiFly_db";
    private static final String TABLE_1 = "Group_list_table";
    private static final String TABLE_2 = "Temp_Group_list_table";
    private static final int DATABASE_VERSION = 3;

    private static final String dID = "_id";
    private static final String dGroupID = "groupID";
    private static final String dGroupName = "group_name";
    private static final String dGroupDesc = "group_desc";
    private static final String dID2 = "_id";
    private static final String dGroupID2 = "groupID";
    private static final String dGroupName2 = "group_name";
    private static final String dGroupDesc2 = "group_desc";

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference myGroupsRef = firebaseFirestore.collection("MY_GROUPS");


    private static final String dCREATE_TABLE = "CREATE TABLE " + TABLE_1 + " (" + dID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + dGroupID + " VARCHAR(255),  " + dGroupName + " VARCHAR(255), " +
            "" + dGroupDesc + " TEXT)";
    private static final String dCREATE_TABLE2 = "CREATE TABLE " + TABLE_2 + " (" + dID2 + " INTEGER PRIMARY KEY AUTOINCREMENT , " + dGroupID2 + " VARCHAR(255),  " + dGroupName2 + " VARCHAR(255), " +
            "" + dGroupDesc2 + " TEXT)";

/*
    private static final String dCREATE_TABLE2 = "CREATE TABLE " + TABLE_2 + " (" + dID2 + " INTEGER PRIMARY KEY AUTOINCREMENT , " + dInvoiceNO + " TEXT, " + dCustomerName + " VARCHAR(255)," +
            "" + dCurrentDate + " TEXT, " + dDueDate + " TEXT," + dDiscount2 + " TEXT," + dDeduction + " TEXT, " + dAmount + " TEXT, " + dPaid + " VARCHAR(255), " + dStatus + " VARCHAR(255), " + dAction + " VARCHAR(255))";
*/

    public LocalDataHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(dCREATE_TABLE);
            db.execSQL(dCREATE_TABLE2);
//            db.execSQL(dCREATE_TABLE2);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }


    public long mAddGroupDetails(String groupID, String groupName, String groupDesc) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(dGroupID, groupID);
        contentValues.put(dGroupName, groupName);
        contentValues.put(dGroupDesc, groupDesc);

        long id = sqLiteDatabase.insert(TABLE_1, null, contentValues);
        return id;
    }


    public long mAddGroupDetails2(String groupID, String groupName, String groupDesc) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(dGroupID2, groupID);
        contentValues.put(dGroupName2, groupName);
        contentValues.put(dGroupDesc2, groupDesc);

        long id = sqLiteDatabase.insert(TABLE_2, null, contentValues);
        return id;
    }


    public List<Groups> showAllGroups() {
        String sql = "SELECT * from " + TABLE_1;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Groups> storeData = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String groupID = cursor.getString(cursor.getColumnIndex(dGroupID));
                String groupName = cursor.getString(cursor.getColumnIndex(dGroupName));
                String groupDesc = cursor.getString(cursor.getColumnIndex(dGroupDesc));

                storeData.add(new Groups(groupID, groupName, groupDesc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;
    }
    public ArrayList<Groups > showAllGroups2() {
        String sql = "SELECT * from " + TABLE_2;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Groups> storeData = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String groupID = cursor.getString(cursor.getColumnIndex(dGroupID));
                String groupName = cursor.getString(cursor.getColumnIndex(dGroupName));
                String groupDesc = cursor.getString(cursor.getColumnIndex(dGroupDesc));

//                storeData.add(groupID);
//                storeData.add(groupName);
//                storeData.add(groupDesc);
                storeData.add(new Groups(groupID, groupName, groupDesc));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeData;
    }

    public void mDeleteProductList() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_1);
    }
}
