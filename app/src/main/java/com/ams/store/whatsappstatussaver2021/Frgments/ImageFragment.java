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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ams.store.whatsappstatussaver2021.Adapters.ImageAdapter;
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

public class ImageFragment extends Fragment {
    ArrayList<StatusModel> imageModelArrayList;
    Handler handler = new Handler();
    ImageAdapter imageAdapter;

    @BindView(R.id.recycler_view_image)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar_image)
    ProgressBar progressBar;
    @BindView(R.id.full_screen_image_view)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        imageModelArrayList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
//        getStatus();
        getData();

//        final IRecyclerViewClickListener listenerImage= new IRecyclerViewClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Toast.makeText(getContext(), "You Touched"+position, Toast.LENGTH_SHORT).show();
//            }
//        };
//        if(!imageModelArrayList.isEmpty()){
//            imageAdapter = new ImageAdapter(getContext(),imageModelArrayList,ImageFragment.this);
//            recyclerView.setAdapter(imageAdapter);
//            imageAdapter.notifyDataSetChanged();
//
//        }
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
                if (!statusModel.isVideo() && !nomedia){
                    imageModelArrayList.add(statusModel);
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
        imageAdapter = new ImageAdapter(getActivity(),imageModelArrayList,ImageFragment.this);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }
    private void getStatus() {
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
                            if (!statusModel.isVideo() && !nomedia){
                                imageModelArrayList.add(statusModel);
                            }
                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
//                                StatusModel statusModel = new StatusModel()
//                                imageModelArrayList.remove(0);
                            imageAdapter = new ImageAdapter(getContext(),imageModelArrayList,ImageFragment.this);
                            recyclerView.setAdapter(imageAdapter);
                            imageAdapter.notifyDataSetChanged();

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

    public void downloadImage(StatusModel statusModel) throws IOException {

        File file = new File(MyConstants.APP_DIR);
        if (!file.exists()){
            file.mkdirs();
        }
        File destFile = new File(file+ File.separator + statusModel.getTitle());
        if (destFile.exists()){
            destFile.delete();
        }
        ((MainActivity)getActivity()).loadAdsFromDownloadButton();
        copyFile(statusModel.getFile(),destFile);

        Toast.makeText(getActivity(), "Download Complete", Toast.LENGTH_SHORT).show();

//        Intent i =new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        i.setData(Uri.fromFile(destFile));
//        getActivity().sendBroadcast(i);

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

    public void openImage(StatusModel statusModel, int positionOfAdapter) {
                //open Full screen Activity
        String path =statusModel.getPath();
        Intent i =new Intent(getActivity(), FullScreenImageActivity.class);
       Uri myUri = Uri.fromFile(statusModel.getFile());
        i.putExtra("file_index_number",positionOfAdapter);
        i.putExtra("path",myUri.toString());
        startActivity(i);
//        imageView.setVisibility(View.VISIBLE);
//        imageView.setImageURI(myUri);
//        Toast.makeText(getActivity(), "You Touched "+statusModel.getPath(), Toast.LENGTH_SHORT).show();
    }

    public void shareImage(StatusModel statusModel, Uri uri) {



            Intent shareIntent =new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Share This Status");
            startActivity(Intent.createChooser(shareIntent,"Share This Status"));


    }
}
