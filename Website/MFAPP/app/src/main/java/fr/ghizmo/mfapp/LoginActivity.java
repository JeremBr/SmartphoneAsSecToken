package fr.ghizmo.mfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = (EditText)findViewById(R.id.username);
        passwordET = (EditText)findViewById(R.id.password);
    }

    public void OnLogin(View view){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,email,password);
    }
}