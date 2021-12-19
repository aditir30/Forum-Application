package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CommunicationInterface{
    CachedUsers users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            goToLogin();
        }
        else
        {
            users = new CachedUsers();
            users.fetchAllUsers();
            goToForums();
        }

    }

    @Override
    public void goToForums() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new ForumsFragment()).commit();
    }

    @Override
    public void goToForum(Forum forum) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,ForumFragment.newInstance(forum)).addToBackStack("Forums").commit();
    }

    @Override
    public void goToCreateForum(String name, String uid) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new NewForumFragment()).addToBackStack("Forums").commit();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToRegister() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new RegisterFragment()).commit();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new LoginFragment()).commit();
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new LoginFragment()).commit();

    }

}