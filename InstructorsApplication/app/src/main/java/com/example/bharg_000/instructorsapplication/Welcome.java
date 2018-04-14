package com.example.bharg_000.instructorsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private List<Course> courseList;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button enroll;

    public Welcome() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mDatabase =FirebaseDatabase.getInstance();
        mDatabaseReference=mDatabase.getReference().child("MCourse");
        mDatabaseReference.keepSynced(true);
        enroll =(Button) findViewByID(R.id.addRegisterList);
        courseList =new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Welcome.this,"Successfully Enrolled",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(Welcome.this,Home.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add :
                if(mUser!=null && mAuth !=null){
                    startActivity(new Intent(Welcome.this,AddCourse.class));
                    finish();
                }
                break;
            case R.id.signout:
                if(mUser!=null && mAuth !=null){
                    startActivity(new Intent(Welcome.this,MainActivity.class));
                    finish();
                }
                mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Course course = dataSnapshot.getValue(Course.class);
                courseList.add(course);
                courseRecyclerAdapter = new CourseRecyclerAdapter(Welcome.this, courseList);
                recyclerView.setAdapter(courseRecyclerAdapter);
                courseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this,"Account Info",Toast.LENGTH_LONG).show();
            AccountFragment accountFragment=new AccountFragment();
            android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layoutforPostCourses,accountFragment,accountFragment.getTag()).commit();


        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"My Courses",Toast.LENGTH_LONG).show();
            MyCoursesFragment myCoursesFragment=new MyCoursesFragment();
            android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layoutforPostCourses,myCoursesFragment,myCoursesFragment.getTag()).commit();

        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {


        }else if (id == R.id.nav_contact) {
            Toast.makeText(this,"Contact Info",Toast.LENGTH_LONG).show();
            ContactFragment contactFragment=new ContactFragment();
            android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.layoutforPostCourses,contactFragment,contactFragment.getTag()).commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
