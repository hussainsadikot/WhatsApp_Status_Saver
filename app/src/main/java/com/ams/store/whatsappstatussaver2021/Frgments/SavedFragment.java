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

import com.ams.store.whatsappstatussaver2021.Adapters.VideoAdapter;
import com.ams.store.whatsappstatussaver2021.FullScreenImageActivity;
import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.ams.store.whatsappstatussaver2021.R;
import com.ams.store.whatsappstatussaver2021.Utils.MyConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedFragment extends Fragment {
    ArrayList<StatusModel> savedModelArrayList;
    SavedStatusAdapter savedStatusAdapter;
    @BindView(R.id.recycler_view_saved)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar_saved)
    ProgressBar progressBar;
    Handler handler = new Handler();
    long initialFileSize;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        savedModelArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        getSavedStatus();
//        checkIfFileLengthChange();
        getData();

    }

    private void checkIfFileLengthChange() {
        long currentSize = savedModelArrayList.size();
        if (currentSize!=initialFileSize){
            File[] updateSavedStatusFiles = MyConstants.APp_DIRECTORY.listFiles();
            if (updateSavedStatusFiles != null && updateSavedStatusFiles.length != 0) {
                Arrays.sort(updateSavedStatusFiles);
                for (final File statusFile : updateSavedStatusFiles) {
                    StatusModel updatedStatusModel = new StatusModel(statusFile, statusFile.getName(), statusFile.getAbsolutePath());
                    updatedStatusModel.setThumbnail(getThumbnail(updatedStatusModel));

                    boolean nomedia = updatedStatusModel.getTitle().endsWith(".nomedia");
                    if (!nomedia) {
                        savedModelArrayList.add(updatedStatusModel);
                        initialFileSize = savedModelArrayList.size();
                    }
                }

            }

//            updateFragment();
//            savedStatusAdapter.notifyDataSetChanged();
//            Bundle bundle = new Bundle();
//
//            String path = statusModel.getPath();
//            Uri myUri = Uri.fromFile(statusModel.getFile());
//            i.putExtra("file_index_number", positionOfAdapter);
//            i.putExtra("path", myUri.toString());
//            bundle.putStringArrayList("file update",savedModelArrayList.);
//            SavedFragment savedFragment = new SavedFragment();
//            getFragmentManager().beginTransaction().replace(R.id.recycler_view_saved,savedFragment).addToBackStack(null).commit();


        }
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
                    savedModelArrayList.add(statusModel);
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
        savedStatusAdapter = new SavedStatusAdapter(getContext(),savedModelArrayList,SavedFragment.this);
        recyclerView.setAdapter(savedStatusAdapter);
        savedStatusAdapter.notifyDataSetChanged();
    }
    private void getSavedStatus() {
        if (MyConstants.APp_DIRECTORY.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] savedStatusFiles = MyConstants.APp_DIRECTORY.listFiles();
                    if (savedStatusFiles != null && savedStatusFiles.length > 0) {
                        Arrays.sort(savedStatusFiles);

                        for (final File statusFile : savedStatusFiles) {
                            StatusModel statusModel = new StatusModel(statusFile, statusFile.getName(), statusFile.getAbsolutePath());
                            statusModel.setThumbnail(getThumbnail(statusModel));

                            boolean nomedia = statusModel.getTitle().endsWith(".nomedia");
                            if (!nomedia) {
                                savedModelArrayList.add(statusModel);
//                                 initialFileSize= savedModelArrayList.size();
                            }
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                savedStatusAdapter = new SavedStatusAdapter(getContext(), savedModelArrayList, SavedFragment.this);
                                recyclerView.setAdapter(savedStatusAdapter);
                                savedStatusAdapter.notifyDataSetChanged();

                            }
                        });


                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Dir does not exist", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            }).start();


        }

    }

    public Bitmap getThumbnail(StatusModel statusModel) {
        if (statusModel.isVideo()) {
//            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath(),MyConstants.THUMBSIZE,MyConstants.THUMBSIZE)

            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()), MyConstants.THUMBSIZE, MyConstants.THUMBSIZE);
        }
    }

    public void openStatus(StatusModel statusModel, int positionOfAdapter) {
        //open Full screen Activity

        String path = statusModel.getPath();
        if (path.endsWith("mp4")) {
            Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
            Uri myUri = Uri.fromFile(statusModel.getFile());
            i.putExtra("video_file_index_number", positionOfAdapter);
            i.putExtra("video_path", myUri.toString());
            startActivity(i);
        } else {
            Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
            Uri myUri = Uri.fromFile(statusModel.getFile());
            i.putExtra("file_index_number", positionOfAdapter);
            i.putExtra("path", myUri.toString());
            startActivity(i);
        }
//        imageView.setVisibility(View.VISIBLE);
//        imageView.setImageURI(myUri);
//        Toast.makeText(getActivity(), "You Touched "+statusModel.getPath(), Toast.LENGTH_SHORT).show();
    }
}
