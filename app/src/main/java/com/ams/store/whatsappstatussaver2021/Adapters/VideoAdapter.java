package com.ams.store.whatsappstatussaver2021.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ams.store.whatsappstatussaver2021.Frgments.VideoFragment;
import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.ams.store.whatsappstatussaver2021.R;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private final List<StatusModel> vidList;
    Context context;
    VideoFragment videoFragment;

    public VideoAdapter(Context context, List<StatusModel> vidList,VideoFragment videoFragment){
        this.context= context;
        this.vidList = vidList;
        this.videoFragment  = videoFragment;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);

        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        StatusModel statusModel= vidList.get(position);
//        holder.ivThumbnailImageView.setImageBitmap(statusModel.getThumbnail());
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Hey It's Working", Toast.LENGTH_SHORT).show();
//            }
//        });
        ImageView imageView = holder.ivThumbnailImageView;
        Glide.with(context).load(statusModel.getPath()).into(imageView);
        if (statusModel.isVideo()){
            holder.playButton.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return vidList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnailImageView  ;
        @BindView(R.id.ibsavetogallery)
        ImageButton imageButtonDownload;
        @BindView(R.id.ib_share)
        ImageButton imageButtonShare;
        private Object BitmapDrawable;
        @BindView(R.id.container)
        CardView container;
        @BindView(R.id.play_button)
        ImageView playButton;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            imageButtonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = vidList.get(getAbsoluteAdapterPosition());
                    if(statusModel!= null){
                        try {
                            videoFragment.downloadVideo(statusModel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            ivThumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = vidList.get(getAbsoluteAdapterPosition());
                    if(statusModel!= null){
                        int positionOfAdapter = getLayoutPosition();
                        videoFragment.openVideo(statusModel,positionOfAdapter);
                    }
                }
            });

            imageButtonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = vidList.get(getAbsoluteAdapterPosition());

                    if (statusModel != null) {
                        int positionOfAdapter = getLayoutPosition();
                        android.graphics.drawable.BitmapDrawable drawable= (BitmapDrawable)ivThumbnailImageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        String bitmapPath = statusModel.getPath();
                        Uri uri= Uri.parse(bitmapPath);


                        videoFragment.shareVideo(statusModel,uri);
                    }
                }
            });

        }
    }
}
