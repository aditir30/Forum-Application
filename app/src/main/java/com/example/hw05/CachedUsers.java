/*
 Assignment : HW 05
 File Name : CachedUsers.java
 Student Full Name: Aditi Raghuwanshi, Pratik Chaudhari
 */
package com.example.hw05;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class CachedUsers {

    public static HashMap<String,String> users = new HashMap<>();
    FirebaseFirestore db;

    public CachedUsers() {
        this.db = FirebaseFirestore.getInstance();
    }

    String getUser(String userID){
        Log.d("TAG", "getUser: I/P"+userID);
        Log.d("TAG", "getUser: Users Array"+users.toString());
        if (!users.containsKey(userID)) {
            db.collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    users.put(userID,documentSnapshot.getString("Name"));
                }
            });
        }
        return users.get(userID);
    }
    Boolean userExists(String userID){

        return users.containsKey(userID);
    }
    Boolean clearCache()
    {
        return null;
    }
    String fetchUser(String userID)
    {
        return userID;
    }

    void fetchAllUsers()
    {
        users.clear();
        Log.d("TAG", "fetchAllUsers: Triggered");
        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot qs : value)
                {
                    users.put(qs.getId(),qs.getString("Name"));
                }
            }
        });

    }
}
