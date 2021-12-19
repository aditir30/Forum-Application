/*
 Assignment : HW 05
 File Name : NewForumFragment.java
 Student Full Name: Aditi Raghuwanshi, Pratik Chaudhari
 */
package com.example.hw05;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NewForumFragment extends Fragment {
    private final String collectionForums = "Forums";
    Button buttonCancel, buttonSubmit;
    EditText forumTitle,forumDesc;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    CommunicationInterface mListner;
    public NewForumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_forum, container, false);
        getActivity().setTitle(R.string.Title_NewForum);
        buttonSubmit =  view.findViewById(R.id.buttonSubmit);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        forumTitle = view.findViewById(R.id.editTextForumTitle);
        forumDesc = view.findViewById(R.id.editTextForumDesc);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof CommunicationInterface)
        {
            mListner = (CommunicationInterface) context;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CachedUsers users = new CachedUsers();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forumTitle.getText().toString().equals("") || forumDesc.getText().toString().equals("")){
                    //Toast.makeText(getContext(), "All Fields are necessary", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error!!").setMessage("All values must be entered!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
                else
                {
                    Map<String,Object> data = new HashMap<>();
                    data.put("CreatedAt", FieldValue.serverTimestamp());
                    data.put("CreatedBy",mAuth.getUid());
                    data.put("ForumDescription",forumDesc.getText().toString());
                    data.put("ForumTitle",forumTitle.getText().toString());
                    data.put("LikedBy",new ArrayList<String>());
                    db.collection(collectionForums).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mListner.popBackStack();

                        }
                    });

                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.popBackStack();
            }
        });
    }
}