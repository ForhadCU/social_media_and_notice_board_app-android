package com.example.myproject.strategyDesignPattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myproject.R;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewGroupName;
    private CardView cardViewNotices, cardViewCreateNotice, cardViewMembers, cardViewInviteMembers, cardViewEditGroup, cardViewHelp;
    private ImageButton imageButtonNotices, imageButtonCreateNotice, imageButtonMembers, imageButtonInviteMembers, imageButtonEditGroup, imageButtonHelp;
    private RelativeLayout relativeLayoutNotices;

    private String gId, gName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intentFromManageGroupActivity = getIntent();
        gId = intentFromManageGroupActivity.getStringExtra("gId");
        gName = intentFromManageGroupActivity.getStringExtra("gName");

        textViewGroupName = findViewById(R.id.tv_groupName);
        cardViewNotices = findViewById(R.id.cv_notices);
        cardViewCreateNotice = findViewById(R.id.cv_createNotices);
        cardViewMembers = findViewById(R.id.cv_Members);
        cardViewInviteMembers = findViewById(R.id.cv_inviteMembers);
        cardViewEditGroup = findViewById(R.id.cv_editGroups);
        cardViewHelp = findViewById(R.id.cv_help);

        textViewGroupName.setText(gName);

        cardViewNotices.setOnClickListener(this);
        cardViewCreateNotice.setOnClickListener(this);
        cardViewMembers.setOnClickListener(this);
        cardViewInviteMembers.setOnClickListener(this);
        cardViewEditGroup.setOnClickListener(this);
        cardViewHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cv_notices:
                /*Intent intentNotice = new Intent(this, NoticesActivity.class);
                intentNotice.putExtra("gId", gId);
                intentNotice.putExtra("gName", gName);
                startActivity(intentNotice);*/
               /* IntentClass intentClass = new IntentClass(GroupActivity.this);
                intentClass.mIntent();*/

                ContextClass contextClass = new ContextClass(new IntentNoticesClass(GroupActivity.this));
                contextClass.mExecuteStrategy(gId, gName);
                break;

            case R.id.cv_createNotices:
               /* Intent intentCreateNotices = new Intent(this, PostActivity.class);
                intentCreateNotices.putExtra("gId", gId);
                intentCreateNotices.putExtra("gName", gName);
                startActivity(intentCreateNotices);*/
                ContextClass contextClass_2 = new ContextClass(new IntentCreateNoticeClass(GroupActivity.this));
                contextClass_2.mExecuteStrategy(gId, gName);
                break;
            case R.id.cv_Members:
                /*Intent intentMembers = new Intent(this, MembersActivity.class);
                intentMembers.putExtra("gId", gId);
                intentMembers.putExtra("gName", gName);
                startActivity(intentMembers);*/
                ContextClass contextClass_3 = new ContextClass(new IntentGroupMembersClass(GroupActivity.this));
                contextClass_3.mExecuteStrategy(gId, gName);
                break;
            case R.id.cv_inviteMembers:
               /* Intent intentInviteMembers = new Intent(this, AddMembersActivity.class);
                intentInviteMembers.putExtra("gId", gId);
                intentInviteMembers.putExtra("gName", gName);
                startActivity(intentInviteMembers);*/
                ContextClass contextClass_4 = new ContextClass(new IntentAddMembersClass(GroupActivity.this));
                contextClass_4.mExecuteStrategy(gId, gName);
                break;
            case R.id.cv_editGroups:
               /* Intent intentEditGroups = new Intent(this, EditGroupActivity.class);
                intentEditGroups.putExtra("gId", gId);
                intentEditGroups.putExtra("gName", gName);
                startActivity(intentEditGroups);*/
                ContextClass contextClass_5 = new ContextClass(new IntentGroupEditClass(GroupActivity.this));
                contextClass_5.mExecuteStrategy(gId, gName);
                break;


        }
    }
}