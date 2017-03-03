package com.wackydeveloper.designersaree;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;
//import com.wackydeveloper.designersaree.Adapter.AppController;
import com.wackydeveloper.designersaree.Adapter.GalleryAdapter;
import com.wackydeveloper.designersaree.Helper.SlideshowDialogFragment;
import com.wackydeveloper.designersaree.model.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static com.wackydeveloper.designersaree.SwipeViewActivity.MyPREFERENCES;

public class gridPicaso extends AppCompatActivity {


    private String TAG = gridPicaso.class.getSimpleName();
    private static final String endpoint = "https://drive.google.com/uc?export=download&id=0Bwg8BpmNgTvUQnl4WEpSRkRCXzg";
    private ArrayList<String> images;
    //private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;


    //private OnFragmentInteractionListener mListener;
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String  data = "" ;
        for(int i = 1 ; i <  SwipeViewActivity.keyGenerator; i++){
            String key = "URL_"+Integer.toString(i) ;
            data = sharedPreferences.getString(key,"");
            if(!images.contains(data)) {
                images.add(data);
            }
            Log.e("Nikhil", data) ;
            Log.d("Nikhil", Integer.toString(i) ) ;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_picaso);
        images = new ArrayList<String>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LoadPreferences();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            final Picasso picasso = Picasso.with(getApplicationContext());

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){

                if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_SETTLING  ) {
                    picasso.resumeTag("Lazy Load");
                } else {
                    picasso.pauseTag("Lazy Load");
                }
            }
            @Override
            public void onScrolled(RecyclerView rc , int dx , int dy){
                picasso.resumeTag("Lazy Load");

            }
 });
//        pDialog = new ProgressDialog(getApplicationContext());
        mAdapter = new GalleryAdapter(this, images);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


//        fetchImages();
        


    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("photos_copy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



//    private void fetchImages() {
//        int  ij = 0 ;
//        images.clear();
//        try {
//            JSONObject obj = new JSONObject(loadJSONFromAsset());
//            JSONArray m_jArry = obj.getJSONArray("Urls");
//
//            for (int i = 0; i < m_jArry.length(); i++) {
//                Image image = new Image();
//                JSONObject object = m_jArry.getJSONObject(i);
//                image.setName(object.getString("name"));
//                JSONObject url = object.getJSONObject("url");
//                image.setSmall(url.getString("small"));
//                image.setMedium(url.getString("medium"));
//                image.setLarge(url.getString("large"));
//                images.add(image);
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mAdapter.notifyDataSetChanged();
//
//
//
//    }
//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 5000);
//    }
}
