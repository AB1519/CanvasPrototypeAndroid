package com.example.bharg_000.instructorsapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by bharg_000 on 4/8/2018.
 */

public class Upload extends AppCompatActivity {
    private Button upload,download;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostDatabase;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private static final int GALLERY_CODE=1;
    private Uri mImageUri;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mProgress=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mPostDatabase=FirebaseDatabase.getInstance().getReference().child("MCourse");
        mStorage = FirebaseStorage.getInstance().getReference();

        upload =(ImageButton) findViewById(R.id.upload);
        download =(EditText) findViewById(R.id.download);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("file/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });
        downloadload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Downloaded");
                mProgress.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode ==RESULT_OK){
            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }
    private void startPosting() {
        mProgress.setMessage("Uploading");
        mProgress.show();

        final String titleVal = mPostCourse.getText().toString().trim();
        final String descVal = mPostDesc.getText().toString().trim();
        if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null){
//            Course course = new Course("Title","description","imageUrl","userid","show");
//            mPostDatabase.setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(getApplicationContext(),"Item Added",Toast.LENGTH_LONG).show();
//                    mProgress.dismiss();
//                }
//            });

            StorageReference filepath =mStorage.child("Course_file").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mPostDatabase.push();
                    Map<String,String > dataToSave =new HashMap<>();
                    dataToSave.put("upload",downloadurl.toString());
                    dataToSave.put("userid",mUser.getUid());

                    newPost.setValue(dataToSave);
                    mProgress.dismiss();
                    startActivity(new Intent(Upload.this,MyCoursesFragment.class));
                    finish();
                }
            });

        }
    }

}