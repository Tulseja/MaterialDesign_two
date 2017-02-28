package com.wackydeveloper.designersaree;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wackydeveloper.designersaree.ImageFragments.ImageContainerFragment;
import com.wackydeveloper.designersaree.model.Image;
import com.wackydeveloper.designersaree.utilities.DisplayUtility;

import java.util.ArrayList;



public class ImageLargeViewActivity extends Activity  {


    //implements ImageContainerFragment.OnFragmentInteractionListener
    private ArrayList<Image> images;
    private int selectedPosition ;
    private SubsamplingScaleImageView iv ;
    private Image img  ;
    private Target target ;
    Image currentImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_image_large_view);
        Bundle obj = getIntent().getBundleExtra("customBundle") ;


        images = (ArrayList<Image>) obj.getSerializable("images");
        selectedPosition = obj.getInt("position");
        iv = (SubsamplingScaleImageView)findViewById(R.id.image_preview) ;



//        final Picasso picasso = Picasso.with(getApplicationContext());
        currentImage = images.get(selectedPosition);

        target  = new Target() {
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

        int height = DisplayUtility.getScreenHeight(this) ;
        int width = DisplayUtility.getScreenWidth(this) ;

        Picasso.with(getApplicationContext())
                .load(currentImage.getLarge())
                .tag("Large")
                .resize(width,height)
                .centerInside()
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
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

}
