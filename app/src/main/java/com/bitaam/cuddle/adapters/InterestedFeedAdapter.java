package com.bitaam.cuddle.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitaam.cuddle.InterestedFeedActivity;
import com.bitaam.cuddle.R;
import com.bitaam.cuddle.modals.ProfileModal;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class InterestedFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ProfileModal> profileModals;
    ArrayList<Integer> likesC;
    ArrayList<Integer> dislikesC;
    Context context;
    RecyclerView recyclerView;


    public InterestedFeedAdapter(ArrayList<ProfileModal> profileModals, Context context, RecyclerView recyclerView) {
        this.profileModals = profileModals;
        this.context = context;
        this.recyclerView = recyclerView;
        this.likesC = new ArrayList<>();
        this.dislikesC = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        ProfileModal modal = profileModals.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.profileNameTv.setText(modal.getName());
        Picasso.get().load(Uri.parse(modal.getImgUrl1())).into(viewHolder.profileImgView);


    }

    @Override
    public int getItemViewType(int position) {

        return 1;
    }

    @Override
    public int getItemCount() {
        return profileModals.size();
    }

    public void update(ProfileModal modal){

        profileModals.add(modal);
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView profileNameTv,likeTv,dislikeTv;
        ImageView profileImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Fresco.initialize(context);

            profileNameTv = itemView.findViewById(R.id.feedNameTv);
            profileImgView = itemView.findViewById(R.id.feedImageView);
            likeTv = itemView.findViewById(R.id.likeFeedTv);
            likeTv.setVisibility(View.GONE);
            dislikeTv = itemView.findViewById(R.id.dislikeFeedTv);
            dislikeTv.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = recyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent(context, InterestedFeedActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("profileInfo", (Serializable) profileModals.get(position));
                    context.startActivity(intent);

                }
            });
        }


    }

}
