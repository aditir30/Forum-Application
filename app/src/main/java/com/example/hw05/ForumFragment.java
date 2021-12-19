package com.example.hw05;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment implements ForumCommentAdapter.commentAdapterInterface {


    private static final String ARG_PARAM1 = "forum";

    TextView textViewForumTitle,textViewForumAuthor,textViewForumDesc,textViewCommentCount;
    EditText newComment;
    Button post;
    RecyclerView RecyclerViewForumComments;
    LinearLayoutManager linearLayoutManager;
    ForumCommentAdapter forumCommentAdapter;
    ArrayList<Comment> comments = new ArrayList<>();
    CachedUsers cachedUsers;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String currentUserName,currentUserUID;
    ListenerRegistration listner;
    private final String collectionComments = "Comments";
    private final String collectionForums = "Forums";

    private Forum forum;

    public ForumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 forum.
     * @return A new instance of fragment ForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(Forum param1) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forum = (Forum) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    void getComments(){


        listner = db.collection(collectionForums).document(forum.getForumID()).collection(collectionComments).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                comments.clear();
                for (QueryDocumentSnapshot qs : value) {

                    if (qs.getId().equals("master") || qs.getMetadata().hasPendingWrites()) {
                        continue;
                    }
                    Comment comment = new Comment(qs.getString("CreatedBy"), qs.getTimestamp("CreatedAt"), qs.getString("CommentDescription")
                            , qs.getId());
                    Log.d("TAG", "Forum --> " + forum);
                    comments.add(comment);
                }

                comments.sort(new Comparator<Comment>() {
                    @Override
                    public int compare(Comment comment, Comment t1) {
                        return -1 * comment.getCreatedAt().compareTo(t1.getCreatedAt());
                    }
                });
                forumCommentAdapter.notifyDataSetChanged();
                textViewCommentCount.setText("" + comments.size());
            }

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        getActivity().setTitle(R.string.Title_Forum);
        textViewForumTitle = view.findViewById(R.id.textViewForumTitle);
        textViewForumAuthor= view.findViewById(R.id.textViewForumAuthor);
        textViewForumDesc= view.findViewById(R.id.textViewForumDesc);
        textViewCommentCount = view.findViewById(R.id.textViewCommentCount);
        textViewCommentCount.setText(" ");

        post = view.findViewById(R.id.buttonPost);
        newComment = view.findViewById(R.id.editTextComment);

        RecyclerViewForumComments = view.findViewById(R.id.RecyclerViewForumComments);
        RecyclerViewForumComments.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewForumComments.setLayoutManager(linearLayoutManager);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        cachedUsers = new CachedUsers();
        currentUserName = cachedUsers.getUser(mAuth.getUid());
        currentUserUID = mAuth.getUid();
        return view;
   }

    @Override
    public void onPause() {
        super.onPause();
        listner.remove();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        forumCommentAdapter = new ForumCommentAdapter(comments,forum,currentUserUID,this);
        RecyclerViewForumComments.setAdapter(forumCommentAdapter);
        RecyclerViewForumComments.addItemDecoration(new DividerItemDecoration(RecyclerViewForumComments.getContext(), DividerItemDecoration.VERTICAL));
        textViewForumTitle.setText(forum.getForumTitle());
        textViewForumAuthor.setText(cachedUsers.getUser(forum.getCreatedBy()));
        textViewForumDesc.setText(forum.getForumDescription());

        if(listner == null) {
            getComments();
        }



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newComment.getText().toString().equals("")){
                    //Toast.makeText(getContext(), "Please enter some comment text", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error!!").setMessage("Please enter a comment!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
                else{
                    Map<String,Object> data= new HashMap<>();
                    data.put("CreatedBy",mAuth.getUid());
                    data.put("CreatedAt", FieldValue.serverTimestamp());
                    data.put("CommentDescription", newComment.getText().toString());
                    db.collection(collectionForums).document(forum.getForumID()).collection(collectionComments).add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            newComment.setText("");
                        }
                    });
                }

            }
        });
    }

    @Override
    public void deleteComment(String commentID) {
        db.collection(collectionForums).document(forum.getForumID()).collection(collectionComments).document(commentID).delete();
    }
}