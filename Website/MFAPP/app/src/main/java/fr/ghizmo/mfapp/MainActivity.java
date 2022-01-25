package fr.ghizmo.mfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progress);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String username, password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if(!username.equals("") && !password.equals("")){
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "email";
                            field[1] = "password";

                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;

                            //PutData putData = new PutData("http://192.168.1.128/MFAPP/login.php", "POST", field, data);
                            PutData putData = new PutData("http://192.168.1.128:8080/appLogin", "POST", field, data);
                            if (putData.startPut()){
                                if (putData.onComplete()){
                                    progressBar.setVisibility(View.GONE);

                                    // we need to check if good credentials
                                    String result = putData.getResult();


                                    //if(result.equals("AAAA")){
                                    if(result.contains("Credentials Accepted")){
                                        Intent intent = new Intent(getApplicationContext(), TokenVerificationActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        //wrong credentials
                                    }



                                }
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}