package com.example.hw05;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

public class ForumsRVAdapter extends RecyclerView.Adapter<ForumsRVAdapter.ForumsViewHolder>{
    ArrayList<Forum> forums;
    forumAdapterInterface mListner;
    String currentUserUID;
    CachedUsers cachedUsers;

    public ForumsRVAdapter(ArrayList<Forum> forums, String currentUserUID,ForumsFragment context) {
        Log.d("TAG", "ForumsRVAdapter: Called");
        this.forums = forums;
        this.currentUserUID = currentUserUID;
        Log.d("TAG", "ForumsRVAdapter: Forums received"+forums);
        if(context instanceof forumAdapterInterface){
            this.mListner = (forumAdapterInterface) context;
        }
        cachedUsers = new CachedUsers();
    }

    @NonNull
    @Override
    public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forums_rv_item,parent,false);
        ForumsViewHolder forumsViewHolder = new ForumsViewHolder(view);
        return forumsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumsViewHolder holder, int position) {
        Boolean liked = false;
        Log.d("TAG", "onBindViewHolder: like"+liked);
        Forum forum = forums.get(position);
        Log.d("TAG", "onBindViewHolder: "+forum);
        Date date = forum.getCreatedAt().toDate();
        String formattedDate = new SimpleDateFormat("MM/dd/yyyy hh:mma").format(date);


        holder.textViewForumTitle.setText(forum.getForumTitle());
        holder.textViewForumAuthor.setText(cachedUsers.getUser(forum.getCreatedBy()));
        holder.textViewForumDesc.setText(forum.getForumDescription());
        holder.textViewForumDesc.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewForumTime.setText(formattedDate);
        holder.textViewLikes.setText(forum.getLikedBy().size()+ " Likes");

        if(currentUserUID.equals(forum.getCreatedBy()))
        {
            holder.buttonDelete.setVisibility(View.VISIBLE);
            holder.buttonDelete.setEnabled(true);

        }
        else{
            holder.buttonDelete.setVisibility(View.INVISIBLE);
            holder.buttonDelete.setEnabled(false);
        }

        if(forum.getLikedBy().contains(currentUserUID)){
            holder.buttonLike.setImageResource(R.drawable.like_favorite);
            holder.buttonLike.setTag(R.drawable.like_favorite);
            liked = true;
        }
        else{
            holder.buttonLike.setImageResource(R.drawable.like_not_favorite);
            liked = false;
        }

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.deleteForum(forum.getForumID());
            }
        });

        Boolean finalLiked = liked;

        holder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalLiked){
                    mListner.unlikeForum(forum.getForumID());
                }
                else{
                    mListner.likeForum(forum.getForumID());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.gotoForum(forum);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (forums != null) {
                return forums.size();
        }
        else{
            return 0;
        }
    }

    public class ForumsViewHolder extends RecyclerView.ViewHolder {						// Creates viewholder class -- change this step 1
        TextView textViewForumTitle,textViewForumAuthor,textViewForumDesc,textViewLikes,textViewForumTime; // Ack all the display items here for the custom view   (Step 3)
        ImageView buttonDelete,buttonLike;
        int position;
        ArrayList<Forum> forumsArrayList;

        public ForumsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewForumTitle = itemView.findViewById(R.id.textViewForumTitle);
            textViewForumAuthor= itemView.findViewById(R.id.textViewForumAuthor);
            textViewForumDesc= itemView.findViewById(R.id.textViewForumDesc);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
            textViewForumTime = itemView.findViewById(R.id.textViewForumTime);
            buttonDelete = itemView.findViewById(R.id.imageViewDeleteForum);
            buttonDelete.setVisibility(View.INVISIBLE);
            buttonDelete.setEnabled(false);
            buttonLike = itemView.findViewById(R.id.imageViewLike);

        }
}

public interface forumAdapterInterface{
        void gotoForum(Forum forum);
        void likeForum(String ForumID);
        void unlikeForum(String ForumID);
        void deleteForum(String ForumID);
}

}
