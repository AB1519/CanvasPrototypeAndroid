package com.example.bharg_000.instructorsapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCoursesFragment extends Fragment {
    private ImageButton mPostImage;
    private EditText mPostCourse,mPostDesc;
    private Button mView;
    private FirebaseAuth mAuth;
    private DatabaseReference mPostDatabase;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private static final int GALLERY_CODE=1;
    private Uri mImageUri;
    private StorageReference mStorage;
        ListView listView;
        String [] listviewitems = new String[]{};

    public MyCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_courses, container, false);

        mProgress=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mPostDatabase=FirebaseDatabase.getInstance().getReference().child("MCourse");
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostImage =(ImageButton) findViewById(R.id.imageButton);
        mPostCourse =(EditText) findViewById(R.id.courseName);
        mPostDesc =(EditText) findViewById(R.id.courseDesc);
        mView =(Button) findViewById(R.id.addShow);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyCoursesFragment.this,Upload.class));
            }
        });




    }

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
                }
            });

        }
    }
}



