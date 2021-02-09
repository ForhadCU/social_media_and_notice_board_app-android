package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myproject.models.Data_Handler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PostActivity";
    private static final String KEY_STATUS = "status";
    private ImageButton imageButton_upload;
    private EditText editText_postTitle, editText_postDesc;
    private static int GALLERY_CODE = 2;
    private ProgressDialog progressDialog;
    private boolean isTitleValid, isDescValid;
    private TextInputLayout inputLayout_title, inputLayout_desc;
    private Toolbar toolbar;

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "desc";
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestoreDB.collection("Notifly_Post_Collection");
    private StorageReference storageReference;
    private Uri imgUri = null;
    private String savedImgUri;
    private DocumentReference userDocRef;
    private FirebaseAuth mAuth;
    private String currentUid;
    private Button buttonPost;
    private String gId, gName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        storageReference = FirebaseStorage.getInstance().getReference().child("Notifly_Post Images");
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        Intent intentFromGroupAct = getIntent();
        gId = intentFromGroupAct.getStringExtra("gId");
        gName = intentFromGroupAct.getStringExtra("gName");

        imageButton_upload = findViewById(R.id.imageButtonId);
        editText_postTitle = findViewById(R.id.edt_postTitleId);
        editText_postDesc = findViewById(R.id.edt_postDescId);
        inputLayout_desc = findViewById(R.id.descErrorID);
        inputLayout_title = findViewById(R.id.titleErrorId);
        buttonPost = findViewById(R.id.btn_post);

        imageButton_upload.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        buttonPost.setOnClickListener(this);
    }

    //Get image from Gallery
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButtonId)
        {
            Intent gallery_intent = new Intent(Intent.ACTION_GET_CONTENT);
            gallery_intent.setType("image/*");
            startActivityForResult(gallery_intent, GALLERY_CODE);

        }
        else if (v.getId() == R.id.btn_post)
        {
            String title = editText_postTitle.getText().toString();
            String desc = editText_postDesc.getText().toString();

            boolean getValidtion = mSetValidation(title, desc);
            if (getValidtion)
            {
                progressDialog.setMessage("Posting..");
                progressDialog.show();
                mSaveImageToStorage(title, desc);
            }
        }
    }
    //Hold photo in ImageUri


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK)
        {
            imgUri = data.getData();
            imageButton_upload.setImageURI(imgUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void mSaveImageToStorage(final String title, final String desc) {
        final StorageReference imgName = storageReference.child("image"+imgUri.getLastPathSegment());
        imgName.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        savedImgUri = String.valueOf(uri);
                        mSaveToFireStore(title, desc, savedImgUri);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void mSaveToFireStore( String title, String desc, final String savedImgUri) {
        Data_Handler data_handler = new Data_Handler(currentUid, gId, gName,  title, desc, savedImgUri);

        collectionReference.add(data_handler).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(PostActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(getApplicationContext(), NoticesActivity.class);
                homeIntent.putExtra("gId", gId);
                homeIntent.putExtra("gName", gName);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PostActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    private boolean mSetValidation(String title, String desc) {
        if (TextUtils.isEmpty(title))
        {
            isTitleValid = false;
            inputLayout_title.setError(getResources().getString(R.string.title_error));
        }
        else
        {
            isTitleValid = true;
            inputLayout_title.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(desc))
        {
            isDescValid = false;
            inputLayout_desc.setError(getResources().getString(R.string.desc_error));
        }
        else
        {
            isDescValid = true;
            inputLayout_desc.setErrorEnabled(false);
        }

        if (isTitleValid && isDescValid)
            return true;
        else
            return false;
    }

    //Online, Offline Method
    private void statusMethod(String status)
    {
        userDocRef = firestoreDB.collection("USERS").document(currentUid);
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_STATUS, status);
        userDocRef.update(map);
//        userDocRef.update(KEY_STATUS, status);

    }

    @Override
    protected void onResume() {
        super.onResume();
        statusMethod("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusMethod("offline");
    }
}
