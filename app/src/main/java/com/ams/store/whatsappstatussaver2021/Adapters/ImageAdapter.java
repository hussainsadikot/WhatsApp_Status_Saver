package com.ams.store.whatsappstatussaver2021.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ams.store.whatsappstatussaver2021.Frgments.ImageFragment;
import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.ams.store.whatsappstatussaver2021.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<StatusModel> imageList;
    Context context;
    ImageFragment imageFragment;
//    IRecyclerViewClickListener clickListener;
    public ImageAdapter(Context context, List<StatusModel> imageList,ImageFragment imageFragment)
    {
        this.context= context;
        this.imageList = imageList;
        this.imageFragment  = imageFragment;

    }
//    public ImageAdapter(Context context, List<StatusModel> imageList,ImageFragment imageFragment,IRecyclerViewClickListener clickListener)
//    {
//        this.context= context;
//        this.imageList = imageList;
//        this.imageFragment  = imageFragment;
//        this.clickListener = clickListener;
//    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);

        return new ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        StatusModel statusModel= imageList.get(position);
//        holder.ivThumbnailImageView.setImageBitmap(statusModel.getThumbnail());
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "Hey It's Working", Toast.LENGTH_SHORT).show();
//            }
//        });



        ImageView imageView = holder.ivThumbnailImageView;
        Glide.with(context).load(statusModel.getPath()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnailImageView;
        @BindView(R.id.ibsavetogallery)
        ImageButton imageButtonDownload;
        @BindView(R.id.ib_share)
        ImageButton imageButtonShare;
        private Object BitmapDrawable;
        @BindView(R.id.container)
        CardView container;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);


            //In this Code We are Clicking the thumbnail and
            //Start Download Image Method From Fragment

            imageButtonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StatusModel statusModel = imageList.get(getAbsoluteAdapterPosition());
                    if(statusModel!= null){
                        try {
                            imageFragment.downloadImage(statusModel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            //Opening new Full Screen Activity but touch applied to recyclerview item position
//            @Override
//            public void onClick(View v){
//                  clickListener.onClick(v,getAbsoluteAdapterPosition());
//            }


            ivThumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = imageList.get(getAbsoluteAdapterPosition());

                    if (statusModel != null) {
                        int positionOfAdapter = getLayoutPosition();
                        imageFragment.openImage(statusModel,positionOfAdapter);
                    }
                }
            });
            imageButtonShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = imageList.get(getAbsoluteAdapterPosition());

                    if (statusModel != null) {
                        int positionOfAdapter = getLayoutPosition();
                        BitmapDrawable drawable= (BitmapDrawable)ivThumbnailImageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        String bitmapPath = statusModel.getPath();
                        Uri uri= Uri.parse(bitmapPath);


                        imageFragment.shareImage(statusModel,uri);
                    }
                }
            });

        }
    }
}