package com.wackydeveloper.designersaree;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.wackydeveloper.designersaree.Adapter.GalleryAdapter;
import com.wackydeveloper.designersaree.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import com.wackydeveloper.designersaree.model.User;
import com.wackydeveloper.designersaree.ui.TinderCardView;
import com.wackydeveloper.designersaree.ui.TinderStackLayout;


public class SwipeViewActivity extends AppCompatActivity {

    public static final int STACK_SIZE = 4;

    public static int topCardIndex  ;
    // endregion
    boolean doubleBackToExitPressedOnce = false;
    // region Views
    private TinderStackLayout tinderStackLayout;
    // endregion
    private ArrayList<Image> images;

    private GalleryAdapter mAdapter;
    // region Member Variables
    private String[] displayNames, userNames, avatarUrls;
    private int index = 0;
    // endregion

    // region Listeners
    // endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topCardIndex = 0 ;
        images = new ArrayList<Image>();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Subtitile");
        fetchImages();


        tinderStackLayout = (TinderStackLayout) findViewById(R.id.tsl);

        TinderCardView tc;
        for(int i=index; index<i+STACK_SIZE; index++){
            tc = new TinderCardView(this);
            tc.bind(getImage(index));
            tinderStackLayout.addCard(tc);
        }



        tinderStackLayout.getPublishSubject()
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),"That's all folks." ,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer == 1){
                            TinderCardView tc;
                            for(int i=index; index<i+(STACK_SIZE-1); index++){
                                tc = new TinderCardView(SwipeViewActivity.this);
                                tc.bind(getImage(index));
                                tinderStackLayout.addCard(tc);
                            }
                        }

                    }
                });
    }

    // region Helper Methods
    private User getUser(int index){
        User user = new User()  ;
        user.setAvatarUrl(avatarUrls[index]);
        user.setDisplayName(displayNames[index]);
        user.setUsername(userNames[index]);
        return user;
    }

    private Image getImage(int index){
        Image img = images.get(index) ;
        return img ;
    }


    // endregion

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



    private void fetchImages() {
        int  ij = 0 ;

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("Urls");

            for (int i = 0; i < m_jArry.length(); i++) {
                Image image = new Image();
                JSONObject object = m_jArry.getJSONObject(i);
                image.setName(object.getString("name"));
                JSONObject url = object.getJSONObject("url");
                image.setSmall(url.getString("small"));
                image.setMedium(url.getString("medium"));
                image.setLarge(url.getString("large"));
                images.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        mAdapter.notifyDataSetChanged();



    }
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 5000);
    }

    public ArrayList<Image> getImagesObject(){
         if(images == null){
             return null ;
         } else {
             return images ;
         }

    }
    public int getIndex(){
        return  tinderStackLayout.getChildCount() ;
    }

public static int getTopCardIndex() {
        return topCardIndex ;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                Toast.makeText(this, "Favourite Selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
//    public int getActionBarHeight(){
//        TypedValue tv = new TypedValue();
//        int actionBarHeight  = 0  ;
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//        {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//        }
//
//        return actionBarHeight ;
//    }
}
