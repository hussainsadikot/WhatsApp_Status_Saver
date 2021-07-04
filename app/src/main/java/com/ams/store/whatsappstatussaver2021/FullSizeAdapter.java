package com.ams.store.whatsappstatussaver2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ams.store.whatsappstatussaver2021.Models.StatusModel;

import java.util.List;

public class FullSizeAdapter extends PagerAdapter {
    private final List<StatusModel> imageList;
    Context context;
    LayoutInflater inflater;

    public FullSizeAdapter(Context context, List<StatusModel> imageList) {
        this.context= context;
        this.imageList = imageList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager)container;
        View view =(View)container;
        vp.removeView(view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.full_item,null);

        ImageView imageView = (ImageView)view.findViewById(R.id.full_screen_image_view);
        Glide.with(context).load(imageList.get(position)).apply(new RequestOptions().centerInside()).into(imageView);
        ViewPager vp = (ViewPager)container;
        vp.addView(view,0);
        return view;

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
