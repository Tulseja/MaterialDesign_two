package com.wackydeveloper.designersaree;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wackydeveloper.designersaree.ImageFragments.ImageContainerFragment;
import com.wackydeveloper.designersaree.model.Image;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ImageLargeViewActivity extends Activity implements ImageContainerFragment.OnFragmentInteractionListener {

    private ArrayList<Image> images;
    private int selectedPosition ;
    private SubsamplingScaleImageView iv ;
    private Image img  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_large_view);
        Bundle obj = getIntent().getBundleExtra("customBundle") ;


        images = (ArrayList<Image>) obj.getSerializable("images");
        selectedPosition = obj.getInt("position");
        iv = (SubsamplingScaleImageView)findViewById(R.id.image_preview) ;

        final Picasso picasso = Picasso.with(getApplicationContext());
        Image image = images.get(selectedPosition);

        final Target target  = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loading of the bitmap was a success
                // TODO do some action with the bitmap
                iv.setImage(ImageSource.bitmap(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // loading of the bitmap failed
                // TODO do some action/warning/error message
                Toast.makeText(getApplicationContext(), "Can't Load this.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getApplicationContext())
                .load(image.getLarge())
                .tag("Large")
                .placeholder(R.drawable.animation)
                .into(target ) ;




    }
//    private void displayMetaInfo(int position) {
//        lblCount.setText((position + 1) + " of " + images.size());
//
//        Image image = images.get(position);
//        lblTitle.setText(image.getName());
////        lblDate.setText(image.getTimestamp());
//    }

//    ImageLargeViewActivity txt = (ImageLargeViewActivity)getFragmentManager().findFragmentById(R.id.);
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
