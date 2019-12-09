package com.example.nouhaila.minifacebookphotosapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nouhaila.minifacebookphotosapp.UserSpace.UserSpace;
import com.facebook.FacebookSdk;
import com.mukeshsolanki.sociallogin.facebook.FacebookHelper;
import com.mukeshsolanki.sociallogin.facebook.FacebookListener;

public class LoginFbActivity extends AppCompatActivity implements FacebookListener {

    Button FbsigninBtn;
    FacebookHelper mFacebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fb);

        //Init Facebook
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(this);
        mFacebook = new FacebookHelper(this);

        FbsigninBtn = findViewById(R.id.loginFb);
        FbsigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacebook.performSignIn(LoginFbActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebook.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFbSignInFail(String errorMessage) {
        Toast.makeText(this, ""+errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess(String authToken, String userId) {
        //Go to User space after sign in
        Intent intent = new Intent(LoginFbActivity.this, UserSpace.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFBSignOut() {
        Toast.makeText(this, "Signing out", Toast.LENGTH_SHORT).show();
    }
}
