package com.example.hw05;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class ForumsFragment extends Fragment implements ForumsRVAdapter.forumAdapterInterface {
    private final String collectionForums = "Forums";
    Button button_new_forum,button_logout;
    RecyclerView RecyclerView_forums;
    ForumsRVAdapter forumsRVAdapter;
    LinearLayoutManager linearLayoutManager;
    //ArrayList<DataServices.Forum> forums = new ArrayList<>();
    ArrayList<Forum> forums =  new ArrayList<>();
    CommunicationInterface mListner;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String currentUserName,currentUserUID;
    ListenerRegistration listner;
    public ForumsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface){
            mListner = (CommunicationInterface) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_forums, container, false);
        getActivity().setTitle(R.string.Title_Forums);
        button_new_forum = view.findViewById(R.id.buttonNewForum);
        button_logout = view.findViewById(R.id.buttonLogout);
        RecyclerView_forums = view.findViewById(R.id.RecyclerViewForums);
        RecyclerView_forums.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView_forums.setLayoutManager(linearLayoutManager);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        CachedUsers cachedUsers = new CachedUsers();
        currentUserName = cachedUsers.getUser(mAuth.getUid());
        currentUserUID = mAuth.getUid();
        return view;
    }

    public void getAllForums()
    {
        Log.d("TAG", "Will Fetch Collections");
        listner = db.collection(collectionForums).addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                forums.clear();
                for (QueryDocumentSnapshot qs : value)
                {
                    if(!value.getMetadata().hasPendingWrites()) {
                        Log.d("TAG", "dbCollections: " + qs.getData().toString());
                        Forum forum = new Forum(qs.getId(), qs.getTimestamp("CreatedAt"), qs.getString("CreatedBy")
                                , qs.getString("ForumDescription"), qs.getString("ForumTitle"), (ArrayList<String>) qs.get("LikedBy"));
                        Log.d("TAG", "Forum --> " + forum);
                        forums.add(forum);
                    }
                }
                forums.sort(new Comparator<Forum>() {
                    @Override
                    public int compare(Forum forum, Forum t1) {
                        return -1 * forum.getCreatedAt().compareTo(t1.getCreatedAt());
                    }
                });
                forumsRVAdapter.notifyDataSetChanged();
            }
        }) ;
        Log.d("TAG", "getAllForums: "+forums.size());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (listner == null) {
            getAllForums();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ;


        forumsRVAdapter = new ForumsRVAdapter(forums,currentUserUID,this);
        RecyclerView_forums.setAdapter(forumsRVAdapter);
        RecyclerView_forums.addItemDecoration(new DividerItemDecoration(RecyclerView_forums.getContext(), DividerItemDecoration.VERTICAL));

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.logout();
            }
        });

        button_new_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.goToCreateForum(currentUserName,currentUserUID);
            }
        });

        if(listner == null) {
            getAllForums();
        }
    }


    public void gotoForum(Forum forum) {
        mListner.goToForum(forum);
    }

    @Override
    public void likeForum(String ForumID) {
        db.collection(collectionForums).document(ForumID).update("LikedBy", FieldValue.arrayUnion(currentUserUID));
    }

    @Override
    public void unlikeForum(String ForumID) {
        db.collection(collectionForums).document(ForumID).update("LikedBy", FieldValue.arrayRemove(currentUserUID));
    }

    @Override
    public void deleteForum(String ForumID) {
        db.collection(collectionForums).document(ForumID).delete();
    }
}