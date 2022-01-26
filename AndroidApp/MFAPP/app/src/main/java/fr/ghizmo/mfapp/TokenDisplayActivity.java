package fr.ghizmo.mfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class TokenDisplayActivity extends AppCompatActivity {

    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_display);

        String token = SaveSharedPreference.getPrefToken(TokenDisplayActivity.this);
        email = getIntent().getStringExtra("email");

        TextView loginToken = (TextView)findViewById(R.id.loginToken);

        int delay = 2000;


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "email";
                field[1] = "createToken";

                String[] data = new String[2];
                data[0] = email;
                data[1] = token;

                PutData putData = new PutData("http://192.168.1.128:8080/loginToken", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {

                        String result = putData.getResult();
                        loginToken.setText(result);

                    }
                }

                handler.postDelayed(this, delay);

            }
        }, delay);
    }
}