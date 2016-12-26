package com.wacky.matdesign;

import android.app.Dialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wacky.matdesign.Activity.* ;


import com.wacky.matdesign.Adapters.LoginDataBaseAdapter;

public class LoginActivity extends AppCompatActivity {

    Button btnSignIn;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        btnSignIn = (Button)findViewById(R.id.btnLogin);

        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();



    }
    public void signIn(View V)
    {
//        final Dialog dialog = new Dialog(LoginActivity.this);
//        dialog.setContentView(R.layout.login);
//        dialog.setTitle("Login");

        // get the Refferences of views
        final EditText editTextUserName=(EditText)findViewById(R.id.login_email);
        final  EditText editTextPassword=(EditText)findViewById(R.id.login_pass);

        Button btnSignIn=(Button)findViewById(R.id.btnLogin);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();
                if (userName.matches("")) {
                    Toast.makeText(LoginActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // fetch the Password form database for respective user name
                String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {
                    Toast.makeText(LoginActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), mainNavigator.class) ;
                    startActivity(i);

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });

//        dialog.show();
    }

}
