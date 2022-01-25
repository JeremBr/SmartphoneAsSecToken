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

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private String verificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_verification);

        String login_url = "192.168.1.128:8080";

        String token = SaveSharedPreference.getPrefToken(TokenVerificationActivity.this);

        if(token.length() == 0){

            inputCode1 = findViewById(R.id.inputCode1);
            inputCode2 = findViewById(R.id.inputCode2);
            inputCode3 = findViewById(R.id.inputCode3);
            inputCode4 = findViewById(R.id.inputCode4);
            inputCode5 = findViewById(R.id.inputCode5);
            inputCode6 = findViewById(R.id.inputCode6);

            setupTokenInputs();

            final ProgressBar progressBar = findViewById(R.id.progressBar);
            final Button buttonVerify = findViewById(R.id.buttonVerify);

            verificationId = getIntent().getStringExtra("verificationId");

            buttonVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputCode1.getText().toString().trim().isEmpty()
                            || inputCode2.getText().toString().trim().isEmpty()
                            || inputCode3.getText().toString().trim().isEmpty()
                            || inputCode4.getText().toString().trim().isEmpty()
                            || inputCode5.getText().toString().trim().isEmpty()
                            || inputCode6.getText().toString().trim().isEmpty()){
                        Toast.makeText(TokenVerificationActivity.this,"Please enter valid code",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String code = inputCode1.getText().toString()
                            + inputCode2.getText().toString()
                            + inputCode3.getText().toString()
                            + inputCode4.getText().toString()
                            + inputCode5.getText().toString()
                            + inputCode6.getText().toString();

                    if(verificationId != null){
                        progressBar.setVisibility(View.VISIBLE);
                        buttonVerify.setVisibility(View.INVISIBLE);

                        try{

                            URL url = new URL(login_url);
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);

                            OutputStream outputStream = httpURLConnection.getOutputStream();
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                            String post_data = URLEncoder.encode("createToken","UTF-8")+"="+URLEncoder.encode(code,"UTF-8");
                            bufferedWriter.write(post_data);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            outputStream.close();

                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                            String result="";
                            String line="";

                            while((line = bufferedReader.readLine()) != null){
                                result += line;
                            }
                            bufferedReader.close();
                            httpURLConnection.disconnect();

                            if(result.contains("Valid Token")){

                                SaveSharedPreference.setPrefToken(TokenVerificationActivity.this,code);

                                Intent intent = new Intent(getApplicationContext(), TokenDisplayActivity.class);
                                startActivity(intent);
                                finish();


                            } else {

                                //bad token

                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }






                    }
                }
            });

        } else if (token.length() != 0){


            try{

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("createToken","UTF-8")+"="+URLEncoder.encode(token,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";

                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                bufferedReader.close();
                httpURLConnection.disconnect();

                if(result.contains("Valid Token")){

                    Intent intent = new Intent(getApplicationContext(), TokenDisplayActivity.class);
                    startActivity(intent);
                    finish();


                } else {

                    //bad token

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


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
    }
}