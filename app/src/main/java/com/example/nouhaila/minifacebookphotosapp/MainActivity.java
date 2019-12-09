package com.example.nouhaila.minifacebookphotosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nouhaila.minifacebookphotosapp.Database.Constants;
import com.example.nouhaila.minifacebookphotosapp.Database.RequestHandler;
import com.example.nouhaila.minifacebookphotosapp.Database.SharedPrefManager;
import com.example.nouhaila.minifacebookphotosapp.UserSpace.UserSpace;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private AppCompatAutoCompleteTextView emailBox;
    private EditText editTextPasswd;
    private Button signupBtn, signinBtn;
    private ProgressDialog progressDialog;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case latter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any latter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private Boolean CheckEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailBox = (AppCompatAutoCompleteTextView) findViewById(R.id.useremail);
        editTextPasswd = (EditText) findViewById(R.id.password);
        signupBtn = (Button) findViewById(R.id.register);
        signinBtn = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }


    public void registerUser(){

        final String emailstr = emailBox.getText().toString().trim();
        final String passstr = editTextPasswd.getText().toString().trim();

        progressDialog.show();

        CheckEditTextIsEmptyOrNot();

        if(!CheckEditText)
        {
            Toast.makeText(getApplicationContext(), "Please complete all fields...", Toast.LENGTH_LONG).show();
            progressDialog.hide();

        }else if(!isvalidateEmail()){
            Toast.makeText(getApplicationContext(), "Please enter a valid email !!", Toast.LENGTH_LONG).show();
            progressDialog.hide();
            emailBox.setText("");

        }else if(!isvalidatePassword()){
            Toast.makeText(getApplicationContext(), "Please enter a valid password !!", Toast.LENGTH_LONG).show();
            progressDialog.hide();
            editTextPasswd.setText("");
        }else {

            //POST user email & password in database
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        //Go to LoginFbActivity to link with FB account
                        startActivity(new Intent(MainActivity.this, LoginFbActivity.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emailstr);
                    params.put("password", passstr);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

    private void userLogin(){
        final String email = emailBox.getText().toString().trim();
        final String password = editTextPasswd.getText().toString().trim();

        progressDialog.show();

        //Check for user email & password to login
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")) {
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("email"),
                                                obj.getString("password")
                                        );
                                //Go to user space
                                Intent intent = new Intent(MainActivity.this, UserSpace.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void CheckEditTextIsEmptyOrNot(){


        String email = emailBox.getText().toString().trim();
        String password = editTextPasswd.getText().toString().trim();


        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public boolean isvalidateEmail(){
        String mailInput = emailBox.getText().toString().trim();
        if(Patterns.EMAIL_ADDRESS.matcher(mailInput).matches())
            return true;
        else
            return false;
    }

    public boolean isvalidatePassword(){
        String passwordInput = editTextPasswd.getText().toString().trim();
        if(PASSWORD_PATTERN.matcher(passwordInput).matches())
            return true;
        else
            return false;
    }

}
