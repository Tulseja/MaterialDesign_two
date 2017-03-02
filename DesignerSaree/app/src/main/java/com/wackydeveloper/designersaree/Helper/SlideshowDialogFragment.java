package com.wackydeveloper.designersaree.Helper;

/**
 * Created by hp on 3/1/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wackydeveloper.designersaree.R;
import com.wackydeveloper.designersaree.model.Image;
import com.wackydeveloper.designersaree.utilities.DisplayUtility;

import static android.R.attr.id;

public class SlideshowDialogFragment extends DialogFragment {

    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<String> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
//     PhotoViewAttacher mAttacher;

    //private ProgressBar mProgress;
    private int mProgressStatus = 0;


    private Handler mHandler = new Handler();

    public static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        lblTitle = (TextView) v.findViewById(R.id.title);
//        mProgress = (ProgressBar)v.findViewById(R.id.progressBar) ;
//        lblDate = (TextView) v.findViewById(R.id.date);

        images = (ArrayList<String>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        String image = images.get(position);
//        lblTitle.setText(image.getName());
//        lblDate.setText(image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false) ;
            final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) view.findViewById(R.id.image_preview);
//            mProgress = (ProgressBar)viewin.findViewById(R.id.progressBar);
//            mProgress.setVisibility(View.VISIBLE);
//            mProgress.setProgress(50);
//


            final Picasso picasso = Picasso.with(getContext());
            picasso.pauseTag("Lazy Load");
            String image = images.get(position);

            final Target target  = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // loading of the bitmap was a success
                    // TODO do some action with the bitmap
                    imageView.setImage(ImageSource.bitmap(bitmap));
//                    mProgress.setProgress(99);
//                    mProgress.setVisibility(View.GONE);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    // loading of the bitmap failed
                    // TODO do some action/warning/error message
                    Toast.makeText(getContext(), "Can't Load this.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            int height = DisplayUtility.getScreenHeight(getContext()) ;
            int width = DisplayUtility.getScreenWidth(getContext()) ;

            Picasso.with(getContext())
                    .load(image)
                    .tag("Large")
                    .centerInside()
                    .resize(width,height)
                    .placeholder(R.drawable.animation)
                    .into(target ) ;
            imageView.setTag(target);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    }





