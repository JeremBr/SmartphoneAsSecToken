package fr.ghizmo.mfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendTokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_token);

        final EditText inputMobile = findViewById(R.id.inputMobile);
        Button buttonGetToken = findViewById(R.id.buttonGetToken);

        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetToken.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(inputMobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(SendTokenActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttonGetToken.setVisibility(View.VISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+337" + inputMobile.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        SendTokenActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetToken.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetToken.setVisibility(View.VISIBLE);
                                Toast.makeText(SendTokenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                buttonGetToken.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(), VerifyTokenActivity.class);
                                intent.putExtra("mobile",inputMobile.getText().toString());
                                intent.putExtra("verificationId",verificationId);
                                startActivity(intent);
                            }
                        }
                );
            }
        });

    }
}