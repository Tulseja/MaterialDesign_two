package com.wackydeveloper.designersaree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class frontActivity extends AppCompatActivity {

//    private Button nextButton =  (Button)findViewById(R.id.play_buttonId) ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);



    }
    public void showNext(View v){
        Intent gridPic = new Intent(this , gridPicaso.class) ;
        startActivity(gridPic);
    }


}


