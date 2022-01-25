package fr.ghizmo.mfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class TokenVerificationActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6, inputCode7, inputCode8, inputCode9, inputCode10;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_verification);

        String token = SaveSharedPreference.getPrefToken(TokenVerificationActivity.this);
        email = getIntent().getStringExtra("email");

        if(token.length() == 0){

            inputCode1 = findViewById(R.id.inputCode1);
            inputCode2 = findViewById(R.id.inputCode2);
            inputCode3 = findViewById(R.id.inputCode3);
            inputCode4 = findViewById(R.id.inputCode4);
            inputCode5 = findViewById(R.id.inputCode5);
            inputCode6 = findViewById(R.id.inputCode6);
            inputCode7 = findViewById(R.id.inputCode7);
            inputCode8 = findViewById(R.id.inputCode8);
            inputCode9 = findViewById(R.id.inputCode9);
            inputCode10 = findViewById(R.id.inputCode10);

            setupTokenInputs();

            final ProgressBar progressBar = findViewById(R.id.progressBar);
            final Button buttonVerify = findViewById(R.id.buttonVerify);



            buttonVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputCode1.getText().toString().trim().isEmpty()
                            || inputCode2.getText().toString().trim().isEmpty()
                            || inputCode3.getText().toString().trim().isEmpty()
                            || inputCode4.getText().toString().trim().isEmpty()
                            || inputCode5.getText().toString().trim().isEmpty()
                            || inputCode6.getText().toString().trim().isEmpty()
                            || inputCode7.getText().toString().trim().isEmpty()
                            || inputCode8.getText().toString().trim().isEmpty()
                            || inputCode9.getText().toString().trim().isEmpty()
                            || inputCode10.getText().toString().trim().isEmpty()) {
                        Toast.makeText(TokenVerificationActivity.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String code = inputCode1.getText().toString()
                            + inputCode2.getText().toString()
                            + inputCode3.getText().toString()
                            + inputCode4.getText().toString()
                            + inputCode5.getText().toString()
                            + inputCode6.getText().toString()
                            + inputCode7.getText().toString()
                            + inputCode8.getText().toString()
                            + inputCode9.getText().toString()
                            + inputCode10.getText().toString();


                    progressBar.setVisibility(View.VISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);


                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "createToken";

                                String[] data = new String[2];
                                data[0] = email;
                                data[1] = code;

                                PutData putData = new PutData("http://192.168.1.128:8080/atestation", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();

                                        if (result.contains("create token valid")) {

                                            SaveSharedPreference.setPrefToken(TokenVerificationActivity.this, code);

                                            Intent intent = new Intent(getApplicationContext(), TokenDisplayActivity.class);
                                            intent.putExtra("email",email);
                                            startActivity(intent);
                                            finish();

                                        } else {

                                            //bad token

                                        }

                                    }
                                }

                            }
                        });


                }

            });

        } else if (token.length() != 0){

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[2];
                    field[0] = "email";
                    field[1] = "createToken";

                    String[] data = new String[2];
                    data[0] = email;
                    data[1] = token;

                    PutData putData = new PutData("http://192.168.1.128:8080/atestation", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {

                            String result = putData.getResult();

                            if (result.contains("create token valid")) {

                                Intent intent = new Intent(getApplicationContext(), TokenDisplayActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();

                            } else {

                                //bad token

                            }

                        }
                    }

                }
            });





        };







    }




    private void setupTokenInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode7.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode8.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode9.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCode9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    inputCode10.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}