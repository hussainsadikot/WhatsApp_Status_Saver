package com.ams.store.whatsappstatussaver2021.Frgments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ams.store.whatsappstatussaver2021.Adapters.ImageAdapter;
import com.ams.store.whatsappstatussaver2021.Adapters.VideoAdapter;
import com.ams.store.whatsappstatussaver2021.FullScreenImageActivity;
import com.ams.store.whatsappstatussaver2021.MainActivity;
import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.ams.store.whatsappstatussaver2021.R;
import com.ams.store.whatsappstatussaver2021.Utils.MyConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment {
    ArrayList<StatusModel> vidModelArrayList;
    Handler handler = new Handler();
    VideoAdapter videoAdapter;
    @BindView(R.id.recycler_view_vid)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar_vid)
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        vidModelArrayList = new ArrayList<>();
                recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//        getStatusVideoes();
        getData();
    }
    private void getData() {
//    StatusModel model;
//    String targetPath =  Environment.getExternalStorageDirectory().getAbsolutePath() +"WhatsApp/Media/.Statuses";
//        File targetDirectory = new File(targetPath);
//        File[] allFiles = targetDirectory.listFiles();
//        assert allFiles != null;
//        Arrays.sort(allFiles);

        File[] statusFiles= MyConstants.STATUS_DIRECTORY.listFiles();
        if (statusFiles != null && statusFiles.length!= 0){
            Arrays.sort(statusFiles);
            for (final File statusFile:statusFiles){
                StatusModel statusModel= new StatusModel(statusFile,statusFile.getName(),statusFile.getAbsolutePath());
//                statusModel.setThumbnail(getThumbnail(statusModel));
                boolean nomedia = statusModel.getTitle().endsWith(".nomedia");
                if (statusModel.isVideo() && !nomedia){
                    vidModelArrayList.add(statusModel);
                }
            }
//        for (int i = 0; i <allFiles.length ; i++) {
//            File file = allFiles[i];
//            if (Uri.fromFile(file).toString().endsWith(".png")||
//                    Uri.fromFile(file).toString().endsWith(".jpg")){
//                model = new StatusModel(file,file.getName(),allFiles[i].getAbsolutePath(),"WASavedStatus"+i,file.getName(),Uri.fromFile(file));
//            imageModelArrayList.add(model);
//            }

        }
        videoAdapter = new VideoAdapter(getContext(),vidModelArrayList,VideoFragment.this);
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.notifyDataSetChanged();
    }
    private void getStatusVideoes() {
        if(MyConstants.STATUS_DIRECTORY.exists()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles= MyConstants.STATUS_DIRECTORY.listFiles();
                    if (statusFiles != null && statusFiles.length> 0){
                        Arrays.sort(statusFiles);
                        for (final File statusFile:statusFiles){
                            StatusModel statusModel= new StatusModel(statusFile,statusFile.getName(),statusFile.getAbsolutePath());
                            statusModel.setThumbnail(getThumbnail(statusModel));
                            boolean nomedia = statusModel.getTitle().endsWith(".nomedia");
                            if (statusModel.isVideo() && !nomedia){
                                vidModelArrayList.add(statusModel);
                            }
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                videoAdapter = new VideoAdapter(getContext(),vidModelArrayList,VideoFragment.this);
                                recyclerView.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();

                            }
                        });


                    }
                    else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Dir does not exist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                }
            }).start();



        }


    }
    private Bitmap getThumbnail(StatusModel statusModel) {
        if(statusModel.isVideo()){
//            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath(),MyConstants.THUMBSIZE,MyConstants.THUMBSIZE)

            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        else{
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()),MyConstants.THUMBSIZE,MyConstants.THUMBSIZE);
        }
    }
    public void downloadVideo(StatusModel statusModel) throws IOException {

        File file = new File(MyConstants.APP_DIR);
        if (!file.exists()){
            file.mkdirs();
        }
        File destFile = new File(file+ File.separator + statusModel.getTitle());
        if (destFile.exists()){
            destFile.delete();
        }
        copyFile(statusModel.getFile(),destFile);
        ((MainActivity)getActivity()).loadAdsFromDownloadButton();
        Toast.makeText(getActivity(), "Download Complete", Toast.LENGTH_SHORT).show();

        Intent i =new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(destFile));
        requireActivity().sendBroadcast(i);
    }

    private void copyFile(File file, File destFile) throws IOException {
        if((destFile.getParentFile()).exists()){
            destFile.getParentFile().mkdirs();

        }
        if (!destFile.exists()){
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(file).getChannel();
        destination= new FileOutputStream(destFile).getChannel();
        destination.transferFrom(source,0,source.size());
        source.close();
        destination.close();

    }
    public void openVideo(StatusModel statusModel, int positionOfAdapter) {
        //open Full screen Activity
        String path =statusModel.getPath();
        Intent i =new Intent(getActivity(), FullScreenImageActivity.class);
        Uri myUri = Uri.fromFile(statusModel.getFile());
        i.putExtra("video_file_index_number",positionOfAdapter);
        i.putExtra("video_path",myUri.toString());
        startActivity(i);
//        imageView.setVisibility(View.VISIBLE);
//        imageView.setImageURI(myUri);
//        Toast.makeText(getActivity(), "You Touched "+statusModel.getPath(), Toast.LENGTH_SHORT).show();
    }

    public void shareVideo(StatusModel statusModel, Uri uri) {

        Intent shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/mp4");
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Share This Status");
        startActivity(Intent.createChooser(shareIntent,"Share This Status"));

    }
}
