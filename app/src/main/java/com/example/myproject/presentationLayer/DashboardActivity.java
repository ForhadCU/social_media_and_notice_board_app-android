package com.example.myproject.presentationLayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.R;
import com.example.myproject.applicationLayer.adapters.MyAdapter_rvAllpost;
import com.example.myproject.databaseLayer.models.Constants;
import com.example.myproject.databaseLayer.models.Data_Handler;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collRefNotices = firebaseFirestore.collection(Constants.coll_notices);
    private CollectionReference collRefUser = firebaseFirestore.collection(Constants.coll_users);
    private ArrayList<Data_Handler> dataHandlerList;
    private MyAdapter_rvAllpost myAdapter_rvAllpost;
    private RecyclerView recyclerView;
    private TextView navUserName, navUserEmail;

    private String gId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mBind();
        mInit();
        mSetToolbar();
        mSetRv();
        mSetDrawer();
        mCheckCurrentUser();
        mGetUserNameEmail();
        mNotification();
    }

    private void mNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Dashboard", "Key: " + key + " Value: " + value);
            }
        }
    }

    private void mCheckCurrentUser() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //check user sign in
                if (mAuth.getCurrentUser() == null) {
                    Intent signInIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                    signInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signInIntent);
                } else
                    mShowAllPosts();
            }
        };

    }

    private void mSetDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void mSetRv() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void mInit() {
        mAuth = FirebaseAuth.getInstance();
        gId = mAuth.getCurrentUser().getUid();
    }

    private void mSetToolbar() {
        toolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        this.setTitle("Dashboard");
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void mBind() {
        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navViewId);
        recyclerView = findViewById(R.id.rv_allPostID);

        View headerView = navigationView.getHeaderView(0);
        navUserName = headerView.findViewById(R.id.navUserNameId);
        navUserEmail = headerView.findViewById(R.id.navEmailId);
    }

    private void mGetUserNameEmail() {
        collRefUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                    if (queryDocumentSnapshot != null && queryDocumentSnapshot.exists()) {
                        if (queryDocumentSnapshot.get("userId").toString().equals(gId)) {
                            navUserName.setText(queryDocumentSnapshot.get("username").toString());
                            navUserEmail.setText(queryDocumentSnapshot.get("email").toString());
                        }
                    }
                }
            }
        });
    }
    //create Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
          /*  case R.id.post_item:
                Intent intent = new Intent(DashboardActivity.this, PostActivity.class);
                startActivity(intent);
                break;*/
        }
        return super.onOptionsItemSelected(item);

    }

    //First of all, check user signed in or not
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /*
        private void mShowAllPosts() {
            uId = mAuth.getCurrentUser().getUid();

            mClearAll();

            //very Real Time
            collectionReferenceNotices.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        if (documentSnapshot.get("currentUid").equals(mAuth.getCurrentUser().getUid())) {
                            Data_Handler data_handler = documentSnapshot.toObject(Data_Handler.class);
                            dataHandlerList.add(data_handler);
                        }
                        myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList, getApplicationContext());
                        recyclerView.setAdapter(myAdapter_rvAllpost);
                        myAdapter_rvAllpost.notifyDataSetChanged();
                    }


                }
            });
        }
    */
    private void mShowAllPosts() {
        mClearAll();
/*        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                String data = "";
                dataHandlerList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {

                    Data_Handler list = documentSnapshot.toObject(Data_Handler.class);
                    dataHandlerList.add(list);

                }
                myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList, getApplicationContext());
                recyclerView.setAdapter(myAdapter_rvAllpost);
//                showpostTextView.setText(data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.toString());
            }
        }); *///it's also work

        //very Real Time
        collRefNotices.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                dataHandlerList = new ArrayList<>();
//                          &&  documentSnapshot.get("gId").equals(gId)

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.get("currentUid").equals(mAuth.getCurrentUser().getUid())) {
                        Data_Handler data_handler = documentSnapshot.toObject(Data_Handler.class);
                        dataHandlerList.add(data_handler);
                        myAdapter_rvAllpost = new MyAdapter_rvAllpost(dataHandlerList, getApplicationContext());
                        recyclerView.setAdapter(myAdapter_rvAllpost);
                        myAdapter_rvAllpost.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void mClearAll() {
        if (dataHandlerList != null) {
            dataHandlerList.clear();
            if (myAdapter_rvAllpost != null)
                myAdapter_rvAllpost.notifyDataSetChanged();
        }
        dataHandlerList = new ArrayList<>();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut_item:
                signOutMethod();
                break;

            case R.id.mGroup_item:
                Intent mGroupIntent = new Intent(DashboardActivity.this, ManageGroupActivity.class);
                mGroupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mGroupIntent);
                break;
            case R.id.mNotificationTester:
                Intent mNotificationIntent = new Intent(DashboardActivity.this, NotificationTestingActivity.class);
                mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mNotificationIntent);
        }
        return false;
    }

    private void signOutMethod() {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

/*    private void mClearAll() {
        if (dataHandlerList != null) {
            dataHandlerList.clear();
            if (myAdapter_rvAllpost != null)
                myAdapter_rvAllpost.notifyDataSetChanged();
        }
        dataHandlerList = new ArrayList<>();
    }*/

}
