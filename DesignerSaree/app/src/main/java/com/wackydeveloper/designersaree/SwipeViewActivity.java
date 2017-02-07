package com.wackydeveloper.designersaree;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private static final int STACK_SIZE = 4;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<Image>();
        fetchImages();
//
//        displayNames = getResources().getStringArray(R.array.display_names);
//        userNames = getResources().getStringArray(R.array.usernames);
//        avatarUrls = getResources().getStringArray(R.array.avatar_urls);

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
                        else if( integer == 0){
                            onCompleted();
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
}
