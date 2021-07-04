package com.ams.store.whatsappstatussaver2021.Frgments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.ams.store.whatsappstatussaver2021.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedStatusAdapter extends RecyclerView.Adapter<SavedStatusAdapter.SavedStatusViewHolder> {
    private final List<StatusModel> savedStatusList;
    Context context;
    SavedFragment savedFragment;
    public SavedStatusAdapter(Context context, List<StatusModel> savedStatusList,SavedFragment savedFragment){
        this.context= context;
        this.savedStatusList = savedStatusList;
        this.savedFragment  = savedFragment;

    }

    @NonNull
    @Override
    public SavedStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_status_for_saved_item,parent,false);

        return new SavedStatusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedStatusViewHolder holder, int position) {
        StatusModel statusModel= savedStatusList.get(position);
        holder.ivThumbnailImageView.setImageBitmap(statusModel.getThumbnail());
        if (statusModel.isVideo()){
            holder.playButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return savedStatusList.size();
    }


    public class SavedStatusViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnailImageView;
        @BindView(R.id.play_button)
        ImageView playButton;

        public SavedStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ivThumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StatusModel statusModel = savedStatusList.get(getAbsoluteAdapterPosition());

                    if (statusModel != null) {
                        int positionOfAdapter = getLayoutPosition();
                        savedFragment.openStatus(statusModel,positionOfAdapter);
                    }
                }
            });
        }
    }

}
