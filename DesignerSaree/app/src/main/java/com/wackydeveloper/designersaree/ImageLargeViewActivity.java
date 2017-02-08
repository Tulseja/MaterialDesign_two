package com.wackydeveloper.designersaree;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wackydeveloper.designersaree.ImageFragments.ImageContainerFragment;

public class ImageLargeViewActivity extends AppCompatActivity implements ImageContainerFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_large_view);
    }

//    ImageLargeViewActivity txt = (ImageLargeViewActivity)getFragmentManager().findFragmentById(R.id.);
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
