package com.ams.store.whatsappstatussaver2021;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.viewpager.widget.ViewPager;

import com.ams.store.whatsappstatussaver2021.Models.StatusModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenImageActivity extends Activity {
    @BindView(R.id.full_image) ImageView imageView;
    @BindView(R.id.viewpager_full_screen)
    ViewPager viewPagerFullScreen;
    @BindView(R.id.full_video)
    VideoView videoView;
    public Uri setImage;
    public Uri setVideo;
    public String filePath;
    StatusModel statusModel;
    public int StartingIndexImage;
    public int StartingIndexVideo;
    File file;
    ArrayList<StatusModel> imageModelArrayList;
    private InterstitialAd mInterstitialAd;
    private AdView adView ;
    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    public void onBackPressed() {
//        if (mInterstitialAd != null) {
//            mInterstitialAd.show(FullScreenImageActivity.this);
//        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screem_image);
        ButterKnife.bind(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        if (savedInstanceState== null){
            Intent intent = getIntent();

            Bundle extras = getIntent().getExtras();

            assert extras != null;
            if(extras.containsKey("path")){
                setImage=Uri.parse( intent.getStringExtra("path"));
                StartingIndexImage = intent.getIntExtra("file_index_number",0);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageURI(setImage);
            }
            else if (extras.containsKey("video_path")){
                setVideo=Uri.parse( intent.getStringExtra("video_path"));
                StartingIndexVideo = intent.getIntExtra("video_file_index_number",0);
//                DisplayMetrics metrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams)
//                        videoView.getLayoutParams();
//                params.width = metrics.widthPixels;
//                params.height = metrics.heightPixels;
//                params.leftMargin = 0;
//                videoView.setLayoutParams(params);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(setVideo);
                MediaController mediaController = new MediaController(this);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        videoView.start();
                    }
                });
            }
        }

//        FullSizeAdapter fullSizeAdapter= new FullSizeAdapter(this,imageModelArrayList);
//        viewPagerFullScreen.setAdapter(fullSizeAdapter);
//        viewPagerFullScreen.setCurrentItem(StartingIndex,true);
//        Glide.with(this).load(setImage).into(imageView);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {}
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();

//        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
//
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                // The mInterstitialAd reference will be null until
//                // an ad is loaded.
//                mInterstitialAd = interstitialAd;
//                Log.i(TAG, "onAdLoaded");
//            }
//
//
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                // Handle the error
//                Log.i(TAG, loadAdError.getMessage());
//                mInterstitialAd = null;
//            }
//        });
//        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                // Called when fullscreen content is dismissed.
//                Log.d("TAG", "The ad was dismissed.");
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                // Called when fullscreen content failed to show.
//                Log.d("TAG", "The ad failed to show.");
//            }
//
//            @Override
//            public void onAdShowedFullScreenContent() {
//                // Called when fullscreen content is shown.
//                // Make sure to set your reference to null so you don't
//                // show it a second time.
//                mInterstitialAd = null;
//                Log.d("TAG", "The ad was shown.");
//            }
//        });
    }
}