package com.example.hw05;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForumCommentAdapter extends RecyclerView.Adapter<ForumCommentAdapter.ForumCommentsHolder> {
    ArrayList<Comment> comments;
    Forum forum;
    String currentUserUID;
    CachedUsers cachedUsers;
    commentAdapterInterface mListner;

    public ForumCommentAdapter(ArrayList<Comment> comments, Forum forum,String currentUserUID,ForumFragment ctx) {
        this.comments = comments;
        this.forum = forum;
        cachedUsers = new CachedUsers();
        this.currentUserUID = currentUserUID;
        if(ctx instanceof commentAdapterInterface)
        {
            mListner = (commentAdapterInterface) ctx;
        }
    }


    @NonNull
    @Override
    public ForumCommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_comments_holder,parent,false);
        ForumCommentsHolder forumCommentsHolder = new ForumCommentsHolder(view);
        return forumCommentsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumCommentsHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.textViewComment.setText(comment.getCommentDescription());
        holder.textViewCommentAuthor.setText(cachedUsers.getUser(comment.getCreatedBy()));

        Date date = comment.getCreatedAt().toDate();
        String formattedDate = new SimpleDateFormat("MM/dd/yyyy hh:mma").format(date);
        holder.textViewCommentTime.setText(formattedDate);

        if(currentUserUID.equals(comment.getCreatedBy()))
        {
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.buttonDelete.setEnabled(true);
        }
        else{
            holder.buttonDelete.setVisibility(View.INVISIBLE);
            holder.buttonDelete.setEnabled(false);
        }

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.deleteComment(comment.getCommentID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ForumCommentsHolder extends RecyclerView.ViewHolder {
        TextView textViewCommentAuthor, textViewComment, textViewCommentTime;
        ImageView buttonDelete;

        public ForumCommentsHolder(@NonNull View itemView) {
            super(itemView);
            textViewCommentAuthor = itemView.findViewById(R.id.textViewCommentAuthor);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewCommentTime = itemView.findViewById(R.id.textViewCommentTime);
            buttonDelete = itemView.findViewById(R.id.imageViewDeleteComment);
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonDelete.setEnabled(false);


        }
    }

    public interface commentAdapterInterface
    {
        void deleteComment(String commentID);
    }
}
