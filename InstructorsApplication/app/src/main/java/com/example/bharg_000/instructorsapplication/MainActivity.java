package com.example.bharg_000.instructorsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText emailField,passwordField;
    private Button loginButton,createActButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiating widgets
        emailField = (EditText) findViewById(R.id.loginEmail);
        passwordField =(EditText) findViewById(R.id.loginPassword);
        loginButton =(Button) findViewById(R.id.loginSubmit);
        createActButton =(Button) findViewById(R.id.newRegistration);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //checks the user state
                mUser = mAuth.getCurrentUser(); // gets current user

                if(mUser!= null){
                    Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,Welcome.class));
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Not Signed In",Toast.LENGTH_LONG).show();
                }
            }
        };

        createActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(emailField.getText().toString())&&!TextUtils.isEmpty(passwordField.getText().toString())){
                    String email = emailField.getText().toString();
                    String pwd = passwordField.getText().toString();

                    login(email,pwd); //calls the login method
                }else{

                }
            }
        });
    }

    private void login(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this,Welcome.class));
                            finish();
                        }else{

                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); //start authentication
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuth != null){
            mAuth.removeAuthStateListener(mAuthListener); // stop the authentication
        }
    }
}


