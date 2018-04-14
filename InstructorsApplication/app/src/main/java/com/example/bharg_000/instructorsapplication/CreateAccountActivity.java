package com.example.bharg_000.instructorsapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName,lastName,email,password;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mDatabase =FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MUsers");
        mAuth =FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        firstName =(EditText) findViewById(R.id.firstNameAct);
        lastName =(EditText) findViewById(R.id.lastNameAct);
        email =(EditText) findViewById(R.id.emailAct);
        password =(EditText) findViewById(R.id.passwordAct);
        createAccountBtn =(Button) findViewById(R.id.createAct);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {

        final String name= firstName.getText().toString().trim();
        final String lname= lastName.getText().toString().trim();
        String em= email.getText().toString().trim();
        String pwd= password.getText().toString().trim();

        if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(lname)&&
                !TextUtils.isEmpty(em)&&!TextUtils.isEmpty(pwd)){
            mProgress.setMessage("Creating Account");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(em,pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult!=null){
                                String userid =mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb= mDatabaseReference.child(userid);
                                currentUserDb.child("firstname").setValue(name);
                                currentUserDb.child("lastname").setValue(lname);

                                mProgress.dismiss();
                                Intent intent=new Intent(CreateAccountActivity.this,Welcome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    });
        }
    }
}
