package com.ams.store.whatsappstatussaver2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ams.store.whatsappstatussaver2021.Adapters.PagerAdapter;
import com.ams.store.whatsappstatussaver2021.Frgments.PermissionFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnUserEarnedRewardListener {
    private int STORAGE_PERMISSION_CODE =1;
   private AdView adView ;



    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.toolbar_main1)
    Toolbar toolbarPermission;
    @BindView(R.id.tabs_main)
    TabLayout tabLayout;
    @BindView(R.id.tabs_main1)
    TabLayout tabLayoutPermission;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.view_pager_permission)
    ViewPager viewPagerPermission;
    @BindView(R.id.view_flipper_main)
    ViewFlipper viewFlipper;
    @BindView(R.id.adView)
    AdView mAdView;
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        setSupportActionBar(toolbar);
//
//        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
//        tabLayout.setupWithViewPager(viewPager);
//        viewFlipper.setDisplayedChild(1);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        loadAdsFromDownloadButton();
        initializeViewFlipperPermission();

        setSupportActionBar(toolbar);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//        if(viewFlipper.getId()== R.id.view_pager){
//            tabLayout.setupWithViewPager(viewPager);
//        } else {
//            tabLayout.setupWithViewPager(viewPagerPermission);
//        }
//        switch (viewFlipper.getId()){
//            case R.id.view_pager:
//                tabLayout.setupWithViewPager(viewPager);
//                break;
//            case R.id.view_pager_permission:
//                tabLayout.setupWithViewPager(viewPagerPermission);
//                break;
//        }
//        final IRecyclerViewClickListener listener =new IRecyclerViewClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                 //Open Full Screen Activity
//            }
//        };


        check4permission();
//        mAdView.setAdSize(AdSize.BANNER);

//        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

//        loadAdsFromDownloadButton();
    }

    public void loadAdsFromDownloadButton() {
        RewardedInterstitialAd.load(MainActivity.this,"ca-app-pub-1839687257478798/1407770961",
                new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                        super.onAdLoaded(rewardedInterstitialAd);
                        rewardedInterstitialAd.show(MainActivity.this,MainActivity.this::onUserEarnedReward);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                });

    }

    private void initializeViewFlipperPermission() {
        setSupportActionBar(toolbarPermission);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Images");
        arrayList.add("Videos");
        prepareViewPager(viewPagerPermission,arrayList,tabLayoutPermission);
        tabLayoutPermission.setupWithViewPager(viewPagerPermission);
        viewFlipper.setDisplayedChild(0);
    }

    private void check4permission() {
        // check if permissions already granted
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED ){
            Toast.makeText(this, "You Have Already Granted Permission", Toast.LENGTH_SHORT).show();

            setSupportActionBar(toolbar);

            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//            Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
            tabLayout.setupWithViewPager(viewPager);
            viewFlipper.setDisplayedChild(1);
        }else{
            requestStoragePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                setSupportActionBar(toolbar);

                viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                tabLayout.setupWithViewPager(viewPager);
                viewFlipper.setDisplayedChild(1);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                viewFlipper.setDisplayedChild(0);
            }
        }
    }

    public void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Require")
                    .setMessage("Storage Permission Needed")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
                    }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    private void prepareViewPager(ViewPager viewpager, ArrayList<String> arraylist,TabLayout tabLayout){
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        PermissionFragment fragment = new PermissionFragment();
        for (int i = 0; i <arraylist.size() ; i++) {
            adapter.addFragment(fragment,arraylist.get(i));
            fragment = new PermissionFragment();
        }
        viewPagerPermission.setAdapter(adapter);

    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//        Toast.makeText(this, "Download Complete", Toast.LENGTH_SHORT).show();
    }

    private class MainAdapter extends FragmentPagerAdapter {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList= new ArrayList<>();


        public void addFragment(Fragment fragment, String title){
        arrayList.add(title);
        fragmentList.add(fragment);

        }


        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }

}