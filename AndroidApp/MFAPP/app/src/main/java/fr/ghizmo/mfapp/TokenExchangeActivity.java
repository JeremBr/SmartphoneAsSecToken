package fr.ghizmo.mfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.goterl.lazysodium.LazySodiumAndroid;
import com.goterl.lazysodium.SodiumAndroid;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.utils.Key;
import com.goterl.lazysodium.utils.KeyPair;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class TokenExchangeActivity extends AppCompatActivity {

    public static LazySodiumAndroid lazySodium = new LazySodiumAndroid(new SodiumAndroid());
    private Box.Lazy cryptoBoxLazy = (Box.Lazy) lazySodium;
    private String pubKey;
    private String email;
    private KeyPair clientKeys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = getIntent().getStringExtra("email");

        try {
            clientKeys = cryptoBoxLazy.cryptoBoxKeypair();
            pubKey = clientKeys.getPublicKey().getAsHexString();

        } catch (SodiumException e) {
            e.printStackTrace();
        }



        Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "pubkuser";
                field[1] = "email";


                String[] data = new String[2];
                data[0] = pubKey;
                data[1] = email;

                PutData putData = new PutData("http://192.168.1.128:8080/keyexchange", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {

                        String pubKeyServ = putData.getResult();
                        Key serverPubKey = Key.fromHexString(pubKeyServ);
                        KeyPair encryptionKeyPair = new KeyPair(serverPubKey, clientKeys.getSecretKey());

                    }
                }


            }
        });





    }
}