package com.example.bharg_000.instructorsapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

public class AddCourse extends AppCompatActivity {
    private ImageButton mPostImage;
    private EditText mPostCourse,mPostDesc;
    private Button mEnroll,mSubmitButton;
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

        mPostImage =(ImageButton) findViewById(R.id.imageButton);
        mPostCourse =(EditText) findViewById(R.id.courseName);
        mPostDesc =(EditText) findViewById(R.id.courseDesc);
        mEnroll =(Button) findViewById(R.id.addRegister);
        mSubmitButton =(Button) findViewById(R.id.courseSubmit);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        mEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(AddCourse.this,Upload.class));
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
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
        mProgress.setMessage("Posting");
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

            StorageReference filepath =mStorage.child("Course_img").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mPostDatabase.push();
                    Map<String,String > dataToSave =new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadurl.toString());
                    dataToSave.put("userid",mUser.getUid());

                    newPost.setValue(dataToSave);
                    mProgress.dismiss();
                    startActivity(new Intent(AddCourse.this,Welcome.class));
                    finish();
                }
            });

        }
    }
}
