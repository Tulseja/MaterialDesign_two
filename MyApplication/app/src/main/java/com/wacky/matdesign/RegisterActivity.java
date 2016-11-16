package com.wacky.matdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
//import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wacky.matdesign.Adapters.LoginDataBaseAdapter;

public class RegisterActivity extends AppCompatActivity {

    EditText mName,mEmail,mPass ;
    Button mCreatenew ;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mName = (EditText)findViewById(R.id.reg_fullname);
        mEmail= (EditText)findViewById(R.id.reg_email);
        mPass = (EditText)findViewById(R.id.reg_password);
        mCreatenew = (Button) findViewById(R.id.btnRegister);

        //initialize database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();



        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });

        mCreatenew.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName=mName.getText().toString();
                String password=mPass.getText().toString();
                String email = mEmail.getText().toString();
//                String confirmPassword=editTextConfirmPassword.getText().toString();

                // check if any of the fields are vaccant
                if(userName.equals("")||password.equals("") ||mEmail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }

                    // Save the Data in Database
                    loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }
}





